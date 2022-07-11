package org.jlab.analysis;

// Groovy Imports
import groovy.transform.CompileStatic;

// CLAS Physics Imports
import org.jlab.jnp.physics.*;

/**
* Adds (essentially) time vertex, chi2pid, and status attributes
* to the standard Particle class minus some problematic methods for groovy
* as well as less used methods.
*
* @version 1.0
* @author  Matthew McEneaney
*/

@CompileStatic
public class DecayProduct {

    int       _pid;
    int       _index;
    int       _parent;
    int       _daughter;
    int       _ppid; //NOTE: ADDED
    int       _charge;
    double    _px;
    double    _py;
    double    _pz;
    double    _m;
    double    _e;
    double    _vx;
    double    _vy;
    double    _vz;
    double    _vt;
    double    _chi2pid;
    int       _stat;
    Constants _constants = new ExtendedConstants(); //TODO: Might slow things down a lot...

    public DecayProduct(int pid, double px, double py, double pz, double vx, double vy, double vz, double vt, double chi2pid, int stat) {
        this.setVector(pid,px,py,pz,vx,vy,vz);
        this._vt       = vt;
        this._chi2pid  = chi2pid;
        this._stat     = stat;
    }

    public DecayProduct(int pid, double px, double py, double pz, double vx, double vy, double vz, int index, int parent, int daughter) {
        this.setVector(pid,px,py,pz,vx,vy,vz);
        this.setIndices(index,parent,daughter);
    } //NOTE: ADDED

    public DecayProduct(int pid, double px, double py, double pz, double vx, double vy, double vz, int index, int parent, int daughter, int ppid) {
        this.setVector(pid,px,py,pz,vx,vy,vz);
        this.setIndices(index,parent,daughter);
        this.ppid(ppid);
    }

    public DecayProduct(int pid, double px, double py, double pz, double vx, double vy, double vz) {
        this.setVector(pid,px,py,pz,vx,vy,vz);
        this._vt       = (double) 0;
        this._chi2pid  = (double) 0;
        this._stat     = (int)    0;
    }

    public DecayProduct(int pid, double px, double py, double pz) {
        this.setVector(pid,px,py,pz,0,0,0);
        this._vt       = (double) 0;
        this._chi2pid  = (double) 0;
        this._stat     = (int)    0;
    }

    public DecayProduct(Particle p, double vt, double chi2pid, int stat) {
        this.setVector(p.pid(),p.px(),p.py(),p.pz(),p.vx(),p.vy(),p.vz());
        this._vt       = (double) vt;
        this._chi2pid  = (double) chi2pid;
        this._stat     = (int)    stat;
    }

    public DecayProduct(Particle p) {
        this.setVector(p.pid(),p.px(),p.py(),p.pz(),p.vx(),p.vy(),p.vz());
        this._vt       = (double) 0;
        this._chi2pid  = (double) 0;
        this._stat     = (int)    0;
    }

    public DecayProduct(DecayProduct p) {
        this.setVector(p.pid(),p.px(),p.py(),p.pz(),p.vx(),p.vy(),p.vz());
        this._vt       = (double) p.vt();
        this._chi2pid  = (double) p.chi2pid();
        this._stat     = (int)    p.status();
        this.setIndices(p.index(),p.parent(),p.daughter());//NOTE: ADDED
        this.ppid(p.ppid());//NOTE: ADDED    
    }

    /**
    * Resets ALL attributes to clone given particle.
    * @param DecayProduct p
    */
    protected void clone(DecayProduct p) {

        this.setVector(p.pid(),p.px(),p.py(),p.pz(),p.vx(),p.vy(),p.vz());
        this._vt      = (double) p.vt();
        this._chi2pid = (double) p.chi2pid();
        this._stat    = (int)    p.status();
    }

    /**
    * Clones this instance.
    * @return DecayProduct this
    */
    protected DecayProduct clone() {

        return new DecayProduct(this);
    }

    /**
    * Set pid, momenta, and vertices for particle.  Also sets charge and mass from pid, and sets the energy from the mass and momenta.
    * @param int pid
    * @param double px
    * @param double py
    * @param double pz
    * @param double vx
    * @param double vy
    * @param double vz
    */
    protected void setVector(int pid, double px, double py, double pz, double vx, double vy, double vz) {

        this.pid(pid); this._px = px; this._py = py; this._pz = pz; this._vx = vx; this._vy = vy; this._vz = vz;
        this._charge = this._constants.getCharge(pid);
        this._m      = this._constants.getMass(pid);
        this._e      = Math.sqrt(this._m*this._m+this.p2());
    }

