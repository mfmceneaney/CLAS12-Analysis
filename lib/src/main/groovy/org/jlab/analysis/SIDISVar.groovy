package org.jlab.analysis;

// Java Imports
import java.io.*;
import java.util.*;

// CLAS Physics Imports
import org.jlab.jnp.hipo4.data.*;
import org.jlab.jnp.hipo4.io.*;
import org.jlab.jnp.physics.*;

/**
* Interface for lambda expressions to compute kinematic quantities
* from a list of particles organized by the given decay chain pids.
*
* @version 1.0
* @author  Matthew McEneaney
*/

public interface SIDISVar {
    double get(Constants constants, ArrayList<Integer> decay, ArrayList<DecayProduct> plist, DecayProduct beam);
}
