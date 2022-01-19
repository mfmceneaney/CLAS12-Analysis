package org.jlab.analysis;

// Java Imports
import java.io.*;
import java.util.*;

// CLAS Physics Imports
import org.jlab.jnp.hipo4.data.*;
import org.jlab.jnp.hipo4.io.*;
import org.jlab.jnp.physics.*;

/**
* Encapsulates some of the more common SIDIS kinematics calculations.
* For more specific/less general analyses you may want to do your own calculations,
* but you can add variables to compute by lambda expressions based off the ConfigVar
* and SIDISVar interfaces.
*
* @version 1.0
* @author  Matthew McEneaney
*/

public class Kinematics {

    protected static ArrayList<Integer> _decay;        // List of Lund pids with first entry as parent particle
    protected static ArrayList<Integer> _parents;      // List of parent Lund pids without parent particle from _decay
    protected static Constants          _constants;
    protected String[]                  _defaults = ["Q2", "nu", "y", "x", "W", "z", "xF", "pT", "phperp", "mass","mx"];
    protected HashMap<String,ConfigVar> _configs;      // HashMap of name to lambda expression for computing
    protected HashMap<String,SIDISVar>  _vars;         // HashMap of name to lambda expression for computing
    protected HashMap<String,Cut>       _cuts;         // HashMap of name to boolean(double) lambda expression cut

    // Options
    protected static boolean _strict        = false;    // use strict pid to mass assignment in kinematics calculations
    protected static boolean _require_e     = true;     // require electron in reconstruction
    protected static boolean _addEvNum      = false;    // include event number in TNTuple
    protected static boolean _addRunNum     = false;    // include event number in TNTuple
    protected static boolean _addTorus      = false;    // include torus scale in TNTuple
    protected static boolean _addHelicity   = true;     // include helicity in TNTuple
    protected static boolean _addBeamCharge = false;    // include beam charge in TNTuple
    protected static boolean _addLiveTime   = false;    // include live time in TNTuple
    protected static boolean _addStartTime  = false;    // include start time in TNTuple
    protected static boolean _addRFTime     = false;    // include RF time in TNTuple
    protected static boolean _addLambdaKin  = false;    // include special two particle decay kinematics from \Lambda^0 analysis

    // built in lambdas
    protected ConfigVar _getEventNum = (HipoReader reader, Event event) -> {
        Schema schema = reader.getSchemaFactory().getSchema("RUN::config");
        Bank bank     = new Bank(schema);
        event.read(bank);
		double num = bank.getInt("event",0);
		return num; };

    protected ConfigVar _getRunNum = (HipoReader reader, Event event) -> {
        Schema schema = reader.getSchemaFactory().getSchema("RUN::config");
        Bank bank     = new Bank(schema);
        event.read(bank);
		double num = bank.getInt("run",0);
		return num; };

    protected ConfigVar _getTorus = (HipoReader reader, Event event) -> {
        Schema schema = reader.getSchemaFactory().getSchema("RUN::config");
        Bank bank     = new Bank(schema);
        event.read(bank);
		double torus = (double) bank.getFloat("torus",0);
		return torus; };

    protected ConfigVar _getHelicity = (HipoReader reader, Event event) -> {
        double helicity  = 0.0;
		Schema schema = reader.getSchemaFactory().getSchema("REC::Event");
        Bank bank     = new Bank(schema);
        event.read(bank);
		helicity = (double) bank.getByte("helicity",0);
		return helicity; };

    protected ConfigVar _getHelicityMC = (HipoReader reader, Event event) -> {
        double helicity  = 0.0;
        Schema schema = reader.getSchemaFactory().getSchema("MC::Header");
        Bank bank     = new Bank(schema);
        event.read(bank);
        helicity = (double) bank.getFloat("helicity",0);
        return helicity; };

    protected ConfigVar _getBeamCharge = (HipoReader reader, Event event) -> {
        double bc = 9999;
		Schema schema = reader.getSchemaFactory().getSchema("REC::Event");
        Bank bank     = new Bank(schema);
        event.read(bank);
		bc = (double) bank.getFloat("beamCharge",0);
		return bc; };