    /**
    * Set bank, parent and daughter indices for particle.
    * @param int index
    * @param int parent
    * @param int daughter
    */
    protected void setIndices(int index, int parent, int daughter) {

        this.index(index); this.parent(parent); this.daughter(daughter);
    }

    /**
    * Set particle's pid.
    * @param int pid
    */
    protected void pid(int pid) {

        this._pid = pid;
        this._m   = this._constants.getMass(pid);
    }

    /**
    * Set particle's pid.  (Duplicates this.pid(int pid) method for 
    * consistency with clas Particle class.)
    * @param int pid
    */
    protected void changePid(int pid) {

        this.pid(pid);
    }

    /**
    * Access particle's pid.
    * @return int _pid
    */
    protected int pid() {

        return this._pid;
    }

    /**
    * Set particle's bank index.
    * @param int index
    */
    protected void index(int index) {

        this._index = index;
    }

    /**
    * Access particle's bank index.
    * @return int _index
    */
    protected int index() {

        return this._index;
    }

    /**
    * Set particle's parent index.
    * @param int parent
    */
    protected void parent(int parent) {

        this._parent = parent;
    }

    /**
    * Access particle's parent index.
    * @return int _parent
    */
    protected int parent() {

        return this._parent;
    }

    /**
    * Set particle's parent pid.
    * @param int ppid
    */
    protected void ppid(int ppid) {

        this._ppid = ppid;
    }

    /**
    * Access particle's parent pid.
    * @return int _ppid
    */
    protected int ppid() {

        return this._ppid;
    }

    /**
    * Set particle's daughter index.
    * @param int daughter
    */
    protected void daughter(int daughter) {

        this._daughter = daughter;
    }

    /**
    * Access particle's daughter index.
    * @return int _daughter
    */
    protected int daughter() {

        return this._daughter;
    }

    /**
    * Set particle's charge.
    * @param int charge
    */
    protected void charge(int charge) {

        this._charge = charge;
    }

    /**
    * Access particle's charge.
    * @return int _charge
    */
    protected int charge() {

        return this._charge;
    }

    /**
    * Set particle's x momentum.
    * @param double px
    */
    protected void px(double px) {

        this._px = px;
    }

    /**
    * Access particle's x momentum.
    * @return double _px
    */
    protected double px() {

        return this._px;
    }

    /**
    * Set particle's y momentum.
    * @param double py
    */
    protected void py(double py) {

        this._py = py;
    }

    /**
    * Access particle's y momentum.
    * @return double _py
    */
    protected double py() {

        return this._py;
    }

    /**
    * Set particle's z momentum.
    * @param double pz
    */
    protected void pz(double pz) {

        this._pz = pz;
    }

    /**
    * Access particle's z momentum.
    * @return double _pz
    */
    protected double pz() {

        return this._pz;
    }

    /**
    * Access particle's transverse momentum.
    * @return double _pt
    */
    protected double pt() {

        return Math.sqrt(this._px*this._px+this._py*this._py);
    }

    /**
    * Access particle's total momentum.
    * @return double p
    */
    protected double p() {

        return Math.sqrt(this._px*this._px+this._py*this._py+this._pz*this._pz);
    }

    /**
    * Access particle's total momentum squared.
    * @return double p2
    */
    protected double p2() {

        return this._px*this._px+this._py*this._py+this._pz*this._pz;
    }

    /**
    * Access particle's azimuthal angle.
    * @return double phi
    */
    protected double phi() {

        return this._py>0 ? Math.acos(this._px/this.pt()) : -Math.acos(this._px/this.pt());
        // return Math.asin(this._py/this.pt()); //NOTE: Keeps return value between ±pi
    }

    /**
    * Access particle's polar angle.
    * @return double theta
    */
    protected double theta() {

        return Math.acos(this._pz/this.p());
    }

    /**
    * Set particle's momentum vector.
    * @param double px
    * @param double py
    * @param double pz
    */
    protected void mom(double px, double py, double pz) {

        this._px = px; this._py = py; this._pz = pz;
    }

