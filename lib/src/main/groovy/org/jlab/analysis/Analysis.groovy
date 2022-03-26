package org.jlab.analysis;

// Java Imports
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

// CLAS Physics Imports
import org.jlab.jnp.hipo4.data.*;
import org.jlab.jnp.hipo4.io.*;
import org.jlab.jnp.physics.*;

// J2ROOT Imports
import org.jlab.jroot.ROOTFile;
import org.jlab.jroot.TNtuple;

// CLAS QADB Import
import clasqa.QADB;

/**
* Encapsulates some common analysis procedures.
* If you are doing a more customized/less general analysis
* you will probably want to use the addVars() methods.
*
* @version 1.0
* @author  Matthew McEneaney
*/

public class Analysis {

    protected Constants          _constants;
    protected ArrayList<Integer> _decay;       // If you pass this to a Decays object make sure the parent pid is first.
    protected ArrayList<Integer> _mcdecay;       // If you pass this to a Decays object make sure the parent pid is first.
    protected ArrayList<Integer> _parents;       // Only contains pids for the parents of the parent in _decay.  DO NOT double enter parent pid.
    protected Kinematics         _kinematics;
    protected QADB               _qa;
    protected String              _qaMethod;
    protected FiducialCuts       _fiducialCuts;
    protected String             _inPath;
    protected String             _outPath;
    protected ROOTFile           _outFile;
    protected TNtuple            _tuple;
    protected String             _treeName;
    protected String             _tupleNames;
    protected int                _event_counter;
    protected int                _data_counter;

	// Options
	protected static int     _n_files      = 10; 					// max number of files to analyze
    protected static int     _n_events     = -1; 					// max number of events to to tree (no max if <0)
    protected static int     _split        = 0;                     // number of events to split outfiles on, won't split if 0, writes to same file different tuple if < 0
	protected static boolean _require_tag  = false;					// pid tag requirement
    protected static boolean _require_ex   = false;					// exclusive tag requirement
	protected static boolean _require_pid  = true;					// decay particles pid requirement
    protected static boolean _require_e    = true;					// electron tag in FT requirement
    protected static boolean _strict       = false;					// strict mass from pid assignment for kinematics calculations
    protected static boolean _addRunNum    = false;					// include event number in TNTuple (always added as zeroeth entry)
    protected static boolean _addEvNum     = false;					// include event number in TNTuple (always added as zeroeth entry or just after run #)
    protected static boolean _lambdaKin    = false;					// include special two particle decay kinematics from \Lambda^0 analysis
    protected static boolean _indivKin     = false;					// include extra individual particle kinematics
    protected static boolean _requireQA    = false;                 // require clasqaDB check for run and event #'s
    protected static boolean _requireFC    = false;                 // require Fiducial cuts using event #'s and pid
    protected static boolean _match        = false;                 // require matching decay in MC (with same parent, specify this._parent if you want a specific parent)
    protected static boolean _combo        = false;                 // use combo of MC::Lund and REC::Particle particles for tuple and kinematics
    protected static boolean _useMC        = false;                 // fill tuple with MC::Lund kinematics instead of REC::Particle
    protected static int     _notify       = 0;                     // notify how many events have been added out of total read so far after given number of events, does nothing if zero
    protected static boolean _addVertices  = false;                 // Add vertices to tree
    protected static boolean _addAngles    = false;                 // Add angles (in degrees) to tree

    // Tagging options
    protected static ArrayList<Integer>       _tag_pids   = new ArrayList<Integer>();        // List of pid tags for event, just checks that at least one is present in event
    protected static HashMap<Integer,Integer> _pid_filter = new HashMap<Integer,Integer>();  // Hashmap of pid to pid count, looks for events with pid counts <= abs(#pid) if #pid >= 0 and > abs(#pid) if #pid < 0 for a given pid
    // TODO: Add option to add pids to tree if using charge identification.  TODO: Add sector cut option.

    /**
    * Constructor stub
    */
    public Analysis() {

        this._constants        = new ExtendedConstants(); // If you know you don't need to look at decays just the particles in the recon banks you can just use the base Constants Class.
        this._decay            = new ArrayList<Integer>(); this._decay.add(2212); this._decay.add(-211);
        this._mcdecay          = new ArrayList<Integer>(this._decay);
        this._parents          = new ArrayList<Integer>();
        this._kinematics       = new Kinematics(this._constants, this._decay);
        this._qaMethod         = new String("OkForAsymmetry");
        this._fiducialCuts     = new FiducialCuts();
        this._inPath           = new String("");
        this._outPath          = new String("Analysis.root");
        this._tupleNames       = new String("");
        this._treeName         = new String("t");
        this._event_counter    = 0;
        this._data_counter     = 0;
    }

    /**
    * Constructor stub
    * @param Constants constants
    * @param ArrayList<Integer> decay
    * @param String inPath
    * @param String outPath
    */
    public Analysis(Constants constants, ArrayList<Integer> decay, String inPath, String outPath) {

        this._constants        = constants; // If you know you don't need to look at decays just the particles in the recon banks you can just use the base Constants Class.
        this._decay            = decay;
        this._mcdecay          = new ArrayList<Integer>(this._decay);
        this._parents          = new ArrayList<Integer>();
        this._kinematics       = new Kinematics(this._constants, this._decay);
        this._qaMethod         = new String("OkForAsymmetry");
        this._fiducialCuts     = new FiducialCuts();
        this._inPath           = inPath;
        this._outPath          = outPath;
        this._tupleNames       = new String("");
        this._treeName         = new String("t");
        this._event_counter    = 0;
        this._data_counter     = 0;
    }

    /**
    * Constructor stub with defaults for processing MC::Lund event (just pass empty list to decay parameter
    * or if you want to use a combo MC/REC event resett the decay for the kinematics object).
    * @param Constants constants
    * @param ArrayList<Integer> decay
    * @param ArrayList<Integer> mcdecay
    * @param ArrayList<Integer> parents
    * @param String inPath
    * @param String outPath
    */
    public Analysis(Constants constants, ArrayList<Integer> decay, ArrayList<Integer> mcdecay, ArrayList<Integer> parents, String inPath, String outPath) {

        //TODO: Preset options
        this._constants        = constants; // If you know you don't need to look at decays just the particles in the recon banks you can just use the base Constants Class.
        this._decay            = decay;
        this._mcdecay          = mcdecay;
        this._parents          = parents;
        this._kinematics       = new Kinematics(this._constants, this._mcdecay);
        this._qaMethod         = new String("OkForAsymmetry");
        this._fiducialCuts     = new FiducialCuts();
        this._inPath           = inPath;
        this._outPath          = outPath;
        this._tupleNames       = new String("");
        this._treeName         = new String("t");
        this._event_counter    = 0;
        this._data_counter     = 0;
    }

