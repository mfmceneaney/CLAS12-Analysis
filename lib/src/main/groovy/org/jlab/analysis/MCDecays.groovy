package org.jlab.analysis;

// Java Imports
import java.util.ArrayList;
import java.util.HashMap;

// CLAS Physics Imports
import org.jlab.jnp.hipo4.data.*;
import org.jlab.jnp.hipo4.io.*;
import org.jlab.jnp.physics.*;

/**
* Searches a hipo event for all unique MC::Lund decays for a given set of Lund pids.
* from a given decay chain.
*
* @version 1.0
* @author  Matthew McEneaney
*/

public class MCDecays {

    protected ArrayList<Integer>                 _decay;        // List of Lund pids with first entry as parent particle
    protected ArrayList<Integer>                 _charges;      // Mirrors this._decay but with corresponding electric charges in [e]
    protected ArrayList<Integer>                 _parents;      // List of Lund pids for parents of parent, just empty if nothing to check
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

    /** 
    * Constructor stub
    * @param ArrayList<Integer> decay - integer list [daughters] for decay chain
    * @param ArrayList<Integer> parents - integer list [parents] for parent in decay
    * @param HipoReader reader
    * @param Event event
    * @param Constants constants
    */
    public MCDecays(ArrayList<Integer> decay, ArrayList<Integer> parents, HipoReader reader, Event event, Constants constants) {

        this._decay     = decay;
        this._parents   = parents;
        this._reader    = reader;
        this._event     = event;
	    this._schema    = this._reader.getSchemaFactory().getSchema("MC::Lund");
        this._bank      = new Bank(this._schema);
        this._constants = constants;

        this._particleList       = new ArrayList<DecayProduct>();
        this._pidList            = new ArrayList<ArrayList<DecayProduct>>();
        this._comboPidList       = new ArrayList<ArrayList<DecayProduct>>();
        this._chargeList         = new ArrayList<ArrayList<DecayProduct>>();
        this._comboChargeList    = new ArrayList<ArrayList<DecayProduct>>();
        this._parPidList         = new ArrayList<ArrayList<DecayProduct>>();
        this._parComboPidList    = new ArrayList<ArrayList<DecayProduct>>();
        this._parChargeList      = new ArrayList<ArrayList<DecayProduct>>();
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
    */
    protected void setFullParticleList() {

        this._event.read(this._bank);
        for (int i = 0; i < this._bank.getRows(); i++) {

            // Get pid, chi2pid, status and charge
            int pid        = this._bank.getInt("pid", i);
            int parent     = this._bank.getInt("parent", i);
            int daughter   = this._bank.getInt("daughter", i);
            
            // Get momenta and vertices
            double px = this._bank.getFloat("px", i);
            double py = this._bank.getFloat("py", i);
            double pz = this._bank.getFloat("pz", i);
            double vx = this._bank.getFloat("vx", i);
            double vy = this._bank.getFloat("vy", i);
            double vz = this._bank.getFloat("vz", i);

            DecayProduct p = new DecayProduct(pid,px,py,pz,vx,vy,vz,i+1,parent,daughter);
            this._particleList.add(p);
        }
    }

    /**
    * Access full particle list for event.
    * @return ArrayList<DecayProduct> _particleList
    */
    protected ArrayList<DecayProduct> getFullParticleList() {

        this.setFullParticleList();

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
            if (!this._decay.contains(pid) && !this._parents.contains(pid) && !this._constants.getBeamPID()==pid) { continue; }
            int parent     = this._bank.getInt("parent", i);
            int daughter   = this._bank.getInt("daughter", i);
            
            // Get momenta and vertices
            double px = this._bank.getFloat("px", i);
            double py = this._bank.getFloat("py", i);
            double pz = this._bank.getFloat("pz", i);
            double vx = this._bank.getFloat("vx", i);
            double vy = this._bank.getFloat("vy", i);
            double vz = this._bank.getFloat("vz", i);

            DecayProduct p = new DecayProduct(pid,px,py,pz,vx,vy,vz,i+1,parent,daughter);
            this._particleList.add(p);
        }
    }

