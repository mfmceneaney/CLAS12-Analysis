package org.jlab.analysis;

// Groovy Imports
import groovy.transform.CompileStatic;

// Java Imports
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

// CLAS Physics Imports
import org.jlab.jnp.hipo4.data.*;
import org.jlab.jnp.hipo4.io.HipoReader;
import org.jlab.clas.physics.*;

/**
* Searches a hipo event for all unique MC::Lund decays for a given set of Lund pids.
* from a given decay chain.
*
* @version 1.0
* @author  Matthew McEneaney
*/

@CompileStatic
public class MCDecays {

    protected ArrayList<Integer>                 _decay;        // List of Lund pids with first entry as parent particle
    protected ArrayList<Integer>                 _charges;      // Mirrors this._decay but with corresponding electric charges in [e]
    protected ArrayList<Integer>                 _parents;      // List of Lund pids for parents of parent, just empty if nothing to check
    protected ArrayList<Integer>                 _dpMap;       // Maps indices in this._decay to corresponding indices of parent in this._parents
    protected ArrayList<Integer>                 _parCharges;   // Mirrors this._parents but with corresponding electric charges in [e]
    protected HipoReader                         _reader;
    protected Event                              _event;
    protected Schema                             _schema;
    protected Bank                               _bank;
    protected ArrayList<DecayProduct>            _particleList;
    protected Constants                          _constants;
    protected ArrayList<DecayProduct>            _pidList;        // List of lists for each pid in decay
    protected ArrayList<ArrayList<DecayProduct>> _comboPidList;
    protected ArrayList<DecayProduct>            _chargeList;     // List of lists for each charge in decay
    protected ArrayList<ArrayList<DecayProduct>> _comboChargeList;
    protected ArrayList<DecayProduct>            _parPidList;        // List of lists for each pid in parent decay
    protected ArrayList<ArrayList<DecayProduct>> _parComboPidList;
    protected ArrayList<DecayProduct>            _parChargeList;     // List of lists for each charge in parent decay
    protected ArrayList<ArrayList<DecayProduct>> _parComboChargeList;
    protected LinkedHashMap<Integer,Integer>     _recMatchingMap;
    protected boolean                            _setFullParticleList = false; // Flag indicating whether full  particle list has been set.

    /** 
    * Constructor stub
    * @param decay      Integer list of daughters for decay chain
    * @param parents    Integer list of parents for decay chain
    * @param dpMap      Integer list to map daughter to parent indices
    * @param reader     HIPO file reader
    * @param event      HIPO event
    * @param constants  Physics constants object
    */
    public MCDecays(ArrayList<Integer> decay, ArrayList<Integer> parents, ArrayList<Integer> dpMap, HipoReader reader, Event event, Constants constants) {

        this._decay     = decay;
        this._parents   = parents;
        this._dpMap     = dpMap;
        this._reader    = reader;
        this._event     = event;
	    this._schema    = this._reader.getSchemaFactory().getSchema("MC::Lund");
        this._bank      = new Bank(this._schema);
        this._constants = constants;

        this._particleList       = new ArrayList<DecayProduct>();
        this._pidList            = new ArrayList<DecayProduct>();
        this._comboPidList       = new ArrayList<ArrayList<DecayProduct>>();
        this._chargeList         = new ArrayList<DecayProduct>();
        this._comboChargeList    = new ArrayList<ArrayList<DecayProduct>>();
        this._parPidList         = new ArrayList<DecayProduct>();
        this._parComboPidList    = new ArrayList<ArrayList<DecayProduct>>();
        this._parChargeList      = new ArrayList<DecayProduct>();
        this._parComboChargeList = new ArrayList<ArrayList<DecayProduct>>();

        this._charges = new ArrayList<Integer>();
        for (Integer pid : this._decay) { this._charges.add(this._constants.getCharge(pid)); }
        Collections.sort(this._charges); //IMPORTANT: Must be sorted otherwise algorithm won't work.

        this._parCharges = new ArrayList<Integer>();
        for (Integer pid : this._parents) { this._parCharges.add(this._constants.getCharge(pid)); }
        Collections.sort(this._parCharges); //IMPORTANT: Must be sorted otherwise algorithm won't work.
    }

    /**
    * Set full list of particles in event. (Used for pid tagging events.)
    * @param fplist
    */
    protected void setFullParticleList(ArrayList<DecayProduct> fplist) {

        this._particleList = fplist;
    }

    /**
    * Check if a given pid is a quark pid
    * @param pid
    * @return isQuark
    */
    protected boolean isQuark(int pid) {
        return Math.abs(pid)<10 && pid!=0;
    }

    /**
    * Check if a given pid is a diquark pid
    * @param pid
    * @return isDiQuark
    */
    protected boolean isDiQuark(int pid) {
        int abs_pid = Math.abs(pid);
        return (abs_pid == 1103 || 
            abs_pid == 2101 || abs_pid == 2103 || 
            abs_pid == 2203 || 
            abs_pid == 3101 || abs_pid == 3103 || 
            abs_pid == 3201 || abs_pid == 3203 || 
            abs_pid == 3303
        );
    }

