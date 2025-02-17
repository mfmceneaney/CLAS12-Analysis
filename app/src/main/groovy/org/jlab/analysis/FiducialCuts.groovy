package org.jlab.analysis;

// Groovy Imports
import groovy.transform.CompileStatic;

// Java Imports
import java.util.ArrayList;
import java.util.HashMap;

// CLAS Physics Imports
import org.jlab.jnp.hipo4.data.*;
import org.jlab.jnp.hipo4.io.*;
import org.jlab.jnp.physics.*;

/**
* Implements Stefan Diehl's (<a href = "mailto: sdiehl@jlab.org">sdiehl@jlab.org</a>) fiducial cuts: version 7/23/2020.
*
* @version 1.0
* @author  Matthew McEneaney
*/

@CompileStatic
public class FiducialCuts {

    static final int nReg = 3; // number of DC regions

    double[] part_Cal_PCAL_found = [0];
    double[] part_Cal_PCAL_sector = [0];
    double[] part_Cal_PCAL_energy = [0]; 
    double[] part_Cal_PCAL_time = [0];
    double[] part_Cal_PCAL_path = [0];
    double[] part_Cal_PCAL_x = [0];
    double[] part_Cal_PCAL_y = [0];
    double[] part_Cal_PCAL_z = [0];
    double[] part_Cal_PCAL_lu = [0];
    double[] part_Cal_PCAL_lv = [0];
    double[] part_Cal_PCAL_lw = [0];

    double[] part_Cal_ECIN_found = [0]; 
    double[] part_Cal_ECIN_sector = [0];
    double[] part_Cal_ECIN_energy = [0];
    double[] part_Cal_ECIN_time = [0];
    double[] part_Cal_ECIN_path = [0];
    double[] part_Cal_ECIN_x = [0];
    double[] part_Cal_ECIN_y = [0];
    double[] part_Cal_ECIN_z = [0];
    double[] part_Cal_ECIN_lu = [0];
    double[] part_Cal_ECIN_lv = [0];
    double[] part_Cal_ECIN_lw = [0];

    double[] part_Cal_ECOUT_found = [0];
    double[] part_Cal_ECOUT_sector = [0];
    double[] part_Cal_ECOUT_energy = [0];
    double[] part_Cal_ECOUT_time = [0];
    double[] part_Cal_ECOUT_path = [0];
    double[] part_Cal_ECOUT_x = [0];
    double[] part_Cal_ECOUT_y = [0];
    double[] part_Cal_ECOUT_z = [0];
    double[] part_Cal_ECOUT_lu = [0];
    double[] part_Cal_ECOUT_lv = [0];
    double[] part_Cal_ECOUT_lw = [0];

    double[] part_DC_Track_found = [0];
    double[] part_DC_Track_chi2 = [0];
    double[] part_DC_Track_NDF = [0];
    double[] part_DC_Track_status = [0];

    double[] part_DC_Traj_found = [0];
    double[] part_DC_c1x = [0];
    double[] part_DC_c1y = [0];
    double[] part_DC_c1z = [0];
    double[] part_DC_c2x = [0];
    double[] part_DC_c2y = [0];
    double[] part_DC_c2z = [0];
    double[] part_DC_c3x = [0];
    double[] part_DC_c3y = [0];
    double[] part_DC_c3z = [0];

    private int[] part_DC_sector = [0];

    private boolean fcutElePCAL = false;
    private boolean[] fcutEleDC = [false, false, false];
    private boolean[] fcutHadDC = [false, false, false];

    // set default torus setting to inbending
    protected boolean inbending=true, outbending=false;

    // initialize public cut booleans
    protected boolean fiduCut = true; // set to true initially, important

    // misc
    protected int errCnt = 0;

    protected boolean[] _levels = [true,false,false];  //use loose cuts by default

    protected HipoReader _reader;
    protected Event      _event;

    protected int[] pCalCountF = [0,0,0,0,0,0]; // currently 6 entries for 6 pids with cuts applied
    protected int[] pCalCountP = [0,0,0,0,0,0]; // (11,2212,211,-211,321,-321)
    protected int[] dcTPCountF = [0,0,0,0,0,0];
    protected int[] dcTPCountP = [0,0,0,0,0,0];
    protected int[] dcXYCountF = [0,0,0,0,0,0];
    protected int[] dcXYCountP = [0,0,0,0,0,0];

    protected Constants _constants;

    /**
    * Constructor stub
    */
    public FiducialCuts(Constants constants) {

        this._constants = constants;
    }

    /**
    * Print out pass/fail statistics by pid for each type of fiducial cut applied.
    */
    protected void statistics() {

        int[] pids = [11,2212,211,-211,321,-321];

        println(" Fiducial Cut Statistics");
        print(sprintf('   Lund PIDs : %1$s\t%2$s\t%3$s\t%4$s\t%5$s\t%6$s\n',pids));
        println("               ---------------------------------------------------------------");
        print(sprintf('   Pass PCal : %1$s\t%2$s\t%3$s\t%4$s\t%5$s\t%6$s\n',this.pCalCountP));
        print(sprintf('   Fail PCal : %1$s\t%2$s\t%3$s\t%4$s\t%5$s\t%6$s\n',this.pCalCountF));
        print(sprintf('   Pass DC θφ: %1$s\t%2$s\t%3$s\t%4$s\t%5$s\t%6$s\n',this.dcTPCountP));
        print(sprintf('   Fail DC θφ: %1$s\t%2$s\t%3$s\t%4$s\t%5$s\t%6$s\n',this.dcTPCountF));
        print(sprintf('   Pass DC XY: %1$s\t%2$s\t%3$s\t%4$s\t%5$s\t%6$s\n',this.dcXYCountP));
        print(sprintf('   Fail DC XY: %1$s\t%2$s\t%3$s\t%4$s\t%5$s\t%6$s\n',this.dcXYCountF));
        println("               ---------------------------------------------------------------");
    }

    /**
    * Set the level of harshness for fiducial cuts: 0-loose, 1-medium, 2-tight.
    * @param level
    */
    protected void setLevel(int level) {

        for (int i=0; i<this._levels.length; i++) { if (i==level) { this._levels[i]=true; } else { this._levels[i]=false; } }
    }

    /**
    * Fill the arrays at the event level for PCal/ECal hits.  Hits in layers 1, 4, and 7 are required for a full track.
    * @param reader
    * @param event
    */
    protected void setPCalArrays(HipoReader reader, Event event) {

        // Read REC::Particle bank for # of entries and reset arrays
        Schema recSchema  = reader.getSchemaFactory().getSchema("REC::Particle");
        Bank   recBank    = new Bank(recSchema);
        event.read(recBank);
        int nrows = recBank.getRows();
        this.part_Cal_PCAL_found  = new int[nrows];
        this.part_Cal_PCAL_sector = new int[nrows];
        
        // Put REC::Calorimeter bank entries in arrays
        Schema calSchema  = reader.getSchemaFactory().getSchema("REC::Calorimeter");
        Bank   calBank    = new Bank(calSchema);

        event.read(calBank);
        for (int current_Row = 0; current_Row < calBank.getRows(); current_Row++) {
            int current_part = calBank.getInt("pindex", current_Row);
            if (calBank.getInt("layer", current_Row)==1) { // layer = 1 refers to PCal layer
                this.part_Cal_PCAL_found[current_part]  = 1;
                this.part_Cal_PCAL_sector[current_part] = calBank.getInt("sector", current_Row);
                this.part_Cal_PCAL_lu[current_part]     = (double) calBank.getFloat("lu", current_Row);
                this.part_Cal_PCAL_lv[current_part]     = (double) calBank.getFloat("lv", current_Row);
                this.part_Cal_PCAL_lw[current_part]     = (double) calBank.getFloat("lw", current_Row);
            }
            if (calBank.getInt("layer", current_Row)==4) { // layer = 4 refers to ECal inner layer
                this.part_Cal_ECIN_found[current_part]  = 1;
                this.part_Cal_ECIN_sector[current_part] = calBank.getInt("sector", current_Row);
                this.part_Cal_ECIN_lu[current_part]     = (double) calBank.getFloat("lu", current_Row);
                this.part_Cal_ECIN_lv[current_part]     = (double) calBank.getFloat("lv", current_Row);
                this.part_Cal_ECIN_lw[current_part]     = (double) calBank.getFloat("lw", current_Row);
            }
            if (calBank.getInt("layer", current_Row)==7) { // layer = 7 refers to ECal outer layer
                this.part_Cal_ECOUT_found[current_part]  = 1;
                this.part_Cal_ECOUT_sector[current_part] = calBank.getInt("sector", current_Row);
                this.part_Cal_ECOUT_lu[current_part]     = (double) calBank.getFloat("lu", current_Row);
                this.part_Cal_ECOUT_lv[current_part]     = (double) calBank.getFloat("lv", current_Row);
                this.part_Cal_ECOUT_lw[current_part]     = (double) calBank.getFloat("lw", current_Row);
            }
        }
    } // setPCalArrays()