    /**
    * Access particle list for event.
    * @return ArrayList<DecayProduct> _particleList
    */
    protected ArrayList<DecayProduct> getParticleList() {

        if (this._particleList.size()==0) { this.setParticleList(); }

        return this._particleList;
    }

    /**
    * Create list of lists of decay particles for each pid in specified decay channel.
    */
    protected void setPidList() {

        if (this._particleList.size()==0) {this.setParticleList();}
        for (int i=0; i<this._decay.size(); i++) {
            int pid = this._decay.get(i);
            if (i!=0) { if (pid == this._decay.get(i-1)) { continue; } } //IMPORTANT: Just get unique entries.  This relies on the fact that decays is sorted!
            for (int j=0; j<this._particleList.size(); j++) {
                DecayProduct p = this._particleList.get(j);
                if (p.pid()==pid) {
                    if (this._pidList.size()>0 && this._parents.size()!=0) {
                        if (this._pidList.get(this._pidList.size()-1).parent()!=p.parent()) { continue; } //IMPORTANT: Check that daughters in decay all have same parent.
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
                        if (this._parPidList.get(this._parPidList.size()-1).parent()!=p.parent()) { continue; } //IMPORTANT: Check that parents all have same daughter.
                    }
                    this._parPidList.add(p);
                }
            } // for(int j=0; ...)
        } // for (int i=0; ...)
    } // setParPidList()

    /**
    * Access list of all possible decay particle combinations identified by Lund pid.
    * @return ArrayList<ArrayList<DecayProduct>> _pidList
    */
    protected ArrayList<ArrayList<DecayProduct>> getPidList() {

        if (this._pidList.size()==0) { this.setPidList(); }

        return this._pidList;
    }

    /**
    * Access list of all possible decay particle combinations identified by Lund pid.
    * @return ArrayList<ArrayList<DecayProduct>> _parPidList
    */
    protected ArrayList<ArrayList<DecayProduct>> getParPidList() {

        if (this._parPidList.size()==0) { this.setParPidList(); }

        return this._parPidList;
    }

    /**
    * Recursive helper function to create combo list for all possible decay particle combinations in event.
    * Relies on <code>this._decay</code> and <code>this._pidList</code> (passed as plist argument the first time) 
    * being sorted.  If these are not sorted this will NOT work!
    * @param int dIndex
    * @param ArrayList<DecayProduct> oldlist
    */
    protected void setComboPidList(int dIndex, ArrayList<DecayProduct> plist, ArrayList<DecayProduct> oldlist) {

        for (int pIndex=0; pIndex<plist.size(); pIndex++) {
            DecayProduct p = plist.get(pIndex);
            if (this._decay.get(dIndex)!=p.pid()) { continue; } //
            ArrayList<DecayProduct> newlist = new ArrayList<DecayProduct>(oldlist); // IMPORTANT: declare new list
            newlist.add(p);
            ArrayList<DecayProduct> newplist = new ArrayList<DecayProduct>(plist); // IMPORTANT: declare new list
            newplist = newplist.subList(pIndex,newplist.size()); // IMPORTANT: Guarantees combos are unique (assumes this._decay and this._pidList are sorted)
            if (dIndex == this._decay.size()-1) { this._comboPidList.add(newlist); } //Important: -1!
            else { setComboPidList(dIndex+1,newplist,newlist); }
        }
    }