    /**
    * Check if a given pid is a Lund string pid.
    * @param pid
    * @return isLundString
    */
    protected boolean isLundString(int pid) {
        return (pid == 91 || pid == 92);
    }

    /**
    * Get the last parent Lund index before the diquark
    * string for a given Lund index.
    * @param idx
    * @return last_parent_index
    */
    protected int getLastParentIndex(int index) {

        // Loop through bank checking status and pids of mothers until you find diquark or quark from scattering
        int idx = index;
        while (idx>0) {

            // Get mother pid and status
            int pid        = this._bank.getInt("pid", idx-1);
            int parent_idx = this._bank.getInt("parent", idx-1);
            int parent_pid = parent_idx>0 ? this._bank.getInt("pid", parent_idx-1) : 0;

            // Check mother pid and status
            if (this.isLundString(parent_pid)) return idx;

            // Reset index
            idx = parent_idx;
        }
        return 0;
    }

    /**
    * Get a list of the last parent Lund indices of all particles in an event.
    * @return lastParentIndices
    */
    protected ArrayList<Integer> getLastParentIndices() {

        // Create list
        ArrayList<Integer> last_parent_indices = new ArrayList<Integer>();

        this._event.read(this._bank);
        for (int i = 0; i < this._bank.getRows(); i++) {
            int last_parent_idx = this.getLastParentIndex(i+1);
            last_parent_indices.add(last_parent_idx);
        }

        return last_parent_indices;
    }

    /**
    * Create an array of flags indicating whether a particle at a given
    * lund index (0,N-1) in MC::Lund originates from CFR or TFR fragmentation.
    * Set flag to 1 if CFR.
    * Set flag to 0 if TFR.
    * Set flag -1 if no usable information.
    */
    protected void setCFRFlags() {

        // Grab target hadron and virtual photon from particle list
        int idx_N = 1;
        int idx_g = 2;

        // Sanity check
        if (this._particleList.size()<idx_g+1) return;
        
        // Grab lorentz vectors
        LorentzVector lv_N = this._particleList.get(idx_N).lv();
        LorentzVector lv_g = this._particleList.get(idx_g).lv();

        // Get boost vector
        LorentzVector lv_gN = new LorentzVector(lv_N);
        lv_gN.add(lv_g);
        Vector3 boost = lv_gN.boostVector();
        boost.negative();

        // Get a list of last parent indices
        ArrayList<Integer> last_parent_indices = this.getLastParentIndices();

        // Loop last parent indices, boost and check the rapidity of the last parent
        for (int i=0; i<last_parent_indices.size(); i++) {

            // Grab last parent index
            int last_parent_idx = last_parent_indices.get(i);

            // Set flag to default and check if last parent index is valid
            int cfr_flag = -1;
            if (last_parent_idx>0) {

                // Grab the last parent lorentz vector and boost
                DecayProduct p = this._particleList.get(last_parent_idx-1);
                LorentzVector lv_p = new LorentzVector(p.lv());
                lv_p.boost(boost);

                // Check whether z component is positive or negative
                cfr_flag = lv_p.pz() > 0 ? 1 : 0;
            }

            // Set particle flag in list
            this._particleList.get(i).is_cfr(cfr_flag);
        }
    }

   /**
    * Set full list of particles in event. (Used for pid tagging events.)
    */
    protected void setFullParticleList() {

        // Set flag
        this._setFullParticleList = true;

        // Reset particle list
        this._particleList = new ArrayList<DecayProduct>();

        this._event.read(this._bank);
        for (int i = 0; i < this._bank.getRows(); i++) {

            // Get pid, chi2pid, status and charge
            int pid        = this._bank.getInt("pid", i);
            int parent     = this._bank.getInt("parent", i);
            int daughter   = this._bank.getInt("daughter", i);
            int ppid       = this._bank.getInt("pid", parent-1); //NOTE: Lund index begins at 1 but bank index at 0
            int gparent    = this._bank.getInt("parent", parent-1); //NOTE: Lund index begins at 1 but bank index at 0
            int gppid      = this._bank.getInt("pid", gparent-1); //NOTE: Lund index begins at 1 but bank index at 0
            int ggparent   = gparent-1>=0? this._bank.getInt("parent", gparent-1) : 0; //NOTE: Lund index begins at 1 but bank index at 0
            int ggppid     = ggparent-1>=0? this._bank.getInt("pid", ggparent-1) : 0; //NOTE: Lund index begins at 1 but bank index at 0
            
            // Get momenta and vertices
            double px = this._bank.getFloat("px", i);
            double py = this._bank.getFloat("py", i);
            double pz = this._bank.getFloat("pz", i);
            double bt = 0.0; //this._bank.getFloat("beta", i);
            double vx = this._bank.getFloat("vx", i);
            double vy = this._bank.getFloat("vy", i);
            double vz = this._bank.getFloat("vz", i);

            DecayProduct p = new DecayProduct(pid,px,py,pz,bt,vx,vy,vz,i+1,parent,daughter,ppid,gppid,ggppid);

            // Set mass
            double m  = this._bank.getFloat("mass", i);
            p.m(m);

            this._particleList.add(p);
        }

        // Now set the cfr flags
        this.setCFRFlags();
    }