    protected ConfigVar _getLiveTime = (HipoReader reader, Event event) -> {
        double liveTime = 9999;
		Schema schema = reader.getSchemaFactory().getSchema("REC::Event");
        Bank bank     = new Bank(schema);
        event.read(bank);
		liveTime = (double) bank.getFloat("liveTime",0);
		return liveTime; };

    protected ConfigVar _getStartTime = (HipoReader reader, Event event) -> {
        double startTime = 9999;
		Schema schema = reader.getSchemaFactory().getSchema("REC::Event");
        Bank bank     = new Bank(schema);
        event.read(bank);
		startTime = (double) bank.getFloat("startTime",0);
		return startTime; };

    protected ConfigVar _getRFTime = (HipoReader reader, Event event) -> {
        double RFTime = 9999;
		Schema schema = reader.getSchemaFactory().getSchema("REC::Event");
        Bank bank     = new Bank(schema);
        event.read(bank);
		RFTime = (double) bank.getFloat("RFTime",0);
		return RFTime; };

    /**
    * Constructor stub
    * @param Constants constants
    * @param ArrayList<Integer> decay
    */
    public Kinematics(Constants constants, ArrayList<Integer> decay) {

        this._constants = constants;
        this._decay     = decay;
        this._configs   = new HashMap<String,ConfigVar>();
        this._vars      = new HashMap<String,SIDISVar>();
        this._cuts      = new HashMap<String,Cut>();

        this._configs.put("helicity",this._getHelicity);
    }

    /**
    * Set constants object for use in calculations.
    * @param Constants constants
    */
    protected void setConstants(Constants constants) {

        this._constants = constants;
    }

    /**
    * Set target mass in constants.
    * @param double TM
    */
    protected void setTargetM(double TM) {

        this._constants.setTargetM(TM);
    }

     /**
    * Set beam energy in constants.
    * @param double BE
    */
    protected void setBeamE(double BE) {

        this._constants.setBeamE(BE);
    }

    /**
    * Access constants object for analysis.
    * @return Constants _constants
    */
    protected Constants getConstants() {

        return this._constants;
    }

    /**
    * Set list of Lund pids for decay.  Parent particle is always first.
    * @param ArrayList<Integer> decay
    */
    protected void setDecay(ArrayList<Integer> decay) {

        this._decay = decay;
    }

    /**
    * Access list of decay lund pids.
    * @return ArrayList<Integer> _decay
    */
    protected ArrayList<Integer> getDecay() {

        return this._decay;
    }

    /**
    * Set list of parent Lund pids for decay in MC::Lund bank.
    * Do not include parent particle from this._decay.
    * @param ArrayList<Integer> parents
    */
    protected void setParents(ArrayList<Integer> parents) {

        this._parents = parents;
    }

    /**
    * Access list of parent Lund pids.
    * @return ArrayList<Integer> _parents
    */
    protected ArrayList<Integer> getParents() {

        return this._parents;
    }

    protected void setStrict(boolean strict) {this._strict = strict;}
    protected boolean strict() {return this._strict;}
    protected void setRequireE(boolean require_e) {this._require_e = require_e;}
    protected boolean requireE() {return this._require_e;}
    protected void setAddEvNum(boolean addEvNum) {this._addEvNum = addEvNum; if (addEvNum) {this._configs.put("event",this._getEventNum);} else {this._configs.remove("event");}}
    protected boolean addEvNum() {return this._addEvNum;}
    protected void setAddRunNum(boolean addRunNum) {this._addRunNum = addRunNum; if (addRunNum) {this._configs.put("run",this._getRunNum);} else {this._configs.remove("run");}}
    protected boolean addRunNum() {return this._addRunNum;}
    protected void setAddTorus(boolean addTorus) {this._addTorus = addTorus; if (addTorus) {this._configs.put("torus",this._getEventNum);} else {this._configs.remove("event");}}
    protected boolean addTorus() {return this._addTorus;}
    protected void setAddHelicity(boolean addHelicity) {this._addHelicity = addHelicity; if (addHelicity) {this._configs.put("helicity",this._getEventNum);} else {this._configs.remove("event");}}
    protected boolean addHelicity() {return this._addHelicity;}
    protected void setAddBeamCharge(boolean addBeamCharge) {this._addBeamCharge = addBeamCharge; if (addBeamCharge) {this._configs.put("beamCharge",this._getEventNum);} else {this._configs.remove("event");}}
    protected boolean addBeamCharge() {return this._addBeamCharge;}
    protected void setAddLiveTime(boolean addLiveTime) {this._addLiveTime = addLiveTime; if (addLiveTime) {this._configs.put("liveTime",this._getEventNum);} else {this._configs.remove("event");}}
    protected boolean addLiveTime() {return this._addLiveTime;}
    protected void setAddStartTime(boolean addStartTime) {this._addStartTime = addStartTime; if (addStartTime) {this._configs.put("startTime",this._getEventNum);} else {this._configs.remove("event");}}
    protected boolean addStartTime() {return this._addStartTime;}
    protected void setAddRFTime(boolean addRFTime) {this._addRFTime = addRFTime; if (addRFTime) {this._configs.put("RFTime",this._getEventNum);} else {this._configs.remove("event");}}
    protected boolean addRFTime() {return this._addRFTime;}
    protected void setAddLambdaKin(boolean addLambdaKin) {
        this._addLambdaKin = addLambdaKin;
        String[] lkin = [ "alpha" , "costheta1" , "costheta2" , "col", "costhetab2b"]; String[] arr = new String[this._defaults.length + lkin.length];
        int i = 0;
	for (String defaults : this._defaults) { arr[i] = defaults; i++; }
	for (String kin : lkin) { arr[i] = kin; i++; }
	this._defaults = arr;
    }
    protected boolean addLambdaKin() {return this._addLambdaKin;}