    /**
    * Recursive helper function to create combo list for all unique parent particle combinations in event.
    * Relies on <code>this._parents</code> and <code>this._parPidList</code> (passed as plist argument the first time)
    * being sorted.  If these are not sorted this will NOT work!
    * @param int dIndex
    * @param ArrayList<DecayProduct> oldlist
    */
    protected void setParComboPidList(int dIndex, ArrayList<DecayProduct> plist, ArrayList<DecayProduct> oldlist) {

        for (int pIndex=0; pIndex<plist.size(); pIndex++) {
            DecayProduct p = plist.get(pIndex);
            if (this._parents.get(dIndex)!=p.pid()) { continue; } //
            ArrayList<DecayProduct> newlist = new ArrayList<DecayProduct>(oldlist); // IMPORTANT: declare new list
            newlist.add(p);
            ArrayList<DecayProduct> newplist = new ArrayList<DecayProduct>(plist); // IMPORTANT: declare new list
            newplist = newplist.subList(pIndex,newplist.size()); // IMPORTANT: Guarantees combos are unique (assumes this._decay and this._pidList are sorted)
            if (dIndex == this._decay.size()-1) { this._parComboPidList.add(newlist); } //Important: -1!
            else { setParComboPidList(dIndex+1,newplist,newlist); }
        }
    }

    /**
    * Access list of all possible decay particle combinations identified by Lund pid.
    * @return ArrayList<ArrayList<DecayProduct>> _comboPidList
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
    * @return ArrayList<ArrayList<DecayProduct>> _parComboPidList
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
    * @return ArrayList<ArrayList<DecayProduct>> mergedComboPidList
    */
    protected ArrayList<ArrayList<DecayProduct>> getCheckedComboPidList() {

        ArrayList<ArrayList<DecayProduct>> checkedComboPidList = new ArrayList<ArrayList<DecayProduct>>();
        if (this._parents.size()==0) { return checkedComboPidList; }
        for (ArrayList<DecayProduct> combo : this.getComboPidList()) {
            for (ArrayList<DecayProduct> check : this.getParComboPidList()) {
                ArrayList<DecayProduct> addList = new ArrayList<DecayProduct>(combo);
                if (combo.get(0).parent()==check.get(0).daughter()) { checkedComboPidList.add(addList); }
            }
         }

        return checkedComboPidList;
    }

    /**
    * Merge list of all unique decay particle combinations identified by Lund pid
    * with a combo list from another decay object.
    * @param ArrayList<ArrayList<DecayProduct>>  mergeList
    * @return ArrayList<ArrayList<DecayProduct>> mergedComboPidList
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
    * @param ArrayList<ArrayList<DecayProduct>>  mergeList
    * @return ArrayList<ArrayList<DecayProduct>> mergedParComboPidList
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
            if (!this._charges.contains(charge) && !this._parCharges.contains(charge) && !this._constants.getQ(this._constants.getBeamPID())==charge) { continue; }
            int parent     = this._bank.getInt("parent", i);
            int daughter   = this._bank.getInt("daughter", i);
            
            // Get momenta and vertices
            double px = this._bank.getFloat("px", i);
            double py = this._bank.getFloat("py", i);
            double pz = this._bank.getFloat("pz", i);
            double vx = this._bank.getFloat("vx", i);
            double vy = this._bank.getFloat("vy", i);
            double vz = this._bank.getFloat("vz", i);

            DecayProduct p = new DecayProduct(pid,px,py,pz,vx,vy,vz,i+1,parent,daughter);
            p.charge(charge); //TODO: Necessary?
            //TODO: sector cut
            this._particleList.add(p);
        }
    }

    /**
    * Create list of lists of decay particles for each charge in specified decay channel.
    */
    protected void setChargeList() {

        if (this._particleList.size()==0) {this.setParticleListByCharge();}
        for (int i=0; i<this._charges.size(); i++) {
            int charge = this._charges.get(i);
            if (i!=0) { if (charge == this._charges.get(i-1)) { continue; } } //IMPORTANT: Just get unique entries.  This relies on the fact that decays is sorted!
            for (int j=0; j<this._particleList.size(); j++) {
                DecayProduct p = this._particleList.get(j);
                if (p.charge()==charge) {
                    if (this._chargeList.size()>0 && this._parCharges.size()!=0) {
                        if (this._chargeList.get(this._chargeList.size()-1).parent()!=p.parent()) { continue; } //IMPORTANT: Check that daughters in decay all have same parent.
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
                        if (this._parChargeList.get(this._parChargeList.size()-1).parent()!=p.parent()) { continue; } //IMPORTANT: Check that parents all have same daughter.
                    }
                    this._parChargeList.add(p);
                }
            } // for(int j=0; ...)
        } // for (int i=0; ...)
    } // setParChargeList()

