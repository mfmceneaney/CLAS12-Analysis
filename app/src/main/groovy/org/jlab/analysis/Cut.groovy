package org.jlab.analysis;

// Groovy Imports
import groovy.transform.CompileStatic;

/**
* Interface for lambda expressions to cut kinematic variables.
*
* @version 1.0
* @author  Matthew McEneaney
*/

@CompileStatic
public interface Cut {
    boolean cut(double var);
}