     /**
    * Access keyset for all variables to access, useful for setting TNTuple entry names.
    * @return String[] keySet
    */
    protected String[] keySet() { // can call from Analysis object

        String[] arr = new String[this._defaults.length + this._configs.size() + this._vars.size()];
        int i = 0;
        for (String con : this._configs.keySet()) { arr[i] = con; i++; }
        for (String defaults : this._defaults)         { arr[i] = defaults; i++; }
        for (String var : this._vars.keySet())    { arr[i] = var; i++; }
        return arr;
    }

     /**
    * Access string array for names of default kinematic variables: Q2, nu, W, Walt x, xF, y, z, pT, phperp, mass.
    * @return String[] _defaults
    */
    protected String[] getDefaults() { // can call from Analysis object

        return this._defaults;
    }

    /**
    * Set hashmap of configuration variable names to lambda expressions for access.
    * @param HashMap<String,ConfigVar> configs
    */
    protected void setConfigVars(HashMap<String,ConfigVar> configs) { // can call from Analysis object

        this._configs = configs;
    }

    /**
    * Add hashmap of configuration variable names to lambda expressions for access.
    * @param HashMap<String,ConfigVar> configs
    */
    protected void addConfigVars(HashMap<String,ConfigVar> configs) { // can call from Analysis object

        for (String key : configs.keySet()) { this._configs.put(key,configs.get(key)); }
    }

     /**
    * Add configuration variable name and lambda expression for access.
    * @param String name
    * @param ConfigVar config
    */
    protected void addVar(String name, ConfigVar config) { // can call from Analysis object

        this._configs.put(name,config);
    }

     /**
    * Access hashmap of configuration variable names lambda expressions for access.
    * @return HashMap<String,ConfigVar> _configs
    */
    protected HashMap<String,ConfigVar> getConfigVars() { // can call from Analysis object

        return this._configs;
    }

    /**
    * Set hashmap of kinematic names lambda expressions for computation.
    * @param HashMap<String,SIDISVar> vars
    */
    protected void setSIDISVars(HashMap<String,SIDISVar> vars) { // can call from Analysis object

        this._vars = vars;
    }

    /**
    * Add hashmap of kinematic names lambda expressions for computation.
    * @param HashMap<String,SIDISVar> vars
    */
    protected void addSIDISVars(HashMap<String,SIDISVar> vars) { // can call from Analysis object

        for (String key : vars.keySet()) { this._vars.put(key,vars.get(key)); }
    }

     /**
    * Add kinematic name and lambda expression for computation.
    * @param String name
    * @param SIDISVar var
    */
    protected void addVar(String name, SIDISVar var) { // can call from Analysis object

        this._vars.put(name,var);
    }

    /**
    * Access hashmap of kinematic names lambda expressions for computation.
    * @return HashMap<String,SIDISVar> _vars
    */
    protected HashMap<String,SIDISVar> getSIDISVars() { // can call from Analysis object

        return this._vars;
    }