    /**
    * Set drift chamber bank arrays at the event level. Hits are required in middle layer of each sector (6,18,36).
    * @param reader
    * @param event
    */
    protected void setTrajArrays(HipoReader reader, Event event) {

        // Read REC::Particle bank for # of entries
        Schema recSchema  = reader.getSchemaFactory().getSchema("REC::Particle");
        Bank   recBank    = new Bank(recSchema);
        event.read(recBank);
        int nrows = recBank.getRows();

        // Reset arrays
        this.part_DC_Traj_found = new int[nrows]; // used
        this.part_DC_c1x = new int[nrows]; // used...so are all the following
        this.part_DC_c1y = new int[nrows];
        this.part_DC_c1z = new int[nrows];
        this.part_DC_c2x = new int[nrows];
        this.part_DC_c2y = new int[nrows];
        this.part_DC_c2z = new int[nrows];
        this.part_DC_c3x = new int[nrows];
        this.part_DC_c3y = new int[nrows];
        this.part_DC_c3z = new int[nrows];

        // Read REC::Traj Bank
        Schema trajSchema  = reader.getSchemaFactory().getSchema("REC::Traj");
        Bank   trajBank    = new Bank(trajSchema);
        event.read(trajBank);

        for (int current_Row = 0; current_Row < trajBank.getRows(); current_Row++) {
            int current_part = trajBank.getInt("pindex", current_Row);
            int region = -1;
            if (trajBank.getInt("layer", current_Row) == 6) {
                region = 0;
                part_DC_c1x[current_part] = trajBank.getFloat("cx", current_Row);
                part_DC_c1y[current_part] = trajBank.getFloat("cy", current_Row);
                part_DC_c1z[current_part] = trajBank.getFloat("cz", current_Row);
            } else if (trajBank.getInt("layer", current_Row) == 18) {
                region = 1;
                part_DC_c2x[current_part] = trajBank.getFloat("cx", current_Row);
                part_DC_c2y[current_part] = trajBank.getFloat("cy", current_Row);
                part_DC_c2z[current_part] = trajBank.getFloat("cz", current_Row);
                part_DC_Traj_found[current_part] = 1;
            } else if (trajBank.getInt("layer", current_Row) == 36) {
                region = 2;
                part_DC_c3x[current_part] = trajBank.getFloat("cx", current_Row);
                part_DC_c3y[current_part] = trajBank.getFloat("cy", current_Row);
                part_DC_c3z[current_part] = trajBank.getFloat("cz", current_Row);
            }
        }
    } // setTrajArrays()

    /**
    * Fill the arrays at the event level for PCal/ECal and DC hits.
    * @param reader
    * @param event
    */
    protected void setArrays(HipoReader reader, Event event) {
        
        this.setPCalArrays(reader,event); this.setTrajArrays(reader,event);
    }

    /**
    * Applies RGA fiducial cuts to particle at index current_part in REC::Particle.
    * IMPORTANT: Make sure arrays are already filled by calling setArrays() method at event level before looping particles.
    * @param runnum_
    * @param current_part
    * @param pid_
    * @param event_
    * @param reader_
    */
    protected boolean applyCuts(int runnum_, int current_part, int pid_, Event event_, HipoReader reader_) {

        fiduCut = true; // IMPORTANT!  Reset each time.
        this._event = event_;
        this._reader = reader_;

        // Use runnumber to determine torus setting
        if(runnum_>=5032 && runnum_<=5419) {
            inbending=true; outbending=false;
        } else if(runnum_>=5422 && runnum_<=5666) {
            inbending=false; outbending=true;
        } else if(runnum_>=6616 && runnum_<=6783) {
            inbending=true; outbending=false;
        } else if(runnum_==11) { // MC
            inbending=false; outbending=true; // My default setting TODO: allow inbending/outbending to be set from analysis object...
        } else {
            System.err.println("ERROR: FiducialCuts does not know whether this run\n");
            System.err.println("       is inbending or outbending; setting to inbending\n");
            inbending=true; outbending=false;
        }

        // Determine sector (requires part_DC_Traj_found to be true)
        part_DC_sector[0] = this.determineSectorDC(current_part);

        // Apply cuts, depending on PID
        if(pid_==11) { // electrons

            // PCAL cut on lv and lw
            fcutElePCAL = this.EC_hit_position_fiducial_cut_homogeneous(0); // only for electrons (and photons, diff. #'s though)
            if(fcutElePCAL) { this.pCalCountP[0] += 1; } else { this.pCalCountF[0] += 1; }

            // DC cuts on chi2/NDF, using straight lines on xy hit plane
            for(int r=0; r<nReg; r++) { fcutEleDC[r] = part_DC_Traj_found[0]>0 ? this.DC_fiducial_cut_XY(current_part, r+1, pid_) : false; }
            if(fcutEleDC[0] && fcutEleDC[1] && fcutEleDC[2]) { this.dcXYCountP[0] += 1; } else { this.dcXYCountF[0] += 1; }

            fiduCut = fcutElePCAL && fcutEleDC[0] && fcutEleDC[1] && fcutEleDC[2];
        }

        else if(pid_==211 || pid_==-211 || pid_==2212 || pid_==321 || pid_==-321) { // pions and protons and kaons (ADDED)

            // DC cuts on chi2/NDF, using polynomial border in (theta,phi) plane for inbending, simple xy cuts for outbending (not tuned)
            if (inbending) {
                for(int r=0; r<nReg; r++) { fcutHadDC[r] = part_DC_Traj_found[0]>0 ? this.DC_fiducial_cut_theta_phi(current_part, r+1, pid_) : false; }
            }
            else if (outbending) {
                for(int r=0; r<nReg; r++) { fcutHadDC[r] = part_DC_Traj_found[0]>0 ? this.DC_fiducial_cut_XY(current_part, r+1, pid_) : false; }
            }
            fiduCut = fcutHadDC[0] && fcutHadDC[1] && fcutHadDC[2];

            // Fill counters
            if(fcutHadDC[0] && fcutHadDC[1] && fcutHadDC[2] && outbending) {
                switch (pid_) {
                    case 2212: this.dcXYCountP[1] += 1; break;
                    case 211:  this.dcXYCountP[2] += 1; break;
                    case -211: this.dcXYCountP[3] += 1; break;
                    case 321:  this.dcXYCountP[4] += 1; break;
                    case -321: this.dcXYCountP[5] += 1; break;
                    default: break;
                }
            }
            else if (!(fcutHadDC[0] && fcutHadDC[1] && fcutHadDC[2]) && outbending) {
                switch (pid_) {
                    case 2212: this.dcXYCountF[1] += 1; break;
                    case 211:  this.dcXYCountF[2] += 1; break;
                    case -211: this.dcXYCountF[3] += 1; break;
                    case 321:  this.dcXYCountF[4] += 1; break;
                    case -321: this.dcXYCountF[5] += 1; break;
                    default: break;
                }
            }
            if(fcutHadDC[0] && fcutHadDC[1] && fcutHadDC[2] && inbending) {
                switch (pid_) {
                    case 2212: this.dcTPCountP[1] += 1; break;
                    case 211:  this.dcTPCountP[2] += 1; break;
                    case -211: this.dcTPCountP[3] += 1; break;
                    case 321:  this.dcTPCountP[4] += 1; break;
                    case -321: this.dcTPCountP[5] += 1; break;
                    default: break;
                }
            }
            else if (!(fcutHadDC[0] && fcutHadDC[1] && fcutHadDC[2]) && inbending){
                switch (pid_) {
                    case 2212: this.dcTPCountF[1] += 1; break;
                    case 211:  this.dcTPCountF[2] += 1; break;
                    case -211: this.dcTPCountF[3] += 1; break;
                    case 321:  this.dcTPCountF[4] += 1; break;
                    case -321: this.dcTPCountF[5] += 1; break;
                    default: break;
                }
            }
        }
        else { fiduCut = true; return true; } // IMPORTANT: Make sure to return true here so that particles you aren't cutting can still be passed.
        return fiduCut; // IMPORTANT: Make sure this is true by default!
    }

    /** 
    * Determines the sector 1-6 in ECal for given particle at index i in REC::Particle bank.
    * @param i 
    */
    protected int determineSectorEC(int i){

        int   retval = 0;
        boolean warn = false;
        // Get sector number from whichever layers have Edep
        if(part_Cal_PCAL_found[i]>0) retval = (int) part_Cal_PCAL_sector[i];
        if(part_Cal_ECIN_found[i]>0) retval = (int) part_Cal_ECIN_sector[i];
        if(part_Cal_ECOUT_found[i]>0) retval = (int) part_Cal_ECOUT_sector[i];

        // Check that each layer with Edep>0 agrees on the sector number
        if(retval>=1 && retval<=6) {
            warn=false;
            if(part_Cal_PCAL_found[i]>0 && retval!=part_Cal_PCAL_sector[i]) warn=true;
            if(part_Cal_ECIN_found[i]>0 && retval!=part_Cal_ECIN_sector[i]) warn=true;
            if(part_Cal_ECOUT_found[i]>0 && retval!=part_Cal_ECOUT_sector[i]) warn=true;
            if(warn) System.err.println(" WARNING: Ill-determined ECal sector.");
        } else retval = -10000;
        return retval;
    }

    /*****************************************************************
    *                     BEGIN STEFAN'S METHODS                     *
    ******************************************************************/

