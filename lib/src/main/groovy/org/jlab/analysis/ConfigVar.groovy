package org.jlab.analysis;

// Java Imports
import java.io.*;
import java.util.*;

// CLAS Physics Imports
import org.jlab.jnp.hipo4.data.*;
import org.jlab.jnp.hipo4.io.*;
import org.jlab.jnp.physics.*;

/**
* Interface for lambda expressions to access run configuration variables
* (or other bank variables) from a specific hipo reader and event.
*
* @version 1.0
* @author  Matthew McEneaney
*/

public interface ConfigVar {
    double get(HipoReader reader, Event event);
}