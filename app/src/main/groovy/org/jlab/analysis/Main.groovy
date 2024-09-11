package org.jlab.analysis;

// Groovy Imports
import groovy.transform.CompileStatic

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

// // Pytorch geometric imports //NOTE: THESE WILL BE REMOVED FROM MAIN
// import org.pytorch.IValue;
// import org.pytorch.Module;
// import org.pytorch.Tensor;

/**
* Setup/customize your analysis here.
*
* @version 1.0
* @author  Matthew McEneaney
*/

// @CompileStatic
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

       //NOTE: LOAD LIBRARIES BEFORE INSTANTIATING ANALYSIS

       System.out.println("Main: System.getenv(\"JAVA_HOME\") = "+System.getenv("JAVA_HOME"));
        System.out.println("Main: System.getenv(\"DEPLOY_JAVA_HOME\") = "+System.getenv("DEPLOY_JAVA_HOME"));
        System.out.println("Main: System.getenv(\"PWD\") = "+System.getenv("PWD"));
        System.out.println("Main: Working Directory = " + System.getProperty("user.dir"));
        String deploy_java_home = System.getenv("DEPLOY_JAVA_HOME");
        System.out.println(" loading : "+deploy_java_home+"/torch_sparse");//DEBUGGING
       System.loadLibrary("pyg");
        System.loadLibrary("torchsparse"); //NOTE: This must be done in a static context, e.g., in main.
        System.loadLibrary("torchscatter");
        System.loadLibrary("torchcluster");
        System.loadLibrary("torchsplineconv");

         Analysis analysis = new Analysis();
         Parser parser     = new Parser(); 
         if(!parser.parse(args,analysis)) { return; }
       /*// Load PyTorch Geometric Dependencies
       System.out.println("Main: System.getenv(\"JAVA_HOME\") = "+System.getenv("JAVA_HOME"));
       System.out.println("Main: System.getenv(\"DEPLOY_JAVA_HOME\") = "+System.getenv("DEPLOY_JAVA_HOME"));
       System.out.println("Main: System.getenv(\"PWD\") = "+System.getenv("PWD"));
        System.out.println("Main: Working Directory = " + System.getProperty("user.dir"));
       String deploy_java_home = System.getenv("DEPLOY_JAVA_HOME");
       System.out.println(" loading : "+deploy_java_home+"/torch_sparse");//DEBUGGING
       System.loadLibrary(System.getenv("DEPLOY_JAVA_HOME")+"/torchsparse"); //NOTE: This must be done in a static context, e.g., in main.
       System.loadLibrary(System.getenv("DEPLOY_JAVA_HOME")+"/torchscatter");
        System.loadLibrary(System.getenv("DEPLOY_JAVA_HOME")+"/torchcluster");
        System.loadLibrary(System.getenv("DEPLOY_JAVA_HOME")+"/torchsplineconv");*/
       
        /*if (true) {
            // Load PyTorch Geometric Dependencies
            System.loadLibrary("torchsparse"); //NOTE: This must be done in a static context, e.g., in main.
            System.loadLibrary("torchscatter");
            System.loadLibrary("torchcluster");
            System.loadLibrary("torchsplineconv");

            //TEST HERE:
            // Check PyTorch Dependency
            Tensor data =
                Tensor.fromBlob(
                    new int[] {1, 2, 3, 4, 5, 6}, // data
                    new long[] {2, 3} // shape
                    );
            System.out.println("DEBUGGING: data pytorch tensor = "+Arrays.toString(data.getDataAsIntArray()));

            // Check args
            if (args.length<=0) {System.out.println("Usage: Library </path/to/pytorch/model.pt>"); System.exit(0); }
            String path = args[0];
            
            // Load and apply model
            Module mod = Module.load(path);
            Tensor x =
                Tensor.fromBlob(
                    new float[] {1, 2, 3, 4, 5, 6, 7, 1, 2, 3, 4, 5, 6, 7, 1, 2, 3, 4, 5, 6, 7}, // data //NOTE: x must have type float
                    new long[] {3, 7} // shape
                    );
            Tensor edge_index =
                Tensor.fromBlob(
                    new long[] {0, 1, 2, 1, 2, 0}, // data //NOTE: edge_index must have type long
                    new long[] {2, 3} // shape
                    );
            IValue result = mod.forward(IValue.from(x), IValue.from(edge_index));
            Tensor output = result.toTensor();
            System.out.println("shape: " + Arrays.toString(output.shape()));
            System.out.println("data: " + Arrays.toString(output.getDataAsFloatArray()));

            // Workaround for https://github.com/facebookincubator/fbjni/issues/25
            System.exit(0);

            //NOTE: Need to write class to load model and read in events
            //TODO: Write instructions for installation of pytorch libraries on readme.md
            //TODO: Check this works to compile without having the pytorch libraries installed?  -> Can just leave on different branches but that is not a great solution.
        }*/
        analysis.processFiles();

    } // main()

} // class
