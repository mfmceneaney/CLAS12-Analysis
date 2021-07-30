// CLAS12-Analysis imports
import org.jlab.analysis.*

/**
* Setup/customize your analysis here.
*
* @version 1.0
* @author  Matthew McEneaney
*/

// Initialize variables 
def analysis = new Analysis();
// def eta = (Constants constants, ArrayList<Integer> decay, ArrayList<DecayProduct> list, DecayProduct beam) -> {
        //TODO collect pz and p
		// return Math.atanh(pz/p) };

def colinearity = (Constants constants, ArrayList<Integer> decay, ArrayList<DecayProduct> list, DecayProduct beam) -> {
        DecayProduct proton, pion
        for (DecayProduct el : list) {
            if (el.pid()==2212) proton = el
            else if (el.pid()==-211) pion = el
        }
        // Get Transverse momentum relative to parent momentum
        double pT = (double) 0.0;
        for (LorentzVector lv : lvList) { pT += lv_parent.vect().cross(lv.vect()).mag()/lv_parent.vect().mag(); } // for some reason using lvUnit.unit normalizes the parent vector
        pT = pT / lvList.size();
        def colinear = 0
		return colinear };

// def cut = (double eta) -> { return (eta > 0.0 ? true : false) }

// Setup analysis
analysis.setAddVertices(true)
analysis.setAddAngles(true)
analysis.setInPath("~/drop/skim*.hipo")
analysis.setOutPath("~/out_skim_new_today.root")
analysis.addVar("colinearity",colinearity)
// analysis.setInPath("/path/to/in.hipo")
// analysis.setOutPath("/path/to/out.root")
// analysis.setDecay([22,22,211,-211])
// analysis.setQAMethod("OkForAsymmetry")
// analysis.addVar("eta",eta)
// analysis.addCut("eta",cut)

// Run analysis
analysis.processFiles()