    /**
    * Set and access full particle list for event.
    * @return _particleList
    */
    protected ArrayList<DecayProduct> getFullParticleList() {

        if (!this._setFullParticleList) this.setFullParticleList();

        return this._particleList;
    }

    /**
    * Set list of particles for event matching pids in <code>this._decay</code>.
    */
    protected void setParticleList() {

        this._event.read(this._bank);
        for (int i = 0; i < this._bank.getRows(); i++) {

            // Get pid and parent and daughter indices
            int pid        = this._bank.getInt("pid", i);
            // if (!this._decay.contains(pid) && !this._parents.contains(pid) && !this._constants.getBeamPID()==pid) { continue; } //DEBUGGING: COMMENTED OUT
            int parent     = this._bank.getInt("parent", i);
            int daughter   = this._bank.getInt("daughter", i);
            int ppid       = this._bank.getInt("pid", parent-1); //NOTE: Lund index begins at 1 but bank index at 0
            int gparent    = this._bank.getInt("parent", parent-1); //NOTE: Lund index begins at 1 but bank index at 0
            int gppid      = this._bank.getInt("pid", gparent-1); //NOTE: Lund index begins at 1 but bank index at 0
            int ggparent   = gparent-1>=0? this._bank.getInt("parent", gparent-1) : 0; //NOTE: Lund index begins at 1 but bank index at 0
            int ggppid     = ggparent-1>=0? this._bank.getInt("pid", ggparent-1) : 0; //NOTE: Lund index begins at 1 but bank index at 0
            
            // Get momenta and vertices
            double px = this._bank.getFloat("px", i);
            double py = this._bank.getFloat("py", i);
            double pz = this._bank.getFloat("pz", i);
            double bt = 0.0; //this._bank.getFloat("beta", i);
            double vx = this._bank.getFloat("vx", i);
            double vy = this._bank.getFloat("vy", i);
            double vz = this._bank.getFloat("vz", i);

            DecayProduct p = new DecayProduct(pid,px,py,pz,bt,vx,vy,vz,i+1,parent,daughter,ppid,gppid,ggppid);

            // Set mass
            double m  = this._bank.getFloat("mass", i);
            p.m(m);

            this._particleList.add(p);
        }

        // Now set the cfr flags
        this.setCFRFlags();
    }

    /**
    * Access particle list for event.
    * @return _particleList
    */
    protected ArrayList<DecayProduct> getParticleList() {

        if (this._particleList.size()==0) { this.setParticleList(); }

        return this._particleList;
    }

    /**
    * Get index to index map to match REC::Particle bank entries to MC::Lund (final state) bank entries.
    * Note that map entries map the actual index in REC::Particle bank (0->nparticles-1) (same as p_rec.index()) to the actual index (0->nparticles-1) in MC::Lund (not the same as p_mc.index())
    * @param fullRecParticleList  Full list of reconstructed particle for an event
    */
    protected void setMatchingMap(ArrayList<DecayProduct> fullRecParticleList) {

        // NOTE: You must set the full particle list for this to work
        if (this._particleList.size()==0) { this.setFullParticleList(); }
        // RUN MC MATCHING //NOTE: ALWAYS MAP REC ELECTRON ABOVE (index=0) to MC electron (index=3)
        LinkedHashMap<Integer,Integer> mc_matching_map = new LinkedHashMap<Integer,Integer>(); // Map REC index to top MC indices closest in theta and phi to REC track
        int rowe_mc = 3; //NOTE: THIS SHOULD ALWAYS BE THE CASE
        for (int row_rec=0; row_rec<fullRecParticleList.size(); row_rec++) { // Row loop 1
            DecayProduct p_rec = fullRecParticleList.get(row_rec);
            
            int row_mc_match = -1;
            double om2_min   = 1000;
                        
            for (int row_mc=rowe_mc+1; row_mc<this._particleList.size(); row_mc++) { // Row loop 1
                    DecayProduct p_mc = this._particleList.get(row_mc);
                    double dtheta = p_rec.theta() - p_mc.theta();
                    double dphi   = Math.abs(p_rec.phi() - p_mc.phi()) > Math.PI ? 2*Math.PI - Math.abs(p_rec.phi()-p_mc.phi()) : Math.abs(p_rec.phi()-p_mc.phi());
                    double dp     = p_rec.p() - p_mc.p();
                    double om2 = dtheta*dtheta+dphi*dphi+dp*dp/(p_mc.p()*p_mc.p());
                    if (om2<om2_min && p_rec.charge()==p_mc.charge() && p_mc.daughter()==0) { //NOTE: MAKE SURE THAT MC MATCHES AT LEAST HAVE SAME CHARGE and final state particle (daughter==0)
                        om2_min = om2;
                        row_mc_match = row_mc;
                    }
            } // Loop MC bank
            mc_matching_map.put(row_rec,row_mc_match);
        } // Loop REC bank

        // Set map as class attribute
        this._recMatchingMap = mc_matching_map;
    }

