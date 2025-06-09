
package org.jlab.analysis;

// Java Imports
import java.io.*;
import java.util.*;

// Groovy Imports
import groovy.json.JsonSlurper;
import java.util.Random;
import groovy.transform.CompileStatic;

/**
* Allows one to smear reconstructed momentum, polar angle, and azimuthal angle values around the MC truth values
* with differences taken randomly from a Gaussian distribution.  The resolutions (widths) and offsets (means) of the Gaussians should be taken
* from fits to the difference of reconstructed - truth in momentum bins and supplied as json files giving the resolutions and bin limits.
*
* @version 1.0
* @author  Matthew McEneaney
*/

@CompileStatic
public class MCSmearing {

    public String  _jsonpath;

    double _smearing_mom   = 0.50;
    double _smearing_theta = 0.50;
    double _smearing_phi   = 0.50;

    LinkedHashMap<Integer, LinkedHashMap<Integer,Cut>> _bincuts_map;
    LinkedHashMap<Integer, LinkedHashMap<Integer,ArrayList<Double>>> _mc_resolution_mom_map;
    LinkedHashMap<Integer, LinkedHashMap<Integer,ArrayList<Double>>> _mc_resolution_theta_map;
    LinkedHashMap<Integer, LinkedHashMap<Integer,ArrayList<Double>>> _mc_resolution_phi_map;

    Random _rng;

    boolean _use_mu = false;

    public MCSmearing() {

        // Initialize maps
        this._bincuts_map = new LinkedHashMap<Integer, LinkedHashMap<Integer,Cut>>();
        this._mc_resolution_mom_map = new LinkedHashMap<Integer, LinkedHashMap<Integer,ArrayList<Double>>>();
        this._mc_resolution_theta_map = new LinkedHashMap<Integer, LinkedHashMap<Integer,ArrayList<Double>>>();
        this._mc_resolution_phi_map = new LinkedHashMap<Integer, LinkedHashMap<Integer,ArrayList<Double>>>();

        // Create random number generator
        this._rng = new Random();
    }

    public MCSmearing(double smearing_mom, double smearing_theta, double smearing_phi, boolean use_mu) {

        // Reset paths and smearing values
        this._smearing_mom = smearing_mom;
        this._smearing_theta = smearing_theta;
        this._smearing_phi = smearing_phi;
        this._use_mu = use_mu

        // Initialize maps
        this._bincuts_map = new LinkedHashMap<Integer, LinkedHashMap<Integer,Cut>>();
        this._mc_resolution_mom_map = new LinkedHashMap<Integer, LinkedHashMap<Integer,ArrayList<Double>>>();
        this._mc_resolution_theta_map = new LinkedHashMap<Integer, LinkedHashMap<Integer,ArrayList<Double>>>();
        this._mc_resolution_phi_map = new LinkedHashMap<Integer, LinkedHashMap<Integer,ArrayList<Double>>>();

        // Create random number generator
        this._rng = new Random();
    }

    /**
    * Set data smearing fraction for momentum.
    * @param smearing_mom
    */
    protected void setMomSmearing(double smearing_mom) {
        this._smearing_mom = smearing_mom;
    }

    /**
    * Set data smearing fraction for theta.
    * @param smearing_theta
    */
    protected void setThetaSmearing(double smearing_theta) {
        this._smearing_theta = smearing_theta;
    }

    /**
    * Set data smearing fraction for phi.
    * @param smearing_phi
    */
    protected void setPhiSmearing(double smearing_phi) {
        this._smearing_phi = smearing_phi;
    }

    /**
    * Set data smearing fractions for momentum, theta, and phi.
    * @param smearing_mom
    * @param smearing_theta
    * @param smearing_phi
    */
    protected void setSmearing(double smearing_mom, double smearing_theta, double smearing_phi) {
        this._smearing_mom = smearing_mom;
        this._smearing_theta = smearing_theta;
        this._smearing_phi = smearing_phi;
    }

    /**
    * Set option to use means of smearing distributions to offset MC truth values.
    * @param use_mu
    */
    protected void setUseMu(boolean use_mu) {
        this._use_mu = use_mu;
    }