    /** 
    * Determines the sector 1-6 in DC for given particle at index i in REC::Particle bank.
    * @param i 
    */
    protected int determineSectorDC(int i){

        double phi = 180 * Math.atan2(part_DC_c2y[i] / (Math.PI * Math.sqrt(Math.pow(part_DC_c2x[i], 2)) + Math.pow(part_DC_c2y[i], 2)
        + Math.pow(part_DC_c2z[i], 2)), part_DC_c2x[i] / Math.sqrt(Math.pow(part_DC_c2x[i], 2) + Math.pow(part_DC_c2y[i], 2) + Math.pow(part_DC_c2z[i], 2)));

        if(phi < 30 && phi >= -30){        return 1;}
        else if(phi < 90 && phi >= 30){    return 2;}
        else if(phi < 150 && phi >= 90){   return 3;}
        else if(phi >= 150 || phi < -150){ return 4;}
        else if(phi < -90 && phi >= -150){ return 5;}
        else if(phi < -30 && phi >= -90){  return 6;}

        return 0;
    }

    /**
    * Stefan's comments:
    * EC hit position homogenous cut
    * This is the main cut for PCAL fiducial cut of electrons.
    * A cut is performed on v and w.  Different versions are available: 
    * For SIDIS I use the loose versions, for cross sections I would recommend the medium or tight version.
    *
    * @param j
    */
    protected boolean EC_hit_position_fiducial_cut_homogeneous(int j){

        // Cut harshness levels: Now can be set from class method setLevel(int level) so easily modifiable
        boolean tight  = this._levels[2];
        boolean medium = this._levels[1];
        boolean loose  = this._levels[0];

        // Cut using the natural directions of the scintillator bars/ fibers:
        double u = part_Cal_PCAL_lu[j];
        double v = part_Cal_PCAL_lv[j];
        double w = part_Cal_PCAL_lw[j];
        
        // v + w is going from the side to the back end of the PCAL, u is going from side to side
        // 1 scintillator bar is 4.5 cm wide. In the outer regions (back) double bars are used.
        // a cut is only applied on v and w

        // inbending:
        double[] min_u_tight_inb = [19.0, 19.0, 19.0, 19.0, 19.0, 19.0];
        double[] min_u_med_inb   = [14.0, 14.0, 14.0, 14.0, 14.0, 14.0];
        double[] min_u_loose_inb = [9.0,  9.0,  9.0,  9.0,  9.0,  9.0 ];
        // 
        double[] max_u_tight_inb = [398, 398, 398, 398, 398, 398]; 
        double[] max_u_med_inb   = [408, 408, 408, 408, 408, 408]; 
        double[] max_u_loose_inb = [420, 420, 420, 420, 420, 420]; 
        // 
        double[] min_v_tight_inb = [19.0, 19.0, 19.0, 19.0, 19.0, 19.0];
        double[] min_v_med_inb   = [14.0, 14.0, 14.0, 14.0, 14.0, 14.0];
        double[] min_v_loose_inb = [9.0,  9.0,  9.0,  9.0,  9.0,  9.0 ];
        //
        double[] max_v_tight_inb = [400, 400, 400, 400, 400, 400];
        double[] max_v_med_inb   = [400, 400, 400, 400, 400, 400];
        double[] max_v_loose_inb = [400, 400, 400, 400, 400, 400];
        //
        double[] min_w_tight_inb = [19.0, 19.0, 19.0, 19.0, 19.0, 19.0];
        double[] min_w_med_inb   = [14.0, 14.0, 14.0, 14.0, 14.0, 14.0];
        double[] min_w_loose_inb = [9.0,  9.0,  9.0,  9.0,  9.0,  9.0 ];
        // 
        double[] max_w_tight_inb = [400, 400, 400, 400, 400, 400];
        double[] max_w_med_inb   = [400, 400, 400, 400, 400, 400];
        double[] max_w_loose_inb = [400, 400, 400, 400, 400, 400];

        // outbending (not adjusted up to now, same as inbending!):
        double[] min_u_tight_out = [19.0, 19.0, 19.0, 19.0, 19.0, 19.0];
        double[] min_u_med_out   = [14.0, 14.0, 14.0, 14.0, 14.0, 14.0];
        double[] min_u_loose_out = [9.0,  9.0,  9.0,  9.0,  9.0,  9.0 ];
        // 
        double[] max_u_tight_out = [398, 398, 398, 398, 398, 398]; 
        double[] max_u_med_out   = [408, 408, 408, 408, 408, 408]; 
        double[] max_u_loose_out = [420, 420, 420, 420, 420, 420]; 
        // 
        double[] min_v_tight_out = [19.0, 19.0, 19.0, 19.0, 19.0, 19.0];
        double[] min_v_med_out   = [14.0, 14.0, 14.0, 14.0, 14.0, 14.0];
        double[] min_v_loose_out = [9.0,  9.0,  9.0,  9.0,  9.0,  9.0 ];
        // 
        double[] max_v_tight_out = [400, 400, 400, 400, 400, 400];
        double[] max_v_med_out   = [400, 400, 400, 400, 400, 400];
        double[] max_v_loose_out = [400, 400, 400, 400, 400, 400];
        //
        double[] min_w_tight_out = [19.0, 19.0, 19.0, 19.0, 19.0, 19.0];
        double[] min_w_med_out   = [14.0, 14.0, 14.0, 14.0, 14.0, 14.0];
        double[] min_w_loose_out = [9.0,  9.0,  9.0,  9.0,  9.0,  9.0 ];
        //
        double[] max_w_tight_out = [400, 400, 400, 400, 400, 400];
        double[] max_w_med_out   = [400, 400, 400, 400, 400, 400];
        double[] max_w_loose_out = [400, 400, 400, 400, 400, 400];

        double min_u = 0; double max_u = 0; double min_v = 0; double max_v = 0; double min_w = 0; double max_w = 0;  

        for(int k = 0; k < 6; k++){  
            if((part_Cal_PCAL_sector[j] - 1) == k && inbending == true){
            if(tight == true){
                min_u = min_u_tight_inb[k]; max_u = max_u_tight_inb[k];
                min_v = min_v_tight_inb[k]; max_v = max_v_tight_inb[k];
                min_w = min_w_tight_inb[k]; max_w = max_w_tight_inb[k];
            }
            if(medium == true){
                min_u = min_u_med_inb[k]; max_u = max_u_med_inb[k];
                min_v = min_v_med_inb[k]; max_v = max_v_med_inb[k];
                min_w = min_w_med_inb[k]; max_w = max_w_med_inb[k];
            }
            if(loose == true){
                min_u = min_u_loose_inb[k]; max_u = max_u_loose_inb[k];
                min_v = min_v_loose_inb[k]; max_v = max_v_loose_inb[k];
                min_w = min_w_loose_inb[k]; max_w = max_w_loose_inb[k];
            }
            }
            if((part_Cal_PCAL_sector[j] - 1) == k && outbending == true){
            if(tight == true){
                min_u = min_u_tight_out[k]; max_u = max_u_tight_out[k];
                min_v = min_v_tight_out[k]; max_v = max_v_tight_out[k];
                min_w = min_w_tight_out[k]; max_w = max_w_tight_out[k];
            }
            if(medium == true){
                min_u = min_u_med_out[k]; max_u = max_u_med_out[k];
                min_v = min_v_med_out[k]; max_v = max_v_med_out[k];
                min_w = min_w_med_out[k]; max_w = max_w_med_out[k];
            }
            if(loose == true){
                min_u = min_u_loose_out[k]; max_u = max_u_loose_out[k];
                min_v = min_v_loose_out[k]; max_v = max_v_loose_out[k];
                min_w = min_w_loose_out[k]; max_w = max_w_loose_out[k];
            }
            }
        }

        if(v > min_v && v < max_v && w > min_w && w < max_w) { return true; }
        else { return false; }
    }
  
