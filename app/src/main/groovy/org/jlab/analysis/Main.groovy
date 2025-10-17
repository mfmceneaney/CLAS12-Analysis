package org.jlab.analysis;

// Groovy Imports
import groovy.transform.CompileStatic

// Java Imports
import java.io.*;
import java.util.*;

// CLAS Physics Imports
import org.jlab.jnp.hipo4.data.*;
import org.jlab.jnp.hipo4.io.HipoReader;
import org.jlab.clas.physics.*;

// J2ROOT Imports
import org.jlab.jroot.ROOTFile;
import org.jlab.jroot.TNtuple;

/**
* Setup/customize your analysis here.
*
* @version 1.0
* @author  Matthew McEneaney
*/

// @CompileStatic
public class Main {

    /**
    * Process command line arguments such as paths for input/output file/directory,
    * limited kinematic cuts, and charge/pid identification requirements.  Then, run an analysis that identifies
    * all unique particle combinations corresponding to the given final state in each HIPO events and
    * outputs particle information and kinematics to a ROOT TNTuple.  Use the <code>-h/--h/-help/--help</code>
    * options when executing the application for full detail or check out the <code>docs/</code> folder.
    * You will probably want to customize this for less general analyses.
    *
    * @param args  Command line arguments
    * @throws IOException
    * @throws InterruptedException
    */
    public static void main(String[] args) throws IOException, InterruptedException {
        Analysis analysis = new Analysis();
        Parser parser     = new Parser(); 
        if(!parser.parse(args,analysis)) { return; }
        analysis.processFiles();

    } // main()

} // class
