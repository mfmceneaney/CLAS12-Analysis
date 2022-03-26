package org.jlab.analysis;

// Java Imports
import java.io.*;
import java.util.*;

// CLAS Physics Imports
import org.jlab.jnp.hipo4.data.*;
import org.jlab.jnp.hipo4.io.*;
import org.jlab.jnp.physics.*;

/**
* Interface for lambda expressions to cut kinematic variables.
*
* @version 1.0
* @author  Matthew McEneaney
*/

public interface Cut {
    boolean cut(double var);
}