    /**
    * Set constants object for analysis and propagate changes to kinematics object.
    * @param Constants constants
    */
    protected void setConstants(Constants constants) {

        this._constants = constants;
        this._kinematics.setConstants(constants);
    }

    /**
    * Set target mass in constants and propagate changes to kinematics.
    * @param double TM
    */
    protected void setTargetM(double TM) {

        this._constants.setTargetM(TM);
        this._kinematics.setConstants(this._constants);
    }

    /**
    * Set target lund pid in constants and propagate changes to kinematics.
    * @param int pid
    */
    protected void setTargetPID(int pid) {

        this._constants.setTargetPID(pid);
        this._kinematics.setConstants(this._constants);
    }

    /**
    * Set beam energy in constants and propagate changes to kinematics.
    * @param double BE
    */
    protected void setBeamE(double BE) {

        this._constants.setBeamE(BE);
        this._kinematics.setConstants(this._constants);
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
        Collections.sort(this._decay); //IMPORTANT: Combinations algorithm relies on this in Decays.groovy.
        this._kinematics.setDecay(decay);
    }

    /**
    * Get list of decay lund pids.
    * @return ArrayList<Integer> _decay
    */
    protected ArrayList<Integer> getDecay() {

        return this._decay;
    }

    /**
    * Set list of Lund pids for decay in MC::Lund bank.
    * @param ArrayList<Integer> mcdecay
    */
    protected void setMCDecay(ArrayList<Integer> mcdecay) {

        this._mcdecay = mcdecay;
        Collections.sort(this._mcdecay); //IMPORTANT: Combinations algorithm relies on this in Decays.groovy.
        // this._kinematics.setMCDecay(mcdecay);//TODO: DO SOON -> Just adjust kinematics before/after using mc?
    }

    /**
    * Get list of decay lund pids for MC::Lund bank.
    * @return ArrayList<Integer> _mcdecay
    */
    protected ArrayList<Integer> getMCDecay() {

        return this._mcdecay;
    }

    /**
    * Set list of Lund pids for parents in MC.  Do not include parent from this._decay.
    * @param ArrayList<Integer> parents
    */
    protected void setParents(ArrayList<Integer> parents) {

        this._parents = parents;
        this._kinematics.setParents(parents);
    }

    /**
    * Get list of parents lund pids in MC.
    * @return ArrayList<Integer> _parents
    */
    protected ArrayList<Integer> getParents() {

        return this._parents;
    }

    /**
    * Set kinematics object for analysis.
    * @param Kinematics kinematics
    */
    protected void setKinematics(Kinematics kinematics) {

        this._kinematics = kinematics;
        this._kinematics.setConstants(this._constants);
    }

    /**
    * Set hashmap of configuration variable names to lambda expressions for access.
    * @param HashMap<String,ConfigVar> configs
    */
    protected void setConfigVars(HashMap<String,ConfigVar> configs) { 

       this._kinematics.setConfigVars(configs);
    }

    /**
    * Add hashmap of configuration variable names to lambda expressions for access.
    * @param HashMap<String,ConfigVar> configs
    */
    protected void addConfigVars(HashMap<String,ConfigVar> configs) { 

        this._kinematics.addConfigVars(configs);
    }

    /**
    * Add configuration variable name and lambda expression for access.
    * @param String name
    * @param ConfigVar config
    */
    protected void addVar(String name, ConfigVar config) { 

        this._kinematics.addVar(name,config);
    }

    /**
    * Set hashmap of kinematic names lambda expressions for computation.
    * @param HashMap<String,SIDISVar> vars
    */
    protected void setSIDISVars(HashMap<String,SIDISVar> vars) { 

        this._kinematics.setSIDISVars(vars);
    }

    /**
    * Add hashmap of kinematic names lambda expressions for computation.
    * @param HashMap<String,SIDISVar> vars
    */
    protected void addSIDISVars(HashMap<String,SIDISVar> vars) { 

        this._kinematics.addSIDISVars(vars);
    }

    /**
    * Add kinematic name and lambda expression for computation.
    * @param String name
    * @param SIDISVar var
    */
    protected void addVar(String name, SIDISVar var) { 

        this._kinematics.addVar(name,var);
    }

    /**
    * Set hashmap of kinematic names to boolean .cut(double) lambda expression cuts.
    * @param HashMap<String,Cut> cuts
    */
    protected void setCuts(HashMap<String,Cut> cuts) { 

        this._kinematics.setCuts(cuts);
    }

    /**
    * Add entries from ahashmap of kinematic names to min, max cuts.
    * @param HashMap<String, Cut> cuts
    */
    protected void addCuts(HashMap<String, Cut> cuts) { 

        this._kinematics.addCuts(cuts);
    }

    /**
    * Add entries from a hashmap of kinematic names to min, max cuts.
    * @param String name
    * @param Cut cut
    */
    protected void addCut(String name, Cut cut) { 

        this._kinematics.addCut(name,cut);
    }

    /**
    * Access kinematics object for analysis.
    * @return Kinematics _kinematics
    */
    protected Kinematics getKinematics() {

        return this._kinematics;
    }

    /**
    * Access string array for names of default kinematic variables: Q2, nu, W, Walt x, xF, y, z, pT, phperp, mass.
    * @return String[] _defaults
    */
    protected String[] getDefaults() {

        return this._kinematics.getDefaults();
    }

    /**
    * Access hashmap of configuration variable names lambda expressions for access.
    * @return HashMap<String,ConfigVar> _configs
    */
    protected HashMap<String,ConfigVar> getConfigVars() {

        return this._kinematics.getConfigVars();
    }