    /**
    * Access the REC::Particle index to MC::Lund index matching map.
    * @return _recMatchingMap
    */
    protected LinkedHashMap<Integer,Integer> getMatchingMap() {
        return this._recMatchingMap;
    }

    /**
    * Create list of lists of decay particles for each pid in specified decay channel.
    */
    protected void setPidList() {

        if (this._particleList.size()==0) {this.setParticleList();}
        for (int i=0; i<this._decay.size(); i++) {
            int pid = this._decay.get(i);
            if (i!=0) { if (pid == this._decay.get(i-1)) { continue; } } //IMPORTANT: Just get unique entries.  This relies on the fact that decays is sorted!
            for (int j=3; j<this._particleList.size(); j++) { // NOTE: For MC Decays incoming electron, target, virtual photon, and scattered electron are always first so start at j = 3.
                DecayProduct p = this._particleList.get(j);
                if (p.pid()==pid) {
                    if (this._pidList.size()>0 && this._parents.size()!=0) {
                        // if (this._pidList.get(this._pidList.size()-1).parent()!=p.parent()) { continue; } //IMPORTANT: Check that daughters in decay all have same parent.
                    }
                    this._pidList.add(p); 
                }
            } // for(int j=0; ...)
        } // for (int i=0; ...)
    } // setPidList()

    /**
    * Create list of lists of decay particles for each pid in specified parent channel.
    */
    protected void setParPidList() {

        if (this._particleList.size()==0) {this.setParticleList();}
        for (int i=0; i<this._parents.size(); i++) {
            int pid = this._parents.get(i);
            if (i!=0) { if (pid == this._parents.get(i-1)) { continue; } } //IMPORTANT: Just get unique entries.  This relies on the fact that decays is sorted!
            for (int j=0; j<this._particleList.size(); j++) {
                DecayProduct p = this._particleList.get(j);
                if (p.pid()==pid) {
                    if (this._parPidList.size()>0 && this._decay.size()!=0) {
                        // if (this._parPidList.get(this._parPidList.size()-1).parent()!=p.parent()) { continue; } //IMPORTANT: Check that parents all have same daughter.
                    }
                    this._parPidList.add(p);
                }
            } // for(int j=0; ...)
        } // for (int i=0; ...)
    } // setParPidList()

    /**
    * Access list of all possible decay particle combinations identified by Lund pid.
    * @return _pidList
    */
    protected ArrayList<DecayProduct> getPidList() {

        if (this._pidList.size()==0) { this.setPidList(); }

        return this._pidList;
    }

    /**
    * Access list of all possible decay particle combinations identified by Lund pid.
    * @return _parPidList
    */
    protected ArrayList<DecayProduct> getParPidList() {

        if (this._parPidList.size()==0) { this.setParPidList(); }

        return this._parPidList;
    }

    /**
    * Recursive helper function to create combo list for all possible decay particle combinations in event.
    * Relies on <code>this._decay</code> and <code>this._pidList</code> (passed as plist argument the first time) 
    * being sorted.  If these are not sorted this will NOT work!
    * @param dIndex
    * @param oldlist
    */
    protected void setComboPidList(int dIndex, ArrayList<DecayProduct> plist, ArrayList<DecayProduct> oldlist) {

        for (int pIndex=0; pIndex<plist.size(); pIndex++) {
            DecayProduct p = plist.get(pIndex);
            if (this._decay.get(dIndex)!=p.pid()) { continue; } //
            ArrayList<DecayProduct> newlist = new ArrayList<DecayProduct>(oldlist); // IMPORTANT: declare new list
            newlist.add(p);
            ArrayList<DecayProduct> newplist = new ArrayList<DecayProduct>(plist); // IMPORTANT: declare new list
            newplist = (ArrayList<DecayProduct>)newplist.subList(Math.min(pIndex+1,newplist.size()),newplist.size()); // IMPORTANT: Guarantees combos are unique (assumes this._decay and this._pidList are sorted)
            if (dIndex == this._decay.size()-1) { this._comboPidList.add(newlist); } //Important: -1!
            else { setComboPidList(dIndex+1,newplist,newlist); }
        }
    }