    /**
    * Stefan's comments:
    * use the following cut for inbending hadrons:
    * j is the index of the particle. The variable part_pid is needed to assign the correct cut
    * The inbending / outbending flags (see top of this document) have to be set to assign the correct cut
    * Note: DIM(arrays) = (6,6,3,4) (pid,sector,region,param)
    *
    * @param j
    * @param region
    * @param part_pid
    */
    protected boolean DC_fiducial_cut_theta_phi(int j, int region, int part_pid){

        // Fitted values for inbending
        double[][][][] maxparams_in = [ // DIM = (6,6,3,4) (pid,sector,region,param)
            [[[-51.2054, 38.3199, -2.34912, 0.0195708],[-35.124, 26.5405, -1.09976, 0.0054436],[-38.4667, 28.8007, -1.29647, 0.00723946]],
            [[-37.5758, 26.6792, -1.00373, 0.00425542],[-45.5585, 35.2513, -2.21029, 0.0196454],[-40.6878, 30.355, -1.42152, 0.00817034]],
            [[-38.3878, 29.6034, -1.68592, 0.0150766],[-36.8921, 27.4735, -1.14582, 0.00554924],[-23.5149, 18.0147, -0.38597, 2.87092e-09]],
            [[-48.2748, 35.4465, -1.92023, 0.0136412],[-32.9275, 24.1143, -0.715458, 5.40587e-05],[-62.2819, 53.7417, -5.16183, 0.0613377]],
            [[-39.9916, 30.4254, -1.72002, 0.015037],[-20.4886, 16.3741, -0.310525, 2.68397e-08],[-38.2334, 31.4077, -2.12755, 0.021869]],
            [[-62.9673, 48.747, -3.71315, 0.036982],[-50.8588, 37.2435, -1.98066, 0.0126239],[-72.3186, 58.7791, -5.21921, 0.0573224]]],
            [[[-7.47342e-07, 12.8291, -0.819744, 0.00818184],[-3.46906, 12.9438, -0.594252, 0.00438008],[-6.57983, 11.8439, -0.240227, 3.27439e-10]],
            [[-2.23025e-08, 12.8274, -0.819395, 0.00817682],[-4.95047, 13.5494, -0.600411, 0.00406046],[-8.411, 12.7605, -0.278882, 2.14459e-10]],
            [[-3.62667e-08, 13.2965, -0.904711, 0.00919042],[-9.89194, 16.9776, -0.934777, 0.00761783],[-5.88727, 11.4158, -0.218181, 5.04197e-11]],
            [[-1.51214e-08, 12.7606, -0.797213, 0.00785312],[-5.63162, 14.3101, -0.702664, 0.00528812],[-4.77062, 10.9184, -0.200027, 2.67841e-13]],
            [[-1.81438e-08, 12.9692, -0.848261, 0.00859668],[-8.30238, 16.2372, -0.899398, 0.00745394],[-7.58827, 12.4473, -0.270602, 2.53855e-10]],
            [[-4.29779e-08, 13.121, -0.861715, 0.0085118],[-7.36773, 15.334, -0.776242, 0.00585805],[-6.54892, 12.0405, -0.259209, 3.73643e-06]]],
            [[[-2.68279e-07, 12.99, -0.846226, 0.00845788],[-14.6317, 19.3874, -1.09244, 0.00899541],[-38.1915, 29.8688, -1.59229, 0.0120089]],
            [[-0.996514, 13.9379, -0.964686, 0.00982941],[-15.9613, 20.2461, -1.16106, 0.00955431],[-35.9455, 29.0996, -1.586, 0.0122175]],
            [[-1.14284e-07, 13.6015, -0.966952, 0.0101523],[-15.5288, 20.3045, -1.20523, 0.0102808],[-34.2682, 26.4216, -1.20609, 0.0078434]],
            [[-1.70075e-08, 13.0005, -0.832325, 0.00817159],[-7.66776, 15.4526, -0.779727, 0.00585967],[-26.8035, 23.9995, -1.2322, 0.00942061]],
            [[-9.53804e-10, 13.2563, -0.898206, 0.00917629],[-6.85083, 14.8485, -0.722803, 0.0053221],[-39.3606, 31.5412, -1.83015, 0.0148302]],
            [[-7.66835e-07, 13.937, -1.05153, 0.0118223],[-9.7913, 16.925, -0.913158, 0.00712552],[-27.722, 23.9412, -1.1314, 0.00761088]]],
            [[[-22.1832, 20.4134, -0.764848, 0.00310923],[-31.0844, 28.2369, -1.715, 0.0145145],[-9.52175, 18.7932, -1.38896, 0.0150233]],
            [[-21.5849, 20.2457, -0.762109, 0.00305359],[-19.5601, 21.5945, -1.18955, 0.00939109],[-1.57084, 13.3989, -0.823161, 0.00795227]],
            [[-16.052, 16.6264, -0.444308, 2.82701e-06],[-13.8291, 18.6541, -1.01549, 0.00825776],[-1.92223e-05, 13.0305, -0.881089, 0.00925281]],
            [[-19.821, 18.4301, -0.516168, 2.17199e-10],[-30.6295, 28.0989, -1.71897, 0.0146585],[-9.23709, 17.1589, -1.03955, 0.00943673]],
            [[-16.1795, 16.7121, -0.448883, 1.53774e-11],[-23.6418, 24.5748, -1.48652, 0.01254],[-4.2626e-09, 12.899, -0.845374, 0.00872171]],
            [[-9.74791, 15.0287, -0.531727, 0.00192371],[-41.0848, 33.1802, -1.97671, 0.0158148],[-4.12428, 14.3361, -0.820483, 0.00725632]]],
            [[[-106.938, 86.1515, -8.33159, 0.101803],[-147.879, 123.116, -13.5824, 0.189579],[-40.461, 32.3475, -1.89454, 0.0152611]],
            [[-116.247, 96.2163, -9.93814, 0.130211],[-148.54, 117.766, -12.0624, 0.156313],[-21.2546, 20.4237, -0.895905, 0.00581147]],
            [[-1.80778e-07, 13.0131, -0.835058, 0.00806212],[-142.768, 110.58, -10.8081, 0.133187],[-68.8917, 52.0911, -3.89921, 0.0389652]],
            [[-120.479, 92.6075, -8.56746, 0.0999391],[-147.549, 115.807, -11.6707, 0.149171],[-68.1786, 52.4364, -4.06328, 0.042723]],
            [[-123.541, 97.8256, -9.57399, 0.118002],[-149.955, 123.896, -13.5475, 0.187947],[-17.0127, 16.3323, -0.393286, 1.67366e-13]],
            [[-98.3311, 81.6497, -8.10227, 0.102672],[-149.833, 127.44, -14.5373, 0.209948],[-0.74045, 9.36883, -0.152603, 6.37437e-05]]],
            [[[-142.357, 114.799, -11.8528, 0.152895],[-149.875, 111.604, -10.4642, 0.125095],[-149.501, 121.578, -13.0379, 0.177271]],
            [[-29.9249, 22.0873, -0.60085, 1.3473e-10],[-42.0584, 33.3444, -1.99565, 0.016557],[-150, 127.502, -14.6523, 0.21559]],
            [[-118.755, 95.587, -9.52135, 0.120561],[-144.344, 109.666, -10.4534, 0.126138],[-149.437, 93.2368, -6.50292, 0.0579289]],
            [[-149.814, 110.828, -10.2785, 0.122489],[-148.167, 114.308, -11.2695, 0.140999],[-149.539, 112.153, -10.6506, 0.128573]],
            [[-33.521, 26.8939, -1.29466, 0.00851674],[-141.738, 107.08, -10.1005, 0.120796],[-150, 108.507, -9.8741, 0.116558]],
            [[-26.8311, 20.6856, -0.549123, 5.34572e-11],[-148.673, 110.129, -10.1547, 0.118973],[-120.868, 101.527, -10.6726, 0.13942]]]] as double[][][][];


        double[][][][] minparams_in = [ // DIM = (6,6,3,4)
            [[[38.6069, -28.188, 1.18288, -0.00589988],[26.3209, -21.6986, 0.768368, -0.00281998],[15.8707, -11.8368, 3.22688e-09, -8.36654e-10]],
            [[44.6032, -32.8751, 1.65153, -0.0105575],[51.7598, -35.5217, 1.29448, -2.11513e-06],[32.9204, -26.2522, 1.46865, -0.0139537],],
            [[32.1412, -24.2383, 1.00154, -0.0064333],[31.3784, -23.4733, 0.774187, -0.00203468],[53.8936, -44.9217, 3.8766, -0.0442173]],
            [[43.416, -32.3319, 1.78081, -0.0143004],[50.0314, -38.5805, 2.56055, -0.0235214],[25.8309, -21.3241, 1.01076, -0.00944255]],
            [[50.4741, -40.1723, 3.0563, -0.0325218],[61.6178, -50.3165, 4.34833, -0.0483999],[12.7766, -11.4712, 0.0511044, -1.74577e-10]],
            [[43.2153, -31.5115, 1.4834, -0.00863028],[69.3381, -51.2282, 3.39516, -0.0272937],[120.36, -85.4755, 7.45064, -0.0836405]]],
            [[[0.923008, -13.7147, 0.895123, -0.00853805],[7.6279, -15.4483, 0.798758, -0.00633026],[5.62644, -11.4345, 0.226312, -1.25172e-09]],
            [[2.50458, -14.7412, 1.01929, -0.0102917],[9.0023, -16.3615, 0.894467, -0.00751183],[3.69192, -10.4985, 0.188359, -3.1185e-10]],
            [[1.5978, -13.7835, 0.890884, -0.00868481],[13.4977, -19.2788, 1.15784, -0.0101226],[3.82792, -10.4417, 0.179639, -2.24507e-10]],
            [[1.97078e-05, -13.0168, 0.817426, -0.00762709],[6.07517, -14.4662, 0.696387, -0.00504973],[6.98517, -12.2129, 0.264062, -2.7665e-11]],
            [[2.39018e-09, -13.0476, 0.838641, -0.00809971],[4.15408, -14.0986, 0.777823, -0.00678748],[3.77333, -10.6691, 0.201849, -4.83515e-10]],
            [[0.00103141, -13.1638, 0.857402, -0.00828706],[7.16766, -15.2964, 0.811382, -0.00673444],[3.39768, -10.2477, 0.172417, -3.86335e-10]]],
            [[[1.59369e-06, -13.8294, 0.990918, -0.0103128],[20.1273, -23.853, 1.58449, -0.0145959],[40.8152, -32.8944, 2.00731, -0.0171007]],
            [[1.4334, -14.5452, 1.04379, -0.0106791],[19.9242, -23.3894, 1.5036, -0.0134429],[45.1348, -34.9897, 2.11238, -0.0175613]],
            [[4.48276e-06, -12.6688, 0.757818, -0.006981],[10.2525, -16.9056, 0.909637, -0.00739798],[33.2958, -27.7763, 1.53467, -0.0123488]],
            [[3.817e-06, -13.2285, 0.856439, -0.0081744],[12.5356, -19.0801, 1.1686, -0.0102758],[37.3388, -29.7344, 1.64296, -0.0130658]],
            [[3.64842e-07, -14.1631, 1.0771, -0.0118569],[9.85442, -17.8198, 1.12641, -0.010627],[34.7, -28.5335, 1.57226, -0.0124004]],
            [[0.828721, -13.6429, 0.895665, -0.00866683],[10.8176, -18.0919, 1.11147, -0.010183],[29.9288, -24.3389, 1.08973, -0.00703934]]],
            [[[15.8302, -16.9632, 0.53561, -0.00136216],[32.8002, -29.2569, 1.79783, -0.015324],[1.98393, -13.0099, 0.70788, -0.00615153]],
            [[16.0367, -16.5901, 0.470678, -0.000728065],[32.4005, -29.7403, 1.92286, -0.0171968],[2.39707, -13.6612, 0.816883, -0.00770837]],
            [[22.0623, -21.6319, 1.02811, -0.00680893],[32.7467, -29.6099, 1.87839, -0.0164223],[1.19902e-08, -12.972, 0.863127, -0.00884759]],
            [[21.5883, -21.198, 0.957819, -0.00575361],[25.7387, -25.4963, 1.5428, -0.0131855],[6.06479, -16.6311, 1.16092, -0.0117194]],
            [[19.6915, -19.1751, 0.704086, -0.00288768],[28.6596, -27.3351, 1.70309, -0.0148193],[5.30096e-08, -11.8562, 0.621373, -0.00541869]],
            [[20.6594, -19.8704, 0.786033, -0.00394155],[20.7612, -22.3774, 1.27116, -0.0104109],[2.56196, -14.4159, 0.98009, -0.0100214]]],
            [[[14.2631, -23.4009, 1.97498, -0.0231565],[4.79173, -12.3009, 0.326886, -1.01998e-11],[116.734, -88.2089, 8.05103, -0.0957363]],
            [[5.23149, -16.0375, 1.0661, -0.0101511],[110.165, -85.9607, 7.97299, -0.0942078],[43.5095, -33.7305, 1.93256, -0.0149425]],
            [[1.85579e-09, -11.9228, 0.619166, -0.00523146],[7.01255, -15.5132, 0.84916, -0.00706963],[149.945, -108.603, 9.82162, -0.114667]],
            [[140.48, -115.163, 12.1689, -0.16217],[133.49, -104.362, 10.1931, -0.125813],[114.099, -83.4723, 7.13202, -0.0793527]],
            [[18.6833, -24.9619, 1.82946, -0.0167602],[11.0804, -17.1484, 0.877833, -0.00652953],[15.5084, -15.7594, 0.378209, -2.63283e-06]],
            [[4.64372e-08, -8.1395, 0.0139504, -9.40293e-06],[74.1514, -64.5468, 6.22895, -0.0765922],[29.0616, -24.2817, 1.13928, -0.00789879]]],
            [[[150, -119.236, 12.4315, -0.16672],[17.764, -15.9517, 0.338355, -9.39693e-11],[149.387, -101.062, 8.19036, -0.0851116]],
            [[130.018, -97.9952, 9.09323, -0.109458],[147.831, -109.347, 10.1054, -0.119381],[149.958, -109.621, 10.0894, -0.119333]],
            [[149.744, -119.521, 12.5308, -0.168366],[105.835, -82.9887, 7.74468, -0.0929233],[135.964, -117.822, 13.5665, -0.197518]],
            [[150, -119.218, 12.431, -0.167282],[43.2244, -35.0239, 2.19659, -0.0185046],[150, -115.904, 11.6807, -0.151716]],
            [[149.062, -108.704, 9.81712, -0.113862],[144.909, -115.147, 11.8228, -0.153516],[150, -115.103, 11.5315, -0.149515]],
            [[149.245, -111.727, 10.5343, -0.126258],[127.9, -98.607, 9.4799, -0.117458],[135.306, -106.942, 10.7421, -0.136543]]]] as double[][][][];

        // Fitted values for outbending
        double[][][][] maxparams_out = [ // DIM = (6,6,3,4)
            [[[-3.69457, 12.3755, -0.41328, 0.00129631],[-54.3237, 40.3308, -2.39952, 0.0181339],[-39.8661, 27.1428, -0.907303, 0.00220974]],
            [[-37.6199, 26.2865, -0.826366, 0.000862203],[-72.4212, 54.7953, -4.04856, 0.0373308],[-21.1791, 17.0759, -0.391795, 0.00151085]],
            [[-0.421685, 10.482, -0.272111, 8.69408e-05],[-43.3635, 32.746, -1.6541, 0.0101454],[-62.6387, 41.1869, -1.97298, 0.0107022]],
            [[-42.0766, 29.6387, -0.993426, 1.97101e-09],[-44.7036, 33.0587, -1.64131, 0.0099416],[-47.2703, 32.6109, -1.46533, 0.00817871]],
            [[-22.2035, 20.6894, -0.689051, 0.000592423],[-74.6572, 54.7065, -3.83999, 0.0351952],[-38.9183, 25.7212, -0.711499, 2.5796e-12]],
            [[-52.078, 45.571, -3.71942, 0.0376577],[-65.4047, 49.1723, -3.36623, 0.0288435],[-53.9611, 35.9294, -1.58589, 0.00772417]]],
            [[[-2.20312e-07, 13.0916, -0.864184, 0.0086342],[-6.44026e-08, 12.056, -0.675801, 0.00643464],[-20.2596, 23.5977, -1.545, 0.0141047]],
            [[-4.42537e-05, 10.2799, -0.322454, 0.00154825],[-1.63659e-07, 11.0228, -0.451412, 0.00308633],[-8.5382, 15.6903, -0.785315, 0.00602734]],
            [[-2.32088, 11.6343, -0.363509, 0.000902217],[-0.301128, 12.0319, -0.643794, 0.00581994],[-22.4378, 25.2772, -1.73656, 0.0164181]],
            [[-7.40627, 13.601, -0.382439, 2.45262e-05],[-5.50415e-08, 11.9792, -0.652368, 0.00597647],[-15.1608, 20.6455, -1.33827, 0.0127123]],
            [[-0.203913, 10.7032, -0.322123, 0.000691162],[-1.73184e-07, 10.735, -0.379993, 0.00196037],[-0.155443, 10.1794, -0.249841, 6.24278e-05]],
            [[-1.87352e-07, 12.4226, -0.730141, 0.0068049],[-1.40236e-07, 12.5356, -0.750615, 0.00719921],[-16.8681, 21.8555, -1.43078, 0.0131935]]],
            [[[-8.89326e-08, 10.0681, -0.240869, 9.9612e-12],[-15.2705, 21.635, -1.55291, 0.0166645],[-10.5976, 17.9928, -1.08432, 0.00950807]],
            [[-0.00389562, 10.2092, -0.254082, 4.15737e-06],[-9.16032e-11, 10.527, -0.334641, 0.00129061],[-9.63013e-07, 11.0668, -0.42453, 0.0022955]],
            [[-2.40163e-06, 13.4151, -0.949883, 0.0107662],[-1.60937e-07, 10.5128, -0.35046, 0.00173787],[-29.2647, 30.1252, -2.20552, 0.0213809]],
            [[-2.69733e-08, 11.7703, -0.589854, 0.00482124],[-3.77564e-08, 11.3764, -0.527037, 0.00416671],[-4.85047, 13.7737, -0.650441, 0.0047428]],
            [[-3.90816e-07, 12.2683, -0.692591, 0.00625884],[-9.70203e-10, 11.0335, -0.438323, 0.00275342],[-2.54193, 13.5404, -0.76861, 0.00684486]],
            [[-3.23439e-10, 10.7412, -0.348557, 0.00113794],[-1.79623, 11.7499, -0.449432, 0.00247294],[-13.1393, 19.4689, -1.17148, 0.00984086]]],
            [[[-5.07611e-08, 11.7796, -0.516966, 0.00295389],[-4.87018, 12.2727, -0.322719, 9.12315e-06],[-35.9369, 31.015, -1.95133, 0.0169834]],
            [[-1.32385e-07, 11.6454, -0.495467, 0.00272602],[-2.70664, 12.0151, -0.434014, 0.00203292],[-8.97137, 15.0453, -0.646138, 0.00429196]],
            [[-7.92247e-09, 12.5189, -0.682231, 0.00539531],[-0.0942499, 10.3465, -0.280521, 0.000405358],[-19.7485, 21.7919, -1.24334, 0.0105088]],
            [[-8.50093e-11, 10.739, -0.302295, 5.6862e-11],[-0.184771, 10.4358, -0.285869, 0.000389546],[-21.9469, 24.9675, -1.77893, 0.0183075]],
            [[-4.34589, 12.5902, -0.362849, 4.996e-15],[-0.000684493, 10.6055, -0.332363, 0.00104632],[-21.328, 22.0864, -1.20993, 0.00989151]],
            [[-0.0202168, 12.0097, -0.539165, 0.00299034],[-0.5239, 10.7167, -0.309141, 0.000535617],[-10.0299, 16.3179, -0.812315, 0.00617078]]],
            [[[-0.169908, 10.902, -0.353938, 0.00100715],[-3.2818, 13.2193, -0.65495, 0.00515117],[-0.013532, 8.51331, -0.070239, 1.755e-05]],
            [[-8.51985e-08, 11.6512, -0.56808, 0.00453582],[-1.2381e-07, 10.6653, -0.368149, 0.00181989],[-9.30287e-08, 10.0352, -0.254321, 0.000417053]],
            [[-0.150407, 10.6338, -0.308676, 0.000481694],[-0.00186321, 10.4259, -0.303092, 0.00073092],[-21.3328, 28.0803, -2.37912, 0.025101]],
            [[-14.4411, 19.817, -1.13705, 0.00894685],[-6.25263e-09, 11.7414, -0.586098, 0.00478932],[-5.49193, 16.1248, -1.11306, 0.0115644]],
            [[-1.54761, 12.0015, -0.462506, 0.00204729],[-5.72883, 14.9638, -0.795325, 0.00616222],[-50.229, 45.8456, -3.88803, 0.0414729]],
            [[-40.7531, 33.6269, -2.03771, 0.01609],[-1.33363e-09, 11.9894, -0.614358, 0.004924],[-27.2506, 29.2602, -2.1426, 0.0203235]]],
            [[[-1.62999e-10, 14.0422, -1.03609, 0.0107179],[-6.71565, 15.6964, -0.887791, 0.00740777],[-38.9148, 32.9935, -2.09023, 0.0177295]],
            [[-1.09078e-05, 13.4131, -0.878092, 0.00825152],[-15.0102, 21.6968, -1.4935, 0.0138851],[-19.5261, 20.3932, -0.969464, 0.00661531]],
            [[-1.39619e-08, 12.3593, -0.618488, 0.00415536],[-5.38271e-07, 11.5631, -0.512607, 0.00334452],[-23.0902, 24.7093, -1.57315, 0.0140132]],
            [[-1.73908e-08, 12.0348, -0.591608, 0.00423834],[-8.35134, 17.3066, -1.11555, 0.010407],[-2.74909e-07, 9.59202, -0.216455, 0.000527479]],
            [[-0.0449157, 10.5243, -0.334389, 0.00134555],[-0.0143489, 10.0993, -0.2434, 1.57595e-10],[-22.3661, 23.2499, -1.32946, 0.0108047]],
            [[-5.83731e-07, 14.5234, -1.14022, 0.0122177],[-1.4586e-08, 11.6946, -0.520935, 0.00324975],[-12.4252, 16.3216, -0.652566, 0.00365791]]]] as double[][][][];


        double[][][][] minparams_out = [ // DIM = (6,6,3,4)
            [[[3.73672, -12.3584,0.390616, -0.000795415],[51.644, -37.8546,1.99228, -0.0119973],[32.3551, -22.9742,0.624096, -4.30811e-05]],
            [[6.11614, -13.6358,0.491668, -0.0018637],[47.5098, -35.902,1.97535, -0.0134876],[82.9536, -58.2741,4.12662, -0.0378612]],
            [[0.000950108, -7.99619,0.000506416, -0.0020788],[64.0688, -47.8642,3.16007, -0.025878],[70.0064, -50.3249,3.38975, -0.029639]],
            [[37.0145, -35.0316,2.61892, -0.0250306],[14.5954, -15.6554,0.426733, -0.000879865],[28.9035, -21.5279,0.610475, -0.00087271]],
            [[5.65685, -13.3347,0.400781, -1.46612e-11],[67.3504, -50.152,3.33677, -0.0270726],[47.0772, -32.1506,1.38851, -0.00719898]],
            [[8.95987, -15.1646,0.585477, -0.00246174],[41.6154, -29.7967,1.1817, -0.00403765],[61.1631, -41.6465,2.32522, -0.0175271]]],
            [[[8.80954e-10, -11.0364,0.413853, -0.00210254],[6.50072e-08, -11.2505,0.501571, -0.00380973],[10.9643, -17.4701,0.989297, -0.00860789]],
            [[2.33292e-08, -11.2353,0.470728, -0.00309666],[2.29373e-07, -11.2458,0.50218, -0.00383969],[29.5429, -29.9965,2.19166, -0.021366]],
            [[1.61826e-08, -11.861,0.577321, -0.00433276],[2.9436e-07, -11.5738,0.581015, -0.00503307],[19.5142, -23.451,1.58724, -0.0151339]],
            [[2.07231e-09, -12.7453,0.751184, -0.00664181],[1.77802e-07, -11.4574,0.537367, -0.00422656],[12.5683, -18.4632,1.05475, -0.00892182]],
            [[7.6216e-08, -13.9769,1.01051, -0.0107372],[1.33092e-08, -11.9128,0.628521, -0.00550105],[13.5537, -20.1708,1.32578, -0.0123213]],
            [[9.25941, -19.658,1.51566, -0.0157124],[6.25983e-10, -11.6806,0.599263, -0.00532588],[17.0479, -22.0046,1.47474, -0.0140475]]],
            [[[4.65436e-08, -11.1925,0.466196, -0.00308992],[18.4968, -22.5122,1.4594, -0.0135962],[18.9488, -23.3348,1.57414, -0.0146183]],
            [[3.67722e-08, -10.9985,0.428395, -0.00257574],[16.3745, -21.0105,1.3093, -0.0119156],[11.4404, -18.6679,1.15919, -0.010306]],
            [[1.46846e-08, -10.865,0.398638, -0.00212392],[20.7337, -23.3738,1.46852, -0.0130115],[28.2098, -28.9406,2.05908, -0.0197782]],
            [[0.237058, -10.4694,0.271985, -1.08731e-07],[2.32759, -11.9354,0.469887, -0.00291497],[13.287, -20.8621,1.49656, -0.0148999]],
            [[0.000149907, -10.4632,0.294713, -0.000431947],[6.96663, -15.3946,0.845078, -0.00724722],[11.0939, -17.4733,0.944239, -0.00747728]],
            [[3.10006e-08, -10.1416,0.247764, -1.36913e-11],[5.41915, -14.6085,0.795369, -0.00684375],[5.89127, -13.0881,0.453024, -0.0020325]]],
            [[[4.16588e-09, -12.9305,0.749425, -0.00611725],[5.65263, -14.1661,0.637395, -0.00400239],[4.66325, -12.9519,0.565753, -0.00442033]],
            [[8.0428e-08, -13.1625,0.836744, -0.00778246],[12.3243, -18.8718,1.11103, -0.00917354],[7.20312, -16.0935,0.987223, -0.00930883]],
            [[0.00147165, -10.4992,0.280542, -1.79846e-06],[3.20232, -11.6892,0.350774, -0.00101099],[8.14117e-08, -10.9813,0.524839, -0.00507885]],
            [[0.470888, -13.5446,0.820782, -0.00768941],[3.9697, -13.0821,0.540847, -0.00303209],[3.44817, -12.3932,0.533804, -0.00414144]],
            [[1.05038e-08, -10.6539,0.297078, -6.04694e-05],[15.0983, -21.1791,1.38383, -0.0124058],[17.3666, -20.3986,1.16663, -0.0102393]],
            [[8.49365e-07, -13.765,0.964056, -0.00956575],[9.38084, -16.7385,0.904339, -0.00707907],[12.1048, -17.3704,0.91318, -0.00757461]]],
            [[[10.6378, -19.5017,1.45275, -0.017057],[1.24368e-08, -10.5134,0.338985, -0.00143696],[37.3291, -35.1606,2.60092, -0.0242728]],
            [[19.1614, -24.0851,1.73932, -0.0185466],[14.1293, -19.8382,1.21613, -0.0107037],[20.9629, -24.0839,1.60283, -0.015173]],
            [[0.000450804, -8.15062,0.0103867, -2.00709e-05],[5.72496, -14.338,0.717819, -0.00567964],[16.9428, -21.8075,1.4216, -0.0131736]],
            [[6.15991e-10, -11.5278,0.536105, -0.00402223],[2.17842e-07, -10.5338,0.327427, -0.0010898],[20.7387, -24.3028,1.65004, -0.0155857]],
            [[0.650351, -10.6177,0.275393, -6.4664e-08],[8.05811, -16.1558,0.913735, -0.00788487],[0.308897, -10.2816,0.275186, -0.000561299]],
            [[0.427836, -10.168,0.240458, -5.90042e-06],[2.30661, -12.8686,0.664796, -0.00562626],[0.00499667, -11.6585,0.62597, -0.00619261]]],
            [[[9.01249e-07, -11.8437,0.494125, -0.00223452],[14.3941, -21.2365,1.46048, -0.0137349],[13.7095, -15.4704,0.408961, -0.000312145]],
            [[0.000251044, -11.3084,0.438545, -0.0020791],[0.00847078, -12.6769,0.804431, -0.00836705],[1.09388, -9.66797,0.175278, -1.8721e-11]],
            [[4.04693e-10, -11.9001,0.585913, -0.00440376],[5.05178, -12.1514,0.31134, -0.000112735],[30.8105, -28.0795,1.73625, -0.0151639]],
            [[3.86607e-11, -13.471,0.889111, -0.0083617],[8.86591e-09, -9.25745,0.163052, -6.08491e-12],[27.1358, -24.3255,1.23326, -0.00891886]],
            [[0.196086, -11.7392,0.480055, -0.00224614],[0.18667, -10.5859,0.287231, -6.53153e-06],[14.8865, -17.1338,0.653576, -0.00333176]],
            [[2.7955e-07, -13.1311,0.848222, -0.00812719],[29.5508, -32.9514,2.77917, -0.0291596],[59.7514, -47.3033,3.54495, -0.0341802]]]] as double[][][][];

        final double[][][][] minparams = ((inbending && !outbending) ? minparams_in : minparams_out);
        final double[][][][] maxparams = ((inbending && !outbending) ? maxparams_in : maxparams_out);

        double theta_DCr = 5000;
        double phi_DCr_raw = 5000;

        switch (region)
        {
            case 1:
            theta_DCr = 180 * Math.acos(part_DC_c1z[j] / (Math.PI * Math.sqrt(Math.pow(part_DC_c1x[j],2)) + Math.pow(part_DC_c1y[j],2) + Math.pow(part_DC_c1z[j],2)));
            phi_DCr_raw = 180 * Math.atan2(part_DC_c1y[j] / (Math.PI * Math.sqrt(Math.pow(part_DC_c1x[j],2)) + Math.pow(part_DC_c1y[j],2) + Math.pow(part_DC_c1z[j],2)), 
            part_DC_c1x[j] /Math.sqrt(Math.pow(part_DC_c1x[j],2) + Math.pow(part_DC_c1y[j],2) + Math.pow(part_DC_c1z[j],2)));
            break;

            case 2:
            theta_DCr = 180 * Math.acos(part_DC_c2z[j] / (Math.PI * Math.sqrt(Math.pow(part_DC_c2x[j],2)) + Math.pow(part_DC_c2y[j],2) + Math.pow(part_DC_c2z[j],2)));
            phi_DCr_raw = 180 * Math.atan2(part_DC_c2y[j] / (Math.PI * Math.sqrt(Math.pow(part_DC_c2x[j],2)) + Math.pow(part_DC_c2y[j],2) + Math.pow(part_DC_c2z[j],2)), 
            part_DC_c2x[j] /Math.sqrt(Math.pow(part_DC_c2x[j],2) + Math.pow(part_DC_c2y[j],2) + Math.pow(part_DC_c2z[j],2)));
            break;

            case 3:
            theta_DCr = 180 * Math.acos(part_DC_c3z[j] / (Math.PI * Math.sqrt(Math.pow(part_DC_c3x[j],2)) + Math.pow(part_DC_c3y[j],2) + Math.pow(part_DC_c3z[j],2)));
            phi_DCr_raw = 180 * Math.atan2(part_DC_c3y[j] / (Math.PI * Math.sqrt(Math.pow(part_DC_c3x[j],2)) + Math.pow(part_DC_c3y[j],2) + Math.pow(part_DC_c3z[j],2)), 
            part_DC_c3x[j] /Math.sqrt(Math.pow(part_DC_c3x[j],2) + Math.pow(part_DC_c3y[j],2) + Math.pow(part_DC_c3z[j],2)));
            break;

            default:
            return false;
        }

        double phi_DCr = 5000;

        if (part_DC_sector[j] == 1) phi_DCr = phi_DCr_raw;
        if (part_DC_sector[j] == 2) phi_DCr = phi_DCr_raw - 60;
        if (part_DC_sector[j] == 3) phi_DCr = phi_DCr_raw - 120;
        if (part_DC_sector[j] == 4 && phi_DCr_raw > 0) phi_DCr = phi_DCr_raw - 180;
        if (part_DC_sector[j] == 4 && phi_DCr_raw < 0) phi_DCr = phi_DCr_raw + 180;
        if (part_DC_sector[j] == 5) phi_DCr = phi_DCr_raw + 120;
        if (part_DC_sector[j] == 6) phi_DCr = phi_DCr_raw + 60;
        int pid = 0;

        switch (part_pid) {
            case 11:   pid = 0; break;
            case 2212: pid = 1; break;
            case 211:  pid = 2; break;
            case -211: pid = 3; break;
            case 321:  pid = 4; break;
            case -321: pid = 5; break;
            default:   return false;
        }

        --region;

        double calc_phi_max = minparams[pid][part_DC_sector[j] - 1][region][0] + minparams[pid][part_DC_sector[j] - 1][region][1] * Math.log(theta_DCr) 
            + minparams[pid][part_DC_sector[j] - 1][region][2] * theta_DCr + minparams[pid][part_DC_sector[j] - 1][region][3] * theta_DCr * theta_DCr;
        double calc_phi_min = maxparams[pid][part_DC_sector[j] - 1][region][0] + maxparams[pid][part_DC_sector[j] - 1][region][1] * Math.log(theta_DCr)
            + maxparams[pid][part_DC_sector[j] - 1][region][2] * theta_DCr + maxparams[pid][part_DC_sector[j] - 1][region][3] * theta_DCr * theta_DCr;

        return ((phi_DCr > calc_phi_min) && (phi_DCr < calc_phi_max));
    } // DC_fiducial_cut_theta_phi()