    /**
    * Access hashmap of kinematic names lambda expressions for computation.
    * @return HashMap<String,SIDISVar> _vars
    */
    protected HashMap<String,SIDISVar> getSIDISVars() {

        return this._kinematics.getSIDISVars();
    }

    /**
    * Access hashmap of kinematic names to min, max cuts.
    * @return HashMap<String, Cut> _cuts
    */
    protected HashMap<String, Cut> getCuts() { 

        return this._kinematics.getCuts();
    }

    /**
    * Set method for CLASQADB. 
    * @param String qaMethod
    */
    protected void setQAMethod(String qaMethod) {

        this._requireQA = true;
        this._qaMethod = qaMethod;
        if (this._qa==null) { this._qa = new QADB(); }
    }

    /**
    * Get method for CLASQADB.
    * @return String qaMethod
    */
    protected String getQAMethod() {

        return this._qaMethod;
    }

    /**
    * Set input file path for analysis.
    * @param String inPath
    */
    protected void setInPath(String inPath) {

        this._inPath = inPath;
    }

    /**
    * Access string for input file path.
    * @return String _inFiles
    */
    protected String getInPath() {

        return this._inPath;
    }

    /**
    * Set output path for analysis.
    * @param String outPath
    */
    protected void setOutPath(String outPath) {

        this._outPath = outPath;
    }

    /**
    * Access string for output file path.
    * @return String _outPath
    */
    protected String getOutPath() {

        return this._outPath;
    }

    /**
    * Set tree name for output TNTuple.
    * @param String treeName
    */
    protected void setTreeName(String treeName) {

        this._treeName = treeName;
    }

    /**
    * Access tree name for output TNTuple.
    * @return String _treeName
    */
    protected String getTreeName() {

        return this._treeName;
    }

    /**
    * Set max mass limit for events to add.
    * @param double maxMass
    */
    protected void setMaxMass(double maxMass) {

        Cut cut = (double mass) -> { if (mass > maxMass) {return false;} else {return true;} };
        this._kinematics.addCut("mass", cut);
    }

    /**
    * Set min xF limit for events to add.
    * @param double minxF
    */
    protected void setMinxF(double minxF) {

        Cut cut = (double xF) -> { if (xF < minxF) {return false;} else {return true;} };
        this._kinematics.addCut("xF", cut);
    }

    /**
    * Set max number of files for analysis to process.
    * @param int n_files
    */
    protected void setNFiles(int n_files) {

        this._n_files = n_files;
    }

    /**
    * Set max number of events for analysis to add to tree.
    * Note: The actual number of events looked at may be greater.
    * @param int n_events
    */
    protected void setNEvents(int n_events) {

        this._n_events = n_events;
    }

    /**
    * Set number of events to split outfiles on.
    * @param int split
    */
    protected void setSplit(int split) {

        this._split = split;
    }

    /**
    * Set boolean for requiring pid tag in event.
    * @param boolean require_tag
    */
    protected void setTag(boolean require_tag) {

        this._require_tag = require_tag;
    }

    /**
    * Set boolean for requiring pid tag(s) in event and lund pid(s) to tag.
    * @param int... tag_pid
    */
    protected void setTag(int... tag_pids) {

        this._require_tag = true;
        this._tag_pids    = tag_pids;
    }

    /**
    * Set boolean for requiring exclusive tag in event.
    * @param boolean require_ex
    */
    protected void setExclusive(boolean require_ex) {

        this._require_ex = require_ex;
    }

    /**
    * Set boolean for requiring pid tag(s) in event and lund pid(s) to tag.
    * @param ArrayList<Integer> tag_pids
    * @param ArrayList<Integer> filters
    */
    protected void setPidFilter(ArrayList<Integer> tag_pids, ArrayList<Integer> filters) {

        this._require_tag = true;
        for (int i=0; i<tag_pids.size(); i++) { this._pid_filter.put(tag_pids.get(i),filters.get(i)); }
    }

    /**
    * Filter list of particles by pid counts.
    * @param  ArrayList<DecayProduct> list
    * @return boolean filter
    */
    protected boolean filter(ArrayList<DecayProduct> list) {
        if (this._require_ex) {
            for (DecayProduct p : list) {
                if (!this._pid_filter.containsKey(p.pid())) { return false; }
            }
        }
        if (this._pid_filter.size()==0) { return true; }
        for (Integer key : this._pid_filter.keySet()){
            int count = 0; for (DecayProduct p : list) { if (p.pid()==key) { count += 1; } }
            if (this._pid_filter.get(key)<0 && count<=Math.abs(this._pid_filter.get(key))) { return false; }
            if (this._pid_filter.get(key)==0 && count>0) { return false; }
            if (this._pid_filter.get(key)>0 && count>Math.abs(this._pid_filter.get(key))) { return false; }
        }
        return true;
    }

    /**
    * Set boolean for requiring particle identification by pid instead of charge.
    * @param boolean require_pid
    */
    protected void setRequirePID(boolean require_pid) {

        this._require_pid = require_pid;
    }

    /**
    * Set boolean for requiring scattered electron in event and propagate changes to kinematics.
    * @param boolean require_e
    */
    protected void setRequireE(boolean require_e) {

        this._require_e = require_e;
        this._kinematics.setRequireE(require_e);
    }

    /**
    * Set boolean for strict pid to mass assignment in kinematics calculations.
    * @param boolean strict
    */
    protected void setStrict(boolean strict) {

        this._strict = strict;
        this._kinematics.setStrict(strict);
    }

    /**
    * Set boolean for adding event number to TNTuple and propagate changes to kinematics.
    * @param boolean addEvNum
    */
    protected void setAddEvNum(boolean addEvNum) {

        this._addEvNum = addEvNum;
        this._kinematics.setAddEvNum(addEvNum);
    }

    /**
    * Set boolean for adding run number to TNTuple and propagate changes to kinematics.
    * @param boolean addRunNum
    */
    protected void setAddRunNum(boolean addRunNum) {

        this._addRunNum = addRunNum;
        this._kinematics.setAddRunNum(addRunNum);
    }

    /**
    * Set boolean for including lambda analysis kinematics and propagate changes to kinematics.
    * Only applies for 2 particle decays.
    * @param boolean LK
    */
    protected void setLambdaKin(boolean LK) {

        if (!this._decay.size()==2) { return; }
        this._lambdaKin = LK;
        this._kinematics.setAddLambdaKin(LK);
    }