    /**
    * Recursive helper function to create combo list for all unique parent particle combinations in event.
    * Relies on <code>this._parents</code> and <code>this._parPidList</code> (passed as plist argument the first time)
    * being sorted.  If these are not sorted this will NOT work!
    * @param dIndex
    * @param oldlist
    */
    protected void setParComboPidList(int dIndex, ArrayList<DecayProduct> plist, ArrayList<DecayProduct> oldlist) {

        //NOTE: Add zero particle if this._parents(dIndex) is zero and continue at same place in plist.
        if (this._parents.get(dIndex)==0) {
            DecayProduct p = new DecayProduct(0,0,0,0,0,0,0,0,-1,-1,0);
            ArrayList<DecayProduct> newlist = new ArrayList<DecayProduct>(oldlist); // IMPORTANT: declare new list
            newlist.add(p);
            ArrayList<DecayProduct> newplist = new ArrayList<DecayProduct>(plist); // IMPORTANT: declare new list AND do NOT shorten
            if (dIndex == this._parents.size()-1) { this._parComboPidList.add(newlist); } //Important: -1!
            else { setParComboPidList(dIndex+1,newplist,newlist); }
        }

        for (int pIndex=0; pIndex<plist.size(); pIndex++) {
            DecayProduct p = plist.get(pIndex);
            if (this._parents.get(dIndex)!=p.pid()) { continue; } //
            ArrayList<DecayProduct> newlist = new ArrayList<DecayProduct>(oldlist); // IMPORTANT: declare new list
            newlist.add(p);
            ArrayList<DecayProduct> newplist = new ArrayList<DecayProduct>(plist); // IMPORTANT: declare new list
            newplist = (ArrayList<DecayProduct>)newplist.subList(Math.min(pIndex+1,newplist.size()),newplist.size()); // IMPORTANT: Guarantees combos are unique (assumes this._decay and this._pidList are sorted)
            if (dIndex == this._parents.size()-1) { this._parComboPidList.add(newlist); } //Important: -1!
            else { setParComboPidList(dIndex+1,newplist,newlist); }
        }
    }

    /**
    * Access list of all possible decay particle combinations identified by Lund pid.
    * @return _comboPidList
    */
    protected ArrayList<ArrayList<DecayProduct>> getComboPidList() {

        if (this._comboPidList.size()!=0) { return this._comboPidList; }
        if (this._pidList.size()==0) { this.setPidList(); }
        ArrayList<DecayProduct> newlist = new ArrayList<DecayProduct>();
        setComboPidList(0,this._pidList,newlist);

        return this._comboPidList;
    }

    /**
    * Access list of all unique parent particle combinations identified by Lund pid.
    * @return _parComboPidList
    */
    protected ArrayList<ArrayList<DecayProduct>> getParComboPidList() {

        if (this._parComboPidList.size()!=0) { return this._parComboPidList; }
        if (this._parPidList.size()==0) { this.setParPidList(); }
        ArrayList<DecayProduct> newlist = new ArrayList<DecayProduct>();
        setParComboPidList(0,this._parPidList,newlist);
        
        return this._parComboPidList;
    }

    /**
    * Check list of all unique decay particle combinations identified by Lund pid
    * for matching parent/daughter index with combo in <code>this._parComboPidList</code>.
    * Relies on check in <code>this.getPidList()</code> method that all combos come from
    * same parent and corresponding check in <code>this.getParPidList()</code>.
    * @return mergedComboPidList
    */
    protected ArrayList<ArrayList<DecayProduct>> getCheckedComboPidList() {

        ArrayList<ArrayList<DecayProduct>> checkedComboPidList = new ArrayList<ArrayList<DecayProduct>>();
        if (this._parents.size()==0) { return checkedComboPidList; }
        for (ArrayList<DecayProduct> combo : this.getComboPidList()) {
            for (ArrayList<DecayProduct> check : this.getParComboPidList()) { //NOTE: Empty particles added at 0 pids.
                ArrayList<DecayProduct> addList = new ArrayList<DecayProduct>(combo); //TODO: Add grouping for pid/index checks.
                //OLD: if (check.size()==1) {if (combo.get(0).parent()==check.get(0).index()) { checkedComboPidList.add(addList); } }
                
                //NOTE: Check that all parents match daughters at each index using parent daughter index map from instantiation
                int size = check.size();
                boolean flag = true;
                for (int i=0; i<this._dpMap.size(); i++) {
                    if (combo.get(i).parent()!=check.get(size>1 ? this._dpMap.get(i) : 0).index() && check.get(size>1 ? this._dpMap.get(i) : 0).pid()!=0) { flag = false; break; } //NOTE: zero particles in check correspond to zero pid entry in this._parents where mother matching doesn't matter
                    if (size==1 && i>0) { if (combo.get(i-1).parent()!=combo.get(i).parent()) { flag = false; break; } } //NOTE: If only one parent provided, check that all decay particles come from same parent.
                }
                if (flag) { checkedComboPidList.add(addList); }
            }
         }

        return checkedComboPidList;
    }

    /**
    * Merge list of all unique decay particle combinations identified by Lund pid
    * with a combo list from another decay object.
    * @param mergeList
    * @return mergedComboPidList
    */
    protected ArrayList<ArrayList<DecayProduct>> mergeComboPidList(ArrayList<ArrayList<DecayProduct>> mergeList) {

        ArrayList<ArrayList<DecayProduct>> mergedComboPidList = new ArrayList<ArrayList<DecayProduct>>();
        for (ArrayList<DecayProduct> combo : this.getComboPidList()) { 
            for (ArrayList<DecayProduct> mergeCombo : mergeList) {
                ArrayList<DecayProduct> addList = new ArrayList<DecayProduct>(combo);
                addList.addAll(mergeCombo);
                mergedComboPidList.add(addList);
            }
         }

        return mergedComboPidList;
    }

