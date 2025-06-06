
package org.jlab.analysis;

// Groovy Imports
import groovy.json.JsonSlurper;
import java.util.Random;
import groovy.transform.CompileStatic;

/**
* Allows one to smear reconstructed momentum, polar angle, and azimuthal angle values around the MC truth values
* with differences taken randomly from a Gaussian distribution.  The resolutions (widths) of the Gaussians should be taken
* from fits to the widths in momentum bins and supplied as json files giving the resolutions and bin limits.
*
* @version 1.0
* @author  Matthew McEneaney
*/

@CompileStatic
public class MCSmearing {

    public String  _jsonpath;

    double _smearing_p     = 0.50;
    double _smearing_theta = 0.50;
    double _smearing_phi   = 0.50;

    Map<Integer, Map<Integer,Cut>> _binlims_map;
    Map<Integer, Map<Integer,Double>> _mc_resolution_p_map;
    Map<Integer, Map<Integer,Double>> _mc_resolution_theta_map;
    Map<Integer, Map<Integer,Double>> _mc_resolution_phi_map;

    Random _rng;

    public MCSmearing(String jsonpath, double smearing_p, double smearing_theta, double smearing_phi) {

        // Reset paths and smearing values
        this._jsonpath = jsonpath;
        this._smearing_p = smearing_p;
        this._smearing_theta = smearing_theta;
        this._smearing_phi = smearing_phi;

        // Load the JSON data
        File jsonFile = new File(this._jsonpath);
        JsonSlurper jsonSlurper = new JsonSlurper();
        Map<String, Object> jsonmap = (Map<String, Object>)jsonSlurper.parse(jsonFile);
        Map<String,Map<Integer,List<Double>>> binlims_map = (Map<String,Map<Integer,List<Double>>>)jsonmap.get("binlims");

        // Initialize maps
        this._binlims_map = new Map<Integer, Map<Integer,Cut>>();
        this._mc_resolution_p_map = new Map<Integer, Map<Integer,Double>>();
        this._mc_resolution_theta_map = new Map<Integer, Map<Integer,Double>>();
        this._mc_resolution_phi_map = new Map<Integer, Map<Integer,Double>>();

        // Add bin limits to map
        for (Integer pid : mymap.values()) {
            Map<Integer,Cut> new_binlim_map = new Map<Integer,Cut>();
            for (Integer binid : mymap.get(pid).values()) {
                double xmin = binlim_map.get(binid).at(0);
                double xmin = binlim_map.get(binid).at(1);
                Cut cut = (double x) -> {if(x >= xmin && x<xmax ) {return true;} else {return false;}};
                new_binlim_map.put(binid,cut);
            }
            this._binlims_map.put(pid,new_binlim_map);
        }

        // Load MC resolution maps
        if (jsonmap.containsKey("p")) {
            System.out.println("Loading momentum ('p') MC resolution map...");
            this._mc_resolution_p_map = (Map<Integer,Map<Integer,Double>>)jsonmap.get("p");
        }
        if (jsonmap.containsKey("theta")) {
            System.out.println("Loading polar angle ('theta') MC resolution map...");
            this._mc_resolution_theta_map = (Map<Integer,Map<Integer,Double>>)jsonmap.get("theta");
        }
        if (jsonmap.containsKey("phi")) {
            System.out.println("Loading azimuthal angle ('phi') MC resolution map...");
            this._mc_resolution_phi_map = (Map<Integer,Map<Integer,Double>>)jsonmap.get("phi");
        }

        // Create random number generator
        this._rng = new Random();
    }

    /**
    * Apply MC smearing to a given particle and MC truth particle
    * @param p
    * @param p_mc
    * @return new_p
    */
    protected DecayProduct applySmearing(DecayProduct p, MCDecayProduct p_mc) {

        // Generate a random Gaussian float
        double gaussian = this._rng.nextGaussian() // mean = 0.0, std dev = 1.0

        // Set PID, momentum, theta, phi for REC particle
        int pid_dt      = p.pid();
        double p_dt     = p.p();
        double theta_dt = p.theta();
        double phi_dt   = p.phi();

        // Set PID and momentum theta phi for REC particle
        int pid_mc      = p_mc.pid();
        double p_mc     = p_mc.p();
        double theta_mc = p_mc.theta();
        double phi_mc   = p_mc.phi();

        // Smear the momentum
        double sigma_p_mc = 0.0;

        // Check the momentum bin
        int binid_mc = -1;
        if (this._binlims_p_map.containsKey(pid_dt)) {

            // Get cut map for pid
            Map<Integer, Cut> cut_map = this._binlims_map.get(pid_dt);

            // Loop cut map
            for (Integer binid : cut_map.values()) {

                // Check which bin the MC truth is in
                if (cut_map.get(binid)(p_mc)) {
                    binid_mc = binid;
                }
            }
        }

        // Get the MC momentum resolution
        double sigma_p_mc = 0.0;
        if (binid_mc>=0 && this._mc_resolution_p_map.get(pid_dt).containsKey(binid_mc)) {
            sigma_p_mc = this._mc_resolution_p_map.get(pid_dt).get(binid_mc);
        }

        // Smear the momentum
        double sigma_p = sigma_p_mc * Math.sqrt(1.0+this._smearing_p*this._smearing_p); // Add in the additional smearing for data
        double new_p_dt = p_mc + gaussian * sigma_p;

        // Get the MC theta resolution
        double sigma_theta_mc = 0.0;
        if (binid_mc>=0 && this._mc_resolution_theta_map.get(pid_dt).containsKey(binid_mc)) {
            sigma_theta_mc = this._mc_resolution_theta_map.get(pid_dt).get(binid_mc);
        }

        // Smear the theta
        double sigma_theta = sigma_theta_mc * Math.sqrt(1.0+this._smearing_theta*this._smearing_theta); // Add in the additional smearing for data
        double new_theta_dt = theta_mc + gaussian * sigma_theta;

        // Get the MC phi resolution
        double sigma_phi_mc = 0.0;
        if (binid_mc>=0 && this._mc_resolution_phi_map.get(pid_dt).containsKey(binid_mc)) {
            sigma_phi_mc = this._mc_resolution_phi_map.get(pid_dt).get(binid_mc);
        }

        // Smear the phi
        double sigma_phi = sigma_phi_mc * Math.sqrt(1.0+this._smearing_phi*this._smearing_phi); // Add in the additional smearing for data
        double new_phi_dt = phi_mc + gaussian * sigma_phi;

        // Reset momentum vector
        DecayProduct new_p = p.clone();
        new_p.setPThetaPhiM(new_p_dt, new_theta_dt, new_phi_dt, p.m());
        return new_p;
        
    }

} // public class MCSmearing {