    /**
    * Set boolean for including more individual particle kinematics (relevant for dihadron analysis) 
    * and propagate changes to kinematics.
    * @param boolean IK
    */
    protected void setIndivKin(boolean IK) {

        if (!this._decay.size()==2) { return; }
        this._indivKin = IK;
        this._kinematics.setAddIndivKin(IK);
    }

    /**
    * Set boolean for using clasqaDB cuts 
    * @see <a href="https://github.com/JeffersonLab/clasqaDB">clasqaDB GitHub repository</a>.
    * @param boolean QA
    */
    protected void setQA(boolean QA) {

        this._requireQA = QA;
        if (QA && this._qa==null) { this._qa = new QADB(); }
    }

    /**
    * Set boolean for requiring fiducial cuts 
    * @see <a href="https://github.com/c-dilks/dispin/tree/master/src">c-dilks GitHub dispin repository</a>. 
    * @param boolean FC
    */
    protected void setFC(boolean FC) {

        this._requireFC = FC;
    }

    /**
    * Set boolean for requiring fiducial cuts 
    * @see <a href="https://github.com/c-dilks/dispin/tree/master/src">c-dilks GitHub dispin repository</a>.
    * @param boolean FC
    * @param int level
    */
    protected void setFCLevel(boolean FC, int level) {

        this._requireFC = FC;
        this._fiducialCuts.setLevel(level);
    }

    /**
    * Set boolean for requiring matching decay in mc.
    * @param boolean MC
    */
    protected void setMatch(boolean match) {

        this._match   = match;
        this._mcdecay = new ArrayList<Integer>(this._decay);
    }

    /**
    * Set boolean for using combo of MC::Lund and REC::Particle particles for kinematics.
    * @param boolean combo
    */
    protected void setCombo(boolean combo) {

        this._combo   = combo;
        ArrayList<Integer> comboDecay = new ArrayList<Integer>(this._decay); comboDecay.addAll(this._mcdecay);
        this._kinematics.setDecay(comboDecay);//NOTE: Has to be called after setting this._mcdecay
    }

    /**
    * Set boolean for using MC::Lund bank instead of REC::Particle.  Also applies for beam for kinematics when using combo event.
    * @param boolean MC
    */
    protected void setUseMC(boolean useMC) {

        this._useMC = useMC;
    }

    /**
    * Set int for # of events processed after which to notify how many events have been added
    * out of total processed so far.
    * @param int notify
    */
    protected void setNotify(int notify) {

        this._notify = notify;
    }

    /**
    * Include particle vertices in tree.
    * @param boolean addVertices
    */
    protected void setAddVertices(boolean addVertices) {

        this._addVertices = addVertices;
    }

    /**
    * Include particle angles (in degrees) in tree.
    * @param boolean addAngles
    */
    protected void setAddAngles(boolean addAngles) {

        this._addAngles = addAngles;
    }

    /**
    * Loop through events and run analysis.
    * You may want to override and customize this method.
    * Make sure you exactly mirror the order of variables in your NTuple
    * when adding to the data array.
    * @param HipoReader reader
    */
    protected void processEvents(HipoReader reader) throws InterruptedException {

        Event event = new Event();
        while(reader.hasNext()) {
            reader.nextEvent(event);

            // QADB Cuts
            Schema schema = reader.getSchemaFactory().getSchema("RUN::config");
            Bank bank     = new Bank(schema);
            event.read(bank);
            int runnum = bank.getInt('run',0);
            int evnum  = bank.getInt('event',0);
            if (this._requireQA && !this._match && !this._useMC) {
                switch (this._qaMethod) {
                    case "OkForAsymmetry": if (!this._qa.OkForAsymmetry(runnum,evnum)) continue; // not sure if this will break event loop or switch statement...
                    case "golden":         if (!this._qa.golden(runnum,evnum)) continue;
                    case "custom":         System.out.println(" *** WARNING *** Congratulations! Custom QADB method is for you to implement;)");
                    default:               System.out.println(" *** WARNING *** QA Method not recognized.  Using OkForAsymmetry()."); if (!this._qa.OkForAsymmetry(runnum,evnum)) continue;
                }
            }

            this._event_counter += 1;

            // Read needed banks only once!
            if (this._requireFC) { this._FC.setArrays(reader,event); }
            Decays decays = new Decays(this._decay,reader,runnum,event,this._constants,this._fiducialCuts,this._requireFC); // Fiducial cuts implemented in Decays object

            // Check for event pid tag and filters if requested
            if (this._require_tag) {
                boolean found_tag = false;
                for (DecayProduct p : decays.getFullParticleList()) { if (this._tag_pids.contains(p.pid())) { found_tag=true; break; } }
                if (!found_tag) { continue; }
                if (!this.filter(decays.getFullParticleList())) { continue; }
            }

            // Get list of particle combinations
            ArrayList<ArrayList<DecayProduct>> list;
            if (!this._require_pid) { list = decays.getComboChargeList(); }
            if (this._require_pid)  { list = decays.getComboPidList();    }

            // Check for scattered electron if requested
            DecayProduct beam = decays.getScatteredBeam(); //NOTE: quicker since already have particle list, also implemented for MC;)
            if (this._require_e && beam.p()==0.0) { continue; } // IMPORTANT! Scattered beam pid and p are set to zero if no scattered electron is found

            // Loop through combinations
            boolean addedEvent = false;
            for (ArrayList<DecayProduct> l : list) {
                if (l.size()==0) { continue; } //IMPORTANT!
                HashMap<String, Double> kinematics = this._kinematics.processEvent(reader, event, l, beam);
                if (kinematics.size()==0) { continue; } else { addedEvent = true; }
                ArrayList<Double> data = new ArrayList<Double>();   
                if (this._require_e) {
                    for (String key : this._kinematics.keySet()) { data.add(kinematics.get(key)); } 
                    data.add(beam.px());
                    data.add(beam.py());
                    data.add(beam.pz());
                    if (this._addVertices) {
                        data.add(beam.vx());
                        data.add(beam.vy());
                        data.add(beam.vz());
                        data.add(beam.vt());
                    }
                    if (this._addAngles) {
                        data.add(beam.theta());
                        data.add(beam.phi());
                    }
                    data.add(beam.chi2pid());
                    data.add(beam.status());
                }
                for (DecayProduct p : l) {
                    data.add(p.px());
                    data.add(p.py());
                    data.add(p.pz());
                    if (this._addVertices) {
                        data.add(p.vx());
                        data.add(p.vy());
                        data.add(p.vz());
                        data.add(p.vt());
                    }
                    if (this._addAngles) {
                        data.add(p.theta());
                        data.add(p.phi());
                    }
                    data.add(p.chi2pid());
                    data.add(p.status());
                    if (!this._require_pid) {
                        data.add(p.pid());
                    }
                }
		
                // Fill TNTuple
                double[] dataArray = new double[data.size()];
                for (int i=0; i<data.size(); i++) { dataArray[i] = (double) data.get(i); }
                this._tuple.fill(dataArray);
            }
            if (addedEvent) { 
                this._data_counter += 1;
                if (this._n_events>0) {
                    if(this._data_counter>=this._n_events) { this.splitOutFile(this._data_counter); break; }
                    }
                if (this._split!=0) {
                    this.splitOutFile(this._data_counter);
                }
            } // counts events selected not # of actual data entries added
            decays.clean(); // Careful! Wipes all lists!
            if (this._notify>0 && (this._event_counter % this._notify)==0) { System.out.println(" Added "+this._data_counter+"/"+this._event_counter+" events total."); }
        }
        // this._tuple.write(); //TODO: Think this might just overwrite successive tuples if looking at multiple files...
    }  // processEvents