    /**
    * Stefan's comments:
    * use the following cut for inbending electrons and all outbending particles
    * j is the index of the particle. Th variable part_pid is needed to assign the correct cut
    * The inbending / outbending flags (see top of this document) have to be set to assign the correct cut
    *
    * @param j
    * @param region
    * @param part_pid
    */
    protected boolean DC_fiducial_cut_XY(int j, int region, int part_pid) {

        // Fitted values for inbending
        double[][][][] maxparams_in = // DIM = (6,6,3,2) (pid,sector,region,param)
        [[[[-14.563, 0.60032],[-19.6768, 0.58729],[-22.2531, 0.544896]],
            [[-12.7486, 0.587631],[-18.8093, 0.571584],[-19.077, 0.519895]],
            [[-11.3481, 0.536385],[-18.8912, 0.58099],[-18.8584, 0.515956]],
            [[-10.7248, 0.52678],[-18.2058, 0.559429],[-22.0058, 0.53808]],
            [[-16.9644, 0.688637],[-17.1012, 0.543961],[-21.3974, 0.495489]],
            [[-13.4454, 0.594051],[-19.4173, 0.58875],[-22.8771, 0.558029]]],
        [[[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]],
            [[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]]],
        [[[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]],
            [[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]]],
        [[[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]],
            [[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]]],
        [[[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]],
            [[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]]],
        [[[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]],
            [[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]]]] as double[][][][];

        double[][][][] minparams_in = // DIM = (6,6,3,2) (pid,sector,region,param)
        [[[[12.2692, -0.583057],[17.6233, -0.605722],[19.7018, -0.518429]],
            [[12.1191, -0.582662],[16.8692, -0.56719],[20.9153, -0.534871]],
            [[11.4562, -0.53549],[19.3201, -0.590815],[20.1025, -0.511234]],
            [[13.202, -0.563346],[20.3542, -0.575843],[23.6495, -0.54525]],
            [[12.0907, -0.547413],[17.1319, -0.537551],[17.861, -0.493782]],
            [[13.2856, -0.594915],[18.5707, -0.597428],[21.6804, -0.552287]]],
        [[[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]],
            [[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]]],
        [[[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]],
            [[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]]],
        [[[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]],
            [[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]]],
        [[[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]],
            [[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]]],
        [[[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]],
            [[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]]]] as double[][][][];

        // Fitted values for outbending
        double[][][][] maxparams_out = [ // DIM = (6,6,3,2) (pid,sector,region,param)
            [[[-15.0486, 0.72351],[-19.2202, 0.611078],[-27.4513, 0.535668]],[[-27.5984, 0.856463],[-16.7873, 0.601081],[-23.1163, 0.552932]],
            [[-10.4537, 0.698218],[-13.8137, 0.559658],[-35.1087, 0.605458]],[[-15.2615, 0.747703],[-18.0476, 0.598169],[-26.6229, 0.540454]],
            [[-13.2849, 0.669102],[-11.4523, 0.50548],[-14.619, 0.448236]],[[-13.9748, 0.706217],[-19.2459, 0.61829],[-32.0969, 0.586403]]],
            [[[1.01396, 0.498685],[-15.5912, 0.576023],[-15.1113, 0.532929]],[[-5.78163, 0.558017],[-16.2599, 0.579884],[-18.757, 0.553068]],
            [[-5.85198, 0.563657],[-12.8175, 0.560827],[-16.0893, 0.53454]],[[-4.73676, 0.550713],[-15.9589, 0.578277],[-16.4839, 0.533793]],
            [[-6.246, 0.567365],[-14.435, 0.570721],[-15.4366, 0.531068]],[[-6.97001, 0.576377],[-14.6786, 0.568779],[-19.7683, 0.560218]]],
            [[[-5.11874, 0.542303],[-15.0805, 0.574983],[-14.4774, 0.525724]],[[-12.0383, 0.614845],[-13.7267, 0.563175],[-15.0412, 0.526371]],
            [[-6.03209, 0.557031],[-13.7801, 0.565532],[-17.3829, 0.544538]],[[-5.11456, 0.544146],[-12.2437, 0.550863],[-17.309, 0.543019]],
            [[-10.4872, 0.598711],[-10.4236, 0.532708],[-14.3734, 0.53108]],[[-1.0749, 0.520489],[-12.8321, 0.562083],[-16.5056, 0.5455]]],
            [[[-4.02259, 0.524186],[-17.1253, 0.594224],[-18.2492, 0.548083]],[[-9.8687, 0.599888],[-16.4612, 0.586687],[-13.8826, 0.523052]],
            [[-6.09038, 0.565961],[-17.7112, 0.599618],[-15.4107, 0.527006]],[[-7.39969, 0.589083],[-17.9769, 0.601895],[-11.6827, 0.508671]],
            [[-9.4989, 0.597539],[-14.5596, 0.570631],[-11.0431, 0.511108]],[[-4.78634, 0.541011],[-15.7703, 0.58688],[-13.4413, 0.527737]]],
            [[[-8.52794, 0.597096],[-15.7673, 0.578371],[-15.3708, 0.532371]],[[-9.93026, 0.599944],[-18.9386, 0.600393],[-16.9471, 0.538744]],
            [[-6.97351, 0.580954],[-14.787, 0.571151],[-14.7902, 0.535692]],[[-10.1381, 0.61534],[-15.7761, 0.578828],[-12.4601, 0.516824]],
            [[-6.96808, 0.575769],[-16.8735, 0.59029],[-16.5667, 0.546479]],[[-4.66291, 0.557931],[-12.8325, 0.564619],[-15.4321, 0.532078]]],
            [[[-7.04376, 0.566423],[-17.1298, 0.584139],[-5.45416, 0.378063]],[[-8.44714, 0.587986],[-18.1342, 0.594626],[1.15458, 0.235862]],
            [[-5.94346, 0.565299],[-13.1923, 0.544233],[-9.35298, 0.356599]],[[-6.21599, 0.565299],[-17.3319, 0.592404],[-9.93697, 0.490374]],
            [[-4.59187, 0.547947],[-18.6261, 0.617462],[-4.56516, 0.318076]],[[-4.51481, 0.54217],[-15.5234, 0.586091],[-3.33085, 0.406969]]]] as double[][][][];

        double[][][][] minparams_out = [ // DIM = (6,6,3,2) (pid,sector,region,param)
            [[[10.4534, -0.646633],[20.3057, -0.634014],[19.8023, -0.456707]],[[8.39762, -0.604501],[16.3439, -0.594578],[24.3968, -0.550637]],
            [[11.7765, -0.682773],[18.4467, -0.616169],[33.5937, -0.587674]],[[10.3321, -0.651872],[20.2277, -0.636832],[18.6805, -0.5109]],
            [[7.23971, -0.624146],[15.3243, -0.578226],[19.042, -0.50646]], [[11.3041, -0.656507],[17.9646, -0.598628],[20.7513, -0.490774]]],
            [[[2.38471, -0.519764],[17.37, -0.58695],[18.0912, -0.543571]],[[5.54502, -0.55162],[15.7411, -0.574163],[14.124, -0.527875]],
            [[-2.07845, -0.477191],[17.6555, -0.581425],[19.773, -0.545526]],[[0.0286526, -0.510017],[14.3589, -0.569907],[18.4092, -0.553169]],
            [[4.65436, -0.546023],[16.5496, -0.579403],[16.1278, -0.53216]],[[2.42162, -0.520676],[13.5759, -0.557783],[15.4258, -0.523538]]],
            [[[5.42714, -0.547252],[17.9145, -0.586462],[12.5151, -0.50246]],[[1.34431, -0.50385],[15.4816, -0.56814],[14.8332, -0.530269]],
            [[4.21559, -0.534567],[16.107, -0.56405],[13.833, -0.514593]],[[4.20743, -0.536347],[15.2913, -0.571454],[16.8639, -0.534427]],
            [[6.1354, -0.558451],[15.8315, -0.575873],[16.3529, -0.541569]],[[8.68088, -0.575933],[16.4584, -0.576239],[15.6839, -0.534505]]],
            [[[4.68623, -0.547042],[15.677, -0.565814],[15.9174, -0.525793]],[[5.63431, -0.550919],[16.3397, -0.582825],[13.0206, -0.519814]],
            [[10.7146, -0.609206],[17.798, -0.588528],[17.5752, -0.535447]],[[5.60915, -0.55906],[15.9808, -0.582329],[15.4179, -0.531727]],
            [[5.02979, -0.550918],[16.5882, -0.588454],[14.8774, -0.521648]],[[11.0076, -0.616694],[16.3595, -0.578062],[14.1671, -0.52539]]],
            [[[7.23685, -0.571469],[12.413, -0.523034],[10.2567, -0.43924]],[[2.89157, -0.524202],[13.7971, -0.541647],[16.9268, -0.51881]],
            [[7.39136, -0.564481],[15.7659, -0.579543],[13.2347, -0.51334]],[[5.30074, -0.555581],[8.29014, -0.455451],[10.9363, -0.504937]],
            [[8.93705, -0.586499],[15.6892, -0.575975],[10.5961, -0.455508]],[[9.26221, -0.591506],[14.466, -0.565013],[15.2475, -0.53195]]],
            [[[7.31173, -0.576571],[15.7123, -0.594739],[19.0354, -0.499379]],[[8.07107, -0.591361],[17.1351, -0.603906],[-0.239706, -0.24005]],
            [[7.83093, -0.577219],[11.579, -0.516213],[14.2709, -0.394561]],[[9.06302, -0.600711],[18.5039, -0.616063],[-3.86975, -0.213802]],
            [[4.71687, -0.547772],[18.0103, -0.604119],[10.7901, -0.437388]],[[7.17946, -0.57369],[11.2732, -0.397794],[-2.79229, -0.137468]]]] as double[][][][];


        final double[][][][] minparams = ((inbending && !outbending) ? minparams_in : minparams_out);
        final double[][][][] maxparams = ((inbending && !outbending) ? maxparams_in : maxparams_out);

        double X;
        double Y;
        switch (region) {
            case 1: X = part_DC_c1x[j];Y = part_DC_c1y[j];break;
            case 2: X = part_DC_c2x[j]; Y = part_DC_c2y[j]; break;
            case 3: X = part_DC_c3x[j]; Y = part_DC_c3y[j]; break;
            default: X = 0; Y = 0; break;
        }

        double reducedPi = Math.PI / 180;
        if(part_DC_sector[j] == 2)
        {
            final double X_new = X * Math.cos(-60 * reducedPi) - Y * Math.sin(-60 * reducedPi);
            Y = X * Math.sin(-60 * reducedPi) + Y * Math.cos(-60 * reducedPi);
            X = X_new;
        }

        if(part_DC_sector[j] == 3)
        {
            final double X_new = X * Math.cos(-120 * reducedPi) - Y * Math.sin(-120 * reducedPi);
            Y = X * Math.sin(-120 * reducedPi) + Y * Math.cos(-120 * reducedPi);
            X = X_new;
        }

        if(part_DC_sector[j] == 4)
        {
            final double X_new = X * Math.cos(-180 * reducedPi) - Y * Math.sin(-180 * reducedPi);
            Y = X * Math.sin(-180 * reducedPi) + Y * Math.cos(-180 * reducedPi);
            X = X_new;
        }

        if(part_DC_sector[j] == 5)
        {
            final double X_new = X * Math.cos(120 * reducedPi) - Y * Math.sin(120 * reducedPi);
            Y = X * Math.sin(120 * reducedPi) + Y * Math.cos(120 * reducedPi);
            X = X_new;
        }

        if(part_DC_sector[j] == 6)
        {
            final double X_new = X * Math.cos(60 * reducedPi) - Y * Math.sin(60 * reducedPi);
            Y = X * Math.sin(60 * reducedPi) + Y * Math.cos(60 * reducedPi);
            X = X_new;
        }

        int pid = 0;

        switch (part_pid) {
            case 11: pid = 0; break;
            case 2212: pid = 1; break;
            case 211: pid = 2; break;
            case -211: pid = 3; break;
            case 321: pid = 4; break;
            case -321: pid = 5; break;
            default: return false;
        }

        if(inbending == true && outbending == false) pid = 0; // use only for electrons in inbending case

        --region;
        double calc_max = minparams[pid][part_DC_sector[j] - 1][region][0] + minparams[pid][part_DC_sector[j] - 1][region][1] * X;
        double calc_min = maxparams[pid][part_DC_sector[j] - 1][region][0] + maxparams[pid][part_DC_sector[j] - 1][region][1] * X; // DEBUGGING switch max and min assignments...not really sure why but seems arrays are mixed up
        return ((Y > calc_min) && (Y < calc_max));
 
    } // DC_fiducial_cut_XY()

} // class