    /**
    * Set hashmap of kinematic names to boolean .cut(double) lambda expression cuts.
    * @param HashMap<String,Cut> cuts
    */
    protected void setCuts(HashMap<String,Cut> cuts) { // can call from Analysis object

        this._cuts = cuts;
    }

    /**
    * Add entries from ahashmap of kinematic names to min, max cuts.
    * @param HashMap<String, Cut> cuts
    */
    protected void addCuts(HashMap<String, Cut> cuts) { // can call from Analysis object

        for (String key : cuts.keySet()) { this._cuts.put(key,cuts.get(key)); }
    }

    /**
    * Add entries from ahashmap of kinematic names to min, max cuts.
    * @param String name
    * @param Cut cut
    */
    protected void addCut(String name, Cut cut) { // can call from Analysis object

        this._cuts.put(name,cut);
    }

    /**
    * Access hashmap of kinematic names to min, max cuts.
    * @return HashMap<String, Cut> _cuts
    */
    protected HashMap<String, Cut> getCuts() { // can call from Analysis object

        return this._cuts;
    }

    /**
    * @param HipoReader reader
    * @param Event event
    * @return HashMap<String, Double> configs
    */
    private HashMap<String, Double> getConfigVariables(HipoReader reader, Event event) {

        HashMap<String, Double> configs = new HashMap<String, Double>();
        for (String var : this._configs.keySet()) { configs.put(var,this._configs.get(var).get(reader,event)); }
        return configs;
    }

    /**
    * @param ArrayList<DecayProduct> plist
    * @param DecayProduct beam
    * @return HashMap<String, Double> vars
    */
    private HashMap<String, Double> getSIDISVariables(ArrayList<DecayProduct> plist, DecayProduct beam) {

        HashMap<String, Double> vars = new HashMap<String, Double>();
        for (String var : this._vars.keySet()) { vars.put(var,this._vars.get(var).get(this._constants,this._decay,plist,beam)); }
        return vars;
    }

    /**
    * Find scattered beam with default limits on absolute value of chi2pid
    * and on the status: |chi2pid|<3 && status<=-2000.
    * @param ArrayList<DecayProduct> list
    * @return DecayProduct beam
    */
    protected DecayProduct getScatteredBeam(ArrayList<DecayProduct> list) { // TODO: kind of obviated by getScatteredBeam method for Decays/MCDecays classes

        DecayProduct beam = new DecayProduct(0,0,0,0);
        for (DecayProduct p : list) {
            if (p.p()>beam.p() && p.pid()==this._constants.getBeamPID() && Math.abs(p.chi2pid())<3 && p.status()<=-2000) {beam.clone(p);}
        }

        return beam;
    }

    /**
    * Find scattered beam with limits on absolute value of chi2pid
    * and on the staus.  Status limit looks for particles with status 
    * higher/lower than the limit depending on whether the status is 
    * positive/negative.
    * @param ArrayList<DecayProduct> list
    * @param float chi2pid
    * @param int status
    * @return DecayProduct beam
    */
    protected DecayProduct getScatteredBeam(ArrayList<DecayProduct> list, float chi2pid, int status) { // TODO: kind of obviated by getScatteredBeam method for Decays/MCDecays classes

        DecayProduct beam = new DecayProduct(0,0,0,0);
        int sign = 1;
        if (status>0) { sign = -1; }
        for (DecayProduct p : list) {
            if (p.p()>beam.p() && p.pid()==this._constants.getBeamPID() && Math.abs(p.chi2pid())<chi2pid && sign*p.status()<=status) { beam.clone(p); }
        }

        return beam;
    }