    /**
    * Loop through events and run analysis.
    * You may want to override and customize this method.
    * Make sure you exactly mirror the order of variables in your NTuple
    * when adding to the data array.
    * @param HipoReader reader
    */
    protected void processMCEvents(HipoReader reader) throws InterruptedException {

        Event event = new Event();
        while(reader.hasNext()) {
            reader.nextEvent(event);
            this._event_counter += 1;

            // Read needed banks only once!
            MCDecays decays = new MCDecays(this._decay,this._parents,reader,event,this._constants);

            // Check for event pid tag if requested
            if (this._require_tag) {
                boolean found_tag = false;
                for (DecayProduct p : decays.getFullParticleList()) { if (this._tag_pids.contains(p.pid())) { found_tag=true; break; } }
                if (!found_tag) { continue; }
                if (!this.filter(decays.getFullParticleList())) { continue; }
            }

            // Get list of particle combinations //TODO: Check that the checks for parent/daughter indexing actually work as intended
            ArrayList<ArrayList<DecayProduct>> list;
            if (!this._require_pid) { list = decays.getComboChargeList(); if (this._parents.size()!=0) { list = decays.getCheckedComboChargeList(); } }
            if (this._require_pid)  { list = decays.getComboPidList();    if (this._parents.size()!=0) { list = decays.getCheckedComboPidList(); } }

            // Check for scattered electron if requested
            DecayProduct beam = decays.getScatteredBeam(); // quicker since already have particle list, also implemented for MC;)
            if (this._require_e && beam.p()==0.0) { continue; } // IMPORTANT! Scattered beam pid and p are set to zero if no scattered electron is found

            // Loop through combinations
            boolean addedEvent = false;
            for (ArrayList<DecayProduct> l : list) {
                if (l.size()==0) { continue; } // IMPORTANT!
                HashMap<String, Double> kinematics = this._kinematics.processMCEvent(reader, event, l, decays.getParents());
                if (kinematics.size()==0) { continue; } else { addedEvent = true; }
                ArrayList<Double> data = new ArrayList<Double>();
                if (this._require_e) {
                    for (String key : this._kinematics.keySet()) { data.add(kinematics.get(key)); }
                    data.add(beam.px());
                    data.add(beam.py());
                    data.add(beam.pz());
                    if (this._addVertices) {
                        data.add(beam.vx());
                        data.add(beam.vy());
                        data.add(beam.vz());
                    }
                    if (this._addAngles) {
                        data.add(beam.theta());
                        data.add(beam.phi());
                    }
                }
                for (DecayProduct p : l) {
                    data.add(p.px());
                    data.add(p.py());
                    data.add(p.pz());
                    if (this._addVertices) {
                        data.add(p.vx());
                        data.add(p.vy());
                        data.add(p.vz());
                    }
                    if (this._addAngles) {
                        data.add(p.theta());
                        data.add(p.phi());
                    }
                    if (!this._require_pid) {
                        data.add(p.pid());
                    }
                }
		
                // Fill TNTuple
                double[] dataArray = new double[data.size()];
                for (int i=0; i<data.size(); i++) { dataArray[i] = (double) data.get(i); }
                this._tuple.fill(dataArray); // You need to add an extra line of code to your jroot installation for this method
            }
            if (addedEvent) {
                this._data_counter += 1;
                if (this._n_events>0) {
                    if (this._data_counter>=this._n_events) { this.splitOutFile(this._data_counter); break; }
                }
                if (this._split!=0) {
                    this.splitOutFile(this._data_counter);
                }
            } // counts events selected not # of actual data entries added
        decays.clean(); // Careful! Wipes all lists!
        if (this._notify>0 && (this._event_counter % this._notify)==0) { System.out.println(" Added "+this._data_counter+"/"+this._event_counter+" events total."); }
        }
        // this._tuple.write(); //TODO: Think this might just overwrite successive tuples if looking at multiple files...
    }  // processEventsMC

