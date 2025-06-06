
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
* with differences taken randomly from a Gaussian distribution.  The resolutions (widths) of the Gaussians should be taken
* from fits to the widths in momentum bins and supplied as json files giving the resolutions and bin limits.
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
    LinkedHashMap<Integer, LinkedHashMap<Integer,Double>> _mc_resolution_mom_map;
    LinkedHashMap<Integer, LinkedHashMap<Integer,Double>> _mc_resolution_theta_map;
    LinkedHashMap<Integer, LinkedHashMap<Integer,Double>> _mc_resolution_phi_map;

    Random _rng;

    public MCSmearing(String jsonpath, double smearing_p, double smearing_theta, double smearing_phi) {

        // Reset paths and smearing values
        this._jsonpath = jsonpath;
        this._smearing_mom = smearing_p;
        this._smearing_theta = smearing_theta;
        this._smearing_phi = smearing_phi;

        // Load the JSON data
        File jsonFile = new File(this._jsonpath);
        JsonSlurper jsonSlurper = new JsonSlurper();
        LinkedHashMap<String, Object> jsonmap = (LinkedHashMap<String, Object>)jsonSlurper.parse(jsonFile);

        // Initialize maps
        this._bincuts_map = new LinkedHashMap<Integer, LinkedHashMap<Integer,Cut>>();
        this._mc_resolution_mom_map = new LinkedHashMap<Integer, LinkedHashMap<Integer,Double>>();
        this._mc_resolution_theta_map = new LinkedHashMap<Integer, LinkedHashMap<Integer,Double>>();
        this._mc_resolution_phi_map = new LinkedHashMap<Integer, LinkedHashMap<Integer,Double>>();

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
            this._mc_resolution_mom_map = (LinkedHashMap<Integer,LinkedHashMap<Integer,Double>>)jsonmap.get("mom");
        }
        if (jsonmap.containsKey("theta")) {
            System.out.println("Loading polar angle ('theta') MC resolution map...");
            this._mc_resolution_theta_map = (LinkedHashMap<Integer,LinkedHashMap<Integer,Double>>)jsonmap.get("theta");
        }
        if (jsonmap.containsKey("phi")) {
            System.out.println("Loading azimuthal angle ('phi') MC resolution map...");
            this._mc_resolution_phi_map = (LinkedHashMap<Integer,LinkedHashMap<Integer,Double>>)jsonmap.get("phi");
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
    protected DecayProduct applySmearing(DecayProduct p, DecayProduct p_mc) {

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
        double sigma_mom_mc = 0.0;
        if (binid_mc>=0 && this._mc_resolution_mom_map.get(pid_dt).containsKey(binid_mc)) {
            sigma_mom_mc = this._mc_resolution_mom_map.get(pid_dt).get(binid_mc);
        }

        // Smear the momentum
        double sigma_mom = sigma_mom_mc * Math.sqrt((double)(1.0+this._smearing_mom*this._smearing_mom)); // Add in the additional smearing for data
        double new_mom_dt = mom_mc * (1.0 + this._rng.nextGaussian() * sigma_mom);

        // Get the MC theta resolution
        double sigma_theta_mc = 0.0;
        if (binid_mc>=0 && this._mc_resolution_theta_map.get(pid_dt).containsKey(binid_mc)) {
            sigma_theta_mc = this._mc_resolution_theta_map.get(pid_dt).get(binid_mc);
        }

        // Smear the theta
        double sigma_theta = sigma_theta_mc * Math.sqrt((double)(1.0+this._smearing_theta*this._smearing_theta)); // Add in the additional smearing for data
        double new_theta_dt = theta_mc * (1.0 + this._rng.nextGaussian() * sigma_theta);

        // Get the MC phi resolution
        double sigma_phi_mc = 0.0;
        if (binid_mc>=0 && this._mc_resolution_phi_map.get(pid_dt).containsKey(binid_mc)) {
            sigma_phi_mc = this._mc_resolution_phi_map.get(pid_dt).get(binid_mc);
        }

        // Smear the phi
        double sigma_phi = sigma_phi_mc * Math.sqrt((double)(1.0+this._smearing_phi*this._smearing_phi)); // Add in the additional smearing for data
        double new_phi_dt = phi_mc * (1.0 + this._rng.nextGaussian() * sigma_phi);

        // Reset momentum vector
        DecayProduct new_p = p.clone();
        new_p.setPThetaPhiM(new_mom_dt, new_theta_dt, new_phi_dt, p.m());
        return new_p;
        
    }

} // public class MCSmearing {