    /**
    * Compute additional kinematics particular to \Lambda^0 Analysis 
    * but potentially useful for other two body decays.
    * @param HashMap<String, Double> kinematics
    * @param ArrayList<LorentzVector> lvList
    * @param LorentzVector lv_parent
    * @param LorentzVector q
    */
    protected void getLKVars(HashMap<String, Double> kinematics, ArrayList<LorentzVector> lvList, LorentzVector lv_parent, LorentzVector q) {

        if (!this._addLambdaKin) { return; }

        // Compute longitudinal momentum asymmetry alpha
        double alpha = (double) 0.0; double sum = (double) 0.0;
        double sign = 1; int i = 0;
        for (LorentzVector lv : lvList) {
            if (this._constants.getCharge(this._decay.get(i))<0) {sign = -1;}
            alpha += sign * lv_parent.vect().dot(lv.vect())/lv_parent.vect().mag(); sum += lv_parent.vect().dot(lv.vect())/lv_parent.vect().mag(); i++;
        }
        alpha /= sum;

        // set cos theta lorentz vectors
        Vector3 boost = lv_parent.boostVector();
        boost.negative();  
        LorentzVector boostedPhoton = new LorentzVector(q);
        boostedPhoton.boost(boost);
        Integer posPid = this._decay.get(1); for (Integer pid : this._decay) { if (this._constants.getCharge(pid)>0 && pid!=this._decay.get(0)) { posPid = pid; break; } } // Grab first positive particle in given decay particles for calculating costheta
        LorentzVector boostedProton = new LorentzVector(lvList.get(this._decay.indexOf(posPid) - 1)); // IMPORTANT make a new one otherwise it modifies the list entry
        boostedProton.boost(boost);
        double costheta1 = boostedProton.vect().dot(lv_parent.vect()) / (boostedProton.vect().mag() * lv_parent.vect().mag());
        double costheta2 = boostedProton.vect().dot(boostedPhoton.vect()) / (boostedProton.vect().mag() * boostedPhoton.vect().mag());

        kinematics.put("alpha",alpha);
        kinematics.put("costheta1",costheta1);
        kinematics.put("costheta2",costheta2);

    }

    /**
    * Compute colinearity cos(theta_colinearity) for V^0 2-body decays.
    * @param HashMap<String, Double> kinematics
    * @param ArrayList<DecayProduct> particles
    * @param LorentzVector lv_parent
    * @param DecayProduct beam
    */
    protected void getColinearity(HashMap<String, Double> kinematics, ArrayList<DecayProduct> list, LorentzVector lv_parent, DecayProduct beam) {

        // if (!this._addColinearity) { return; }

        // Compute Colinearity
        Vector3 vtx = list.get(0).vtx(); // IMPORTANT: initiate at the first entry
        for (int i=1; i<list.size(); i++) { //IMPORTANT: start at 0 since beam is separate from list, but then we want the difference so start at 1
            DecayProduct p = list.get(i);
            vtx.add(p.vtx());
        }
        vtx.setMagThetaPhi(vtx.mag()/list.size(),vtx.theta(),vtx.phi()); // Divide by total # particles in decay list
        vtx.sub(beam.vtx());
        double col = lv_parent.vect().dot(vtx) / (lv_parent.vect().mag() * vtx.mag());

        kinematics.put("col",col);

    }

    /**
    * Compute back-to-back angle cos(theta_b2b) for 2-body decays.
    * @param HashMap<String, Double> kinematics
    * @param ArrayList<DecayProduct> particles
    * @param LorentzVector lv_parent
    * @param DecayProduct beam
    */
    protected void getBack2Back(HashMap<String, Double> kinematics, ArrayList<DecayProduct> list, LorentzVector lv_parent, DecayProduct beam) {

        // if (!this._addColinearity) { return; }

        // set cos theta lorentz vectors
        Vector3 boost = lv_parent.boostVector();
        boost.negative();
        Integer posPid = this._decay.get(1); for (Integer pid : this._decay) { if (this._constants.getCharge(pid)>0 && pid!=this._decay.get(0)) { posPid = pid; break; } } // Grab first positive particle in given decay particles for calculating costheta
        LorentzVector boostedProton = new LorentzVector(lvList.get(this._decay.indexOf(posPid) - 1)); // IMPORTANT make a new one otherwise it modifies the list entry
        boostedProton.boost(boost);
        Integer negPid = this._decay.get(1); for (Integer pid : this._decay) { if (this._constants.getCharge(pid)<0 && pid!=this._decay.get(0)) { negPid = pid; break; } } // Grab first positive particle in given decay particles for calculating costheta
        LorentzVector boostedPion = new LorentzVector(lvList.get(this._decay.indexOf(negPid) - 1)); // IMPORTANT make a new one otherwise it modifies the list entry
        boostedPion.boost(boost);
        double costhetab2b = boostedProton.vect().dot(boostedPion.vect()) / (boostedProton.vect().mag() * boostedPion.vect().mag());

        kinematics.put("costhetab2b",costhetab2b);

    }