    /**
    * Loop through events and run analysis using combination of MC::Lund and REC::Particle particles.
    * You may want to override and customize this method.
    * Make sure you exactly mirror the order of variables in your NTuple
    * when adding to the data array.
    * @param HipoReader reader
    */
    protected void processComboEvents(HipoReader reader) throws InterruptedException {

        Event event = new Event();
        while(reader.hasNext()) {
            reader.nextEvent(event);
            this._event_counter += 1;

            // Get Event # and Run #
            Schema schema = reader.getSchemaFactory().getSchema("RUN::config");
            Bank bank     = new Bank(schema);
            event.read(bank);
            int runnum = bank.getInt('run',0);
            int evnum  = bank.getInt('event',0);

            // Read needed banks only once!
            if (this._requireFC) { this._FC.setArrays(reader,event); }
            Decays decays     = new Decays(this._decay,reader,runnum,event,this._constants,this._fiducialCuts,this._requireFC); // Fiducial cuts implemented in Decays object
            MCDecays mcdecays = new MCDecays(this._mcdecay,this._parents,reader,event,this._constants);

            // Check for event pid tag and filters if requested
            if (this._require_tag) {
                boolean found_tag = false;
                for (DecayProduct p : decays.getFullParticleList()) { if (this._tag_pids.contains(p.pid())) { found_tag=true; break; } }
                if (!found_tag) { continue; }
                if (!this.filter(decays.getFullParticleList())) { continue; }
            }

            // Get combined list of particle combinations from REC::Particle and MC::Lund banks
            ArrayList<ArrayList<DecayProduct>> list;
            if (!this._require_pid) { list = decays.mergeComboChargeList(mcdecays.getComboChargeList()); }
            if (this._require_pid)  { list = decays.mergeComboPidList(mcdecays.getComboPidList()); }

            // Check for scattered electron if requested
            DecayProduct beam;
            if (this._useMC) { beam = mcdecays.getScatteredBeam(); } //NOTE: quicker since already have particle list, also implemented for MC;)
            else             { beam = decays.getScatteredBeam(); }
            if (this._require_e && beam.p()==0.0) { continue; } // IMPORTANT! Scattered beam pid and p are set to zero if no scattered electron is found

            // Loop through combinations
            boolean addedEvent = false;
            for (ArrayList<DecayProduct> l : list) {
                if (l.size()==0) { continue; } //IMPORTANT!
                HashMap<String, Double> kinematics;
                if (this._useMC) { kinematics = this._kinematics.processMCEvent(reader, event, l, mcdecays.getParents()); }
                else             { kinematics = this._kinematics.processEvent(reader, event, l, beam); }
                if (kinematics.size()==0) { continue; } else { addedEvent = true; }
                ArrayList<Double> data = new ArrayList<Double>();
                if (this._require_e) {
                    for (String key : this._kinematics.keySet()) { data.add(kinematics.get(key)); }
                    data.add(beam.px());
                    data.add(beam.py());
                    data.add(beam.pz());
                    if (this._addVertices) {
                        data.add(beam.vx());
                        data.add(beam.vy());
                        data.add(beam.vz());
                        data.add(beam.vt());
                    }
                    if (this._addAngles) {
                        data.add(beam.theta());
                        data.add(beam.phi());
                    }
                    data.add(beam.chi2pid());
                    data.add(beam.status());
                }
                for (DecayProduct p : l) {
                    data.add(p.px());
                    data.add(p.py());
                    data.add(p.pz());
                    if (this._addVertices) {
                        data.add(p.vx());
                        data.add(p.vy());
                        data.add(p.vz());
                        data.add(p.vt());
                    }
                    if (this._addAngles) {
                        data.add(p.theta());
                        data.add(p.phi());
                    }
                    data.add(p.chi2pid());
                    data.add(p.status());
                    if (!this._require_pid) {
                        data.add(p.pid());
                    }
                }
		
                // Fill TNTuple
                double[] dataArray = new double[data.size()];
                for (int i=0; i<data.size(); i++) { dataArray[i] = (double) data.get(i); }
                this._tuple.fill(dataArray);
            }
            // Count events selected not # of actual data entries added and split output file/tuple if requested.
            if (addedEvent) { 
                this._data_counter += 1;
                if (this._n_events>0) {
                    if(this._data_counter>=this._n_events) { this.splitOutFile(this._data_counter); break; }
                    }
                if (this._split!=0) {
                    this.splitOutFile(this._data_counter);
                }
            } // if (addedEvent)
            decays.clean(); // Careful! Wipes all lists!
            if (this._notify>0 && (this._event_counter % this._notify)==0) { System.out.println(" Added "+this._data_counter+"/"+this._event_counter+" events total."); }
        }
        // this._tuple.write(); //TODO: Think this might just overwrite successive tuples if looking at multiple files...
    }  // processComboEvents

