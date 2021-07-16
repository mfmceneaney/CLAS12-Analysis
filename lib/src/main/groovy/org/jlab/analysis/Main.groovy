package org.jlab.analysis;

// Java Imports
import java.io.*;
import java.util.*;

// CLAS Physics Imports
import org.jlab.jnp.hipo4.data.*;
import org.jlab.jnp.hipo4.io.*;
import org.jlab.jnp.physics.*;

// J2ROOT Imports
import org.jlab.jroot.ROOTFile;
import org.jlab.jroot.TNtuple;

/**
* Setup/customize your analysis here.
*
* @version 1.0
* @author  Matthew McEneaney
*/

public class Main {

    /**
    * Main method processes command line arguments such as paths for input/output file/directory,
    * limited kinematic cuts, and charge/pid identification requirements.
    * Use the <code>-h/--h/-help/--help</code> options for full detail or check out the <code>docs/groovy</code> folder.
    * You will probably want to customize this for less general analyses.
    *
    * @param String[] args
    */
    public static void main(String[] args) throws IOException, InterruptedException {
        
        Analysis analysis = new Analysis();
        Parser parser     = new Parser(); 
        if(!parser.parse(args,analysis)) { return; }
        analysis.processFiles();

    } // main()

} // class