    /**
    * Access list of all possible decay particle combinations identified by charge.
    * @return ArrayList<ArrayList<DecayProduct>> _chargeList
    */
    protected ArrayList<ArrayList<DecayProduct>> getChargeList() {

        if (this._chargeList.size()==0) { this.setChargeList(); }

        return this._chargeList;
    }

    /**
    * Access list of all possible decay particle combinations identified by charge.
    * @return ArrayList<ArrayList<DecayProduct>> _parChargeList
    */
    protected ArrayList<ArrayList<DecayProduct>> getParChargeList() {

        if (this._parChargeList.size()==0) { this.setParChargeList(); }

        return this._parChargeList;
    }

    /**
    * Recursive helper function to create combo list for all possible decay particle combinations in event.
    * Relies on <code>this._charges</code> and <code>this._chargeList</code> (passed as plist argument the first time) 
    * being sorted.  If these are not sorted this will NOT work!
    * @param int dIndex
    * @param ArrayList<DecayProduct> oldlist
    */
    protected void setComboChargeList(int dIndex, ArrayList<DecayProduct> plist, ArrayList<DecayProduct> oldlist) {

        for (int pIndex=0; pIndex<plist.size(); pIndex++) {
            DecayProduct p = plist.get(pIndex);
            if (this._charges.get(dIndex)!=p.charge()) { continue; } //
            ArrayList<DecayProduct> newlist = new ArrayList<DecayProduct>(oldlist); // IMPORTANT: declare new list
            newlist.add(p);
            ArrayList<DecayProduct> newplist = new ArrayList<DecayProduct>(plist); // IMPORTANT: declare new list
            newplist = newplist.subList(pIndex,newplist.size()); // IMPORTANT: Guarantees combos are unique (assumes this._charges and this._chargeList are sorted)
            if (dIndex == this._charges.size()-1) { this._comboChargeList.add(newlist); } //Important: -1!
            else { setComboChargeList(dIndex+1,newplist,newlist); }
        }
    }

    /**
    * Recursive helper function to create combo list for all unique parent particle combinations in event.
    * Relies on <code>this._parCharges</code> and <code>this._parChargeList</code> (passed as plist argument the first time)
    * being sorted.  If these are not sorted this will NOT work!
    * @param int dIndex
    * @param ArrayList<DecayProduct> oldlist
    */
    protected void setParComboChargeList(int dIndex, ArrayList<DecayProduct> plist, ArrayList<DecayProduct> oldlist) {

        for (int pIndex=0; pIndex<plist.size(); pIndex++) {
            DecayProduct p = plist.get(pIndex);
            if (this._parCharges.get(dIndex)!=p.charge()) { continue; } //
            ArrayList<DecayProduct> newlist = new ArrayList<DecayProduct>(oldlist); // IMPORTANT: declare new list
            newlist.add(p);
            ArrayList<DecayProduct> newplist = new ArrayList<DecayProduct>(plist); // IMPORTANT: declare new list
            newplist = newplist.subList(pIndex,newplist.size()); // IMPORTANT: Guarantees combos are unique (assumes this._charges and this._chargeList are sorted)
            if (dIndex == this._charges.size()-1) { this._parComboChargeList.add(newlist); } //Important: -1!
            else { setParComboChargeList(dIndex+1,newplist,newlist); }
        }
    }

