package org.jlab.analysis;

// Groovy Imports
import groovy.transform.CompileStatic

// Java Imports
import java.util.HashMap;

/**
* Sets the Lund pid to particle mass/charge/name hashmaps and other analysis constants.
*
* @version 1.0
* @author  Matthew McEneaney
*/

@CompileStatic
public class Constants {

    protected static double  _C;
    protected double         _S, _T, _BE, _BM, _TE, _TM;
    protected int            _BPID, _TPID;
    protected HashMap<Integer, Double>   _MMAP;
    protected HashMap<Integer, Integer>  _QMAP;
    protected HashMap<Integer, String>   _NMAP;
    protected static int _pid_e, _pid_pi, _pid_k, _pid_p, _pid_d, _pid_n, _pid_g;
    protected static double _m_e, _m_pi, _m_k, _m_p, _m_d, _m_n, _m_g;
    protected static int _q_e, _q_pi, _q_k, _q_p, _q_d, _q_n, _q_g;
    protected static String _n_e, _n_pi, _n_k, _n_p, _n_d, _n_n, _n_g;

    /**
    * Constructor stub
    */
    public Constants() {

        // Set constants
        this._C      = (double) 2.99792458 * Math.pow(10,8); // speed of light [m/s]
        this._S      = (double) -1.0;                        // Scaled solenoid field
        this._T      = (double) 1.0;                         // Scaled torus field
        this._BE     = (double) 10.6;                        // (electron) beam energy [GeV]
        this._BM     = (double) 0.000510998950;              // (electron) beam particle mass [GeV/c^2]
        this._BPID   = (int)    11;                          // (electron) beam particle pid [Lund pid]
        this._TE     = (double) 0.0;                         // (proton) target energy [GeV]
        this._TM     = (double) 0.93827208816;               // (proton) target particle mass [GeV/c^2]
        this._TPID   = (int)    2212;                        // (proton) target particle pid [Lund pid]               

        // Common pids
        this._pid_e  = 11;    // [Lund pid]
        this._pid_pi = 211;   // [Lund pid]
        this._pid_k  = 321;   // [Lund pid]
        this._pid_p  = 2212;  // [Lund pid]
        this._pid_d  = 45;    // [Lund pid] // ???
        this._pid_n  = 2112;  // [Lund pid]
        this._pid_g  = 22;    // [Lund pid]

        // Common masses
        this._m_e  = (double) 0.000510998950;   // [GeV/c^2]
        this._m_pi = (double) 0.139570;         // [GeV/c^2]
        this._m_k  = (double) 0.493677;         // [GeV/c^2]
        this._m_p  = (double) 0.93827208816;    // [GeV/c^2]
        this._m_d  = (double) 0.0;              // [GeV/c^2] // ???
        this._m_n  = (double) 0.93956542052;    // [GeV/c^2]
        this._m_g  = (double) 0.0;              // [GeV/c^2]

        // Common charges
        this._q_e  = (int) -1;   // [e]
        this._q_pi = (int) 1;    // [e]
        this._q_k  = (int) 1;    // [e]
        this._q_p  = (int) 1;    // [e]
        this._q_d  = (int) 1;    // [e] // ???
        this._q_n  = (int) 0;    // [e]
        this._q_g  = (int) 0;    // [e]

        // Common names
        this._n_e  = new String("e");
        this._n_pi = new String("pi");
        this._n_k  = new String("k");
        this._n_p  = new String("p");
        this._n_d  = new String("d");
        this._n_n  = new String("n");
        this._n_g  = new String("g");

        // Create mass map and add key value pairs
        this._MMAP = new HashMap<Integer, Double>();
        this._MMAP.put(0,(double) 0.0);
        this._MMAP.put(this._pid_e,this._m_e);
        this._MMAP.put(this._pid_pi,this._m_pi);
        this._MMAP.put(this._pid_k,this._m_k);
        this._MMAP.put(this._pid_p,this._m_p);
        this._MMAP.put(this._pid_d,this._m_d);
        this._MMAP.put(this._pid_n,this._m_n);
        this._MMAP.put(this._pid_g,this._m_g);

        // Create charge map and add key value pairs
        this._QMAP = new HashMap<Integer, Integer>();
        this._QMAP.put(0,0);
        this._QMAP.put(this._pid_e,this._q_e);
        this._QMAP.put(this._pid_pi,this._q_pi);
        this._QMAP.put(this._pid_k,this._q_k);
        this._QMAP.put(this._pid_p,this._q_p);
        this._QMAP.put(this._pid_d,this._q_d);
        this._QMAP.put(this._pid_n,this._q_n);
        this._QMAP.put(this._pid_g,this._q_g);

        // Create name map and add key value pairs
        this._NMAP = new HashMap<Integer, String>();
        this._NMAP.put(0,"undefined");
        this._NMAP.put(this._pid_e,this._n_e);
        this._NMAP.put(this._pid_pi,this._n_pi);
        this._NMAP.put(this._pid_k,this._n_k);
        this._NMAP.put(this._pid_p,this._n_p);
        this._NMAP.put(this._pid_d,this._n_d);
        this._NMAP.put(this._pid_n,this._n_n);
        this._NMAP.put(this._pid_g,this._n_g);

        
    } // constructor stub

    /**
    * Access speed of light [m/s].
    * @return double _C
    */
    protected double getC() {
        return this._C;
    }

    /**
    * Access scaled solenoid field [T].
    * @return float _S
    */
    protected double getS() {
        return this._S;
    }

