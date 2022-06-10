package org.jlab.analysis;

// Groovy Imports
import groovy.transform.CompileStatic

// CLAS Physics Imports
import org.jlab.jnp.hipo4.data.Event;
import org.jlab.jnp.hipo4.io.HipoReader;

/**
* Interface for lambda expressions to access run configuration variables
* (or other bank variables) from a specific hipo reader and event.
*
* @version 1.0
* @author  Matthew McEneaney
*/
import groovy.transform.CompileStatic

@CompileStatic
public interface ConfigVar {
    double get(HipoReader reader, Event event);
}