    /**
    * Loop through events and run analysis looking for matching decays of MC::Lund and REC::Particle particles.
    * You may want to override and customize this method.
    * Make sure you exactly mirror the order of variables in your NTuple
    * when adding to the data array.
    * @param HipoReader reader
    */
    protected void processMatchEvents(HipoReader reader) throws InterruptedException {

        Event event = new Event();
        while(reader.hasNext()) {
            reader.nextEvent(event);
            this._event_counter += 1;

            // Get Event # and Run #
            Schema schema = reader.getSchemaFactory().getSchema("RUN::config");
            Bank bank     = new Bank(schema);
            event.read(bank);
            int runnum = bank.getInt('run',0);
            int evnum  = bank.getInt('event',0);

            // Read needed banks only once!
            if (this._requireFC) { this._FC.setArrays(reader,event); }
            Decays decays     = new Decays(this._decay,reader,runnum,event,this._constants,this._fiducialCuts,this._requireFC); // Fiducial cuts implemented in Decays object
            MCDecays mcdecays = new MCDecays(this._mcdecay,this._parents,reader,event,this._constants);

            // Check for event pid tag if requested
            if (this._require_tag) {
                boolean found_tag = false;
                for (DecayProduct p : decays.getFullParticleList()) { if (this._tag_pids.contains(p.pid())) { found_tag=true; break; } }
                if (!found_tag) { continue; }
                if (!this.filter(decays.getFullParticleList())) { continue; }
            }

            // Get combined list of particle combinations from REC::Particle and MC::Lund banks
            ArrayList<ArrayList<DecayProduct>> list;
            if (!this._require_pid) { list = decays.mergeComboChargeList(mcdecays.getComboChargeList()); }
            if (this._require_pid)  { list = decays.mergeComboPidList(mcdecays.getComboPidList()); }

            // Check for scattered electron if requested
            DecayProduct beam   = decays.getScatteredBeam();
            DecayProduct mcbeam = mcdecays.getScatteredBeam();
            if (this._require_e && beam.p()==0.0 || mcbeam.p()==0.0) { continue; } // IMPORTANT! Scattered beam pid and p are set to zero if no scattered electron is found

            // Loop through combinations
            boolean addedEvent = false;
            for (ArrayList<DecayProduct> l : list) {
                if (l.size()==0) { continue; } //IMPORTANT!
                HashMap<String, Double> kinematics = this._kinematics.processEvent(reader, event, (ArrayList<DecayProduct>)l.subList(0,this._decay.size()), beam);
                HashMap<String, Double> mckinematics = this._kinematics.processMCEvent(reader, event, (ArrayList<DecayProduct>)l.subList(this._decay.size(),this._decay.size()*2), mcdecays.getParents());
                if (kinematics.size()==0 || mckinematics.size()==0) { continue; } else { addedEvent = true; }
                ArrayList<Double> data = new ArrayList<Double>();
                if (this._require_e) {
                    for (String key : this._kinematics.keySet()) { data.add(kinematics.get(key)); }
                    for (String key : this._kinematics.keySet()) { data.add(mckinematics.get(key)); }
                    // Add REC::Particle Beam
                    data.add(beam.px());
                    data.add(beam.py());
                    data.add(beam.pz());
                    if (this._addVertices) {
                        data.add(beam.vx());
                        data.add(beam.vy());
                        data.add(beam.vz());
                        data.add(beam.vt());
                    }
                    if (this._addAngles) {
                        data.add(beam.theta());
                        data.add(beam.phi());
                    }
                    data.add(beam.chi2pid());
                    data.add(beam.status());

                    // Add MC::Lund beam
                    data.add(mcbeam.px());
                    data.add(mcbeam.py());
                    data.add(mcbeam.pz());
                    if (this._addVertices) {
                        data.add(mcbeam.vx());
                        data.add(mcbeam.vy());
                        data.add(mcbeam.vz());
                        data.add(mcbeam.vt());
                    }
                    if (this._addAngles) {
                        data.add(mcbeam.theta());
                        data.add(mcbeam.phi());
                    }
                    data.add(mcbeam.chi2pid());
                    data.add(mcbeam.status());
                }

                // Add REC::Particle particles
                for (DecayProduct p : l.subList(0,this._decay.size())) {
                    data.add(p.px());
                    data.add(p.py());
                    data.add(p.pz());
                    if (this._addVertices) {
                        data.add(p.vx());
                        data.add(p.vy());
                        data.add(p.vz());
                        data.add(p.vt());
                    }
                    if (this._addAngles) {
                        data.add(p.theta());
                        data.add(p.phi());
                    }
                    data.add(p.chi2pid());
                    data.add(p.status());
                    if (!this._require_pid) {
                        data.add(p.pid());
                    }
                }
                
                // Add MC::Lund Particles
                for (DecayProduct p : l.subList(this._decay.size(),this._decay.size()*2)) {
                    data.add(p.px());
                    data.add(p.py());
                    data.add(p.pz());
                    if (this._addVertices) {
                        data.add(p.vx());
                        data.add(p.vy());
                        data.add(p.vz());
                        data.add(p.vt());
                    }
                    if (this._addAngles) {
                        data.add(p.theta());
                        data.add(p.phi());
                    }
                    if (!this._require_pid) {
                        data.add(p.pid());
                    }
                }
		
                // Fill TNTuple
                double[] dataArray = new double[data.size()];
                for (int i=0; i<data.size(); i++) { dataArray[i] = (double) data.get(i); }
                this._tuple.fill(dataArray);
            }
            // Count events selected not # of actual data entries added and split output file/tuple if requested.
            if (addedEvent) { 
                this._data_counter += 1;
                if (this._n_events>0) {
                    if(this._data_counter>=this._n_events) { this.splitOutFile(this._data_counter); break; }
                    }
                if (this._split!=0) {
                    this.splitOutFile(this._data_counter);
                }
            } // if (addedEvent)
            decays.clean(); // Careful! Wipes all lists!
            if (this._notify>0 && (this._event_counter % this._notify)==0) { System.out.println(" Added "+this._data_counter+"/"+this._event_counter+" events total."); }
        }
        // this._tuple.write(); //TODO: Think this might just overwrite successive tuples if looking at multiple files...
    }  // processMatchEvents()

    /**
    * Write TNTuple to ROOT files and close.
    * @throws InterruptedException
    */
    protected void write2Root() throws InterruptedException {
        
        this._tuple.write();
        this._outFile.close();
    }