    /**
    * Load smearing maps from a JSON file.
    * @param jsonpath
    */
    protected void loadJSON(String jsonpath) {

        // Load the JSON data
        File jsonFile = new File(this._jsonpath);
        JsonSlurper jsonSlurper = new JsonSlurper();
        LinkedHashMap<String, Object> jsonmap = (LinkedHashMap<String, Object>)jsonSlurper.parse(jsonFile);

        // Initialize maps
        this._bincuts_map = new LinkedHashMap<Integer, LinkedHashMap<Integer,Cut>>();
        this._mc_resolution_mom_map = new LinkedHashMap<Integer, LinkedHashMap<Integer,ArrayList<Double>>>();
        this._mc_resolution_theta_map = new LinkedHashMap<Integer, LinkedHashMap<Integer,ArrayList<Double>>>();
        this._mc_resolution_phi_map = new LinkedHashMap<Integer, LinkedHashMap<Integer,ArrayList<Double>>>();

        // Load bin limits map
        LinkedHashMap<Integer,LinkedHashMap<Integer,ArrayList<Double>>> mombinlims_map = new LinkedHashMap<Integer,LinkedHashMap<Integer,ArrayList<Double>>>();
        if (jsonmap.containsKey("mombinlims")) {
            System.out.println("Loading momentum bin limits ('mombinlims') map...");
            mombinlims_map = (LinkedHashMap<Integer,LinkedHashMap<Integer,ArrayList<Double>>>)jsonmap.get("mobinlims");
        }

        // Add cuts to bin cuts map
        for (Integer pid : mombinlims_map.keySet()) {
            LinkedHashMap<Integer,Cut> new_binlim_map = new LinkedHashMap<Integer,Cut>();
            for (Integer binid : mombinlims_map.get(pid).keySet()) {
                double xmin = mombinlims_map.get(pid).get(binid).get(0);
                double xmax = mombinlims_map.get(pid).get(binid).get(1);
                Cut cut = (double x) -> {if(x >= xmin && x<xmax ) {return true;} else {return false;}};
                new_binlim_map.put(binid,cut);
            }
            this._bincuts_map.put(pid,new_binlim_map);
        }

        // Load MC resolution maps
        if (jsonmap.containsKey("mom")) {
            System.out.println("Loading momentum ('mom') MC resolution map...");
            this._mc_resolution_mom_map = (LinkedHashMap<Integer,LinkedHashMap<Integer,ArrayList<Double>>>)jsonmap.get("mom");
        }
        if (jsonmap.containsKey("theta")) {
            System.out.println("Loading polar angle ('theta') MC resolution map...");
            this._mc_resolution_theta_map = (LinkedHashMap<Integer,LinkedHashMap<Integer,ArrayList<Double>>>)jsonmap.get("theta");
        }
        if (jsonmap.containsKey("phi")) {
            System.out.println("Loading azimuthal angle ('phi') MC resolution map...");
            this._mc_resolution_phi_map = (LinkedHashMap<Integer,LinkedHashMap<Integer,ArrayList<Double>>>)jsonmap.get("phi");
        }

        // Create random number generator
        this._rng = new Random();
    }