    /**
    * Checks if event passes given cuts.
    * @param HashMap<String, Double> kinematics
    * @return boolean passesCuts
    */
    protected boolean passesCuts(HashMap<String, Double> kinematics) {

	    if (kinematics.size()==0) return false; // size will be zero if no particles are found so suppress warning statement in this case
        for (String key : this._cuts.keySet()) {
            if (!this._cuts.get(key).cut(kinematics.get(key))) {return false;}
            try { if (!this._cuts.get(key).cut(kinematics.get(key))) {return false;} }
            catch (Exception e) { System.out.println(" *** WARNING *** Issue accessing key: "+key+" for variable cut. Skipping."); }
        }
        return true;
    }

    /**
    * Compute decay kinematics for a single decay particle combination in an event.
    * This is probably a method you will want to override if you are customizing 
    * this class.
    * @param ArrayList<DecayProduct> list
    * @param DecayProduct beam
    * @return HashMap<String, Double>
    */
    protected HashMap<String, Double> getDefaultVars(ArrayList<DecayProduct> list, DecayProduct beam) {
        
        HashMap<String, Double> kinematics = new HashMap<String, Double>();
        if (!this._require_e) { return kinematics; } // Return empty hashmap if don't require electron

        // Set final state lorentz vectors
        ArrayList<LorentzVector> lvList = new ArrayList<LorentzVector>();
        for (int i=0; i<list.size(); i++) { //IMPORTANT: start at 0 since beam is separate from list
            DecayProduct p = list.get(i);
            if (!this._strict) { p.changePid(this._decay.get(i)); } //NOTE: Calculate with assumed mass unless strict option selected
            lvList.add(p.lv());
        }
        LorentzVector lv_max    = beam.lv();
        LorentzVector lv_beam   = new LorentzVector(); lv_beam.setPxPyPzM(0.0, 0.0, Math.sqrt(Math.pow(this._constants.getBeamE(),2) - Math.pow(this._constants.getBeamM(),2)), this._constants.getBeamM()); // Assumes all energy is along pz...?
        LorentzVector lv_target = new LorentzVector(); lv_target.setPxPyPzM(0.0, 0.0, this._constants.getTargetE(), this._constants.getTargetM()); //TODO: should fix this so mirrors line above...
        LorentzVector lv_parent = new LorentzVector(); double px = 0; double py = 0; double pz = 0; double en = 0;
        for (LorentzVector lv : lvList) { px += lv.px(); py += lv.py(); pz += lv.pz(); en += lv.e(); }
        lv_parent.setPxPyPzE(px,py,pz,en); //NOTE: for some reason lv_parent.add(lv) doesn't do what it's supposed to...it seems like it's boosting to some other frame in the process

        LorentzVector q = new LorentzVector(lv_beam);
        q.sub(lv_max);
        LorentzVector gN = new LorentzVector(q);
        gN.add(lv_target);
        Vector3 gNBoost = gN.boostVector();
        gNBoost.negative();
        LorentzVector boostedParent = new LorentzVector(lv_parent);
        boostedParent.boost(gNBoost);
        LorentzVector boostedMax = new LorentzVector(lv_max);
        boostedMax.boost(gNBoost);
        LorentzVector lv_miss = new LorentzVector(gN);
        lv_miss.sub(lv_parent);

        // Compute SIDIS variables
        double Q2   = (-1) * (q.mass2());
        double nu   = q.e();
        double z    = lv_parent.e() / nu;
        double y    = q.e() / lv_beam.e();		
        double x    = Q2 / (2 * this._constants.getTargetM() * nu);
        double W    = Math.sqrt(this._constants.getTargetM()*this._constants.getTargetM()+Q2 * (1 - x) / x);
        double Walt = gN.mass();
        double xF   = boostedParent.pz() / (Walt/2);
        double mass = lv_parent.mass();
        double mx   = lv_miss.mass();

        // Get Transverse momentum relative to parent momentum
        double pT = (double) 0.0;
        for (LorentzVector lv : lvList) { pT += lv_parent.vect().cross(lv.vect()).mag()/lv_parent.vect().mag(); } // for some reason using lvUnit.unit normalizes the parent vector
        pT = pT / lvList.size();

        // Get hadron momentum perpendicular to plane of scattered electron...check this
        double phperp = lv_parent.vect().cross(q.vect()).mag()/q.vect().mag(); // for some reason using qUnit.unit normalizes the parent vector

        kinematics.put("Q2",Q2);
        kinematics.put("nu",nu);
        kinematics.put("z",z);
        kinematics.put("y",y);
        kinematics.put("x",x);
        kinematics.put("W",W);
        kinematics.put("xF",xF);
        kinematics.put("pT",pT);
        kinematics.put("phperp",phperp);
        kinematics.put("mass",mass);
        kinematics.put("mx",mx);

        if (this._addLambdaKin) { this.getLKVars(kinematics,lvList,lv_parent,q); }
        if (this._addLambdaKin) { this.getColinearity(kinematics,list,lv_parent,beam); }//TODO: Add option for this?
        if (this._addLambdaKin) { this.getBack2Back(kinematics,list,lv_parent,beam); }//TODO: Add option for this?

        return kinematics;
    }

