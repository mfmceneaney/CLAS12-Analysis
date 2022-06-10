package org.jlab.analysis;

// Groovy Imports
import groovy.transform.CompileStatic

// Java Imports
import java.util.*;

/**
* Interface for lambda expressions to compute kinematic quantities
* from a list of particles organized by the given decay chain pids.
*
* @version 1.0
* @author  Matthew McEneaney
*/

@CompileStatic
public interface SIDISVar {
    double get(Constants constants, ArrayList<Integer> decay, ArrayList<DecayProduct> plist, DecayProduct beam);
}
