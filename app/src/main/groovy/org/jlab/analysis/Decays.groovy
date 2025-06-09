package org.jlab.analysis;

// Groovy Imports
import groovy.transform.CompileStatic;

// Java Imports
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

// CLAS Physics Imports
import org.jlab.jnp.hipo4.data.*;
import org.jlab.jnp.hipo4.io.*;
import org.jlab.jnp.physics.*;

/**
* Searches a hipo event for all unique decay combinations for a given set of Lund pids.
*
* @version 1.0
* @author  Matthew McEneaney
*/

@CompileStatic
public class Decays {

    protected ArrayList<Integer>                 _decay;        // List of Lund pids with first entry as parent particle
    protected ArrayList<Integer>                 _charges;      // Mirrors this._decay but with corresponding electric charges in [e]
    protected HipoReader                         _reader;
    protected Integer                            _runnum;
    protected Event                              _event;
    protected Schema                             _schema;
    protected Bank                               _bank;
    protected Schema                             _rec_track_schema;
    protected Bank                               _rec_track_bank;
    protected ArrayList<DecayProduct>            _particleList;
    protected Constants                          _constants;
    protected FiducialCuts                       _FC;
    protected boolean                            _requireFC;
    protected MomentumCorrections                _MC;
    protected boolean                            _requireMC;
    protected ArrayList<DecayProduct>            _pidList;        // List of lists for each pid in decay
    protected ArrayList<ArrayList<DecayProduct>> _comboPidList;
    protected ArrayList<DecayProduct>            _chargeList;     // List of lists for each charge in decay
    protected ArrayList<ArrayList<DecayProduct>> _comboChargeList;
    protected boolean                            _setFullParticleList; // Flag indicating whether full  particle list has been set.