    /**
    * Merge list of all unique parent particle combinations identified by Lund pid
    * with a combo list from another decay object.
    * @param mergeList
    * @return mergedParComboPidList
    */
    protected ArrayList<ArrayList<DecayProduct>> mergeParComboPidList(ArrayList<ArrayList<DecayProduct>> mergeList) {

        ArrayList<ArrayList<DecayProduct>> mergedParComboPidList = new ArrayList<ArrayList<DecayProduct>>();
        for (ArrayList<DecayProduct> combo : this.getParComboPidList()) { 
            for (ArrayList<DecayProduct> mergeCombo : mergeList) {
                ArrayList<DecayProduct> addList = new ArrayList<DecayProduct>(combo);
                addList.addAll(mergeCombo);
                mergedParComboPidList.add(addList);
            }
         }

        return mergedParComboPidList;
    }

    /**
    * Set list of particles for event matching charges in <code>this._charges</code>.
    */
    protected void setParticleListByCharge() {

        this._event.read(this._bank);
        for (int i = 0; i < this._bank.getRows(); i++) {

            // Get charge and parent and daughter indices
            int pid        = this._bank.getInt("pid", i);
            int charge     = this._bank.getInt("charge", i);
            // if (!this._charges.contains(charge) && !this._parCharges.contains(charge) && !this._constants.getCharge(this._constants.getBeamPID())==charge) { continue; }
            int parent     = this._bank.getInt("parent", i);
            int daughter   = this._bank.getInt("daughter", i);
            int ppid       = this._bank.getInt("pid", parent-1);
            int gparent    = this._bank.getInt("parent", parent-1); //NOTE: Lund index begins at 1 but bank index at 0
            int gppid      = this._bank.getInt("pid", gparent-1); //NOTE: Lund index begins at 1 but bank index at 0
            int ggparent   = gparent-1>=0? this._bank.getInt("parent", gparent-1) : 0; //NOTE: Lund index begins at 1 but bank index at 0
            int ggppid     = ggparent-1>=0? this._bank.getInt("pid", ggparent-1) : 0; //NOTE: Lund index begins at 1 but bank index at 0

            // Get momenta and vertices
            double px = this._bank.getFloat("px", i);
            double py = this._bank.getFloat("py", i);
            double pz = this._bank.getFloat("pz", i);
            double bt = 0.0; //this._bank.getFloat("beta", i);
            double vx = this._bank.getFloat("vx", i);
            double vy = this._bank.getFloat("vy", i);
            double vz = this._bank.getFloat("vz", i);

            DecayProduct p = new DecayProduct(pid,px,py,pz,bt,vx,vy,vz,i+1,parent,daughter,ppid,gppid,ggppid);

            p.charge(charge); //TODO: Necessary?
            //TODO: sector cut
            this._particleList.add(p);
        }

        // Now set the cfr flags
        this.setCFRFlags();
    }

    /**
    * Create list of lists of decay particles for each charge in specified decay channel.
    */
    protected void setChargeList() {

        if (this._particleList.size()==0) {this.setParticleListByCharge();}
        for (int i=0; i<this._charges.size(); i++) {
            int charge = this._charges.get(i);
            if (i!=0) { if (charge == this._charges.get(i-1)) { continue; } } //IMPORTANT: Just get unique entries.  This relies on the fact that decays is sorted!
            for (int j=3; j<this._particleList.size(); j++) { // NOTE: For MC Decays incoming electron, target, virtual photon, and scattered electron are always first so start and j = 3.
                DecayProduct p = this._particleList.get(j);
                if (p.charge()==charge) {
                    if (this._chargeList.size()>0 && this._parCharges.size()!=0) {
                        // if (this._chargeList.get(this._chargeList.size()-1).parent()!=p.parent()) { continue; } //IMPORTANT: Check that daughters in decay all have same parent.
                    }
                    this._chargeList.add(p); 
                }
            } // for(int j=0; ...)
        } // for (int i=0; ...)
    } // setChargeList()

    /**
    * Create list of lists of decay particles for each charge in specified parent channel.
    */
    protected void setParChargeList() {

        if (this._particleList.size()==0) { this.setChargeList(); }
        for (int i=0; i<this._parCharges.size(); i++) {
            int charge = this._parCharges.get(i);
            if (i!=0) { if (charge == this._parCharges.get(i-1)) { continue; } } //IMPORTANT: Just get unique entries.  This relies on the fact that decays is sorted!
            for (int j=0; j<this._particleList.size(); j++) {
                DecayProduct p = this._particleList.get(j);
                if (p.charge()==charge) {
                    if (this._parChargeList.size()>0 && this._charges.size()!=0) {
                        // if (this._parChargeList.get(this._parChargeList.size()-1).parent()!=p.parent()) { continue; } //IMPORTANT: Check that parents all have same daughter.
                    }
                    this._parChargeList.add(p);
                }
            } // for(int j=0; ...)
        } // for (int i=0; ...)
    } // setParChargeList()

