package org.jlab.analysis;

// Groovy Imports
import groovy.transform.CompileStatic;

// CLAS Physics Imports
import org.jlab.clas.physics.*;

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
    int       _gppid; //NOTE: ADDED
    int       _ggppid; //NOTE: ADDED
    int       _charge;
    double    _px;
    double    _py;
    double    _pz;
    double    _beta;
    double    _m;
    double    _e;
    double    _vx;
    double    _vy;
    double    _vz;
    double    _vt;
    double    _chi2pid;
    int       _stat;
    int       _detector;
    int       _sector;
    int       _detector_status;
    double    _detector_chi2ndf;
    int       _is_cfr;
    Constants _constants = new ExtendedConstants(); //TODO: Might slow things down a lot...

    public DecayProduct(int pid, double px, double py, double pz, double beta, double vx, double vy, double vz, double vt, double chi2pid, int stat) {
        this.setVector(pid,px,py,pz,vx,vy,vz);
        this._beta     = beta;
        this._vt       = vt;
        this._chi2pid  = chi2pid;
        this._stat     = stat;
    }

    public DecayProduct(int pid, double px, double py, double pz, double beta, double vx, double vy, double vz, int index, int parent, int daughter) {
        this.setVector(pid,px,py,pz,vx,vy,vz);
        this.setIndices(index,parent,daughter);
        this._beta = beta;
    } //NOTE: ADDED

    public DecayProduct(int pid, double px, double py, double pz, double beta, double vx, double vy, double vz, int index, int parent, int daughter, int ppid, int gppid, int ggppid) {
        this.setVector(pid,px,py,pz,vx,vy,vz);
        this.setIndices(index,parent,daughter);
        this.ppid(ppid);
        this.gppid(gppid);
        this.ggppid(ggppid);
        this._beta = beta;
    }

    public DecayProduct(int pid, double px, double py, double pz, double beta, double vx, double vy, double vz) {
        this.setVector(pid,px,py,pz,vx,vy,vz);
        this._beta     = beta;
        this._vt       = (double) 0;
        this._chi2pid  = (double) 0;
        this._stat     = (int)    0;
    }

    public DecayProduct(int pid, double px, double py, double pz, double beta) {
        this.setVector(pid,px,py,pz,0,0,0);
        this._beta     = beta;
        this._vt       = (double) 0;
        this._chi2pid  = (double) 0;
        this._stat     = (int)    0;
    }

    public DecayProduct(Particle p, double beta, double vt, double chi2pid, int stat) {
        this.setVector(p.pid(),p.px(),p.py(),p.pz(),p.vx(),p.vy(),p.vz());
        this._beta     = (double) beta;
        this._vt       = (double) vt;
        this._chi2pid  = (double) chi2pid;
        this._stat     = (int)    stat;
    }

    public DecayProduct(Particle p) {
        this.setVector(p.pid(),p.px(),p.py(),p.pz(),p.vx(),p.vy(),p.vz());
        this._beta     = (double) 0;
        this._vt       = (double) 0;
        this._chi2pid  = (double) 0;
        this._stat     = (int)    0;
    }

    public DecayProduct(DecayProduct p) {
        this.setVector(p.pid(),p.px(),p.py(),p.pz(),p.vx(),p.vy(),p.vz());
        this._beta             = (double) p.beta();
        this._vt               = (double) p.vt();
        this._chi2pid          = (double) p.chi2pid();
        this._stat             = (int)    p.status();
        this._detector         = (int)    p.detector();
        this._sector           = (int)    p.sector();
        this._detector_status  = (int)    p.detector_status();
        this._detector_chi2ndf = (double) p.detector_chi2ndf();
        this.setIndices(p.index(),p.parent(),p.daughter());//NOTE: ADDED
        this.ppid(p.ppid());//NOTE: ADDED
        this.gppid(p.gppid());//NOTE: ADDED
        this.ggppid(p.ggppid());//NOTE: ADDED
        this.m(p.m());
    }

    /**
    * Resets ALL attributes to clone given particle.
    * @param p
    */
    protected void clone(DecayProduct p) {

        this.setVector(p.pid(),p.px(),p.py(),p.pz(),p.vx(),p.vy(),p.vz());
        this._beta             = (double) p.beta();
        this._vt               = (double) p.vt();
        this._chi2pid          = (double) p.chi2pid();
        this._stat             = (int)    p.status();
        this._detector         = (int)    p.detector();
        this._sector           = (int)    p.sector();
        this._detector_status  = (int)    p.detector_status();
        this._detector_chi2ndf = (double) p.detector_chi2ndf();
        this.setIndices(p.index(),p.parent(),p.daughter());//NOTE: ADDED
        this.ppid(p.ppid());//NOTE: ADDED
        this.gppid(p.gppid());//NOTE: ADDED
        this.ggppid(p.ggppid());//NOTE: ADDED
        this.m(p.m());
    }

    /**
    * Clones this instance.
    * @return this
    */
    protected DecayProduct clone() {

        return new DecayProduct(this);
    }

    /**
    * Set pid, momenta, and vertices for particle.  Also sets charge and mass from pid, and sets the energy from the mass and momenta.
    * @param pid
    * @param px
    * @param py
    * @param pz
    * @param vx
    * @param vy
    * @param vz
    */
    protected void setVector(int pid, double px, double py, double pz, double vx, double vy, double vz) {

        this.pid(pid); this._px = px; this._py = py; this._pz = pz; this._vx = vx; this._vy = vy; this._vz = vz;
        this._charge = this._constants.getCharge(pid);
        this._m      = this._constants.getMass(pid);
        this._e      = Math.sqrt(this._m*this._m+this.p2());
    }

    /**
    * Set bank, parent and daughter indices for particle.
    * @param index
    * @param parent
    * @param daughter
    */
    protected void setIndices(int index, int parent, int daughter) {

        this.index(index); this.parent(parent); this.daughter(daughter);
    }

    /**
    * Set particle's pid.
    * @param pid
    */
    protected void pid(int pid) {

        this._pid = pid;
        this._m   = this._constants.getMass(pid);
    }

    /**
    * Set particle's pid.  (Duplicates this.pid(int pid) method for 
    * consistency with clas Particle class.)
    * @param pid
    */
    protected void changePid(int pid) {

        this.pid(pid);
    }

    /**
    * Access particle's pid.
    * @return _pid
    */
    protected int pid() {

        return this._pid;
    }

    /**
    * Set particle's bank index.
    * @param index
    */
    protected void index(int index) {

        this._index = index;
    }

    /**
    * Access particle's bank index.
    * @return _index
    */
    protected int index() {

        return this._index;
    }

    /**
    * Set particle's parent index.
    * @param parent
    */
    protected void parent(int parent) {

        this._parent = parent;
    }

    /**
    * Access particle's parent index.
    * @return _parent
    */
    protected int parent() {

        return this._parent;
    }

    /**
    * Set particle's parent pid.
    * @param ppid
    */
    protected void ppid(int ppid) {

        this._ppid = ppid;
    }

    /**
    * Access particle's parent pid.
    * @return _ppid
    */
    protected int ppid() {

        return this._ppid;
    }

    /**
    * Set particle's grandparent pid.
    * @param gppid
    */
    protected void gppid(int gppid) {

        this._gppid = gppid;
    }

    /**
    * Access particle's grandparent pid.
    * @return _gppid
    */
    protected int gppid() {

        return this._gppid;
    }

    /**
    * Set particle's great grandparent pid.
    * @param ggppid
    */
    protected void ggppid(int ggppid) {

        this._ggppid = ggppid;
    }

    /**
    * Access particle's great grandparent pid.
    * @return _ggppid
    */
    protected int ggppid() {

        return this._ggppid;
    }

    /**
    * Set particle's daughter index.
    * @param daughter
    */
    protected void daughter(int daughter) {

        this._daughter = daughter;
    }

    /**
    * Access particle's daughter index.
    * @return _daughter
    */
    protected int daughter() {

        return this._daughter;
    }

    /**
    * Set particle's charge.
    * @param charge
    */
    protected void charge(int charge) {

        this._charge = charge;
    }

    /**
    * Access particle's charge.
    * @return _charge
    */
    protected int charge() {

        return this._charge;
    }

    /**
    * Set particle's x momentum.
    * @param px
    */
    protected void px(double px) {

        this._px = px;
    }

    /**
    * Access particle's x momentum.
    * @return _px
    */
    protected double px() {

        return this._px;
    }

    /**
    * Set particle's y momentum.
    * @param py
    */
    protected void py(double py) {

        this._py = py;
    }

    /**
    * Access particle's y momentum.
    * @return _py
    */
    protected double py() {

        return this._py;
    }

    /**
    * Set particle's z momentum.
    * @param pz
    */
    protected void pz(double pz) {

        this._pz = pz;
    }

    /**
    * Access particle's z momentum.
    * @return _pz
    */
    protected double pz() {

        return this._pz;
    }

    /**
    * Set particle's beta.
    * @param beta
    */
    protected void beta(double beta) {

        this._beta = beta;
    }

    /**
    * Access particle's beta.
    * @return _beta
    */
    protected double beta() {

        return this._beta;
    }

    /**
    * Access particle's transverse momentum.
    * @return _pt
    */
    protected double pt() {

        return Math.sqrt(this._px*this._px+this._py*this._py);
    }

    /**
    * Access particle's total momentum.
    * @return p
    */
    protected double p() {

        return Math.sqrt(this._px*this._px+this._py*this._py+this._pz*this._pz);
    }

    /**
    * Access particle's total momentum squared.
    * @return p2
    */
    protected double p2() {

        return this._px*this._px+this._py*this._py+this._pz*this._pz;
    }

    /**
    * Access particle's azimuthal angle.
    * @return phi
    */
    protected double phi() {

        return this._py>0 ? Math.acos(this._px/this.pt()) : -Math.acos(this._px/this.pt());
        // return Math.asin(this._py/this.pt()); //NOTE: Keeps return value between ±pi
    }

    /**
    * Access particle's polar angle.
    * @return theta
    */
    protected double theta() {

        return Math.acos(this._pz/this.p());
    }

    /**
    * Access particle's rapidity.
    * @return rapidity
    */
    protected double rapidity() {
        double arg = (this.p() + this.pz()) / (this.p() - this.pz());
        if (arg<=0.0) {
            return this.pz() > 0.0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        }
        return 0.5 * Math.log(arg);
    }

    /**
    * Access particle's pseudorapidity eta.
    * @return eta
    */
    protected double eta() {
        double arg = Math.tan(this.theta()/2.0);
        if (arg<=0.0) {
            return this.pz() > 0.0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        }
        return - Math.log(arg);
    }

    /**
    * Set particle's momentum vector.
    * @param px
    * @param py
    * @param pz
    */
    protected void mom(double px, double py, double pz) {

        this._px = px; this._py = py; this._pz = pz;
    }

    /**
    * Access particle's momentum vector.
    * @return mom
    */
    protected Vector3 mom() {

        return new Vector3(this._px,this._py,this._pz);
    }

    /**
    * Set particle's mass.
    * @param m
    */
    protected void m(double m) {

        this._m = m;
        this._e = Math.sqrt(m*m+this.p2());
    }

    /**
    * Change particle's mass leaving pid same
    * @param pid
    */
    protected void changeMass(int pid) {

        this.m(this._constants.getMass(pid));
    }

    /**
    * Access particle's mass.
    * @return _m
    */
    protected double m() {

        return this._m;
    }

    /**
    * Set particle's energy.
    * @param e
    */
    protected void e(double e) {

        this._e = e;
        this._m = Math.sqrt(e*e-this.p2());
    }

    /**
    * Access particle's energy.
    * @return _e
    */
    protected double e() {

        return this._e;
    }

    /**
    * Access particle's lorentz vector.
    * @return lv
    */
    protected LorentzVector lv() {

        return new LorentzVector(this._px,this._py,this._pz,this._e);
    }

    /**
    * Set particle's lorentz vector.
    * @param px
    * @param py
    * @param pz
    * @param e
    */
    protected void setPxPyPzE(double px, double py, double pz, double e) {

        this._px = px; this._py = py; this._pz = pz; this._e = e;
    }

    /**
    * Set particle's lorentz vector using particle mass instead of energy.
    * @param px
    * @param py
    * @param pz
    * @param mass
    */
    protected void setPxPyPzM(double px, double py, double pz, double mass) {

        this._px = px; this._py = py; this._pz = pz; this.m(mass);
    }

    /**
    * Set particle's lorentz vector using particle mass instead of energy.
    * @param px
    * @param py
    * @param pz
    * @param mass
    */
    protected void setPThetaPhiM(double p, double theta, double phi, double mass) {

        double px = p * Math.sin(theta) * Math.cos(phi);
        double py = p * Math.sin(theta) * Math.sin(phi);
        double pz = p * Math.cos(theta);

        this.setPxPyPzM(px,py,pz,mass);
    }

    /**
    * Set particle's x vertex.
    * @param vx
    */
    protected void vx(double vx) {

        this._vx = vx;
    }

    /**
    * Access particle's x vertex.
    * @return _vx
    */
    protected double vx() {

        return this._vx;
    }

    /**
    * Set particle's y vertex.
    * @param vy
    */
    protected void vy(double vy) {

        this._vy = vy;
    }

    /**
    * Access particle's y vertex.
    * @return _vy
    */
    protected double vy() {

        return this._vy;
    }

    /**
    * Set particle's z vertex.
    * @param vz
    */
    protected void vz(double vz) {

        this._vz = vz;
    }

    /**
    * Access particle's z vertex.
    * @return _vz
    */
    protected double vz() {

        return this._vz;
    }

    /**
    * Set particle's time vertex.
    * @param vt
    */
    protected void vt(double vt) {

        this._vt = vt;
    }

    /**
    * Access particle's time vertex.
    * @return _vt
    */
    protected double vt() {

        return this._vt;
    }

    /**
    * Set particle's vertex.
    * @param vx
    * @param vy
    * @param vz
    */
    protected void vtx(double vx, double vy, double vz) {

        this._vx = vx; this._vy = vy; this._vz = vz;
    }

    /**
    * Access particle's vertex.
    * @return vtx
    */
    protected Vector3 vtx() {

        return new Vector3(this._vx,this._vy,this._vz);
    }

    /**
    * Set particle's vertex.
    * @param vx
    * @param vy
    * @param vz
    * @param vt
    */
    protected void vLv(double vx, double vy, double vz, double vt) {

        this._vx = vx; this._vy = vy; this._vz = vz; this._vt = vt;
    }

    /**
    * Access particle's vertex lorentz vector.
    * @return vLv
    */
    protected LorentzVector vLv() {

        return new LorentzVector(this._vx,this._vy,this._vz,this._vt);
    }

    /**
    * Set particle's chi2pid.
    * @param chi2pid
    */
    protected void chi2pid(double chi2pid) {

        this._chi2pid = chi2pid;
    }

    /**
    * Access REC::Particle particle's chi2pid.
    * @return _chi2pid
    */
    protected double chi2pid() {

        return this._chi2pid;
    }

    /**
    * Set REC::Particle particle's status.
    * @param stat
    */
    protected void status(int stat) {

        this._stat = stat;
    }

    /**
    * Access REC::Particle particle's status.
    * @return _stat
    */
    protected int status() {

        return this._stat;
    }

    /**
    * Set REC::Particle particle's detector from REC::Track.
    * @param detector
    */
    protected void detector(int detector) {

        this._detector = detector;
    }

    /**
    * Access REC::Particle particle's detector from REC::Track.
    * @return _detector
    */
    protected int detector() {

        return this._detector;
    }

    /**
    * Set REC::Particle particle's sector from REC::Track.
    * @param sector
    */
    protected void sector(int sector) {

        this._sector = sector;
    }

    /**
    * Access REC::Particle particle's sector from REC::Track.
    * @return _sector
    */
    protected int sector() {

        return this._sector;
    }

    /**
    * Set REC::Particle particle's detector status from REC::Track.
    * @param detector_status
    */
    protected void detector_status(int detector_status) {

        this._detector_status = detector_status;
    }

    /**
    * Access REC::Particle particle's detector status from REC::Track.
    * @return _detector_status
    */
    protected int detector_status() {

        return this._detector_status;
    }

    /**
    * Set REC::Particle particle's detector chi2/ndf from REC::Track.
    * @param detector_chi2ndf
    */
    protected void detector_chi2ndf(double detector_chi2ndf) {

        this._detector_chi2ndf = detector_chi2ndf;
    }

    /**
    * Access REC::Particle particle's detector chi2/ndf from REC::Track.
    * @return _detector_chi2ndf
    */
    protected double detector_chi2ndf() {

        return this._detector_chi2ndf;
    }

    /**
    * Set MC::Lund particle fragmentation mechanism status.
    * @param is_cfr
    */
    protected void is_cfr(int is_cfr) {

        this._is_cfr = is_cfr;
    }

    /**
    * Access MC::Lund particle fragmentation mechanism status.
    * @return _is_cfr
    */
    protected double is_cfr() {

        return this._is_cfr;
    }

} // class