    /**
    * Access particle's momentum vector.
    * @return Vector3 mom
    */
    protected Vector3 mom() {

        return new Vector3(this._px,this._py,this._pz);
    }

    /**
    * Set particle's mass.
    * @param double m
    */
    protected void m(double m) {

        this._m = m;
        this._e = Math.sqrt(m*m+this.p2());
    }

    /**
    * Change particle's mass leaving pid same
    * @param int pid
    */
    protected void changeMass(int pid) {

        this.m(this._constants.getMass(pid));
    }

    /**
    * Access particle's mass.
    * @return double _m
    */
    protected double m() {

        return this._m;
    }

    /**
    * Set particle's energy.
    * @param double e
    */
    protected void e(double e) {

        this._e = e;
        this._m = Math.sqrt(e*e-this.p2());
    }

    /**
    * Access particle's energy.
    * @return double _e
    */
    protected double e() {

        return this._e;
    }

    /**
    * Access particle's lorentz vector.
    * @return Vector3 lv
    */
    protected LorentzVector lv() {

        return new LorentzVector(this._px,this._py,this._pz,this._e);
    }

    /**
    * Set particle's lorentz vector.
    * @param double px
    * @param double py
    * @param double pz
    * @param double e
    */
    protected void setPxPyPzE(double px, double py, double pz, double e) {

        this._px = px; this._py = py; this._pz = pz; this._e = e;
    }

    /**
    * Set particle's lorentz vector using particle mass instead of energy.
    * @param double px
    * @param double py
    * @param double pz
    * @param double mass
    */
    protected void setPxPyPzM(double px, double py, double pz, double mass) {

        this._px = px; this._py = py; this._pz = pz; this.m(mass);
    }

    /**
    * Set particle's x vertex.
    * @param double vx
    */
    protected void vx(double vx) {

        this._vx = vx;
    }

    /**
    * Access particle's x vertex.
    * @return double _vx
    */
    protected double vx() {

        return this._vx;
    }

    /**
    * Set particle's y vertex.
    * @param double vy
    */
    protected void vy(double vy) {

        this._vy = vy;
    }

    /**
    * Access particle's y vertex.
    * @return double _vy
    */
    protected double vy() {

        return this._vy;
    }

    /**
    * Set particle's z vertex.
    * @param double vz
    */
    protected void vz(double vz) {

        this._vz = vz;
    }

    /**
    * Access particle's z vertex.
    * @return double _vz
    */
    protected double vz() {

        return this._vz;
    }

    /**
    * Set particle's time vertex.
    * @param double vt
    */
    protected void vt(double vt) {

        this._vt = vt;
    }

    /**
    * Access particle's time vertex.
    * @return double _vt
    */
    protected double vt() {

        return this._vt;
    }

    /**
    * Set particle's vertex.
    * @param double vx
    * @param double vy
    * @param double vz
    */
    protected void vtx(double vx, double vy, double vz) {

        this._vx = vx; this._vy = vy; this._vz = vz;
    }

    /**
    * Access particle's vertex.
    * @return Vector3 vtx
    */
    protected Vector3 vtx() {

        return new Vector3(this._vx,this._vy,this._vz);
    }

    /**
    * Set particle's vertex.
    * @param double vx
    * @param double vy
    * @param double vz
    * @param double vt
    */
    protected void vLv(double vx, double vy, double vz, double vt) {

        this._vx = vx; this._vy = vy; this._vz = vz; this._vt = vt;
    }

    /**
    * Access particle's vertex lorentz vector.
    * @return Vector3 vLv
    */
    protected LorentzVector vLv() {

        return new LorentzVector(this._vx,this._vy,this._vz,this._vt);
    }

    /**
    * Set particle's chi2pid.
    * @param double chi2pid
    */
    protected void chi2pid(double chi2pid) {

        this._chi2pid = chi2pid;
    }

    /**
    * Access REC::Particle particle's chi2pid.
    * @return double _chi2pid
    */
    protected double chi2pid() {

        return this._chi2pid;
    }

    /**
    * Set REC::Particle particle's status.
    * @param int stat
    */
    protected void status(int stat) {

        this._stat = stat;
    }

    /**
    * Access REC::Particle particle's status.
    * @return int _stat
    */
    protected int status() {

        return this._stat;
    }

} // class