    /**
    * Access list of all possible decay particle combinations identified by charge.
    * @return _chargeList
    */
    protected ArrayList<DecayProduct> getChargeList() {

        if (this._chargeList.size()==0) { this.setChargeList(); }

        return this._chargeList;
    }

    /**
    * Access list of all possible decay particle combinations identified by charge.
    * @return _parChargeList
    */
    protected ArrayList<DecayProduct> getParChargeList() {

        if (this._parChargeList.size()==0) { this.setParChargeList(); }

        return this._parChargeList;
    }

    /**
    * Recursive helper function to create combo list for all possible decay particle combinations in event.
    * Relies on <code>this._charges</code> and <code>this._chargeList</code> (passed as plist argument the first time) 
    * being sorted.  If these are not sorted this will NOT work!
    * @param dIndex
    * @param oldlist
    */
    protected void setComboChargeList(int dIndex, ArrayList<DecayProduct> plist, ArrayList<DecayProduct> oldlist) {

        for (int pIndex=0; pIndex<plist.size(); pIndex++) {
            DecayProduct p = plist.get(pIndex);
            if (this._charges.get(dIndex)!=p.charge()) { continue; } //
            ArrayList<DecayProduct> newlist = new ArrayList<DecayProduct>(oldlist); // IMPORTANT: declare new list
            newlist.add(p);
            ArrayList<DecayProduct> newplist = new ArrayList<DecayProduct>(plist); // IMPORTANT: declare new list
            newplist = (ArrayList<DecayProduct>)newplist.subList(Math.min(pIndex+1,newplist.size()),newplist.size()); // IMPORTANT: Guarantees combos are unique (assumes this._charges and this._chargeList are sorted)
            if (dIndex == this._charges.size()-1) { this._comboChargeList.add(newlist); } //Important: -1!
            else { setComboChargeList(dIndex+1,newplist,newlist); }
        }
    }

    /**
    * Recursive helper function to create combo list for all unique parent particle combinations in event.
    * Relies on <code>this._parCharges</code> and <code>this._parChargeList</code> (passed as plist argument the first time)
    * being sorted.  If these are not sorted this will NOT work!
    * @param dIndex
    * @param oldlist
    */
    protected void setParComboChargeList(int dIndex, ArrayList<DecayProduct> plist, ArrayList<DecayProduct> oldlist) {

        for (int pIndex=0; pIndex<plist.size(); pIndex++) {
            DecayProduct p = plist.get(pIndex);
            if (this._parCharges.get(dIndex)!=p.charge()) { continue; } //
            ArrayList<DecayProduct> newlist = new ArrayList<DecayProduct>(oldlist); // IMPORTANT: declare new list
            newlist.add(p);
            ArrayList<DecayProduct> newplist = new ArrayList<DecayProduct>(plist); // IMPORTANT: declare new list
            newplist = (ArrayList<DecayProduct>)newplist.subList(Math.min(pIndex+1,newplist.size()),newplist.size()); // IMPORTANT: Guarantees combos are unique (assumes this._charges and this._chargeList are sorted)
            if (dIndex == this._charges.size()-1) { this._parComboChargeList.add(newlist); } //Important: -1!
            else { setParComboChargeList(dIndex+1,newplist,newlist); }
        }
    }

    /**
    * Access list of all possible decay particle combinations identified by charge.
    * @return _comboChargeList
    */
    protected ArrayList<ArrayList<DecayProduct>> getComboChargeList() {

        if (this._comboChargeList.size()!=0) { return this._comboChargeList; }
        if (this._chargeList.size()==0) { this.setChargeList(); }
        ArrayList<DecayProduct> newlist = new ArrayList<DecayProduct>();
        setComboChargeList(0,this._chargeList,newlist);

        return this._comboChargeList;
    }

    /**
    * Access list of all unique parent particle combinations identified by charge.
    * @return _parComboChargeList
    */
    protected ArrayList<ArrayList<DecayProduct>> getParComboChargeList() {

        if (this._parComboChargeList.size()!=0) { return this._parComboChargeList; }
        if (this._parChargeList.size()==0) { this.setParChargeList(); }
        ArrayList<DecayProduct> newlist = new ArrayList<DecayProduct>();
        setParComboChargeList(0,this._parChargeList,newlist);

        return this._parComboChargeList;
    }

    /**
    * Check list of all unique decay particle combinations identified by charge
    * for matching parent/daughter index with combo in <code>this._parComboChargeList</code>.
    * Relies on check in <code>this.getChargeList()</code> method that all combos come from
    * same parent and corresponding check in <code>this.getParChargeList()</code>.
    * @return mergedComboChargeList
    */
    protected ArrayList<ArrayList<DecayProduct>> getCheckedComboChargeList() {

        ArrayList<ArrayList<DecayProduct>> checkedComboChargeList = new ArrayList<ArrayList<DecayProduct>>();
        if (this._parCharges.size()==0) { return checkedComboChargeList; }
        for (ArrayList<DecayProduct> combo : this.getComboChargeList()) {
            for (ArrayList<DecayProduct> check : this.getParComboChargeList()) {
                ArrayList<DecayProduct> addList = new ArrayList<DecayProduct>(combo);
                if (combo.get(0).parent()==check.get(0).index()) { checkedComboChargeList.add(addList); }
            }
         }

        return checkedComboChargeList;
    }