    /** 
    * Default constructor
    * @param decay
    * @param reader
    * @param runnum
    * @param event
    * @param constants
    * @param FC
    * @param requireFC
    * @param MC
    * @param requireMC
    */
    public Decays(ArrayList<Integer> decay, HipoReader reader, Integer runnum, Event event, Constants constants, FiducialCuts FC, boolean requireFC, MomentumCorrections MC, boolean requireMC) {

        this._decay     = decay;
        this._reader    = reader;
        this._runnum    = runnum;
        this._event     = event;
		this._schema    = this._reader.getSchemaFactory().getSchema("REC::Particle");
        this._bank      = new Bank(this._schema);
        this._rec_track_schema = this._reader.getSchemaFactory().getSchema("REC::Track");
        this._rec_track_bank   = new Bank(this._rec_track_schema);
        this._constants = constants;
        this._FC        = FC;
        this._requireFC = requireFC;
        this._MC        = MC;
        this._requireMC = requireMC;

        this._particleList    = new ArrayList<DecayProduct>();
        this._pidList         = new ArrayList<DecayProduct>();
        this._comboPidList    = new ArrayList<ArrayList<DecayProduct>>();
        this._chargeList      = new ArrayList<DecayProduct>();
        this._comboChargeList = new ArrayList<ArrayList<DecayProduct>>();
        
        this._charges = new ArrayList<Integer>();
        for (Integer pid : this._decay) { this._charges.add(this._constants.getCharge(pid)); }
        //IMPORTANT: this._decays must be sorted otherwise algorithm won't work. 
        // this._charges should NOT be sorted otherwise the selected charges will be out of 
        // order and not correspond to branch names set by pid in Analysis.groovy.
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
            double chi2pid = this._bank.getFloat("chi2pid", i);
            int status     = this._bank.getInt("status", i);
            int charge     = this._bank.getInt("charge", i);
            
            // Get momenta and vertices
            double px = this._bank.getFloat("px", i);
            double py = this._bank.getFloat("py", i);
            double pz = this._bank.getFloat("pz", i);
            double bt = this._bank.getFloat("beta", i);
            double vx = this._bank.getFloat("vx", i);
            double vy = this._bank.getFloat("vy", i);
            double vz = this._bank.getFloat("vz", i);
            double vt = this._bank.getFloat("vt", i);

            // Check REC::Track for matching entries
            this._event.read(this._rec_track_bank);
            int detector = -1;
            int sector = -1;
            int detector_status = -9999;
            double detector_chi2ndf = -9999.0;
            for (int j=0; j<this._rec_track_bank.getRows(); j++) {

                int pindex = this._rec_track_bank.getInt("pindex", j);
                if (pindex==i) {
                    detector = this._rec_track_bank.getInt("detector", j);
                    sector   = this._rec_track_bank.getInt("sector", j);
                    detector_status = this._rec_track_bank.getInt("status", j);
                    double chi2 = this._rec_track_bank.getFloat("chi2", j);
                    double ndf = (double)this._rec_track_bank.getInt("NDF", j);
                    detector_chi2ndf = (double)chi2/ndf;
                    break; //NOTE: Important: only add first match.  Matches should be unique but need to double check this.
                }
            }
            this._event.read(this._bank);

            DecayProduct p = new DecayProduct(pid,px,py,pz,bt,vx,vy,vz,vt,chi2pid,status);
            p.charge(charge); //TODO: Is this necessary??
            p.index(i);
            p.detector(detector);
            p.sector(sector);
            p.detector_status(detector_status);
            p.detector_chi2ndf(detector_chi2ndf); //TODO: Figure out best way to map to this......
            if (this._requireMC) p = this._MC.applyMomentumCorrection(p);
            if (!this._requireFC) { this._particleList.add(p); continue; }
            // if (this._sectorCut) //TODO
            if (this._requireFC) { if (!this._FC.applyCuts(this._runnum, i, pid, this._event, this._reader)) continue; }
            this._particleList.add(p);
        }
    }

    /**
    * Access full particle list for event.
    * @return _particleList
    */
    protected ArrayList<DecayProduct> getFullParticleList() {

        if (!this._setFullParticleList) this.setFullParticleList();

        return this._particleList;
    }

    /**
    * Set list of particles for event matching pids in this._decay.
    */
    protected void setParticleList() {

        this._event.read(this._bank);
        for (int i = 0; i < this._bank.getRows(); i++) {

            // Get pid, chi2pid, status and charge
            int pid        = this._bank.getInt("pid", i);
            if (!this._decay.contains(pid) && !this._constants.getBeamPID()==pid) { continue; }
            int charge     = this._bank.getInt("charge", i);
            double chi2pid = this._bank.getFloat("chi2pid", i);
            int status     = this._bank.getInt("status", i);
            
            // Get momenta and vertices
            double px = this._bank.getFloat("px", i);
            double py = this._bank.getFloat("py", i);
            double pz = this._bank.getFloat("pz", i);
            double bt = this._bank.getFloat("beta", i);
            double vx = this._bank.getFloat("vx", i);
            double vy = this._bank.getFloat("vy", i);
            double vz = this._bank.getFloat("vz", i);
            double vt = this._bank.getFloat("vt", i);

            // Check REC::Track for matching entries
            this._event.read(this._rec_track_bank);
            int detector = -1;
            int sector = -1;
            int detector_status = -9999;
            double detector_chi2ndf = -9999.0;
            for (int j=0; j<this._rec_track_bank.getRows(); j++) {

                int pindex = this._rec_track_bank.getInt("pindex", j);
                if (pindex==i) {
                    detector = this._rec_track_bank.getInt("detector", j);
                    sector   = this._rec_track_bank.getInt("sector", j);
                    detector_status = this._rec_track_bank.getInt("status", j);
                    double chi2 = this._rec_track_bank.getFloat("chi2", j);
                    double ndf = (double)this._rec_track_bank.getInt("NDF", j);
                    detector_chi2ndf = (double)chi2/ndf;
                    break; //NOTE: Important: only add first match.  Matches should be unique but need to double check this.
                }
            }
            this._event.read(this._bank);

            DecayProduct p = new DecayProduct(pid,px,py,pz,bt,vx,vy,vz,vt,chi2pid,status);
            p.charge(charge); //TODO: Is this necessary?
            p.index(i);
            p.detector(detector);
            p.sector(sector);
            p.detector_status(detector_status);
            p.detector_chi2ndf(detector_chi2ndf); //TODO: Figure out best way to map to this......
            if (this._requireMC) p = this._MC.applyMomentumCorrection(p);
            if (!this._requireFC) { this._particleList.add(p); continue; }
            // if (this._sectorCut) //TODO
            if (this._requireFC) { if (!this._FC.applyCuts(this._runnum, i, pid, this._event, this._reader)) continue; }
            this._particleList.add(p);
        }
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
    * Create list of lists of decay particles for each pid in specified decay channel.
    */
    protected void setPidList() {

        if (this._particleList.size()==0) {this.setParticleList();}
        for (int i=0; i<this._decay.size(); i++) {
            int pid = this._decay.get(i);
            if (i!=0) { if (pid == this._decay.get(i-1)) { continue; } } //IMPORTANT: Just get unique entries.  This relies on the fact that decays is sorted!
            for (int j=0; j<this._particleList.size(); j++) {
                DecayProduct p = this._particleList.get(j);
                if (p.pid()==pid) { this._pidList.add(p); }
            }
        }
    }

    /**
    * Access list of all possible decay particle combinations identified by Lund pid.
    * @return _pidList
    */
    protected ArrayList<DecayProduct> getPidList() {

        if (this._pidList.size()==0) { this.setPidList(); }

        return this._pidList;
    }

    /**
    * Recursive helper function to create combo list for all possible decay particle combinations in event.
    * Relies on this._decay and this._pidList (passed as plist argument the first time) 
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
                mergedComboPidList.add(addList)
            }
         }

        return mergedComboPidList;
    }

    /**
    * Merge list of all unique decay particle combinations from REC::Particle
    * using a mc matching map of REC::Particle to MC::Lund bank indices (not Lund indices)
    * and the full list of MC::Lund particles.  This essentially duplicates each
    * particle in each combo list with an mc counterpart appended to the end of the combo list.
    * @param recMatchingMap
    * @param mcFullParticleList
    * @return mergedComboPidList 
    */
    protected ArrayList<ArrayList<DecayProduct>> mergeComboPidList(LinkedHashMap<Integer,Integer> recMatchingMap, ArrayList<DecayProduct> mcFullParticleList) {

        ArrayList<ArrayList<DecayProduct>> mergedComboPidList = new ArrayList<ArrayList<DecayProduct>>();
        for (ArrayList<DecayProduct> combo : this.getComboPidList()) {
            ArrayList<DecayProduct> mergeCombo = new ArrayList<DecayProduct>();
            for (DecayProduct p : combo) {
                int idx_mc = recMatchingMap.get(p.index());
                DecayProduct p_mc;
                if (idx_mc>=0) { p_mc = mcFullParticleList.get(idx_mc); }
                else { p_mc = new DecayProduct(0,0,0,0,0); }
                mergeCombo.add(p_mc);
            }
            ArrayList<DecayProduct> addList = new ArrayList<DecayProduct>(combo);
            addList.addAll(mergeCombo);
            mergedComboPidList.add(addList);
        }

        return mergedComboPidList;
    }

    /**
    * Set list of particles for event matching charges in this._charges.
    */
    protected void setParticleListByCharge() {

        this._event.read(this._bank);
        for (int i = 0; i < this._bank.getRows(); i++) { //NOTE: Should add option here so you don't add in scattered electron into combos...

            // Get pid, chi2pid, status and charge
            int charge     = this._bank.getInt("charge", i);
            if (!this._charges.contains(charge) && i!=0) { continue; }
            int pid        = this._bank.getInt("pid", i);
            double chi2pid = this._bank.getFloat("chi2pid", i);
            int status     = this._bank.getInt("status", i);
            
            // Get momenta and vertices
            double px = this._bank.getFloat("px", i);
            double py = this._bank.getFloat("py", i);
            double pz = this._bank.getFloat("pz", i);
            double bt = this._bank.getFloat("beta", i);
            double vx = this._bank.getFloat("vx", i);
            double vy = this._bank.getFloat("vy", i);
            double vz = this._bank.getFloat("vz", i);
            double vt = this._bank.getFloat("vt", i);

            // Check REC::Track for matching entries
            this._event.read(this._rec_track_bank);
            int detector = -1;
            int sector = -1;
            int detector_status = -9999;
            double detector_chi2ndf = -9999.0;
            for (int j=0; j<this._rec_track_bank.getRows(); j++) {

                int pindex = this._rec_track_bank.getInt("pindex", j);
                if (pindex==i) {
                    detector = this._rec_track_bank.getInt("detector", j);
                    sector   = this._rec_track_bank.getInt("sector", j);
                    detector_status = this._rec_track_bank.getInt("status", j);
                    double chi2 = this._rec_track_bank.getFloat("chi2", j);
                    double ndf = (double)this._rec_track_bank.getInt("NDF", j);
                    detector_chi2ndf = (double)chi2/ndf;
                    break; //NOTE: Important: only add first match.  Matches should be unique but need to double check this.
                }
            }
            this._event.read(this._bank);

            DecayProduct p = new DecayProduct(pid,px,py,pz,bt,vx,vy,vz,vt,chi2pid,status);
            p.pid(pid); //NOTE: Not really sure why this is a problem and needs to be added...
            p.charge(charge);
            p.detector(detector);
            p.sector(sector);
            p.detector_status(detector_status);
            p.detector_chi2ndf(detector_chi2ndf); //TODO: Figure out best way to map to this......
            if (this._requireMC) p = this._MC.applyMomentumCorrection(p);
            if (!this._requireFC) { this._particleList.add(p); continue; }
            // if (this._sectorCut) //TODO
            if (this._requireFC) { if (!this._FC.applyCuts(this._runnum, i, pid, this._event, this._reader)) continue; }
            this._particleList.add(p);
        }
    }

    /**
    * Create list of lists of decay particles for each charge in specified decay channel.
    */
    protected void setChargeList() {

        if (this._particleList.size()==0) {this.setParticleListByCharge();}
        this._chargeList = new ArrayList<DecayProduct>(); //NOTE: Updated 11/4/22, just add entries with matching charges for speed.
        for (int i=1; i<this._particleList.size(); i++) { //NOTE: Exclude scattered electron which should have index 0 if it's the trigger particle.
            DecayProduct p = this._particleList.get(i);
            if (this._charges.contains(p.charge())) { this._chargeList.add(p); }
        }
    }

    /**
    * Access list of all possible decay particle combinations identified by charge.
    * @return _chargeList
    */
    protected ArrayList<DecayProduct> getChargeList() {

        if (this._chargeList.size()==0) { this.setChargeList(); }

        return this._chargeList;
    }

    /**
    * Recursive helper function to create combo list for all possible decay particle combinations in event.
    * Relies on this._charges and this._chargeList (passed as plist argument the first time) 
    * being sorted.  If these are not sorted this will NOT work!
    * @param dIndex
    * @param oldlist
    */
    protected void setComboChargeList(int dIndex, ArrayList<DecayProduct> plist, ArrayList<DecayProduct> oldlist) {

        for (int pIndex=0; pIndex<plist.size(); pIndex++) {
            DecayProduct p = plist.get(pIndex);
            if (this._charges.get(dIndex)!=p.charge()) { continue; } //NOTE: Don't sort this._charges instead keep same order as this._decays so that it will have the correct order when added to the leaves which are named by the pid.
            ArrayList<DecayProduct> newlist = new ArrayList<DecayProduct>(oldlist); // IMPORTANT: declare new list
            newlist.add(p);
            ArrayList<DecayProduct> newplist = new ArrayList<DecayProduct>(plist); // IMPORTANT: declare new list
            newplist = (ArrayList<DecayProduct>)newplist.minus([pIndex]); // IMPORTANT: Guarantees combos are unique (assumes this._charges and this._chargeList are sorted)
            if (dIndex == this._charges.size()-1) { this._comboChargeList.add(newlist); } //Important: -1!
            else { setComboChargeList(dIndex+1,newplist,newlist); }
        }
    }

    /**
    * Access list of all possible decay particle combinations identified by charge.
    * @return _comboChargeList
    */
    protected ArrayList<ArrayList<DecayProduct>> getComboChargeList() {

        if (this._comboChargeList.size()!=0) { return this._comboChargeList; }
        if (this._chargeList.size()==0) { this.setChargeList(); } //NOTE: STILL GRABS SCATTERED ELECTRON IN INDEX 0 FOR this._particleList.
        ArrayList<DecayProduct> newlist = new ArrayList<DecayProduct>();
        setComboChargeList(0,this._chargeList,newlist);

        return this._comboChargeList;
    }

    /**
    * Merge list of all unique decay particle combinations identified by charge
    * with a combo list from another decay object.
    * @param mergeList
    * @return mergedComboPidList
    */
    protected ArrayList<ArrayList<DecayProduct>> mergeComboChargeList(ArrayList<ArrayList<DecayProduct>> mergeList) {

        ArrayList<ArrayList<DecayProduct>> mergedComboPidList = new ArrayList<ArrayList<DecayProduct>>();
        for (ArrayList<DecayProduct> combo : this.getComboChargeList()) { 
            for (ArrayList<DecayProduct> mergeCombo : mergeList) {
                ArrayList<DecayProduct> addList = new ArrayList<DecayProduct>(combo);
                addList.addAll(mergeCombo);
                mergedComboPidList.add(addList)
            }
         }

        return mergedComboPidList;
    }

    /**
    * Merge list of all unique decay particle combinations from REC::Particle identified by charge
    * using a mc matching map of REC::Particle to MC::Lund bank indices (not Lund indices)
    * and the full list of MC::Lund particles.  This essentially duplicates each
    * particle in each combo list with an mc counterpart appended to the end of the combo list.
    * @param recMatchingMap
    * @param mcFullParticleList
    * @return mergedComboPidList
    */
    protected ArrayList<ArrayList<DecayProduct>> mergeComboChargeList(LinkedHashMap<Integer,Integer> recMatchingMap, ArrayList<DecayProduct> mcFullParticleList) {

        ArrayList<ArrayList<DecayProduct>> mergedComboPidList = new ArrayList<ArrayList<DecayProduct>>();
        for (ArrayList<DecayProduct> combo : this.getComboChargeList()) {
            ArrayList<DecayProduct> mergeCombo = new ArrayList<DecayProduct>();
            for (DecayProduct p : combo) {
                int idx_mc = recMatchingMap.get(p.index());
                DecayProduct p_mc;
                if (idx_mc>=0) { p_mc = mcFullParticleList.get(idx_mc); }
                else { p_mc = new DecayProduct(0,0,0,0,0); }
                mergeCombo.add(p_mc);
            }
            ArrayList<DecayProduct> addList = new ArrayList<DecayProduct>(combo);
            addList.addAll(mergeCombo);
            mergedComboPidList.add(addList);
        }

        return mergedComboPidList;
    }

     /**
    * Find scattered beam with default limits on absolute value of chi2pid
    * and on the status: |chi2pid|<3 && stat<=-2000.
    * @return beam
    */
    protected DecayProduct getScatteredBeam() {

        DecayProduct beam = new DecayProduct(0,0,0,0,0);
        if (this._particleList.size()==0) { this.setParticleList(); }
        for (DecayProduct p : this._particleList) {
            if (p.p()>beam.p() && p.pid()==this._constants.getBeamPID() && Math.abs(p.chi2pid())<3 && p.status()<=-2000) { beam.clone(p); }
        }

        return beam;
    }

    /**
    * Find scattered beam with limits on absolute value of chi2pid
    * and on the staus.  status limit looks for particles with status 
    * higher/lower than the limit depending on whether the status is 
    * positive/negative.
    * @param chi2pid
    * @param stat
    * @return beam
    */
    protected DecayProduct getScatteredBeam(float chi2pid, int stat) {

        DecayProduct beam = new DecayProduct(0,0,0,0,0);
        int sign = 1;
        if (this._particleList.size()==0) { this.setParticleList(); }
        if (stat>0) { sign = -1; }
        for (DecayProduct p : this._particleList) {
            if (p.p()>beam.p() && p.pid()==this._constants.getBeamPID() && Math.abs(p.chi2pid())<chi2pid && sign * p.status()<=stat) { beam.clone(p); }
        }

        return beam;
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