    /**
    * Smear the momentum, theta, and phi of a reconstructed particle and an MC truth particle.
    * @param p
    * @param p_mc
    * @return new_p
    */
    protected DecayProduct smear(DecayProduct p, DecayProduct p_mc) {

        // Set PID, momentum, theta, phi for REC particle
        int pid_dt      = p.pid();
        double mom_dt   = p.p();
        double theta_dt = p.theta();
        double phi_dt   = p.phi();

        // Set PID and momentum theta phi for REC particle
        int pid_mc      = p_mc.pid();
        double mom_mc   = p_mc.p();
        double theta_mc = p_mc.theta();
        double phi_mc   = p_mc.phi();

        // Check the momentum bin
        int binid_mc = -1;
        if (this._bincuts_map.containsKey(pid_dt)) {

            // Get cut map for pid
            LinkedHashMap<Integer, Cut> cut_map = this._bincuts_map.get(pid_dt);

            // Loop cut map
            for (Integer binid : cut_map.keySet()) {

                // Check which bin the MC truth is in
                if (cut_map.get(binid)(mom_mc)) {
                    binid_mc = binid;
                }
            }
        }

        // Get the MC momentum resolution
        double mu_mom_mc = 0.0;
        double sigma_mom_mc = 0.0;
        if (binid_mc>=0 && this._mc_resolution_mom_map.get(pid_dt).containsKey(binid_mc)) {
            mu_mom_mc = this._mc_resolution_mom_map.get(pid_dt).get(binid_mc).get(0);
            sigma_mom_mc = this._mc_resolution_mom_map.get(pid_dt).get(binid_mc).get(1);
        }

        // Smear the momentum
        double sigma_mom = sigma_mom_mc * Math.sqrt((double)(1.0+this._smearing_mom*this._smearing_mom)); // Add in the additional smearing for data
        double new_mom_dt = mom_mc * (1.0 + (this._use_mu ? mu_mom_mc : 0.0) + this._rng.nextGaussian() * sigma_mom); //NOTE: Momentum should be Delta p / p but angles are just raw difference Delta theta, Delta phi.

        // Get the MC theta resolution
        double mu_theta_mc = 0.0;
        double sigma_theta_mc = 0.0;
        if (binid_mc>=0 && this._mc_resolution_theta_map.get(pid_dt).containsKey(binid_mc)) {
            mu_theta_mc = this._mc_resolution_theta_map.get(pid_dt).get(binid_mc).get(0);
            sigma_theta_mc = this._mc_resolution_theta_map.get(pid_dt).get(binid_mc).get(1);
        }

        // Smear the theta
        double sigma_theta = sigma_theta_mc * Math.sqrt((double)(1.0+this._smearing_theta*this._smearing_theta)); // Add in the additional smearing for data
        double new_theta_dt = theta_mc + (this._use_mu ? mu_theta_mc : 0.0) + this._rng.nextGaussian() * sigma_theta; //NOTE: Momentum should be Delta p / p but angles are just raw difference Delta theta, Delta phi.

        // Get the MC phi resolution
        double mu_phi_mc = 0.0;
        double sigma_phi_mc = 0.0;
        if (binid_mc>=0 && this._mc_resolution_phi_map.get(pid_dt).containsKey(binid_mc)) {
            mu_phi_mc = this._mc_resolution_phi_map.get(pid_dt).get(binid_mc).get(0);
            sigma_phi_mc = this._mc_resolution_phi_map.get(pid_dt).get(binid_mc).get(1);
        }

        // Smear the phi
        double sigma_phi = sigma_phi_mc * Math.sqrt((double)(1.0+this._smearing_phi*this._smearing_phi)); // Add in the additional smearing for data
        double new_phi_dt = phi_mc + (this._use_mu ? mu_phi_mc : 0.0) + this._rng.nextGaussian() * sigma_phi; //NOTE: Momentum should be Delta p / p but angles are just raw difference Delta theta, Delta phi.

        // Reset momentum vector
        DecayProduct new_p = p.clone();
        new_p.setPThetaPhiM(new_mom_dt, new_theta_dt, new_phi_dt, p.m());
        return new_p;
        
    }

    protected ArrayList<DecayProduct> smear(ArrayList<DecayProduct> rclist, ArrayList<DecayProduct> mclist) {

        // Check lengths match
        if (rclist.size()!=mclist.size()) throw new IllegalArgumentException("List sizes do not match: rclist.size()=" + rclist.size() + ", but mclist.size()=" + mclist.size());

        // Loop lists and smear
        ArrayList<DecayProduct> plist = new ArrayList<DecayProduct>();
        for (int idx=0; idx<rclist.size(); idx++) {
            DecayProduct p = this.smear(rclist.get(idx), mclist.get(idx));
            plist.add(p);
        }

        return plist;
    }

} // public class MCSmearing {