    /**
    * Compute decay kinematics for a single decay particle combination in an event.
    * This is probably a method you will want to override if you are customizing 
    * this class.
    * @param ArrayList<DecayProduct> list
    * @param ArrayList<DecayProduct> ilist
    * @return HashMap<String, Double>
    */
    protected HashMap<String, Double> getMCDefaultVars(ArrayList<DecayProduct> list, ArrayList<DecayProduct> ilist) {

        HashMap<String, Double> kinematics = new HashMap<String, Double>();
        if (!this._require_e) { return kinematics; } // Return empty hashmap if don't require electron
	    if (list.size() == 0 || ilist.size() == 0) { return kinematics; } // Return empty hashmap if no particles or MC particles

        // Set final state lorentz vectors
        ArrayList<LorentzVector> lvList = new ArrayList<LorentzVector>();
        for (int i=0; i<list.size(); i++) {
            DecayProduct p = list.get(i);
            lvList.add(p.lv());
        }

        DecayProduct beam, target, photon, max, parent;
        beam = ilist.get(0); target = ilist.get(1); photon = ilist.get(2); max = ilist.get(3); // IMPORTANT! MC::Lund order preserved
        parent = list.get(0); //IMPORTANT! MCDecays should set the particle parent at index 0

        LorentzVector lv_beam   = beam.lv();
        LorentzVector lv_target = target.lv();
        LorentzVector q         = photon.lv();
        LorentzVector lv_max    = max.lv();
        LorentzVector lv_parent = parent.lv();

        LorentzVector gN = new LorentzVector(q);
        gN.add(lv_target);
        Vector3 gNBoost = gN.boostVector();
        gNBoost.negative();
        LorentzVector boostedParent = new LorentzVector(lv_parent);
        boostedParent.boost(gNBoost);
        LorentzVector boostedMax = new LorentzVector(lv_max);
        boostedMax.boost(gNBoost);
        LorentzVector lv_miss = new LorentzVector(gN);
        lv_miss.sub(lv_parent);

        // Compute SIDIS variables
        double Q2   = (-1) * (q.mass2());
        double nu   = q.e();
        double z    = lv_parent.e() / nu;
        double y    = q.e() / lv_beam.e();		
        double x    = Q2 / (2 * this._constants.getTargetM() * nu);
        double W    = Math.sqrt(this._constants.getTargetM()*this._constants.getTargetM()+Q2 * (1 - x) / x);
        double Walt = gN.mass();
        double xF   = boostedParent.pz() / (Walt/2);
        double mass = lv_parent.mass();
        double mx   = lv_miss.mass();

        // Get Transverse momentum relative to parent momentum
        double pT = (double) 0.0;
        for (LorentzVector lv : lvList) { pT += lv_parent.vect().cross(lv.vect()).mag()/lv_parent.vect().mag(); } // for some reason using lvUnit.unit normalizes the parent vector
        pT = pT / lvList.size();

        // Get hadron momentum perpendicular to plane of scattered electron...check this
        double phperp = lv_parent.vect().cross(q.vect()).mag()/q.vect().mag(); // for some reason using qUnit.unit normalizes the parent vector

        kinematics.put("Q2",Q2);
        kinematics.put("nu",nu);
        kinematics.put("z",z);
        kinematics.put("y",y);
        kinematics.put("x",x);
        kinematics.put("W",W);
        kinematics.put("xF",xF);
        kinematics.put("pT",pT);
        kinematics.put("phperp",phperp);
        kinematics.put("mass",mass);
        kinematics.put("mx",mx);

        if (this._addLambdaKin) { this.getLKVars(kinematics,lvList,lv_parent,q); }

        return kinematics;
    }