    /**
    * Set scaled solenoid field [T].
    * @param double S
    */
    protected void setS(double S) {
        this._S = S;
    }
   /**
    * Access scaled torus field [T].
    * @return double _T
    */
    protected double getT() {
        return this._T;
    }

    /**
    * Set scaled torus field [T].
    * @param double T
    */
    protected void setT(double T) {
        this._T = T;
    }

    /**
    * Access beam energy [GeV].
    * @return double _BE
    */
    protected double getBeamE() {
        return this._BE;
    }

    /**
    * Set beam energy [GeV].
    * @param double BE
    */
    protected void setBeamE(double BE) {
        this._BE = BE;
    }

    /**
    * Access beam mass [GeV/c^2].
    * @return double _BM
    */
    protected double getBeamM() {
        return this._BM;
    }

    /**
    * Set beam mass [GeV/c^2].  Currently does nothing.
    * @param double BM
    */
    protected void setBeamM(double BM) {
        //this._BM = BM
    }

    /**
    * Access beam Lund pid.
    * @return int _BPID
    */
    protected int getBeamPID() {
        return this._BPID;
    }

    /**
    * Set beam Lund pid.
    * @param int BPID
    */
    protected void setBeamPID(int BPID) {
        this._BPID = BPID;
        this._BM   = this._MMAP.get(BPID);
    }

    /**
    * Access target energy [GeV].
    * @return double _TE
    */
    protected double getTargetE() {
        return this._TE;
    }

    /**
    * Set target energy [GeV].
    * @param double TE
    */
    protected void setTargetE(double TE) {
        this._TE = TE;
    }

    /**
    * Access target mass [GeV/c^2].
    * @return double _TM
    */
    protected double getTargetM() {
        return this._TM;
    }

    /**
    * Set Target mass [GeV/c^2].  Currently does nothing unless pid mass pair is already loaded.
    * @param double TM
    */
    protected void setTargetM(double TM) {
        this._TM = TM;
        for (int key : this._MMAP.keySet()) { if (this._MMAP.get(key)==TM) { this._TPID = key; } } //TODO: Not really sure if this will work all the time...

    }

    /**
    * Access target Lund pid.
    * @return int _TPID
    */
    protected int getTargetPID() {
        return this._TPID;
    }

    /**
    * Set Target Lund pid.
    * @param int TPID
    */
    protected void setTargetPID(int TPID) {
        this._TPID = TPID;
        this._TM   = this._MMAP.get(TPID);
    }

    /**
    * Access hashmap of Lund pid to mass.
    * @return HashMap<Integer,Double> _MMAP
    */
    protected HashMap<Integer,Double> getMMap() {
        return this._MMAP;
    }

    /**
    * Set hashmap of Lund pid to mass.
    * @param HashMap<Integer,Double> map
    */
    protected void setMMap(HashMap<Integer,Double> map) {
        this._MMAP = map;
    }

    /** 
    * Add key value pair to hashmap of Lund pid to mass.
    * @param int pid
    * @param double mass
    */
    protected void addMMapEntry(int pid, double mass) {
        this._MMAP.put(pid,mass);
    }

    /** 
    * Get mass by Lund pid (charge does not matter).  Returns 0 if pid not found.
    * @param int pid
    * @return double mass
    */
    protected double getMass(int pid) {
        if (this._MMAP.containsKey(Math.abs(pid))) { return this._MMAP.get(Math.abs(pid)); }
        else { return 0; }
    }

    /**
    * Access hashmap of Lund pid to charge.
    * @return HashMap<Integer,Integer> _QMAP
    */
    protected HashMap<Integer,Integer> getQMap() {
        return this._QMAP;
    }

    /**
    * Set hashmap of Lund pid to mass.
    * @param HashMap<Integer,Integer> map
    */
    protected void setQMap(HashMap<Integer,Integer> map) {
        this._QMAP = map;
    }

    /** 
    * Add key value pair to hashmap of Lund pid to charge.
    * @param int pid
    * @param int charge
    */
    protected void addQMapEntry(int pid, int q) {
        this._QMAP.put(pid,q);
    }

    /** 
    * Access charge by Lund pid.  Sets charge to 0 if not found in maps.
    * @param int pid
    * @return int charge
    */
    protected int getCharge(int pid) {
        int sign = 1;
        if (pid<0) { sign = -1; }
        try { return sign*this._QMAP.get(Math.abs(pid)); }
	    catch (Exception e) { return 0; }
    }

    /**
    * Access hashmap of Lund pid to name.
    * @return HashMap<Integer,String> _NMAP
    */
    protected HashMap<Integer,String> getNMap() {
            return this._NMAP;
        }

    /**
    * Set hashmap of Lund pid to name.
    * @param HashMap<Integer,String> map
    */
    protected void setNMap(HashMap<Integer,String> map) {
        this._NMAP = map;
    }

    /** 
    * Add key value pair to hashmap of Lund pid to name.
    * @param int pid
    * @param String n
    */
    protected void addNMapEntry(int pid, String n) {
        this._NMAP.put(Math.abs(pid),n);
    }

    /** 
    * Access particle name by Lund pid.  Returns string pid if pid not found in name map.
    * @param int pid
    * @return String name
    */
    protected String getName(int pid) {
        if (!this._NMAP.containsKey(Math.abs(pid))) { return Integer.toString(pid); }
        if (pid<0 && this._QMAP.get(Math.abs(pid))>0) { return this._NMAP.get(Math.abs(pid))+"m"; }
        if (pid<0 && this._QMAP.get(Math.abs(pid))<0) { return this._NMAP.get(Math.abs(pid))+"p"; }
        else { return this._NMAP.get(Math.abs(pid)); }
    }

} // class