    /**
    * Access list of all possible decay particle combinations identified by charge.
    * @return ArrayList<ArrayList<DecayProduct>> _comboChargeList
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
    * @return ArrayList<ArrayList<DecayProduct>> _parComboChargeList
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
    * @return ArrayList<ArrayList<DecayProduct>> mergedComboChargeList
    */
    protected ArrayList<ArrayList<DecayProduct>> getCheckedComboChargeList() {

        ArrayList<ArrayList<DecayProduct>> checkedComboChargeList = new ArrayList<ArrayList<DecayProduct>>();
        if (this._parCharges.size()==0) { return checkedComboChargeList; }
        for (ArrayList<DecayProduct> combo : this.getComboChargeList()) {
            for (ArrayList<DecayProduct> check : this.getParComboChargeList()) {
                ArrayList<DecayProduct> addList = new ArrayList<DecayProduct>(combo);
                if (combo.get(0).parent()==check.get(0).daughter()) { checkedComboChargeList.add(addList); }
            }
         }

        return checkedComboChargeList;
    }

    /**
    * Merge list of all unique decay particle combinations identified by charge
    * with a combo list from another decay object.
    * @param ArrayList<ArrayList<DecayProduct>>  mergeList
    * @return ArrayList<ArrayList<DecayProduct>> mergedComboChargeList
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
    * @param ArrayList<ArrayList<DecayProduct>>  mergeList
    * @return ArrayList<ArrayList<DecayProduct>> mergedParComboChargeList
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
    * @return DecayProduct beam
    */
    protected DecayProduct getBeam() {
        if (this._particleList.size()==0) { this.setParticleList(); }
	    if (this._particleList.size()==0) { return new DecayProduct(0,0,0,0); }
        try { return this._particleList[0]; } catch (Exception e) { System.out.println("*** WARNING *** Trying to access non-existent element of MC particle list."); return new DecayProduct(0,0,0,0); }
    }

    /**
    * Get Target as 1st entry from MC::Lund bank.
    * @return DecayProduct target
    */
    protected DecayProduct getTarget() {
        if (this._particleList.size()==0) { this.setParticleList(); }
	    if (this._particleList.size()==0) { return new DecayProduct(0,0,0,0); }
        try { return this._particleList[1]; } catch (Exception e) { System.out.println("*** WARNING *** Trying to access non-existent element of MC particle list."); return new DecayProduct(0,0,0,0); }
    }

    /**
    * Get virtual photon as 2nd entry from MC::Lund bank.
    * @return DecayProduct Q
    */
    protected DecayProduct getQ() {
        if (this._particleList.size()==0) { this.setParticleList(); }
        if (this._particleList.size()==0) { return new DecayProduct(0,0,0,0); }
        try { return this._particleList[2]; } catch (Exception e) { System.out.println("*** WARNING *** Trying to access non-existent element of MC particle list."); return new DecayProduct(0,0,0,0); }
    }

    /**
    * Get scattered beam as 3rd entry from MC::Lund bank.
    * @return DecayProduct beam
    */
    protected DecayProduct getScatteredBeam() {
        if (this._particleList.size()==0) { this.setParticleList(); } 
	    if (this._particleList.size()==0) { return new DecayProduct(0,0,0,0); }
        try { return this._particleList[3]; } catch (Exception e) { System.out.println("*** WARNING *** Trying to access non-existent element of MC particle list."); return new DecayProduct(0,0,0,0); }
    }

    /**
    * Get list of entries 0:3 from MC::Lund bank, corresponding to beam, target, Q, and scattered beam.
    * @return ArrayList<DecayProduct> parents
    */
    protected ArrayList<DecayProduct> getParents() {
        if (this._particleList.size()==0) { this.setParticleList(); }
	    if (this._particleList.size() < 4 ) { return new ArrayList<DecayProduct>(); }
        try { return this._particleList.subList(0,4); } catch (Exception e) { System.out.println("+ *** WARNING *** Trying to access non-existent element of MC particle list."); return new ArrayList<DecayProduct>(); }
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