    /**
    * Get variables and check they pass cuts, returns empty hashmap if not so make sure to check size.
    * @param ArrayList<DecayProduct> list
    * @param DecayProduct beam
    * @return HashMap<String,Double>
    */
    protected HashMap<String,Double> processEvent(ArrayList<DecayProduct> list, DecayProduct beam) { 

        HashMap<String,Double> map = new HashMap<String,Double>();
        HashMap<String,Double> defaults = this.getDefaultVars(list,beam);
        for (String key : defaults.keySet()) { map.put(key,defaults.get(key));}
        HashMap<String,Double> var = this.getSIDISVariables(list,beam);
        for (String key : var.keySet()) { map.put(key,var.get(key));}
        if (!this.passesCuts(map)) { map.clear(); }
        return map;
    }

    /**
    * Get variables and check they pass cuts, returns empty hashmap if not so make sure to check size.
    * @param HipoReader reader
    * @param Event event
    * @return HashMap<String,Double>
    */
    protected HashMap<String,Double> processEvent(HipoReader reader, Event event) { 

        HashMap<String,Double> map = this.getConfigVariables(reader,event);
        if (!this.passesCuts(map)) { map.clear(); }
        return map;
    }

    /**
    * Get variables and check they pass cuts, returns empty hashmap if not so make sure to check size.
    * @param HipoReader reader
    * @param Event event
    * @param ArrayList<DecayProduct> list
    * @param DecayProduct beam
    * @return HashMap<String,Double>
    */
    protected HashMap<String,Double> processEvent(HipoReader reader, Event event, ArrayList<DecayProduct> list, DecayProduct beam) { 

        HashMap<String,Double> map = new HashMap<String,Double>();
        HashMap<String,Double> defaults = this.getDefaultVars(list, beam);
        for (String key : defaults.keySet()) { map.put(key,defaults.get(key));}
        HashMap<String,Double> var = this.getSIDISVariables(list, beam);
        for (String key : var.keySet()) { map.put(key,var.get(key));}
        HashMap<String,Double> con = this.getConfigVariables(reader, event);
        for (String key : con.keySet()) { map.put(key,con.get(key));}
        if (!this.passesCuts(map)) { map.clear(); }
        return map;
    }

    /**
    * Get MC variables and check they pass cuts, returns empty hashmap if not so make sure to check size.
    * @param HipoReader reader
    * @param Event event
    * @param ArrayList<DecayProduct> list
    * @return HashMap<String,Double>
    */
    protected HashMap<String,Double> processMCEvent(HipoReader reader, Event event, ArrayList<DecayProduct> list, ArrayList<DecayProduct> ilist) { 
	
        this._configs.put("helicity",this._getHelicityMC);

        int beamIndex = 3; // TODO: Fairly certain this should be the same for all lund banks... Beam, Target, q, e, final state particles...
        DecayProduct beam; HashMap<String,Double> map = new HashMap<String,Double>();
        try { beam = ilist.get(beamIndex); } catch (Exception e) { System.out.println(" *** WARNING *** ilist empty setting beam to 0"); beam = new DecayProduct(0,0,0,0); return map; }
        HashMap<String,Double> defaults = this.getMCDefaultVars(list,ilist);
        for (String key : defaults.keySet()) { map.put(key,defaults.get(key)); }
        HashMap<String,Double> var = this.getSIDISVariables(list, beam);
        for (String key : var.keySet()) { map.put(key,var.get(key)); }
        HashMap<String,Double> con = this.getConfigVariables(reader, event);
        for (String key : con.keySet()) { map.put(key,con.get(key)); }
        if (!this.passesCuts(map)) { map.clear(); }

	    this._configs.put("helicity",this._getHelicity);
        return map;
    }

} // class