    /**
    * Sets the tuple variable names.  Make sure ordering is the same in the array used to add data to the tuple.
    */
    protected void setTupleNames() {

        // Add NTuple: "this._treeName" : Kinematics/Custom Variables, Scattered Beam, Decay products
        this._tupleNames = new String("");
        for (String kin : this._kinematics.keySet()) { this._tupleNames += kin + ":"; }
        if (this._match) {//NOTE: Double kinematics if matching REC/MC banks
            for (String kin : this._kinematics.keySet()) { this._tupleNames += kin + "_MC" + ":"; }
        }
        String[] names = ["px_",":py_",":pz_"];
        if (this._addVertices) { names += ((this._useMC && !this._combo && !this._match) ? [":vx_",":vy_",":vz_"] : [":vx_",":vy_",":vz_",":vt_"]); } //NOTE: Just a groovy capability
        if (this._addAngles) { names += [":theta_",":phi_"]; } //NOTE: Just a groovy capability
        if (!this._useMC || this._combo || this._match) { names += [":chi2pid_",":status_"]; }
        if (!this._require_pid) {names += [":pid_"]; } //NOTE: Just a groovy capability // use .addAll() for java
        String pname = this._constants.getName(this._constants.getBeamPID());
        if (this._require_e) {
            for (String name : names) { this._tupleNames += name + pname; }
            this._tupleNames += ":";
            if (this._match) {//NOTE: Double entries for matching MC/REC banks
                for (String name : names) { this._tupleNames += name + pname + "_MC"; }
            this._tupleNames += ":";
            }
        }

        HashMap<Integer,Integer> pidCounts = new HashMap<Integer,Integer>();
        ArrayList<Integer> unique_pids = this._decay.stream().distinct().collect(Collectors.toList());
        for (Integer pid : unique_pids) { pidCounts.put(pid,0); }
        for (int i=0; i<this._decay.size(); i++) {
            Integer pid = this._decay.get(i);
            pidCounts[pid] += 1;
            pname = this._constants.getName(pid);
            if (i!=0) { if (pid==this._decay.get(i-1)) { pname += pidCounts.get(pid); } }
            for (String name : names) { this._tupleNames += name + pname; }
            if (i!=this._decay.size()-1) { this._tupleNames += ":"; }
        }

        if (this._combo) { //NOTE: MC Part for MC/REC combo //TODO: Note: vt and chi2pid/status entries (all 0) are added for combo MC particles...could make nicer...
            this._tupleNames += ":"; // IMPORTANT! Not added to last entry from above.
            HashMap<Integer,Integer> pidCountsMC = new HashMap<Integer,Integer>();
            ArrayList<Integer> unique_pidsMC = this._mcdecay.stream().distinct().collect(Collectors.toList());
            for (Integer pid : unique_pidsMC) { pidCountsMC.put(pid,0); }
            for (int i=0; i<this._mcdecay.size(); i++) {
                Integer pid = this._mcdecay.get(i);
                pidCountsMC[pid] += 1;
                pname = this._constants.getName(pid);
                if (i!=0) { if (pid==this._mcdecay.get(i-1)) { pname += pidCountsMC.get(pid); } }
                for (String name : names) { this._tupleNames += name + pname + "_MC"; }
                if (i!=this._mcdecay.size()-1) { this._tupleNames += ":"; }
            }
        }

        // Reset names
        names = ["px_",":py_",":pz_"];
        if (this._addVertices) { names += [":vx_",":vy_",":vz_"]; } //NOTE: Just a groovy capability, MC::Lund does not have vt entry
        if (this._addAngles) { names += [":theta_",":phi_"]; } //NOTE: Just a groovy capability
        if (!this._require_pid) {names += [":pid_"]; } //NOTE: Just a groovy capability // use .addAll() for java

        // Double entries if requiring MC::Lund and REC::Particle (matching option)
        if (this._match) {
            this._tupleNames += ":"; // IMPORTANT! Not added to last entry from above. 
            for (int i=0; i<this._decay.size(); i++) {
                Integer pid = this._decay.get(i);
                pidCounts[pid] += 1;
                pname = this._constants.getName(pid);
                if (i!=0) { if (pid==this._decay.get(i-1)) { pname += pidCounts.get(pid); } }
                for (String name : names) { this._tupleNames += name + pname + "_MC"; }
                if (i!=this._decay.size()-1) { this._tupleNames += ":"; }
            }
        }
    }

    /**
    * Creates new outfiles with successively numbered names, splitting on number of
    * data events added to output or event/run number (TODO), modifiable via command
    * line argument.
    *
    * @param int num
    */
    protected void splitOutFile(int num) {

        if (this._split==0) { return; } // split number is zero if you don't want to split
        if (this._data_counter<Math.abs(this._split)) { return; } // don't split for first batch since tuple is already created in processFiles() method
        if (num % this._split !=0) { return; }
        if (this._split < 0) { // create new tuple in same file if split<0
            this._tuple.write();
            if ((this._data_counter>=this._n_events  && this._n_events>0)) { return; }
            this._tuple = this._outFile.makeNtuple(this._treeName+(num / -this._split),"title",this._tupleNames);
            return;
        }
        this.write2Root(); //IMPORTANT: Make sure you write out first, otherwise this is kind of pointless...

        // Print out useful info
        if (this._requireFC) {this._fiducialCuts.statistics();}
        System.out.println(" Events selected:  "+this._data_counter+"/"+this._event_counter);

        // Create ROOT Output file
        int index = num/this._split-1;
        int last; if (num == this._split) { last = 6; } else { last = 6+(index>=10 ? 3 : 2); }
        this._outPath = this._outPath[0..-last] + "_" + index + ".root"; // just a groovy capability, assumes file name end is .root
        this._outFile = new ROOTFile(this._outPath); //WARNING: This will currently overwrite existing files
        System.out.println(" Created new file: "+this._outPath);
        this._tuple = this._outFile.makeNtuple(this._treeName,"title",this._tupleNames);
    }

    /**
    * Loop through events in input files and write to output files.
    * @throws InterruptedException
    */
    protected void processFiles() throws InterruptedException {

        // Setup ROOT Output file and tuple
        this._outFile = new ROOTFile(this._outPath);
        this.setTupleNames(); // IMPORTANT! Call AFTER parsing.
        this._tuple = this._outFile.makeNtuple(this._treeName+(this._split<0 ? "0" : ""),"title",this._tupleNames);

        // Single file
        File folder = new File(this._inPath);
        if (folder.isFile() && folder.getName().endsWith(".hipo")) {

            // Open HIPO file and run analysis
            HipoReader reader = new HipoReader();
            reader.open(this._inPath);
            if (this._useMC && !this._combo && !this._match) { this.processMCEvents(reader); }
            if (this._combo) { this.processComboEvents(reader); }
            if (this._match) { this.processMatchEvents(reader); }
            else { this.processEvents(reader); }
        }

        // Multiple Files
        if (folder.isDirectory()) {
            File[] listOfFiles = folder.listFiles();
            int counter = 1;

            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().endsWith(".hipo")) {
                    System.out.println("============================== Accessing file "+counter+(counter>9 ? "" : " ")+" ==============================");
                                    
                    // Open HIPO file and run analysis
                    HipoReader reader = new HipoReader();
                    reader.open(this._inPath+file.getName());
                    if (this._useMC && !this._combo && !this._match) { this.processMCEvents(reader); }
                    if (this._combo) { this.processComboEvents(reader); }
                    if (this._match) { this.processMatchEvents(reader); }
                    else { this.processEvents(reader); }
                    counter += 1; if (this._split==1) { this.splitOutFile(counter); }
                }
                if (counter > this._n_files)
                    break;
            }
        }

        // Close out files
        this.write2Root();
        this.endMessage();

    }  // processFiles

    /**
    * Loop through events in input files and write to output files.
    * @param String files
    */
    protected void processFiles(String files) throws InterruptedException {

        this._inPath = files;
        this.processFiles();
    }

    /**
    * Print out ending message.  Pretty straightforward right?
    */
    protected void endMessage() {

        if (this._requireFC) {this._fiducialCuts.statistics();}
        System.out.println(" Done! Events selected:  "+this._data_counter+"/"+this._event_counter);
        System.out.println("------------------------------------------------------------------------------");
    }

} // class