    /**
    * Merge list of all unique decay particle combinations identified by charge
    * with a combo list from another decay object.
    * @param mergeList
    * @return mergedComboChargeList
    */
    protected ArrayList<ArrayList<DecayProduct>> mergeComboChargeList(ArrayList<ArrayList<DecayProduct>> mergeList) {

        ArrayList<ArrayList<DecayProduct>> mergedComboChargeList = new ArrayList<ArrayList<DecayProduct>>();
        for (ArrayList<DecayProduct> combo : this.getComboChargeList()) { 
            for (ArrayList<DecayProduct> mergeCombo : mergeList) {
                ArrayList<DecayProduct> addList = new ArrayList<DecayProduct>(combo);
                addList.addAll(mergeCombo);
                mergedComboChargeList.add(addList);
            }
         }

        return mergedComboChargeList;
    }

    /**
    * Merge list of all unique parent particle combinations identified by charge
    * with a combo list from another decay object.
    * @param mergeList
    * @return mergedParComboChargeList
    */
    protected ArrayList<ArrayList<DecayProduct>> mergeParComboChargeList(ArrayList<ArrayList<DecayProduct>> mergeList) {

        ArrayList<ArrayList<DecayProduct>> mergedParComboChargeList = new ArrayList<ArrayList<DecayProduct>>();
        for (ArrayList<DecayProduct> combo : this.getParComboChargeList()) { 
            for (ArrayList<DecayProduct> mergeCombo : mergeList) {
                ArrayList<DecayProduct> addList = new ArrayList<DecayProduct>(combo);
                addList.addAll(mergeCombo);
                mergedParComboChargeList.add(addList);
            }
         }

        return mergedParComboChargeList;
    }

    /**
    * Get beam as 0th entry from MC::Lund bank.
    * @return beam
    */
    protected DecayProduct getBeam() {
        if (this._particleList.size()==0) { this.setParticleList(); }
	    if (this._particleList.size()==0) { return new DecayProduct(0,0,0,0,0); }
        try { return this._particleList[0]; } catch (Exception e) { System.out.println("*** WARNING *** Trying to access non-existent element of MC particle list."); return new DecayProduct(0,0,0,0,0); }
    }

    /**
    * Get Target as 1st entry from MC::Lund bank.
    * @return target
    */
    protected DecayProduct getTarget() {
        if (this._particleList.size()==0) { this.setParticleList(); }
	    if (this._particleList.size()==0) { return new DecayProduct(0,0,0,0,0); }
        try { return this._particleList[1]; } catch (Exception e) { System.out.println("*** WARNING *** Trying to access non-existent element of MC particle list."); return new DecayProduct(0,0,0,0,0); }
    }

    /**
    * Get virtual photon as 2nd entry from MC::Lund bank.
    * @return Q
    */
    protected DecayProduct getQ() {
        if (this._particleList.size()==0) { this.setParticleList(); }
        if (this._particleList.size()==0) { return new DecayProduct(0,0,0,0,0); }
        try { return this._particleList[2]; } catch (Exception e) { System.out.println("*** WARNING *** Trying to access non-existent element of MC particle list."); return new DecayProduct(0,0,0,0,0); }
    }

    /**
    * Get scattered beam as 3rd entry from MC::Lund bank.
    * @return beam
    */
    protected DecayProduct getScatteredBeam() {
        if (this._particleList.size()==0) { this.setParticleList(); } 
	    if (this._particleList.size()==0) { return new DecayProduct(0,0,0,0,0); }
        try { return this._particleList[3]; } catch (Exception e) { System.out.println("*** WARNING *** Trying to access non-existent element of MC particle list."); return new DecayProduct(0,0,0,0,0); }
    }

    /**
    * Get list of entries 0:3 from MC::Lund bank, corresponding to beam, target, Q, and scattered beam.
    * @return parents
    */
    protected ArrayList<DecayProduct> getParents() { //TODO: This does not match logic for setParticleList() method above.... 7/5/22
        if (this._particleList.size()==0) { this.setParticleList(); }
	    if (this._particleList.size() < 4 ) { return new ArrayList<DecayProduct>(); }
        try { return (ArrayList<DecayProduct>)this._particleList.subList(0,4); } catch (Exception e) { System.out.println("*** WARNING *** Trying to access non-existent element of MC particle list."); return new ArrayList<DecayProduct>(); }
    }
    
    /**
    * Clear all lists. Hopefully will help with memory.
    */
    protected void clean() {

        this._comboChargeList.clear();
        this._comboPidList.clear();
        this._chargeList.clear();
        this._pidList.clear();
    }

} // class
