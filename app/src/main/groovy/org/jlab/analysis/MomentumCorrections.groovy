
package org.jlab.analysis;

// Groovy Imports
import groovy.transform.CompileStatic;

/**
* Implements Andrey Kim (<a href = "mailto: andrey.kim@uconn.edu">andrey.kim@uconn.edu</a>)
* and Nicholaus Trotta's (<a href = "mailto: nicholaus.trotta@uconn.edu">nicholaus.trotta@uconn.edu</a>)
* momentum corrections: version 5/19/2025.
*
* @version 1.0
* @author  Matthew McEneaney
*/

@CompileStatic
public class MomentumCorrections {

    public String  _dataset;
    public int     _pass;
    public boolean _outbending;

    public MomentumCorrections() {
        this._dataset = "Fall2018";
        this._pass = 1;
        this._outbending = false;
    }

    public MomentumCorrections(String dataset, int pass, boolean outbending) {
        this._dataset = dataset;
        this._pass = pass;
        this._outbending = outbending;
    }

    /**
    * Compute pi+ absolute energy loss.
    * @param pion_p
    * @param pip_theta
    * @param pion_det
    * @param outbending
    * @return eloss_pip
    */
    protected double eloss_pip(double pion_p, double pip_theta, int pion_det, boolean outbending){
        // momentum loss correction for low momentum pions:
        // input: p = pion momentum in GeV, pip_theta = pion theta in degree, 
        //        pion_det = pion detector (2 = FD, 3 = CD),  outbending = torus polarity
        // output: dp_pion = generated momentum - reconstructed momentum = momentum loss (+) / gain (-)

        double dp_pion = 0.0;

        if(outbending == false){ // INBENDING
            if(pion_det == 2){   // Forward Detector
                if(pip_theta < 27){                                       dp_pion =  0.00342646 + (-0.00282934) *pion_p + (0.00205983)   *Math.pow((double)pion_p,2) + (-0.00043158)  *Math.pow((double)pion_p,3) + (0) *Math.pow((double)pion_p,4);}
                if(pip_theta < 27 && pion_p >= 2.5){                      dp_pion =  0.00342646 + (-0.00282934) *2.5    + (0.00205983)   *Math.pow((double)2.5,2)    + (-0.00043158)  *Math.pow((double)2.5,3)    + (0) *Math.pow((double)2.5,4);}
                if(pip_theta > 27 && pip_theta < 28){                     dp_pion =  0.00328565 + (-0.00376042) *pion_p + (0.00433886)   *Math.pow((double)pion_p,2) + (-0.00141614)  *Math.pow((double)pion_p,3) + (0) *Math.pow((double)pion_p,4);}
                if(pip_theta > 27 && pip_theta < 28 && pion_p >= 1.83){   dp_pion =  0.00328565 + (-0.00376042) *1.83   + (0.00433886)   *Math.pow((double)1.83,2)   + (-0.00141614)  *Math.pow((double)1.83,3)   + (0) *Math.pow((double)1.83,4);}
                if(pip_theta > 28 && pip_theta < 29){                     dp_pion =  0.00328579 + (-0.00281121) *pion_p + (0.00342749)   *Math.pow((double)pion_p,2) + (-0.000932614) *Math.pow((double)pion_p,3) + (0) *Math.pow((double)pion_p,4);}
                if(pip_theta > 28 && pip_theta < 29 && pion_p >= 2){      dp_pion =  0.00328579 + (-0.00281121) *2      + (0.00342749)   *Math.pow((double)2,2)      + (-0.000932614) *Math.pow((double)2,3)      + (0) *Math.pow((double)2,4);}
                if(pip_theta > 29 && pip_theta < 30){                     dp_pion =  0.00167358 + (0.00441871)  *pion_p + (-0.000834667) *Math.pow((double)pion_p,2) + (-0.000137968) *Math.pow((double)pion_p,3) + (0) *Math.pow((double)pion_p,4);}
                if(pip_theta > 29 && pip_theta < 30 && pion_p >= 1.9){    dp_pion =  0.00167358 + (0.00441871)  *1.9    + (-0.000834667) *Math.pow((double)1.9,2)    + (-0.000137968) *Math.pow((double)1.9,3)    + (0) *Math.pow((double)1.9,4);}
                if(pip_theta > 30 && pip_theta < 31){                     dp_pion =  0.00274159 + (0.00635686)  *pion_p + (-0.00380977)  *Math.pow((double)pion_p,2) + (0.00071627)   *Math.pow((double)pion_p,3) + (0) *Math.pow((double)pion_p,4);}
                if(pip_theta > 30 && pip_theta < 31 && pion_p >= 1.9){    dp_pion =  0.00274159 + (0.00635686)  *1.9    + (-0.00380977)  *Math.pow((double)1.9,2)    + (0.00071627)   *Math.pow((double)1.9,3)    + (0) *Math.pow((double)1.9,4);}
                if(pip_theta > 31 && pip_theta < 32){                     dp_pion =  0.00450241 + (0.00248969)  *pion_p + (-0.00336795)  *Math.pow((double)pion_p,2) + (0.00111193)   *Math.pow((double)pion_p,3) + (0) *Math.pow((double)pion_p,4);}
                if(pip_theta > 31 && pip_theta < 32 && pion_p >= 1.8){    dp_pion =  0.00450241 + (0.00248969)  *1.8    + (-0.00336795)  *Math.pow((double)1.8,2)    + (0.00111193)   *Math.pow((double)1.8,3)    + (0) *Math.pow((double)1.8,4);}
                if(pip_theta > 32 && pip_theta < 33){                     dp_pion =  0.00505593 + (-0.00246203) *pion_p + (0.00172984)   *Math.pow((double)pion_p,2) + (-0.000406701) *Math.pow((double)pion_p,3) + (0) *Math.pow((double)pion_p,4);}
                if(pip_theta > 32 && pip_theta < 33 && pion_p >= 1.8){    dp_pion =  0.00505593 + (-0.00246203) *1.8    + (0.00172984)   *Math.pow((double)1.8,2)    + (-0.000406701) *Math.pow((double)1.8,3)    + (0) *Math.pow((double)1.8,4);}
                if(pip_theta > 33 && pip_theta < 34){                     dp_pion =  0.00273402 + (0.00440449)  *pion_p + (-0.00373488)  *Math.pow((double)pion_p,2) + (0.000996612)  *Math.pow((double)pion_p,3) + (0) *Math.pow((double)pion_p,4);}
                if(pip_theta > 33 && pip_theta < 34 && pion_p >= 1.8){    dp_pion =  0.00273402 + (0.00440449)  *1.8    + (-0.00373488)  *Math.pow((double)1.8,2)    + (0.000996612)  *Math.pow((double)1.8,3)    + (0) *Math.pow((double)1.8,4);}
                if(pip_theta > 34 && pip_theta < 35){                     dp_pion =  0.00333542 + (0.00439874)  *pion_p + (-0.00397776)  *Math.pow((double)pion_p,2) + (0.00105586)   *Math.pow((double)pion_p,3) + (0) *Math.pow((double)pion_p,4);}
                if(pip_theta > 34 && pip_theta < 35 && pion_p >= 1.8){    dp_pion =  0.00333542 + (0.00439874)  *1.8    + (-0.00397776)  *Math.pow((double)1.8,2)    + (0.00105586)   *Math.pow((double)1.8,3)    + (0) *Math.pow((double)1.8,4);}
                if(pip_theta > 35 && pip_theta < 36){                     dp_pion =  0.00354663 + (0.00565397)  *pion_p + (-0.00513503)  *Math.pow((double)pion_p,2) + (0.00153346)   *Math.pow((double)pion_p,3) + (0) *Math.pow((double)pion_p,4);}
                if(pip_theta > 35 && pip_theta < 36 && pion_p >= 1.8){    dp_pion =  0.00354663 + (0.00565397)  *1.8    + (-0.00513503)  *Math.pow((double)1.8,2)    + (0.00153346)   *Math.pow((double)1.8,3)    + (0) *Math.pow((double)1.8,4);}
                if(pip_theta > 36 && pip_theta < 37){                     dp_pion =  0.00333909 + (0.00842367)  *pion_p + (-0.0077321)   *Math.pow((double)pion_p,2) + (0.0022489)    *Math.pow((double)pion_p,3) + (0) *Math.pow((double)pion_p,4);}
                if(pip_theta > 36 && pip_theta < 37 && pion_p >= 1.8){    dp_pion =  0.00333909 + (0.00842367)  *1.8    + (-0.0077321)   *Math.pow((double)1.8,2)    + (0.0022489)    *Math.pow((double)1.8,3)    + (0) *Math.pow((double)1.8,4);}
                if(pip_theta > 37 && pip_theta < 38){                     dp_pion =  0.00358828 + (0.0112108)   *pion_p + (-0.0133854)   *Math.pow((double)pion_p,2) + (0.00486924)   *Math.pow((double)pion_p,3) + (0) *Math.pow((double)pion_p,4);}
                if(pip_theta > 37 && pip_theta < 38 && pion_p >= 1.4){    dp_pion =  0.00358828 + (0.0112108)   *1.4    + (-0.0133854)   *Math.pow((double)1.4,2)    + (0.00486924)   *Math.pow((double)1.4,3)    + (0) *Math.pow((double)1.4,4);}
                if(pip_theta > 38 && pip_theta < 39){                     dp_pion =  0.00354343 + (0.0117121)   *pion_p + (-0.0129649)   *Math.pow((double)pion_p,2) + (0.00455602)   *Math.pow((double)pion_p,3) + (0) *Math.pow((double)pion_p,4);}
                if(pip_theta > 38 && pip_theta < 39 && pion_p >= 1.3){    dp_pion =  0.00354343 + (0.0117121)   *1.3    + (-0.0129649)   *Math.pow((double)1.3,2)    + (0.00455602)   *Math.pow((double)1.3,3)    + (0) *Math.pow((double)1.3,4);}
                if(pip_theta > 39 && pip_theta < 40){                     dp_pion = -0.00194951 + (0.0409713)   *pion_p + (-0.0595861)   *Math.pow((double)pion_p,2) + (0.0281588)    *Math.pow((double)pion_p,3) + (0) *Math.pow((double)pion_p,4);}
                if(pip_theta > 39 && pip_theta < 40 && pion_p >= 0.9){    dp_pion = -0.00194951 + (0.0409713)   *0.9    + (-0.0595861)   *Math.pow((double)0.9,2)    + (0.0281588)    *Math.pow((double)0.9,3)    + (0) *Math.pow((double)0.9,4);}
                if(pip_theta > 40 && pip_theta < 41){                     dp_pion = -0.0099217  + (0.0808096)   *pion_p + (-0.119836)    *Math.pow((double)pion_p,2) + (0.0559553)    *Math.pow((double)pion_p,3) + (0) *Math.pow((double)pion_p,4);}
                if(pip_theta > 40 && pip_theta < 41 && pion_p >= 0.75){   dp_pion = -0.0099217  + (0.0808096)   *0.75   + (-0.119836)    *Math.pow((double)0.75,2)   + (0.0559553)    *Math.pow((double)0.75,3)   + (0) *Math.pow((double)0.75,4);}
                if(pip_theta > 41 && pip_theta < 42){                     dp_pion =  0.00854898 + (0.00025037)  *pion_p + (-0.0113992)   *Math.pow((double)pion_p,2) + (0.0145178)    *Math.pow((double)pion_p,3) + (0) *Math.pow((double)pion_p,4);}
                if(pip_theta > 41 && pip_theta < 42 && pion_p >= 0.65){   dp_pion =  0.00854898 + (0.00025037)  *0.65   + (-0.0113992)   *Math.pow((double)0.65,2)   + (0.0145178)    *Math.pow((double)0.65,3)   + (0) *Math.pow((double)0.65,4);}
                if(pip_theta > 42){                                       dp_pion =  0.00564818 + (0.00706606)  *pion_p + (0.0042602)    *Math.pow((double)pion_p,2) + (-0.01141)     *Math.pow((double)pion_p,3) + (0) *Math.pow((double)pion_p,4);}
                if(pip_theta > 42 && pion_p >= 0.65){                     dp_pion =  0.00564818 + (0.00706606)  *0.65   + (0.0042602)    *Math.pow((double)0.65,2)   + (-0.01141)     *Math.pow((double)0.65,3)   + (0) *Math.pow((double)0.65,4);}
            }
            if(pion_det == 3){  // Central Detector
                if(pip_theta < 39){                                       dp_pion = -0.045      + (-0.102652) + (0.455589) *pion_p + (-0.671635)   *Math.pow((double)pion_p,2) + (0.303814)   *Math.pow((double)pion_p,3);}
                if(pip_theta < 39  && pion_p >= 0.7){                     dp_pion = -0.045      + (-0.102652) + (0.455589) *0.7    + (-0.671635)   *Math.pow((double)0.7,2)    + (0.303814)   *Math.pow((double)0.7,3);}
                if(pip_theta > 39  && pip_theta < 40){                    dp_pion =  0.0684552  + (-0.766492)              *pion_p + (1.73092)     *Math.pow((double)pion_p,2) + (-1.46215)   *Math.pow((double)pion_p,3) + (0.420127) *Math.pow((double)pion_p,4);}
                if(pip_theta > 39  && pip_theta < 40 && pion_p >= 1.4){   dp_pion =  0.0684552  + (-0.766492)              *1.4    + (1.73092)     *Math.pow((double)1.4,2)    + (-1.46215)   *Math.pow((double)1.4,3)    + (0.420127) *Math.pow((double)1.4,4);}
                if(pip_theta > 40  && pip_theta < 41){                    dp_pion =  0.751549   + (-7.4593)                *pion_p + (26.8037)     *Math.pow((double)pion_p,2) + (-47.1576)   *Math.pow((double)pion_p,3) + (43.8527)  *Math.pow((double)pion_p,4) + (-20.7039) *Math.pow((double)pion_p,5) + (3.90931)  *Math.pow((double)pion_p,6);}
                if(pip_theta > 40  && pip_theta < 41 && pion_p >= 1.45){  dp_pion =  0.751549   + (-7.4593)                *1.45   + (26.8037)     *Math.pow((double)1.45,2)   + (-47.1576)   *Math.pow((double)1.45,3)   + (43.8527)  *Math.pow((double)1.45,4)   + (-20.7039) *Math.pow((double)1.45,5)   + (3.90931)  *Math.pow((double)1.45,6);}
                if(pip_theta > 41  && pip_theta < 42){                    dp_pion = -1.35043    + (10.0788)                *pion_p + (-30.4829)    *Math.pow((double)pion_p,2) + (47.7792)    *Math.pow((double)pion_p,3) + (-40.996)  *Math.pow((double)pion_p,4) + (18.2662)  *Math.pow((double)pion_p,5) + (-3.30449) *Math.pow((double)pion_p,6);}
                if(pip_theta > 41  && pip_theta < 42 && pion_p >= 1.2){   dp_pion = -1.35043    + (10.0788)                *1.2    + (-30.4829)    *Math.pow((double)1.2,2)    + (47.7792)    *Math.pow((double)1.2,3)    + (-40.996)  *Math.pow((double)1.2,4)    + (18.2662)  *Math.pow((double)1.2,5)    + (-3.30449) *Math.pow((double)1.2,6);}
                if(pip_theta > 42  && pip_theta < 43){                    dp_pion = -0.0231195  + (0.0744589)              *pion_p + (-0.0807029)  *Math.pow((double)pion_p,2) + (0.0264266)  *Math.pow((double)pion_p,3) + (0)        *Math.pow((double)pion_p,4);}
                if(pip_theta > 42  && pip_theta < 43 && pion_p >= 1.3){   dp_pion = -0.0231195  + (0.0744589)              *1.3    + (-0.0807029)  *Math.pow((double)1.3,2)    + (0.0264266)  *Math.pow((double)1.3,3)    + (0)        *Math.pow((double)1.3,4);}
                if(pip_theta > 43  && pip_theta < 44){                    dp_pion = -0.00979928 + (0.0351043)              *pion_p + (-0.0365865)  *Math.pow((double)pion_p,2) + (0.00977218) *Math.pow((double)pion_p,3) + (0)        *Math.pow((double)pion_p,4);}
                if(pip_theta > 43  && pip_theta < 44 && pion_p >= 1.1){   dp_pion = -0.00979928 + (0.0351043)              *1.1    + (-0.0365865)  *Math.pow((double)1.1,2)    + (0.00977218) *Math.pow((double)1.1,3)    + (0)        *Math.pow((double)1.1,4);}
                if(pip_theta > 44  && pip_theta < 45){                    dp_pion =  0.00108491 + (-0.00924885)            *pion_p + (0.0216431)   *Math.pow((double)pion_p,2) + (-0.0137762) *Math.pow((double)pion_p,3) + (0)        *Math.pow((double)pion_p,4);}
                if(pip_theta > 44  && pip_theta < 45 && pion_p >= 1.1){   dp_pion =  0.00108491 + (-0.00924885)            *1.1    + (0.0216431)   *Math.pow((double)1.1,2)    + (-0.0137762) *Math.pow((double)1.1,3)    + (0)        *Math.pow((double)1.1,4);}
                if(pip_theta > 45  && pip_theta < 55){                    dp_pion =  0.0092263  + (-0.0676178)             *pion_p + (0.168778)    *Math.pow((double)pion_p,2) + (-0.167463)  *Math.pow((double)pion_p,3) + (0.05661)  *Math.pow((double)pion_p,4);}
                if(pip_theta > 45  && pip_theta < 55 && pion_p >= 1.3){   dp_pion =  0.0092263  + (-0.0676178)             *1.3    + (0.168778)    *Math.pow((double)1.3,2)    + (-0.167463)  *Math.pow((double)1.3,3)    + (0.05661)  *Math.pow((double)1.3,4);}
                if(pip_theta > 55  && pip_theta < 65){                    dp_pion =  0.00805642 + (-0.0670962)             *pion_p + (0.188536)    *Math.pow((double)pion_p,2) + (-0.20571)   *Math.pow((double)pion_p,3) + (0.0765)   *Math.pow((double)pion_p,4);}
                if(pip_theta > 55  && pip_theta < 65 && pion_p >= 1.05){  dp_pion =  0.00805642 + (-0.0670962)             *1.05   + (0.188536)    *Math.pow((double)1.05,2)   + (-0.20571)   *Math.pow((double)1.05,3)   + (0.0765)   *Math.pow((double)1.05,4);}
                if(pip_theta > 65  && pip_theta < 75){                    dp_pion =  0.00312202 + (-0.0269717)             *pion_p + (0.0715236)   *Math.pow((double)pion_p,2) + (-0.0545622) *Math.pow((double)pion_p,3) + (0)        *Math.pow((double)pion_p,4);}
                if(pip_theta > 65  && pip_theta < 75 && pion_p >= 0.75){  dp_pion =  0.00312202 + (-0.0269717)             *0.75   + (0.0715236)   *Math.pow((double)0.75,2)   + (-0.0545622) *Math.pow((double)0.75,3)   + (0)        *Math.pow((double)0.75,4);}
                if(pip_theta > 75  && pip_theta < 85){                    dp_pion =  0.00424971 + (-0.0367683)             *pion_p + (0.10417)     *Math.pow((double)pion_p,2) + (-0.0899651) *Math.pow((double)pion_p,3) + (0)        *Math.pow((double)pion_p,4);}
                if(pip_theta > 75  && pip_theta < 85 && pion_p >= 0.65){  dp_pion =  0.00424971 + (-0.0367683)             *0.65   + (0.10417)     *Math.pow((double)0.65,2)   + (-0.0899651) *Math.pow((double)0.65,3)   + (0)        *Math.pow((double)0.65,4);}
                if(pip_theta > 85  && pip_theta < 95){                    dp_pion =  0.00654123 + (-0.0517915)             *pion_p + (0.147888)    *Math.pow((double)pion_p,2) + (-0.14253)   *Math.pow((double)pion_p,3) + (0)        *Math.pow((double)pion_p,4);}
                if(pip_theta > 85  && pip_theta < 95 && pion_p >= 0.5){   dp_pion =  0.00654123 + (-0.0517915)             *0.5    + (0.147888)    *Math.pow((double)0.5,2)    + (-0.14253)   *Math.pow((double)0.5,3)    + (0)        *Math.pow((double)0.5,4);}
                if(pip_theta > 95  && pip_theta < 105){                   dp_pion = -0.00111721 + (0.00478119)             *pion_p + (0.0158753)   *Math.pow((double)pion_p,2) + (-0.052902)  *Math.pow((double)pion_p,3) + (0)        *Math.pow((double)pion_p,4);}
                if(pip_theta > 95  && pip_theta < 105 && pion_p >= 0.45){ dp_pion = -0.00111721 + (0.00478119)             *0.45   + (0.0158753)   *Math.pow((double)0.45,2)   + (-0.052902)  *Math.pow((double)0.45,3)   + (0)        *Math.pow((double)0.45,4);}
                if(pip_theta > 105 && pip_theta < 115){                   dp_pion = -0.00239839 + (0.00790738)             *pion_p + (0.0311713)   *Math.pow((double)pion_p,2) + (-0.104157)  *Math.pow((double)pion_p,3) + (0)        *Math.pow((double)pion_p,4);}
                if(pip_theta > 105 && pip_theta < 115 && pion_p >= 0.35){ dp_pion = -0.00239839 + (0.00790738)             *0.35   + (0.0311713)   *Math.pow((double)0.35,2)   + (-0.104157)  *Math.pow((double)0.35,3)   + (0)        *Math.pow((double)0.35,4);}
                if(pip_theta > 115 && pip_theta < 125){                   dp_pion = -0.00778793 + (0.0256774)              *pion_p + (0.0932503)   *Math.pow((double)pion_p,2) + (-0.32771)   *Math.pow((double)pion_p,3) + (0)        *Math.pow((double)pion_p,4);}
                if(pip_theta > 115 && pip_theta < 125 && pion_p >= 0.35){ dp_pion = -0.00778793 + (0.0256774)              *0.35   + (0.0932503)   *Math.pow((double)0.35,2)   + (-0.32771)   *Math.pow((double)0.35,3)   + (0)        *Math.pow((double)0.35,4);}
                if(pip_theta > 125 && pip_theta < 135){                   dp_pion = -0.00292778 + (-0.00536697)            *pion_p + (-0.00414351) *Math.pow((double)pion_p,2) + (0.0196431)  *Math.pow((double)pion_p,3) + (0)        *Math.pow((double)pion_p,4);}
                if(pip_theta > 125 && pip_theta < 135 && pion_p >= 0.35){ dp_pion = -0.00292778 + (-0.00536697)            *0.35   + (-0.00414351) *Math.pow((double)0.35,2)   + (0.0196431)  *Math.pow((double)0.35,3)   + (0)        *Math.pow((double)0.35,4);}
            }
        }
        if(outbending == true){ // OUTBENDING
            if(pion_det == 2){  // Forward Detector
                if(pip_theta < 27){                                       dp_pion = 0.00389945  + (-0.004062)    *pion_p + (0.00321842)  *Math.pow((double)pion_p,2) + (-0.000698299) *Math.pow((double)pion_p,3) + (0)          *Math.pow((double)pion_p,4);}
                if(pip_theta < 27 && pion_p >= 2.3){                      dp_pion = 0.00389945  + (-0.004062)    *2.3    + (0.00321842)  *Math.pow((double)2.3,2)    + (-0.000698299) *Math.pow((double)2.3,3)    + (0)          *Math.pow((double)2.3,4);}
                if(pip_theta > 27 && pip_theta < 28){                     dp_pion = 0.00727132  + (-0.0117989)   *pion_p + (0.00962999)  *Math.pow((double)pion_p,2) + (-0.00267005)  *Math.pow((double)pion_p,3) + (0)          *Math.pow((double)pion_p,4);}
                if(pip_theta > 27 && pip_theta < 28 && pion_p >= 1.7){    dp_pion = 0.00727132  + (-0.0117989)   *1.7    + (0.00962999)  *Math.pow((double)1.7,2)    + (-0.00267005)  *Math.pow((double)1.7,3)    + (0)          *Math.pow((double)1.7,4);}
                if(pip_theta > 28 && pip_theta < 29){                     dp_pion = 0.00844551  + (-0.0128097)   *pion_p + (0.00945956)  *Math.pow((double)pion_p,2) + (-0.00237992)  *Math.pow((double)pion_p,3) + (0)          *Math.pow((double)pion_p,4);}
                if(pip_theta > 28 && pip_theta < 29 && pion_p >= 2){      dp_pion = 0.00844551  + (-0.0128097)   *2      + (0.00945956)  *Math.pow((double)2,2)      + (-0.00237992)  *Math.pow((double)2,3)      + (0)          *Math.pow((double)2,4);}
                if(pip_theta > 29 && pip_theta < 30){                     dp_pion = 0.00959007  + (-0.0139218)   *pion_p + (0.0122966)   *Math.pow((double)pion_p,2) + (-0.0034012)   *Math.pow((double)pion_p,3) + (0)          *Math.pow((double)pion_p,4);}
                if(pip_theta > 29 && pip_theta < 30 && pion_p >= 1.9){    dp_pion = 0.00959007  + (-0.0139218)   *1.9    + (0.0122966)   *Math.pow((double)1.9,2)    + (-0.0034012)   *Math.pow((double)1.9,3)    + (0)          *Math.pow((double)1.9,4);}
                if(pip_theta > 30 && pip_theta < 31){                     dp_pion = 0.00542816  + (-5.10739e-05) *pion_p + (0.000572038) *Math.pow((double)pion_p,2) + (-0.000488883) *Math.pow((double)pion_p,3) + (0)          *Math.pow((double)pion_p,4);}
                if(pip_theta > 30 && pip_theta < 31 && pion_p >= 1.9){    dp_pion = 0.00542816  + (-5.10739e-05) *1.9    + (0.000572038) *Math.pow((double)1.9,2)    + (-0.000488883) *Math.pow((double)1.9,3)    + (0)          *Math.pow((double)1.9,4);}
                if(pip_theta > 31 && pip_theta < 32){                     dp_pion = 0.0060391   + (-0.000516936) *pion_p + (-0.00286595) *Math.pow((double)pion_p,2) + (0.00136604)   *Math.pow((double)pion_p,3) + (0)          *Math.pow((double)pion_p,4);}
                if(pip_theta > 31 && pip_theta < 32 && pion_p >= 1.8){    dp_pion = 0.0060391   + (-0.000516936) *1.8    + (-0.00286595) *Math.pow((double)1.8,2)    + (0.00136604)   *Math.pow((double)1.8,3)    + (0)          *Math.pow((double)1.8,4);}
                if(pip_theta > 32 && pip_theta < 33){                     dp_pion = 0.0140305   + (-0.0285832)   *pion_p + (0.0248799)   *Math.pow((double)pion_p,2) + (-0.00701311)  *Math.pow((double)pion_p,3) + (0)          *Math.pow((double)pion_p,4);}
                if(pip_theta > 32 && pip_theta < 33 && pion_p >= 1.6){    dp_pion = 0.0140305   + (-0.0285832)   *1.6    + (0.0248799)   *Math.pow((double)1.6,2)    + (-0.00701311)  *Math.pow((double)1.6,3)    + (0)          *Math.pow((double)1.6,4);}
                if(pip_theta > 33 && pip_theta < 34){                     dp_pion = 0.010815    + (-0.0194244)   *pion_p + (0.0174474)   *Math.pow((double)pion_p,2) + (-0.0049764)   *Math.pow((double)pion_p,3) + (0)          *Math.pow((double)pion_p,4);}
                if(pip_theta > 33 && pip_theta < 34 && pion_p >= 1.5){    dp_pion = 0.010815    + (-0.0194244)   *1.5    + (0.0174474)   *Math.pow((double)1.5,2)    + (-0.0049764)   *Math.pow((double)1.5,3)    + (0)          *Math.pow((double)1.5,4);}
                if(pip_theta > 34 && pip_theta < 35){                     dp_pion = 0.0105522   + (-0.0176248)   *pion_p + (0.0161142)   *Math.pow((double)pion_p,2) + (-0.00472288)  *Math.pow((double)pion_p,3) + (0)          *Math.pow((double)pion_p,4);}
                if(pip_theta > 34 && pip_theta < 35 && pion_p >= 1.6){    dp_pion = 0.0105522   + (-0.0176248)   *1.6    + (0.0161142)   *Math.pow((double)1.6,2)    + (-0.00472288)  *Math.pow((double)1.6,3)    + (0)          *Math.pow((double)1.6,4);}
                if(pip_theta > 35 && pip_theta < 36){                     dp_pion = 0.0103938   + (-0.0164003)   *pion_p + (0.0164045)   *Math.pow((double)pion_p,2) + (-0.00517012)  *Math.pow((double)pion_p,3) + (0)          *Math.pow((double)pion_p,4);}
                if(pip_theta > 35 && pip_theta < 36 && pion_p >= 1.5){    dp_pion = 0.0103938   + (-0.0164003)   *1.5    + (0.0164045)   *Math.pow((double)1.5,2)    + (-0.00517012)  *Math.pow((double)1.5,3)    + (0)          *Math.pow((double)1.5,4);}
                if(pip_theta > 36 && pip_theta < 37){                     dp_pion = 0.0441471   + (-0.183937)    *pion_p + (0.338784)    *Math.pow((double)pion_p,2) + (-0.298985)    *Math.pow((double)pion_p,3) + (0.126905)   *Math.pow((double)pion_p,4) + (-0.0208286) *Math.pow((double)pion_p,5);}
                if(pip_theta > 36 && pip_theta < 37 && pion_p >= 1.8){    dp_pion = 0.0441471   + (-0.183937)    *1.8    + (0.338784)    *Math.pow((double)1.8,2)    + (-0.298985)    *Math.pow((double)1.8,3)    + (0.126905)   *Math.pow((double)1.8,4)    + (-0.0208286) *Math.pow((double)1.8,5);}
                if(pip_theta > 37 && pip_theta < 38){                     dp_pion = 0.0726119   + (-0.345004)    *pion_p + (0.697789)    *Math.pow((double)pion_p,2) + (-0.685948)    *Math.pow((double)pion_p,3) + (0.327195)   *Math.pow((double)pion_p,4) + (-0.0605621) *Math.pow((double)pion_p,5);}
                if(pip_theta > 37 && pip_theta < 38 && pion_p >= 1.7){    dp_pion = 0.0726119   + (-0.345004)    *1.7    + (0.697789)    *Math.pow((double)1.7,2)    + (-0.685948)    *Math.pow((double)1.7,3)    + (0.327195)   *Math.pow((double)1.7,4)    + (-0.0605621) *Math.pow((double)1.7,5);}
                if(pip_theta > 38 && pip_theta < 39){                     dp_pion = 0.0247648   + (-0.0797376)   *pion_p + (0.126535)    *Math.pow((double)pion_p,2) + (-0.086545)    *Math.pow((double)pion_p,3) + (0.0219304)  *Math.pow((double)pion_p,4);}
                if(pip_theta > 38 && pip_theta < 39 && pion_p >= 1.6){    dp_pion = 0.0247648   + (-0.0797376)   *1.6    + (0.126535)    *Math.pow((double)1.6,2)    + (-0.086545)    *Math.pow((double)1.6,3)    + (0.0219304)  *Math.pow((double)1.6,4);}
                if(pip_theta > 39 && pip_theta < 40){                     dp_pion = 0.0208867   + (-0.0492068)   *pion_p + (0.0543187)   *Math.pow((double)pion_p,2) + (-0.0183393)   *Math.pow((double)pion_p,3) + (0)          *Math.pow((double)pion_p,4);}
                if(pip_theta > 39 && pip_theta < 40 && pion_p >= 1.2){    dp_pion = 0.0208867   + (-0.0492068)   *1.2    + (0.0543187)   *Math.pow((double)1.2,2)    + (-0.0183393)   *Math.pow((double)1.2,3)    + (0)          *Math.pow((double)1.2,4);}
                if(pip_theta > 40 && pip_theta < 41){                     dp_pion = 0.0148655   + (-0.0203483)   *pion_p + (0.00835867)  *Math.pow((double)pion_p,2) + (0.00697134)   *Math.pow((double)pion_p,3) + (0)          *Math.pow((double)pion_p,4);}
                if(pip_theta > 40 && pip_theta < 41 && pion_p >= 1.0){    dp_pion = 0.0148655   + (-0.0203483)   *1.0    + (0.00835867)  *Math.pow((double)1.0,2)    + (0.00697134)   *Math.pow((double)1.0,3)    + (0)          *Math.pow((double)1.0,4);}
                if(pip_theta > 41 && pip_theta < 42){                     dp_pion = 0.0223585   + (-0.0365262)   *pion_p + (-0.0150027)  *Math.pow((double)pion_p,2) + (0.0854164)    *Math.pow((double)pion_p,3) + (-0.0462718) *Math.pow((double)pion_p,4);}
                if(pip_theta > 41 && pip_theta < 42 && pion_p >= 0.7){    dp_pion = 0.007617;}
                if(pip_theta > 42){                                       dp_pion = 0.0152373   + (-0.0106377)   *pion_p + (-0.0257573)  *Math.pow((double)pion_p,2) + (0.0344851)    *Math.pow((double)pion_p,3) + (0)          *Math.pow((double)pion_p,4);}
                if(pip_theta > 42 && pion_p >= 0.75){                     dp_pion = 0.0152373   + (-0.0106377)   *0.75   + (-0.0257573)  *Math.pow((double)0.75,2)   + (0.0344851)    *Math.pow((double)0.75,3)   + (0)          *Math.pow((double)0.75,4);}
            }
            if(pion_det == 3){ // Central Detector
                if(pip_theta < 39){                                       dp_pion = -0.05        + (-0.0758897) + (0.362231) *pion_p + (-0.542404)   *Math.pow((double)pion_p,2) + (0.241344)   *Math.pow((double)pion_p,3);}
                if(pip_theta < 39  && pion_p >= 0.8){                     dp_pion = -0.05        + (-0.0758897) + (0.362231) *0.8    + (-0.542404)   *Math.pow((double)0.8,2)    + (0.241344)   *Math.pow((double)0.8,3);}
                if(pip_theta > 39  && pip_theta < 40){                    dp_pion =  0.0355259   + (-0.589712)               *pion_p + (1.4206)      *Math.pow((double)pion_p,2) + (-1.24179)   *Math.pow((double)pion_p,3) + (0.365524)  *Math.pow((double)pion_p,4);}
                if(pip_theta > 39  && pip_theta < 40  && pion_p >= 1.35){ dp_pion =  0.0355259   + (-0.589712)               *1.35   + (1.4206)      *Math.pow((double)1.35,2)   + (-1.24179)   *Math.pow((double)1.35,3)   + (0.365524)  *Math.pow((double)1.35,4);}
                if(pip_theta > 40  && pip_theta < 41){                    dp_pion = -0.252336    + (1.02032)                 *pion_p + (-1.51461)    *Math.pow((double)pion_p,2) + (0.967772)   *Math.pow((double)pion_p,3) + (-0.226028) *Math.pow((double)pion_p,4);}
                if(pip_theta > 40  && pip_theta < 41  && pion_p >= 1.4){  dp_pion = -0.252336    + (1.02032)                 *1.4    + (-1.51461)    *Math.pow((double)1.4,2)    + (0.967772)   *Math.pow((double)1.4,3)    + (-0.226028) *Math.pow((double)1.4,4);}
                if(pip_theta > 41  && pip_theta < 42){                    dp_pion = -0.710129    + (4.49613)                 *pion_p + (-11.01)      *Math.pow((double)pion_p,2) + (12.9945)    *Math.pow((double)pion_p,3) + (-7.41641)  *Math.pow((double)pion_p,4) + (1.63923)   *Math.pow((double)pion_p,5);}
                if(pip_theta > 41  && pip_theta < 42  && pion_p >= 1.2){  dp_pion = -0.710129    + (4.49613)                 *1.2    + (-11.01)      *Math.pow((double)1.2,2)    + (12.9945)    *Math.pow((double)1.2,3)    + (-7.41641)  *Math.pow((double)1.2,4)    + (1.63923)   *Math.pow((double)1.2,5);}
                if(pip_theta > 42  && pip_theta < 43){                    dp_pion = -0.0254912   + (0.0851432)               *pion_p + (-0.0968583)  *Math.pow((double)pion_p,2) + (0.0350334)  *Math.pow((double)pion_p,3) + (0)         *Math.pow((double)pion_p,4);}
                if(pip_theta > 42  && pip_theta < 43  && pion_p >= 1.2){  dp_pion = -0.0254912   + (0.0851432)               *1.2    + (-0.0968583)  *Math.pow((double)1.2,2)    + (0.0350334)  *Math.pow((double)1.2,3)    + (0)         *Math.pow((double)1.2,4);}
                if(pip_theta > 43  && pip_theta < 44){                    dp_pion = -0.0115965   + (0.0438726)               *pion_p + (-0.0500474)  *Math.pow((double)pion_p,2) + (0.0163627)  *Math.pow((double)pion_p,3) + (0)         *Math.pow((double)pion_p,4);}
                if(pip_theta > 43  && pip_theta < 44  && pion_p >= 1.4){  dp_pion = -0.0115965   + (0.0438726)               *1.4    + (-0.0500474)  *Math.pow((double)1.4,2)    + (0.0163627)  *Math.pow((double)1.4,3)    + (0)         *Math.pow((double)1.4,4);}
                if(pip_theta > 44  && pip_theta < 45){                    dp_pion =  0.00273414  + (-0.01851)                *pion_p + (0.0377032)   *Math.pow((double)pion_p,2) + (-0.0226696) *Math.pow((double)pion_p,3) + (0)         *Math.pow((double)pion_p,4);}
                if(pip_theta > 44  && pip_theta < 45  && pion_p >= 1){    dp_pion =  0.00273414  + (-0.01851)                *1      + (0.0377032)   *Math.pow((double)1,2)      + (-0.0226696) *Math.pow((double)1,3)      + (0)         *Math.pow((double)1,4);}
                if(pip_theta > 45  && pip_theta < 55){                    dp_pion =  0.0271952   + (-0.25981)                *pion_p + (0.960051)    *Math.pow((double)pion_p,2) + (-1.76651)   *Math.pow((double)pion_p,3) + (1.72872)   *Math.pow((double)pion_p,4) + (-0.856946) *Math.pow((double)pion_p,5) + (0.167564) *Math.pow((double)pion_p,6);}
                if(pip_theta > 45  && pip_theta < 55  && pion_p >= 1.4){  dp_pion =  0.0271952   + (-0.25981)                *1.4    + (0.960051)    *Math.pow((double)1.4,2)    + (-1.76651)   *Math.pow((double)1.4,3)    + (1.72872)   *Math.pow((double)1.4,4)    + (-0.856946) *Math.pow((double)1.4,5)    + (0.167564) *Math.pow((double)1.4,6);}
                if(pip_theta > 55  && pip_theta < 65){                    dp_pion =  0.00734975  + (-0.0598841)              *pion_p + (0.161495)    *Math.pow((double)pion_p,2) + (-0.1629)    *Math.pow((double)pion_p,3) + (0.0530098) *Math.pow((double)pion_p,4);}
                if(pip_theta > 55  && pip_theta < 65  && pion_p >= 1.2){  dp_pion =  0.00734975  + (-0.0598841)              *1.2    + (0.161495)    *Math.pow((double)1.2,2)    + (-0.1629)    *Math.pow((double)1.2,3)    + (0.0530098) *Math.pow((double)1.2,4);}
                if(pip_theta > 65  && pip_theta < 75){                    dp_pion =  0.00321351  + (-0.0289322)              *pion_p + (0.0786484)   *Math.pow((double)pion_p,2) + (-0.0607041) *Math.pow((double)pion_p,3) + (0)         *Math.pow((double)pion_p,4);}
                if(pip_theta > 65  && pip_theta < 75  && pion_p >= 0.95){ dp_pion =  0.00321351  + (-0.0289322)              *0.95   + (0.0786484)   *Math.pow((double)0.95,2)   + (-0.0607041) *Math.pow((double)0.95,3)   + (0)         *Math.pow((double)0.95,4);}
                if(pip_theta > 75  && pip_theta < 85){                    dp_pion =  0.00644253  + (-0.0543896)              *pion_p + (0.148933)    *Math.pow((double)pion_p,2) + (-0.1256)    *Math.pow((double)pion_p,3) + (0)         *Math.pow((double)pion_p,4);}
                if(pip_theta > 75  && pip_theta < 85  && pion_p >= 0.7){  dp_pion =  0.00644253  + (-0.0543896)              *0.7    + (0.148933)    *Math.pow((double)0.7,2)    + (-0.1256)    *Math.pow((double)0.7,3)    + (0)         *Math.pow((double)0.7,4);}
                if(pip_theta > 85  && pip_theta < 95){                    dp_pion =  0.00671152  + (-0.0537269)              *pion_p + (0.154509)    *Math.pow((double)pion_p,2) + (-0.147667)  *Math.pow((double)pion_p,3) + (0)         *Math.pow((double)pion_p,4);}
                if(pip_theta > 85  && pip_theta < 95  && pion_p >= 0.65){ dp_pion =  0.00671152  + (-0.0537269)              *0.65   + (0.154509)    *Math.pow((double)0.65,2)   + (-0.147667)  *Math.pow((double)0.65,3)   + (0)         *Math.pow((double)0.65,4);}
                if(pip_theta > 95  && pip_theta < 105){                   dp_pion = -0.000709077 + (0.00331818)              *pion_p + (0.0109241)   *Math.pow((double)pion_p,2) + (-0.0351682) *Math.pow((double)pion_p,3) + (0)         *Math.pow((double)pion_p,4);}
                if(pip_theta > 95  && pip_theta < 105 && pion_p >= 0.45){ dp_pion = -0.000709077 + (0.00331818)              *0.45   + (0.0109241)   *Math.pow((double)0.45,2)   + (-0.0351682) *Math.pow((double)0.45,3)   + (0)         *Math.pow((double)0.45,4);}
                if(pip_theta > 105 && pip_theta < 115){                   dp_pion = -0.00260164  + (0.00846919)              *pion_p + (0.0315497)   *Math.pow((double)pion_p,2) + (-0.105756)  *Math.pow((double)pion_p,3) + (0)         *Math.pow((double)pion_p,4);}
                if(pip_theta > 105 && pip_theta < 115 && pion_p >= 0.45){ dp_pion = -0.00260164  + (0.00846919)              *0.45   + (0.0315497)   *Math.pow((double)0.45,2)   + (-0.105756)  *Math.pow((double)0.45,3)   + (0)         *Math.pow((double)0.45,4);}
                if(pip_theta > 115 && pip_theta < 125){                   dp_pion = -0.00544336  + (0.018256)                *pion_p + (0.0664618)   *Math.pow((double)pion_p,2) + (-0.240312)  *Math.pow((double)pion_p,3) + (0)         *Math.pow((double)pion_p,4);}
                if(pip_theta > 115 && pip_theta < 125 && pion_p >= 0.45){ dp_pion = -0.00544336  + (0.018256)                *0.45   + (0.0664618)   *Math.pow((double)0.45,2)   + (-0.240312)  *Math.pow((double)0.45,3)   + (0)         *Math.pow((double)0.45,4);}
                if(pip_theta > 125 && pip_theta < 135){                   dp_pion = -0.00281073  + (-0.00495863)             *pion_p + (-0.00362356) *Math.pow((double)pion_p,2) + (0.0178764)  *Math.pow((double)pion_p,3) + (0)         *Math.pow((double)pion_p,4);}
                if(pip_theta > 125 && pip_theta < 135 && pion_p >= 0.35){ dp_pion = -0.00281073  + (-0.00495863)             *0.35   + (-0.00362356) *Math.pow((double)0.35,2)   + (0.0178764)  *Math.pow((double)0.35,3)   + (0)         *Math.pow((double)0.35,4);}
            }
        }

        return dp_pion;
    }

    /**
    * Compute pi+ fractional energy loss.
    * @param pip
    * @param detector
    * @param outbending
    * @return f_pip_loss
    */
    protected double f_pip_loss(DecayProduct pip, int detector, boolean outbending) {
        double p_pip_loss = this.eloss_pip(pip.p(), pip.theta(), detector, outbending);
        double f_pip_loss = ((pip.p()+p_pip_loss)/pip.p());
        return f_pip_loss;
    }

    /**
    * Compute e-, pi+, pi-, or p fractional momentum correction for inbending data.
    * @param Px
    * @param Py
    * @param Pz
    * @param sec
    * @param ivvec
    * @param corEl
    * @param corPip
    * @param corPim
    * @param corPro
    * @return dpp
    */
    protected double dppC_inbending(float Px, float Py, float Pz, int sec, int ivec, int corEl, int corPip, int corPim, int corPro) { 
        
        // 'Px'/'Py'/'Pz'   ==> Corresponds to the Cartesian Components of the particle momentum being corrected
        // 'sec'            ==> Corresponds to the Forward Detector Sectors where the given particle is detected (6 total)
        // 'ivec'           ==> Corresponds to the particle being corrected (See below)    
            // (*) ivec = 0 --> Electron Corrections
            // (*) ivec = 1 --> Pi+ Corrections
            // (*) ivec = 2 --> Pi- Corrections
            // (*) ivec = 3 --> Proton Corrections
        // 'corEl'/'corPip'/'corPim'/'corPro' ==> Controls which version of the particle correction is used
            // Includes:
                // (*) Correction On/Off
                // (*) Pass Version
                // (*) Data Set (Fall 2018 or Spring 2019)
        // 'corEl'         ==> Controls the ELECTRON Corrections
            // corEl == 0  --> No Correction (Off)
            // corEl == 1  --> Fall  2018 - Pass 1
            // corEl == 2  --> Sping 2019 - Pass 2
            // corEl == 3  --> Fall  2018 - Pass 2
        // 'corPip'        ==> Controls the π+ PION Corrections
            // corPip == 0 --> No Correction
            // corPip == 1 --> Fall  2018 - Pass 1
            // corPip == 2 --> Sping 2019 - Pass 2
            // corPip == 3 --> Fall  2018 - Pass 2
        // 'corPim'        ==> Controls the π- PION Corrections
            // corPim == 0 --> No Correction
            // corPim == 1 --> Fall  2018 - Pass 1 (Created by Nick Trotta)
        // corPim == 2 --> Fall  2018 - Pass 2 (Created by Nick Trotta)
        // 'corPro'        ==> Controls the PROTON Corrections (Momentum)
            // corPro == 0 --> No Correction
            // corPro == 1 --> Fall  2018 - Pass 1

        // Momentum Magnitude
        double pp = Math.sqrt(Px*Px + Py*Py + Pz*Pz);

        // Initializing the correction factor
        double dp = 0;

        // Defining Phi Angle
        double Phi = (180/3.1415926)*Math.atan2(Py, Px);

        // Central Detector Corrections Not Included (Yet)

        // (Initial) Shift of the Phi Angle (done to realign sectors whose data is separated when plotted from ±180˚)
        if(((sec == 4 || sec == 3) && Phi < 0) || (sec > 4 && Phi < 90)){
            Phi += 360;
        }

        // Getting Local Phi Angle
        double PhiLocal = Phi - (sec - 1)*60;

        // Applying Shift Functions to Phi Angles (local shifted phi = phi)
        double phi = PhiLocal;

        // For Electron Shift
        if(ivec == 0){
            phi = PhiLocal - 30/pp;
        }

        // For π+ Pion/Proton Shift
        if(ivec == 1 || ivec == 3){
            phi = PhiLocal + (32/(pp-0.05));
        }

        // For π- Pion Shift
        if(ivec == 2){
            phi = PhiLocal - (32/(pp-0.05));
        }


        //===============//===============//     No Corrections     //===============//===============//
        if(corEl == 0 && ivec == 0){ // No Electron Correction
            return dp/pp;
        }
        if(corPip == 0 && ivec == 1){ // No π+ Pion Correction
            return dp/pp;
        }
        if(corPim == 0 && ivec == 2){ // No π- Pion Correction
            return dp/pp;
        }
        if(corPro == 0 && ivec == 3){ // No Proton Correction
            return dp/pp;
        }
        //==============//==============//     No Corrections (End)     //==============//==============//

        //==============================//     Electron Corrections     //==============================//
        if(corEl != 0 && ivec == 0){
            if(corEl == 1){ // Fall 2018 - Pass 1 Corrections
                if(sec == 1){
                    dp = ((-4.3303e-06)*phi*phi +  (1.1006e-04)*phi + (-5.7235e-04))*pp*pp +  ((3.2555e-05)*phi*phi +  (-0.0014559)*phi +   (0.0014878))*pp + ((-1.9577e-05)*phi*phi +   (0.0017996)*phi + (0.025963));
                }
                if(sec == 2){
                    dp = ((-9.8045e-07)*phi*phi +  (6.7395e-05)*phi + (-4.6757e-05))*pp*pp + ((-1.4958e-05)*phi*phi +  (-0.0011191)*phi +  (-0.0025143))*pp +  ((1.2699e-04)*phi*phi +   (0.0033121)*phi + (0.020819));
                }
                if(sec == 3){
                    dp = ((-5.9459e-07)*phi*phi + (-2.8289e-05)*phi + (-4.3541e-04))*pp*pp + ((-1.5025e-05)*phi*phi +  (5.7730e-04)*phi +  (-0.0077582))*pp +  ((7.3348e-05)*phi*phi +   (-0.001102)*phi + (0.057052));
                }
                if(sec == 4){
                    dp = ((-2.2714e-06)*phi*phi + (-3.0360e-05)*phi + (-8.9322e-04))*pp*pp +  ((2.9737e-05)*phi*phi +  (5.1142e-04)*phi +   (0.0045641))*pp + ((-1.0582e-04)*phi*phi + (-5.6852e-04)*phi + (0.027506));
                }
                if(sec == 5){
                    dp = ((-1.1490e-06)*phi*phi + (-6.2147e-06)*phi + (-4.7235e-04))*pp*pp +  ((3.7039e-06)*phi*phi + (-1.5943e-04)*phi + (-8.5238e-04))*pp +  ((4.4069e-05)*phi*phi +   (0.0014152)*phi + (0.031933));
                }
                if(sec == 6){
                    dp =  ((1.1076e-06)*phi*phi +  (4.0156e-05)*phi + (-1.6341e-04))*pp*pp + ((-2.8613e-05)*phi*phi + (-5.1861e-04)*phi +  (-0.0056437))*pp +  ((1.2419e-04)*phi*phi +  (4.9084e-04)*phi + (0.049976));
                }
            }

            if(corEl == 2){ // Spring 2019 - Pass 2 Corrections
                if(sec == 1){
                    dp = ((-1.4215599999999998e-06)*phi*phi + (4.91084e-06)*phi + (-0.00012995999999999998))*pp*pp + ((1.6952059999999994e-05)*phi*phi + (-0.00033224299999999997)*phi + (-0.0018080400000000003))*pp + ((-3.1853499999999996e-05)*phi*phi + (0.0016001439999999997)*phi + (0.03187985));
                }
                if(sec == 2){
                    dp =             ((-5.4471e-06)*phi*phi + (-4.69579e-05)*phi + (0.000462807))*pp*pp + ((5.0258819999999995e-05)*phi*phi + (0.00023192399999999994)*phi + (-0.01118006))*pp + ((-8.5754e-05)*phi*phi + (0.00017097299999999994)*phi + (0.05324023));
                }
                if(sec == 3){
                    dp = ((-3.4392460000000005e-06)*phi*phi + (9.860100000000002e-06)*phi + (-3.8414000000000015e-05))*pp*pp + ((1.7492300000000002e-05)*phi*phi + (-4.111499999999996e-05)*phi + (-0.0052975509999999984))*pp + ((1.0045499999999984e-05)*phi*phi + (-2.1412000000000004e-05)*phi + (0.03514576));
                }
                if(sec == 4){
                    dp =  ((2.4865599999999998e-06)*phi*phi + (2.9090599999999996e-05)*phi + (0.00016154500000000003))*pp*pp + ((-2.7148730000000002e-05)*phi*phi + (-0.000136352)*phi + (-0.00543832))*pp + ((4.917660000000001e-05)*phi*phi + (-0.0001558459999999999)*phi + (0.04322285000000001));
                }
                if(sec == 5){
                    dp =  ((-5.340280000000001e-06)*phi*phi + (1.355319e-05)*phi + (0.001362661))*pp*pp + ((5.858976999999999e-05)*phi*phi + (-0.00024119909999999995)*phi + (-0.02025752))*pp + ((-0.0001475504)*phi*phi + (0.0005707250000000001)*phi + (0.07970399));
                }
                if(sec == 6){
                    dp = ((-3.0325500000000003e-06)*phi*phi + (-4.7810870999999994e-05)*phi + (0.001092504))*pp*pp + ((2.4123071999999996e-05)*phi*phi + (0.00047091400000000007)*phi + (-0.01504266))*pp + ((-9.523899999999999e-06)*phi*phi + (-0.0008819019999999998)*phi + (0.048088700000000005));
                }
            }
            
            if(corEl == 3){ // Fall 2018 - Pass 2 Corrections
                if(sec == 1){
                    dp            =                ((-9.82416e-06)*phi*phi +            (-2.29956e-05)*phi +  (0.00029664199999999996))*pp*pp +           ((0.0001113414)*phi*phi +  (-2.041300000000001e-05)*phi +            (-0.00862226))*pp +            ((-0.000281738)*phi*phi +             (0.00058712)*phi +              (0.0652737));
                    if(pp < 7){dp = dp +            ((-3.4001e-06)*phi*phi +             (-2.2885e-05)*phi +              (9.9705e-04))*pp*pp +             ((2.1840e-05)*phi*phi +              (2.4238e-04)*phi +             (-0.0091904))*pp +             ((-2.9180e-05)*phi*phi +            (-6.4496e-04)*phi +               (0.022505));}
                    else{      dp = dp +            ((-6.3656e-05)*phi*phi +              (1.7266e-04)*phi +              (-0.0017909))*pp*pp +                ((0.00104)*phi*phi +              (-0.0028401)*phi +                (0.02981))*pp +              ((-0.0041995)*phi*phi +               (0.011537)*phi +                (-0.1196));}
                    dp            = dp + ((3.2780000000000006e-07)*phi*phi +              (6.7084e-07)*phi +  (-4.390000000000004e-05))*pp*pp + ((-7.230999999999999e-06)*phi*phi +            (-2.37482e-05)*phi +  (0.0004909000000000007))*pp +   ((3.285299999999999e-05)*phi*phi +            (9.63723e-05)*phi +               (-0.00115));
                }
                if(sec == 2){
                    dp            =               ((-7.741952e-06)*phi*phi + (-2.2402167000000004e-05)*phi + (-0.00042652900000000004))*pp*pp +            ((7.54079e-05)*phi*phi + (-1.3333999999999984e-05)*phi +  (0.0002420100000000004))*pp +            ((-0.000147876)*phi*phi +             (0.00057905)*phi +              (0.0253551));
                    if(pp < 7){dp = dp +             ((5.3611e-06)*phi*phi +              (8.1979e-06)*phi +              (5.9789e-04))*pp*pp +            ((-4.8185e-05)*phi*phi +             (-1.5188e-04)*phi +             (-0.0084675))*pp +              ((9.2324e-05)*phi*phi +             (6.4420e-04)*phi +               (0.026792));}
                    else{      dp = dp +            ((-6.1139e-05)*phi*phi +              (5.4087e-06)*phi +              (-0.0021284))*pp*pp +              ((0.0010007)*phi*phi +              (9.3492e-05)*phi +               (0.039813))*pp +              ((-0.0040434)*phi*phi +             (-0.0010953)*phi +               (-0.18112));}
                    dp            = dp +           ((6.221217e-07)*phi*phi +  (1.9596000000000003e-06)*phi +              (-9.826e-05))*pp*pp +           ((-1.28576e-05)*phi*phi +            (-4.36589e-05)*phi +             (0.00130342))*pp +             ((5.80399e-05)*phi*phi +            (0.000215388)*phi + (-0.0040414000000000005));
                }
                if(sec == 3){
                    dp            =      ((-5.115364000000001e-06)*phi*phi + (-1.1983000000000004e-05)*phi +  (-0.0006832899999999999))*pp*pp +            ((4.52287e-05)*phi*phi +  (0.00020855000000000003)*phi +  (0.0034986999999999996))*pp +  ((-9.044610000000001e-05)*phi*phi +            (-0.00106657)*phi +   (0.017954199999999997));
                    if(pp < 7){dp = dp +             ((9.9281e-07)*phi*phi +              (3.4879e-06)*phi +               (0.0011673))*pp*pp +            ((-2.0071e-05)*phi*phi +             (-3.1362e-05)*phi +              (-0.012329))*pp +              ((6.9463e-05)*phi*phi +             (3.5102e-05)*phi +               (0.037505));}
                    else{      dp = dp +            ((-3.2178e-06)*phi*phi +              (4.0630e-05)*phi +               (-0.005209))*pp*pp +             ((2.0884e-05)*phi*phi +             (-6.8800e-04)*phi +               (0.086513))*pp +              ((3.9530e-05)*phi*phi +              (0.0029306)*phi +                (-0.3507));}
                    dp            = dp + ((-4.045999999999999e-07)*phi*phi + (-1.3115999999999994e-06)*phi +  (3.9510000000000006e-05))*pp*pp +              ((5.521e-06)*phi*phi +  (2.4436999999999997e-05)*phi +             (-0.0016887))*pp + ((-1.0962999999999997e-05)*phi*phi +           (-0.000151944)*phi +   (0.009313599999999998));
                }
                if(sec == 4){
                    dp            =     ((-3.9278116999999996e-06)*phi*phi +  (2.2289300000000004e-05)*phi +  (0.00012665000000000002))*pp*pp + ((4.8649299999999995e-05)*phi*phi +             (-0.00012554)*phi +  (-0.005955500000000001))*pp + ((-0.00014617199999999997)*phi*phi +            (-0.00028571)*phi +              (0.0606998));
                    if(pp < 7){dp = dp +            ((-4.8455e-06)*phi*phi +             (-1.2074e-05)*phi +               (0.0013221))*pp*pp +             ((3.2207e-05)*phi*phi +              (1.3144e-04)*phi +              (-0.010451))*pp +             ((-3.7365e-05)*phi*phi +            (-4.2344e-04)*phi +               (0.019952));}
                    else{      dp = dp +            ((-3.9554e-05)*phi*phi +              (5.5496e-06)*phi +              (-0.0058293))*pp*pp +             ((6.5077e-04)*phi*phi +              (2.6735e-05)*phi +               (0.095025))*pp +              ((-0.0026457)*phi*phi +            (-6.1394e-04)*phi +                (-0.3793));}
                    dp            = dp +          ((-4.593089e-07)*phi*phi +             (1.40673e-05)*phi +                (6.69e-05))*pp*pp +             ((4.0239e-06)*phi*phi +            (-0.000180863)*phi + (-0.0008272199999999999))*pp + ((-5.1310000000000005e-06)*phi*phi +             (0.00049748)*phi +             (0.00255231));
                }
                if(sec == 5){
                    dp            =       ((8.036599999999999e-07)*phi*phi +             (2.58072e-05)*phi +             (0.000360217))*pp*pp + ((-9.932400000000002e-06)*phi*phi +           (-0.0005168531)*phi +              (-0.010904))*pp +  ((1.8516299999999998e-05)*phi*phi +  (0.0015570900000000001)*phi +               (0.066493));
                    if(pp < 7){dp = dp +             ((7.7156e-07)*phi*phi +             (-3.9566e-05)*phi +             (-2.3589e-04))*pp*pp +            ((-9.8309e-06)*phi*phi +              (3.7353e-04)*phi +              (0.0020382))*pp +              ((2.9506e-05)*phi*phi +            (-8.0409e-04)*phi +             (-0.0045615));}
                    else{      dp = dp +            ((-3.2410e-05)*phi*phi +             (-4.3301e-05)*phi +              (-0.0028742))*pp*pp +             ((5.3787e-04)*phi*phi +              (6.8921e-04)*phi +               (0.049578))*pp +              ((-0.0021955)*phi*phi +             (-0.0027698)*phi +               (-0.21142));}
                    dp            = dp +            ((-1.2151e-06)*phi*phi +             (-8.5087e-06)*phi +               (4.968e-05))*pp*pp +            ((1.46998e-05)*phi*phi +             (0.000115047)*phi +            (-0.00039269))*pp + ((-4.0368600000000005e-05)*phi*phi +            (-0.00037078)*phi +             (0.00073998));
                }
                if(sec == 6){
                    dp            =     ((-1.9552099999999998e-06)*phi*phi +   (8.042199999999997e-06)*phi + (-2.1324000000000028e-05))*pp*pp + ((1.6969399999999997e-05)*phi*phi +  (-6.306600000000001e-05)*phi +            (-0.00485568))*pp +             ((-2.7723e-05)*phi*phi + (-6.828400000000003e-05)*phi +              (0.0447535));
                    if(pp < 7){dp = dp +            ((-8.2535e-07)*phi*phi +              (9.1433e-06)*phi +              (3.5395e-04))*pp*pp +            ((-3.4272e-06)*phi*phi +             (-1.3012e-04)*phi +             (-0.0030724))*pp +              ((4.9211e-05)*phi*phi +             (4.5807e-04)*phi +              (0.0058932));}
                    else{      dp = dp +            ((-4.9760e-05)*phi*phi +             (-7.2903e-05)*phi +              (-0.0020453))*pp*pp +             ((8.0918e-04)*phi*phi +               (0.0011688)*phi +               (0.037042))*pp +              ((-0.0032504)*phi*phi +             (-0.0046169)*phi +               (-0.16331));}
                    dp            = dp + ((-7.153000000000002e-07)*phi*phi +             (1.62859e-05)*phi +               (8.129e-05))*pp*pp + ((7.2249999999999994e-06)*phi*phi +            (-0.000178946)*phi + (-0.0009485399999999999))*pp + ((-1.3018000000000003e-05)*phi*phi + (0.00046643000000000005)*phi +             (0.00266508));
                }
            }
        }
        //==============================//  Electron Corrections (End)  //==============================//
        
        //==============================//        π+ Corrections        //==============================//
        if(corPip != 0 && ivec == 1){
            if(corPip == 1){ // Fall 2018 - Pass 1 Corrections
                if(sec == 1){
                    dp =      ((-5.4904e-07)*phi*phi + (-1.4436e-05)*phi +  (3.1534e-04))*pp*pp +  ((3.8231e-06)*phi*phi +  (3.6582e-04)*phi +  (-0.0046759))*pp + ((-5.4913e-06)*phi*phi + (-4.0157e-04)*phi +    (0.010767));
                    dp = dp +  ((6.1103e-07)*phi*phi +  (5.5291e-06)*phi + (-1.9120e-04))*pp*pp + ((-3.2300e-06)*phi*phi +  (1.5377e-05)*phi +  (7.5279e-04))*pp +  ((2.1434e-06)*phi*phi + (-6.9572e-06)*phi + (-7.9333e-05));
                    dp = dp + ((-1.3049e-06)*phi*phi +  (1.1295e-05)*phi +  (4.5797e-04))*pp*pp +  ((9.3122e-06)*phi*phi + (-5.1074e-05)*phi +  (-0.0030757))*pp + ((-1.3102e-05)*phi*phi +  (2.2153e-05)*phi +   (0.0040938));
                }
                if(sec == 2){
                    dp =      ((-1.0087e-06)*phi*phi +  (2.1319e-05)*phi +  (7.8641e-04))*pp*pp +  ((6.7485e-06)*phi*phi +  (7.3716e-05)*phi +  (-0.0094591))*pp + ((-1.1820e-05)*phi*phi + (-3.8103e-04)*phi +    (0.018936));
                    dp = dp +  ((8.8155e-07)*phi*phi + (-2.8257e-06)*phi + (-2.6729e-04))*pp*pp + ((-5.4499e-06)*phi*phi +  (3.8397e-05)*phi +   (0.0015914))*pp +  ((6.8926e-06)*phi*phi + (-5.9386e-05)*phi +  (-0.0021749));
                    dp = dp + ((-2.0147e-07)*phi*phi +  (1.1061e-05)*phi +  (3.8827e-04))*pp*pp +  ((4.9294e-07)*phi*phi + (-6.0257e-05)*phi +  (-0.0022087))*pp +  ((9.8548e-07)*phi*phi +  (5.9047e-05)*phi +   (0.0022905));
                }
                if(sec == 3){
                    dp =       ((8.6722e-08)*phi*phi + (-1.7975e-05)*phi +  (4.8118e-05))*pp*pp +  ((2.6273e-06)*phi*phi +  (3.1453e-05)*phi +  (-0.0015943))*pp + ((-6.4463e-06)*phi*phi + (-5.8990e-05)*phi +   (0.0041703));
                    dp = dp +  ((9.6317e-07)*phi*phi + (-1.7659e-06)*phi + (-8.8318e-05))*pp*pp + ((-5.1346e-06)*phi*phi +  (8.3318e-06)*phi +  (3.7723e-04))*pp +  ((3.9548e-06)*phi*phi + (-6.9614e-05)*phi +  (2.1393e-04));
                    dp = dp +  ((5.6438e-07)*phi*phi +  (8.1678e-06)*phi + (-9.4406e-05))*pp*pp + ((-3.9074e-06)*phi*phi + (-6.5174e-05)*phi +  (5.4218e-04))*pp +  ((6.3198e-06)*phi*phi +  (1.0611e-04)*phi + (-4.5749e-04));
                }
                if(sec == 4){
                    dp =       ((4.3406e-07)*phi*phi + (-4.9036e-06)*phi +  (2.3064e-04))*pp*pp +  ((1.3624e-06)*phi*phi +  (3.2907e-05)*phi +  (-0.0034872))*pp + ((-5.1017e-06)*phi*phi +  (2.4593e-05)*phi +   (0.0092479));
                    dp = dp +  ((6.0218e-07)*phi*phi + (-1.4383e-05)*phi + (-3.1999e-05))*pp*pp + ((-1.1243e-06)*phi*phi +  (9.3884e-05)*phi + (-4.1985e-04))*pp + ((-1.8808e-06)*phi*phi + (-1.2222e-04)*phi +   (0.0014037));
                    dp = dp + ((-2.5490e-07)*phi*phi + (-8.5120e-07)*phi +  (7.9109e-05))*pp*pp +  ((2.5879e-06)*phi*phi +  (8.6108e-06)*phi + (-5.1533e-04))*pp + ((-4.4521e-06)*phi*phi + (-1.7012e-05)*phi +  (7.4848e-04));
                }
                if(sec == 5){
                    dp =       ((2.4292e-07)*phi*phi +  (8.8741e-06)*phi +  (2.9482e-04))*pp*pp +  ((3.7229e-06)*phi*phi +  (7.3215e-06)*phi +  (-0.0050685))*pp + ((-1.1974e-05)*phi*phi + (-1.3043e-04)*phi +   (0.0078836));
                    dp = dp +  ((1.0867e-06)*phi*phi + (-7.7630e-07)*phi + (-4.4930e-05))*pp*pp + ((-5.6564e-06)*phi*phi + (-1.3417e-05)*phi +  (2.5224e-04))*pp +  ((6.8460e-06)*phi*phi +  (9.0495e-05)*phi + (-4.6587e-04));
                    dp = dp +  ((8.5720e-07)*phi*phi + (-6.7464e-06)*phi + (-4.0944e-05))*pp*pp + ((-4.7370e-06)*phi*phi +  (5.8808e-05)*phi +  (1.9047e-04))*pp +  ((5.7404e-06)*phi*phi + (-1.1105e-04)*phi + (-1.9392e-04));
                }
                if(sec == 6){
                    dp =       ((2.1191e-06)*phi*phi + (-3.3710e-05)*phi +  (2.5741e-04))*pp*pp + ((-1.2915e-05)*phi*phi +  (2.3753e-04)*phi + (-2.6882e-04))*pp +  ((2.2676e-05)*phi*phi + (-2.3115e-04)*phi +   (-0.001283));
                    dp = dp +  ((6.0270e-07)*phi*phi + (-6.8200e-06)*phi +  (1.3103e-04))*pp*pp + ((-1.8745e-06)*phi*phi +  (3.8646e-05)*phi + (-8.8056e-04))*pp +  ((2.0885e-06)*phi*phi + (-3.4932e-05)*phi +  (4.5895e-04));
                    dp = dp +  ((4.7349e-08)*phi*phi + (-5.7528e-06)*phi + (-3.4097e-06))*pp*pp +  ((1.7731e-06)*phi*phi +  (3.5865e-05)*phi + (-5.7881e-04))*pp + ((-9.7008e-06)*phi*phi + (-4.1836e-05)*phi +   (0.0035403));
                }
            }
            
            if(corPip == 2){ // Spring 2019 - Pass 2 Corrections
                if(sec == 1){
                    dp =                   ((1.07338e-06)*phi*phi + (0.00011237500000000001)*phi + (0.00046984999999999996))*pp*pp + ((-2.9323999999999997e-06)*phi*phi + (-0.000777199)*phi + (-0.0061279))*pp + ((3.7362e-06)*phi*phi + (0.00049608)*phi + (0.0156802));
                    if(pp < 3.5){dp = dp + ((-8.0699e-06)*phi*phi + (3.3838e-04)*phi + (0.0051143))*pp*pp + ((3.0234e-05)*phi*phi + (-0.0015167)*phi + (-0.023081))*pp + ((-1.3818e-05)*phi*phi + (0.0011894)*phi + (0.015812));}
                    else{        dp = dp +  ((2.8904e-07)*phi*phi + (-1.0534e-04)*phi + (-0.0023996))*pp*pp + ((2.3276e-06)*phi*phi + (0.0010502)*phi + (0.022682))*pp + ((-1.9319e-05)*phi*phi + (-0.0025179)*phi + (-0.050285));}
                }
                if(sec == 2){
                    dp =                   ((2.97335e-06)*phi*phi + (7.68257e-05)*phi + (0.001132483))*pp*pp + ((-1.86553e-05)*phi*phi + (-0.000511963)*phi + (-0.0111051))*pp + ((2.16081e-05)*phi*phi + (0.000100984)*phi + (0.0189673));
                    if(pp < 3.5){dp = dp + ((-1.4761e-06)*phi*phi + (4.9397e-06)*phi + (0.0014986))*pp*pp + ((6.4311e-06)*phi*phi + (-3.8570e-05)*phi + (-0.005309))*pp + ((2.2896e-06)*phi*phi + (-1.8426e-04)*phi + (-0.0030622));}
                    else{        dp = dp +  ((3.3302e-06)*phi*phi + (-8.4794e-05)*phi + (-0.0020262))*pp*pp + ((-3.5962e-05)*phi*phi + (9.1367e-04)*phi + (0.019333))*pp + ((9.5116e-05)*phi*phi + (-0.0023371)*phi + (-0.045778));}
                }
                if(sec == 3){
                    dp =        ((1.9689700000000002e-07)*phi*phi + (-6.73721e-05)*phi + (0.001145664))*pp*pp + ((-1.3357999999999998e-07)*phi*phi + (0.0004974620000000001)*phi + (-0.01087555))*pp + ((5.23389e-06)*phi*phi + (-0.00038631399999999996)*phi + (0.012021909999999999));
                    if(pp < 3.5){dp = dp + ((-3.7071e-06)*phi*phi + (-6.7985e-05)*phi + (0.0073195))*pp*pp + ((1.2081e-05)*phi*phi + (4.0719e-04)*phi + (-0.032716))*pp + ((1.8109e-06)*phi*phi + (-5.6304e-04)*phi + (0.022124));}
                    else{        dp = dp +  ((2.9228e-06)*phi*phi + (-7.4216e-07)*phi + (-0.0033922))*pp*pp + ((-2.7026e-05)*phi*phi + (-7.5709e-06)*phi + (0.03267))*pp + ((5.8592e-05)*phi*phi + (3.8319e-05)*phi + (-0.076661));}
                }
                if(sec == 4){
                    dp =                    ((5.4899e-07)*phi*phi + (-1.82236e-05)*phi + (0.0007486388))*pp*pp + ((-1.0743e-06)*phi*phi + (0.000125103)*phi + (-0.00743795))*pp + ((1.9187e-06)*phi*phi + (-5.0545e-05)*phi + (0.01528271));
                    if(pp < 3.5){dp = dp + ((-7.1834e-06)*phi*phi + (1.2815e-04)*phi + (0.004323))*pp*pp + ((2.7688e-05)*phi*phi + (-4.9122e-04)*phi + (-0.020112))*pp + ((-1.5879e-05)*phi*phi + (3.5148e-04)*phi + (0.013367));}
                    else{        dp = dp + ((-2.2635e-06)*phi*phi + (3.3612e-05)*phi + (-0.0024779))*pp*pp + ((2.7765e-05)*phi*phi + (-4.4868e-04)*phi + (0.02433))*pp + ((-7.6567e-05)*phi*phi + (0.0013553)*phi + (-0.058136));}
                }
                if(sec == 5){
                    dp =                    ((9.5628e-07)*phi*phi + (-1.4e-06)*phi + (0.00116279))*pp*pp + ((-3.723047e-06)*phi*phi + (2.09447e-05)*phi + (-0.0101853))*pp + ((9.326299999999999e-06)*phi*phi + (-0.0001111214)*phi + (0.0130134));
                    if(pp < 3.5){dp = dp + ((-8.2807e-06)*phi*phi + (-1.2620e-04)*phi + (0.0060821))*pp*pp + ((3.8915e-05)*phi*phi + (6.3989e-04)*phi + (-0.028784))*pp + ((-3.7765e-05)*phi*phi + (-7.0844e-04)*phi + (0.021177));}
                    else{        dp = dp + ((-8.7415e-08)*phi*phi + (3.5806e-05)*phi + (-0.0022065))*pp*pp + ((5.3612e-06)*phi*phi + (-4.2740e-04)*phi + (0.022369))*pp + ((-2.3587e-05)*phi*phi + (0.0011096)*phi + (-0.056773));}
                }
                if(sec == 6){
                    dp =                   ((5.86478e-07)*phi*phi + (3.5833999999999994e-06)*phi + (0.00108574))*pp*pp + ((-4.433118e-06)*phi*phi + (-5.3565999999999995e-05)*phi + (-0.00873827))*pp + ((2.0270600000000002e-05)*phi*phi + (-7.0902e-05)*phi + (0.0077521));
                    if(pp < 3.5){dp = dp +  ((1.4952e-06)*phi*phi + (1.3858e-05)*phi + (0.0028677))*pp*pp + ((-8.0852e-06)*phi*phi + (-1.1384e-04)*phi + (-0.015643))*pp + ((9.5078e-06)*phi*phi + (1.3285e-04)*phi + (0.014019));}
                    else{        dp = dp + ((-5.7308e-07)*phi*phi + (-3.8697e-05)*phi + (-0.0030495))*pp*pp + ((1.0905e-05)*phi*phi + (3.8288e-04)*phi + (0.030355))*pp + ((-3.1873e-05)*phi*phi + (-9.6019e-04)*phi + (-0.074345));}
                }
            }
            
            if(corPip == 3){ // Fall 2018 - Pass 2 Corrections
                if(sec == 1){
                    dp              =           ((1.338454e-06)*phi*phi +   (4.714629999999999e-05)*phi +  (0.00014719))*pp*pp + ((-2.8460000000000004e-06)*phi*phi +            (-0.000406925)*phi +           (-0.00367325))*pp +           ((-1.193548e-05)*phi*phi +            (-0.000225083)*phi +           (0.01544091));
                    if(pp < 2.5){dp = dp +        ((1.0929e-05)*phi*phi +             (-3.8002e-04)*phi +    (-0.01412))*pp*pp +             ((-2.8491e-05)*phi*phi +              (5.0952e-04)*phi +              (0.037728))*pp +              ((1.6927e-05)*phi*phi +              (1.8165e-04)*phi +            (-0.027772));}
                    else{        dp = dp +        ((4.3191e-07)*phi*phi +             (-9.0581e-05)*phi +  (-0.0011766))*pp*pp +             ((-3.6232e-06)*phi*phi +               (0.0010342)*phi +              (0.012454))*pp +              ((1.2235e-05)*phi*phi +              (-0.0025855)*phi +            (-0.035323));}
                    dp              = dp +       ((-3.7494e-07)*phi*phi +             (-1.5439e-06)*phi +  (4.2760e-05))*pp*pp +              ((3.5348e-06)*phi*phi +              (4.8165e-05)*phi +           (-2.3799e-04))*pp +             ((-8.2116e-06)*phi*phi +             (-7.1750e-05)*phi +           (1.5984e-04));
                }
                if(sec == 2){
                    dp              =             ((5.8222e-07)*phi*phi +  (5.0666599999999994e-05)*phi +  (0.00051782))*pp*pp +              ((3.3785e-06)*phi*phi +            (-0.000343093)*phi + (-0.007453400000000001))*pp + ((-2.2014899999999998e-05)*phi*phi + (-0.00027579899999999997)*phi + (0.015119099999999998));
                    if(pp < 2.5){dp = dp +        ((9.2373e-06)*phi*phi +             (-3.3151e-04)*phi +   (-0.019254))*pp*pp +             ((-2.7546e-05)*phi*phi +              (5.3915e-04)*phi +              (0.052516))*pp +              ((2.5220e-05)*phi*phi +              (7.5362e-05)*phi +            (-0.033504));}
                    else{        dp = dp +        ((2.2654e-08)*phi*phi +             (-8.8436e-05)*phi +  (-0.0013542))*pp*pp +              ((3.0630e-07)*phi*phi +              (9.4319e-04)*phi +                (0.0147))*pp +             ((-3.5941e-06)*phi*phi +              (-0.0022473)*phi +            (-0.036874));}
                    dp              = dp +        ((4.3694e-07)*phi*phi +              (1.1476e-05)*phi +  (1.1123e-04))*pp*pp +             ((-2.4617e-06)*phi*phi +             (-7.5353e-05)*phi +           (-6.2511e-04))*pp +             ((-1.0387e-06)*phi*phi +              (5.8447e-05)*phi +           (6.4986e-04));
                }
                if(sec == 3){
                    dp              =           ((-6.17815e-07)*phi*phi + (-1.4503600000000001e-05)*phi + (0.000584689))*pp*pp +             ((8.27871e-06)*phi*phi +              (9.2796e-05)*phi +         (-0.0078185692))*pp + ((-1.6866360000000002e-05)*phi*phi +  (-8.065000000000001e-05)*phi +            (0.0159476));
                    if(pp < 2.5){dp = dp +        ((1.8595e-06)*phi*phi +              (3.6900e-04)*phi +  (-0.0099622))*pp*pp +              ((8.4410e-06)*phi*phi +              (-0.0010457)*phi +              (0.027038))*pp +             ((-1.2191e-05)*phi*phi +              (6.0203e-04)*phi +            (-0.019176));}
                    else{        dp = dp +        ((6.8265e-07)*phi*phi +              (3.0246e-05)*phi +  (-0.0011116))*pp*pp +             ((-4.8481e-06)*phi*phi +             (-3.7082e-04)*phi +              (0.011452))*pp +              ((7.2478e-06)*phi*phi +              (9.9858e-04)*phi +            (-0.027972));}
                    dp              = dp +        ((1.8639e-07)*phi*phi +              (4.9444e-06)*phi + (-2.9030e-05))*pp*pp +             ((-1.3752e-06)*phi*phi +             (-3.3709e-05)*phi +            (3.8288e-04))*pp +              ((1.0113e-06)*phi*phi +              (5.1273e-05)*phi +          (-6.7844e-04));
                }
                if(sec == 4){
                    dp              =  ((9.379499999999998e-07)*phi*phi + (-2.8101700000000002e-05)*phi +  (0.00053373))*pp*pp + ((-1.6185199999999991e-06)*phi*phi +  (0.00017444500000000001)*phi + (-0.005648269999999999))*pp +  ((-3.495700000000003e-06)*phi*phi +  (-7.845739999999999e-05)*phi + (0.010768400000000001));
                    if(pp < 2.5){dp = dp +        ((9.5779e-06)*phi*phi +              (3.5339e-04)*phi +    (-0.01054))*pp*pp +             ((-1.8077e-05)*phi*phi +              (-0.0010543)*phi +              (0.028379))*pp +              ((3.1773e-06)*phi*phi +              (5.6223e-04)*phi +            (-0.018865));}
                    else{        dp = dp +        ((7.7000e-07)*phi*phi +              (4.1000e-06)*phi +  (-0.0010144))*pp*pp +             ((-8.1960e-06)*phi*phi +             (-4.7753e-05)*phi +              (0.010594))*pp +              ((2.0716e-05)*phi*phi +              (1.2151e-04)*phi +            (-0.028619));}
                    dp              = dp +        ((4.8394e-07)*phi*phi +              (3.6342e-06)*phi + (-2.0136e-04))*pp*pp +             ((-3.2757e-06)*phi*phi +             (-3.5397e-05)*phi +             (0.0015599))*pp +              ((3.2095e-06)*phi*phi +              (7.9013e-05)*phi +            (-0.002012));
                }
                if(sec == 5){
                    dp              = ((1.7566900000000006e-07)*phi*phi +             (2.21337e-05)*phi +   (0.0011632))*pp*pp +   ((2.812770000000001e-06)*phi*phi + (-0.00018654499999999998)*phi + (-0.011854620000000001))*pp +  ((-8.442900000000003e-06)*phi*phi + (-0.00011505800000000001)*phi +            (0.0176174));
                    if(pp < 2.5){dp = dp +        ((3.3685e-05)*phi*phi +              (2.8972e-04)*phi +   (-0.017862))*pp*pp +             ((-8.4089e-05)*phi*phi +             (-9.8038e-04)*phi +              (0.050405))*pp +              ((4.3478e-05)*phi*phi +              (6.9924e-04)*phi +            (-0.033066));}
                    else{        dp = dp +        ((4.6106e-07)*phi*phi +             (-3.6786e-05)*phi +  (-0.0015894))*pp*pp +             ((-4.4217e-06)*phi*phi +              (3.7321e-04)*phi +              (0.015917))*pp +              ((7.5188e-06)*phi*phi +             (-8.0676e-04)*phi +            (-0.036944));}
                    dp              = dp +        ((4.3113e-07)*phi*phi +              (2.6869e-06)*phi + (-2.1326e-04))*pp*pp +             ((-3.1063e-06)*phi*phi +             (-2.7152e-05)*phi +             (0.0017964))*pp +              ((3.1946e-06)*phi*phi +              (4.2059e-05)*phi +           (-0.0031325));
                }
                if(sec == 6){
                    dp              =            ((1.94354e-06)*phi*phi +  (1.3306000000000006e-05)*phi +  (0.00067634))*pp*pp +             ((-7.9584e-06)*phi*phi +  (-7.949999999999998e-05)*phi + (-0.005861990000000001))*pp +   ((6.994000000000005e-07)*phi*phi +             (-0.00022435)*phi +            (0.0118564));
                    if(pp < 2.5){dp = dp +        ((1.7381e-05)*phi*phi +              (5.4630e-04)*phi +   (-0.019637))*pp*pp +             ((-3.8681e-05)*phi*phi +              (-0.0017358)*phi +                (0.0565))*pp +              ((1.2268e-05)*phi*phi +               (0.0011412)*phi +            (-0.035608));}
                    else{        dp = dp +       ((-8.9398e-08)*phi*phi +             (-1.2347e-05)*phi +  (-0.0018442))*pp*pp +              ((7.8164e-08)*phi*phi +              (1.3063e-04)*phi +               (0.01783))*pp +              ((8.2374e-06)*phi*phi +             (-3.5862e-04)*phi +            (-0.047011));}
                    dp              = dp +        ((4.9123e-07)*phi*phi +              (5.1828e-06)*phi + (-1.3898e-04))*pp*pp +             ((-3.4108e-06)*phi*phi +             (-5.0009e-05)*phi +             (0.0014879))*pp +              ((4.0320e-06)*phi*phi +              (6.5853e-05)*phi +           (-0.0032227));
                }
                
            }
        }
        //==============================//     π+ Corrections (End)     //==============================//
        
        //==============================//        π- Corrections        //==============================//
        if(corPim != 0 && ivec == 2){
            if(corPim == 1){
                if(sec == 1){ // Fall 2018 - Pass 1 Corrections (Only)
                    dp =      ((-4.0192658422317425e-06)*phi*phi -  (2.660222128967742e-05)*phi + 0.004774434682983547)*pp*pp;
                    dp = dp +  ((1.9549520962477972e-05)*phi*phi -    0.0002456062756770577*phi - 0.03787692408323466)*pp; 
                    dp = dp +   (-2.128953094937459e-05)*phi*phi +    0.0002461708852239913*phi + 0.08060704449822174 - 0.01;
                }
                if(sec == 2){
                    dp =        ((1.193010521758372e-05)*phi*phi -  (5.996221756031922e-05)*phi + 0.0009093437955814359)*pp*pp;
                    dp = dp +   ((-4.89113824430594e-05)*phi*phi +   0.00021676479488147118*phi - 0.01861892053916726)*pp;  
                    dp = dp +    (4.446394152208071e-05)*phi*phi - (3.6592784167335244e-05)*phi + 0.05498710249944096 - 0.01;
                }
                if(sec == 3){
                    dp =      ((-1.6596664895992133e-07)*phi*phi +  (6.317189710683516e-05)*phi + 0.0016364212312654086)*pp*pp;
                    dp = dp +  ((-2.898409777520318e-07)*phi*phi -   0.00014531513577533802*phi - 0.025456145839203827)*pp;  
                    dp = dp +   (2.6432552410603506e-06)*phi*phi +   0.00018447151306275443*phi + 0.06442602664627255 - 0.01;
                }
                if(sec == 4){
                    dp =       ((2.4035259647558634e-07)*phi*phi -  (8.649647351491232e-06)*phi + 0.004558993439848128)*pp*pp;
                    dp = dp +  ((-5.981498144060984e-06)*phi*phi +   0.00010582131454222416*phi - 0.033572004651981686)*pp;  
                    dp = dp +     (8.70140266889548e-06)*phi*phi -   0.00020137414379966883*phi + 0.07258774523336173 - 0.01;   
                }
                if(sec == 5){
                    dp =       ((2.5817024702834863e-06)*phi*phi +   0.00010132810066914441*phi + 0.003397314538804711)*pp*pp;
                    dp = dp + ((-1.5116941263931812e-05)*phi*phi -   0.00040679799541839254*phi - 0.028144285760769876)*pp;  
                    dp = dp +   (1.4701931057951464e-05)*phi*phi +    0.0002426350390593454*phi + 0.06781682510174941 - 0.01;
                }
                if(sec == 6){
                    dp =       ((-8.196823669099362e-07)*phi*phi -  (5.280412421933636e-05)*phi + 0.0018457238328451137)*pp*pp;
                    dp = dp +  ((5.2675062282094536e-06)*phi*phi +    0.0001515803461044587*phi - 0.02294371578470564)*pp;  
                    dp = dp +   (-9.459454671739747e-06)*phi*phi -    0.0002389523716779765*phi + 0.06428970810739926 - 0.01;
                }
            }
        
            if(corPim == 2){ // Fall 2018 - Pass 2 Corrections
                    if(sec == 1){
                    
                    dp = (6.9146E-07*phi*phi + -1.5092E-04*phi + -9.4808E-04)*pp*pp +(-5.1024E-07*phi*phi + 6.4123E-04*phi + -6.5162E-03+0.0005)*pp + 2.2973E-06*phi*phi + -4.1286E-04*phi + 2.9309E-02-0.005;
                    }
                    if(sec == 2){
                    dp =(9.2131E-07*phi*phi + -1.1244E-04*phi + -2.2340E-03)*pp*pp +(3.9077E-06*phi*phi + 4.4248E-04*phi + -9.0149E-04+0.0005)*pp  + -1.1772E-05*phi*phi + -1.5779E-04*phi + 2.3741E-02-0.005;
                    }
                    if(sec == 3){
                    dp =(-1.9080E-06*phi*phi + 1.4174E-04*phi + 5.1208E-04)*pp*pp +(1.3378E-05*phi*phi + -7.9992E-04*phi + -1.3799E-02+0.0005)*pp  + -1.4611E-05*phi*phi + 4.5592E-04*phi + 3.3211E-02-0.005;  
        
                    }
                    if(sec == 4){
                    dp =(-2.0143E-06*phi*phi + 1.0543E-04*phi + -4.9656E-04)*pp*pp +(1.3347E-05*phi*phi + -5.2722E-04*phi + -8.5912E-03+0.0005)*pp  + -1.4954E-05*phi*phi + 1.2390E-04*phi + 2.9517E-02-0.005;
    
                    }
                    if(sec == 5){
                    dp =(-5.8723E-06*phi*phi + 2.9329E-05*phi + -9.1761E-04)*pp*pp +(3.3092E-05*phi*phi + -1.1637E-04*phi + -6.4689E-03+0.0005)*pp  + -3.3110E-05*phi*phi + -8.2647E-05*phi + 2.5352E-02-0.005;
    
                    }   
                    if(sec == 6){
                    dp =(2.7815E-06*phi*phi + 7.8696E-05*phi + -2.0778E-03)*pp*pp +(-6.4001E-07*phi*phi + -3.8675E-04*phi + -4.9488E-03+0.0005)*pp  + -7.5491E-06*phi*phi + 2.0364E-04*phi + 2.8251E-02-0.005;


                }
            }
        }
        //==============================//     π- Corrections (End)     //==============================//
        
        //==============================//      Proton Corrections      //==============================//
        if(corPro != 0 && ivec == 3){
            if(sec == 1){ // Fall 2018 - Pass 1 Corrections (Only)
                dp = ((1 + Math.signum((double)(pp - 1.4)))/2)*((4.4034e-03)*pp   + (-0.01703))    + ((1 + Math.signum((double)-(pp - 1.4)))/2)*((-0.10898)*(pp  - 1.4)*(pp  - 1.4)  + (-0.09574)*(pp - 1.4)  + ((4.4034e-03)*1.4   + (-0.01703)));
            }
            if(sec == 2){
                dp = ((1 + Math.signum((double)(pp - 1.5)))/2)*((0.01318)*pp      + (-0.03403))    + ((1 + Math.signum((double)-(pp - 1.5)))/2)*((-0.09829)*(pp  - 1.5)*(pp  - 1.5)  +  (-0.0986)*(pp - 1.5)  + ((0.01318)*1.5      + (-0.03403)));
            }
            if(sec == 3){
                dp = ((1 + Math.signum((double)(pp - 1.05)))/2)*((-4.7052e-03)*pp + (1.2410e-03))  + ((1 + Math.signum((double)-(pp - 1.05)))/2)*((-0.22721)*(pp - 1.05)*(pp - 1.05) + (-0.09702)*(pp - 1.05) + ((-4.7052e-03)*1.05 + (1.2410e-03)));
            }
            if(sec == 4){
                dp = ((1 + Math.signum((double)(pp - 1.4)))/2)*((-1.0900e-03)*pp  + (-4.0573e-03)) + ((1 + Math.signum((double)-(pp - 1.4)))/2)*((-0.09236)*(pp  - 1.4)*(pp  - 1.4)  +   (-0.073)*(pp - 1.4)  + ((-1.0900e-03)*1.4  + (-4.0573e-03)));
            }
            if(sec == 5){
                dp = ((1 + Math.signum((double)(pp - 1.5)))/2)*((7.3965e-03)*pp   + (-0.02428))    + ((1 + Math.signum((double)-(pp - 1.5)))/2)*((-0.09539)*(pp  - 1.5)*(pp  - 1.5)  + (-0.09263)*(pp - 1.5)  + ((7.3965e-03)*1.5   + (-0.02428)));
            }
            if(sec == 6){
                dp = ((1 + Math.signum((double)(pp - 1.15)))/2)*((-7.6214e-03)*pp + (8.1014e-03))  + ((1 + Math.signum((double)-(pp - 1.15)))/2)*((-0.12718)*(pp - 1.15)*(pp - 1.15) + (-0.06626)*(pp - 1.15) + ((-7.6214e-03)*1.15 + (8.1014e-03)));
            }
        }
        //==============================//   End of Proton Correction   //==============================//

        return dp/pp;
    }

    /**
    * Compute e-, pi+, pi-, or p (not implemented yet) fractional momentum correction for outbending data.
    * @param Px
    * @param Py
    * @param Pz
    * @param sec
    * @param ivvec
    * @param corEl
    * @param corPip
    * @param corPim
    * @param corPro
    * @return dpp
    */
    protected double dppC_outbending(float Px, float Py, float Pz, int sec, int ivec, int corEl, int corPip, int corPim, int corPro) { 
        
        // 'Px'/'Py'/'Pz'   ==> Corresponds to the Cartesian Components of the particle momentum being corrected
        // 'sec'            ==> Corresponds to the Forward Detector Sectors where the given particle is detected (6 total)
        // 'ivec'           ==> Corresponds to the particle being corrected (See below)    
            // (*) ivec = 0 --> Electron Corrections
            // (*) ivec = 1 --> Pi+ Corrections
            // (*) ivec = 2 --> Pi- Corrections
            // (*) ivec = 3 --> Proton Corrections
        // 'corEl'/'corPip'/'corPim'/'corPro' ==> Controls which version of the particle correction is used
            // Includes:
                // (*) Correction On/Off
                // (*) Pass Version
                // (*) Data Set (Fall 2018 or Spring 2019)
        // 'corEl'         ==> Controls the ELECTRON Corrections
            // corEl  == 0 --> No Correction (Off)
            // corEl  == 1 --> Fall  2018 - Pass 1
            // corEl  == 2 --> Fall  2018 - Pass 2
        // 'corPip'        ==> Controls the π+ PION Corrections
            // corPip == 0 --> No Correction
            // corPip == 1 --> Fall  2018 - Pass 1
            // corPip == 2 --> Fall  2018 - Pass 2
        // 'corPim'        ==> Controls the π- PION Corrections
            // corPim == 0 --> No Correction
            // corPim == 1 --> Fall  2018 - Pass 1 (Created by Nick Trotta)
        // corPim == 2 --> Fall  2018 - Pass 2 (Created by Nick Trotta)
        // 'corPro'        ==> Controls the PROTON Corrections (Momentum)
            // corPro == 0 --> No Correction
            // corPro == 1 --> Not Avaliable

        // Momentum Magnitude
        double pp = Math.sqrt(Px*Px + Py*Py + Pz*Pz);

        // Initializing the correction factor
        double dp = 0;

        // Defining Phi Angle
        double Phi = (180/3.1415926)*Math.atan2(Py, Px);

        // Central Detector Corrections Not Included (Yet)

        // (Initial) Shift of the Phi Angle (done to realign sectors whose data is separated when plotted from ±180˚)
        if(((sec == 4 || sec == 3) && Phi < 0) || (sec > 4 && Phi < 90)){
            Phi += 360;
        }

        // Getting Local Phi Angle
        double PhiLocal = Phi - (sec - 1)*60;

        // Applying Shift Functions to Phi Angles (local shifted phi = phi)
        double phi = PhiLocal;

        // For Electron Shift
        if(ivec == 0){
            phi = PhiLocal - 30/pp;
        }

        // For π+ Pion/Proton Shift
        if(ivec == 1 || ivec == 3){
            phi = PhiLocal + (32/(pp-0.05));
        }

        // For π- Pion Shift
        if(ivec == 2){
            phi = PhiLocal - (32/(pp-0.05));
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////
        //===============//===============//     No Corrections     //===============//===============//
        ////////////////////////////////////////////////////////////////////////////////////////////////
        if(corEl == 0 && ivec == 0){ // No Electron Correction
            return dp/pp;
        }
        if(corPip == 0 && ivec == 1){ // No π+ Pion Correction
            return dp/pp;
        }
        if(corPim == 0 && ivec == 2){ // No π- Pion Correction
            return dp/pp;
        }
        if(corPro == 0 && ivec == 3){ // No Proton Correction
            return dp/pp;
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////
        //==============//==============//     No Corrections (End)     //==============//==============//
        //////////////////////////////////////////////////////////////////////////////////////////////////
        
        
        //=======================//        Electron Corrections        //=======================//
        if(ivec == 0 && corEl != 0){
            if(corEl == 1){ // Fall 2018 - Pass 1 Corrections
                if(sec == 1){
                    dp =     ((1.3189e-06)*phi*phi +  (4.26057e-05)*phi +  (-0.002322628))*pp*pp +  ((-1.1409e-05)*phi*phi +    (2.2188e-05)*phi + (0.02878927))*pp +   ((2.4950e-05)*phi*phi +   (1.6170e-06)*phi + (-0.061816275));
                }
                if(sec == 2){
                    dp =    ((-2.9240e-07)*phi*phi +   (3.2448e-07)*phi +  (-0.001848308))*pp*pp +   ((4.4500e-07)*phi*phi +   (4.76324e-04)*phi + (0.02219469))*pp +   ((6.9220e-06)*phi*phi +  (-0.00153517)*phi +   (-0.0479058));
                }
                if(sec == 3){
                    dp =    ((2.71911e-06)*phi*phi + (1.657148e-05)*phi +  (-0.001822211))*pp*pp + ((-4.96814e-05)*phi*phi + (-3.761117e-04)*phi + (0.02564148))*pp +  ((1.97748e-04)*phi*phi +  (9.58259e-04)*phi +  (-0.05818292));
                }
                if(sec == 4){
                    dp =    ((1.90966e-06)*phi*phi +  (-2.4761e-05)*phi +   (-0.00231562))*pp*pp +  ((-2.3927e-05)*phi*phi +   (2.25262e-04)*phi +  (0.0291831))*pp +   ((8.0515e-05)*phi*phi + (-6.42098e-04)*phi +  (-0.06159197));
                }
                if(sec == 5){
                    dp = ((-3.6760323e-06)*phi*phi +  (4.04398e-05)*phi + (-0.0021967515))*pp*pp +  ((4.90857e-05)*phi*phi +  (-4.37437e-04)*phi + (0.02494339))*pp + ((-1.08257e-04)*phi*phi +   (0.00146111)*phi +   (-0.0648485));
                }
                if(sec == 6){
                    dp =    ((-6.2488e-08)*phi*phi +  (2.23173e-05)*phi +   (-0.00227522))*pp*pp +   ((1.8372e-05)*phi*phi +   (-7.5227e-05)*phi +   (0.032636))*pp +  ((-6.6566e-05)*phi*phi +  (-2.4450e-04)*phi +    (-0.072293));
                }
            }
            
            if(corEl == 2){ // Fall 2018 - Pass 2 Corrections
                if(sec == 1){
                    dp =                               ((-5.74868e-06)*phi*phi +              (2.2331e-06)*phi +  (0.00025487000000000003))*pp*pp +             ((8.18043e-05)*phi*phi +   (9.838300000000004e-05)*phi +             (-0.0056062))*pp + ((-0.00023930959999999998)*phi*phi +            (-0.001175548)*phi +   (0.048679200000000006));
                    if(pp < 6.5){ dp = dp +            ((-1.18269e-05)*phi*phi +  (-3.330000000000001e-05)*phi +  (0.00024240000000000004))*pp*pp +               ((8.541e-05)*phi*phi +  (0.00022060000000000005)*phi +  (0.0006090000000000002))*pp +             ((-0.00012801)*phi*phi +              (-0.0002079)*phi + (-0.0074169999999999965));}
                    else{         dp = dp + ((-1.1703000000000005e-06)*phi*phi +             (-9.8669e-05)*phi +  (-0.0028839999999999994))*pp*pp +               ((2.591e-05)*phi*phi +   (0.0016955400000000002)*phi +               (0.045387))*pp +             ((-0.00012285)*phi*phi +             (-0.00710292)*phi +   (-0.17896999999999996));}
                    dp =               dp +              ((2.5096e-06)*phi*phi +             (-5.5392e-06)*phi +  (0.00015815000000000002))*pp*pp + ((-2.8354999999999995e-05)*phi*phi +              (2.7965e-05)*phi + (-0.0018551000000000002))*pp +   ((5.803000000000001e-05)*phi*phi +  (0.00012528099999999998)*phi +                (0.00604));
                }
                if(sec == 2){
                    dp =                               ((-4.29711e-06)*phi*phi +  (1.3610499999999998e-05)*phi +   (0.0004070599999999999))*pp*pp +   ((6.264497999999999e-05)*phi*phi +   (9.898099999999999e-05)*phi +             (-0.0059589))*pp +            ((-0.000175865)*phi*phi +            (-0.001329597)*phi +   (0.030681999999999997));
                    if(pp < 6.5){ dp = dp +             ((1.42031e-05)*phi*phi +             (-4.3073e-05)*phi +              (-0.0013709))*pp*pp +           ((-0.0001367324)*phi*phi +              (0.00031778)*phi +   (0.012973499999999999))*pp +  ((0.00032084699999999997)*phi*phi + (-0.00048824000000000003)*phi +             (-0.0299448));}
                    else{         dp = dp +             ((-7.3709e-06)*phi*phi +  (3.0237000000000002e-05)*phi +  (-0.0055625299999999996))*pp*pp +   ((9.814100000000001e-05)*phi*phi +  (-0.0004278699999999999)*phi +               (0.089933))*pp +             ((-0.00029204)*phi*phi +   (0.0013842000000000004)*phi +              (-0.355822));}
                    dp =               dp +             ((-3.0129e-06)*phi*phi +             (6.52082e-06)*phi +              (0.00024848))*pp*pp +              ((3.2428e-05)*phi*phi +              (-6.488e-05)*phi +             (-0.0026107))*pp +             ((-7.7834e-05)*phi*phi +   (5.972999999999997e-05)*phi +   (0.005700500000000001));
                }
                if(sec == 3){
                    dp =                               ((-3.54616e-06)*phi*phi +  (2.1382000000000003e-05)*phi + (-0.00029814999999999985))*pp*pp +             ((4.09174e-05)*phi*phi + (-0.00025837170000000005)*phi +  (0.0028637999999999992))*pp +             ((-9.1374e-05)*phi*phi +              (0.00082102)*phi +   (0.008180000000000003));
                    if(pp < 4.75){dp = dp +             ((-4.1035e-05)*phi*phi + (-0.00034996999999999996)*phi +   (0.0037639000000000006))*pp*pp +  ((0.00027659000000000006)*phi*phi +   (0.0025882999999999995)*phi +  (-0.027381999999999997))*pp +              ((-0.0004434)*phi*phi +   (-0.004775700000000001)*phi +   (0.049736999999999996));}
                    else{         dp = dp +              ((8.0625e-06)*phi*phi + (-1.5445000000000002e-05)*phi +             (-0.00213057))*pp*pp +            ((-0.000115274)*phi*phi +  (0.00023843999999999997)*phi +   (0.031481100000000005))*pp +              ((0.00038237)*phi*phi +  (-0.0008649999999999999)*phi +              (-0.110204));}
                    dp =               dp +   ((8.688999999999998e-07)*phi*phi +   (4.309999999999998e-06)*phi +              (0.00010751))*pp*pp +  ((-9.233000000000001e-06)*phi*phi +  (-7.687999999999998e-05)*phi + (-0.0016526999999999998))*pp + ((2.6340000000000002e-05)*phi*phi + (0.00034939999999999993)*phi + (0.0056121999999999995));
                }
                if(sec == 4){
                    dp =                                ((2.43038e-06)*phi*phi +             (-6.4715e-06)*phi +  (-0.0007619799999999998))*pp*pp + ((-2.6067030000000002e-05)*phi*phi +   (7.125799999999999e-05)*phi +             (0.00984946))*pp +   ((6.693939999999999e-05)*phi*phi +  (-0.0002641639999999999)*phi +  (-0.002134799999999997));
                    if(pp < 6.5){ dp = dp +  ((4.3553000000000005e-06)*phi*phi + (-5.2965000000000005e-05)*phi +               (0.0005391))*pp*pp + ((-3.0708999999999993e-05)*phi*phi +  (0.00046772999999999997)*phi +  (-0.007264999999999999))*pp +  ((2.5310000000000013e-05)*phi*phi +              (-0.0010619)*phi +   (0.022312999999999996));}
                    else{         dp = dp +             ((-1.6976e-05)*phi*phi +              (0.00011328)*phi +              (-0.0032102))*pp*pp +              ((0.00027722)*phi*phi +  (-0.0019253299999999998)*phi +               (0.051299))*pp +              ((-0.0011038)*phi*phi +              (0.00811758)*phi +   (-0.20214999999999997));}
                    dp =               dp + ((-3.6703000000000002e-06)*phi*phi +             (1.53405e-05)*phi +  (0.00029439000000000006))*pp*pp +              ((4.0481e-05)*phi*phi +            (-0.000220246)*phi +             (-0.0036454))*pp +               ((-7.86e-05)*phi*phi +              (0.00077819)*phi +               (0.008246));
                }
                if(sec == 5){
                    dp =                    ((-1.7474881000000007e-06)*phi*phi +             (5.20199e-05)*phi +   (0.0005694600000000001))*pp*pp +  ((2.5791260000000002e-05)*phi*phi +             (-0.00049884)*phi +  (-0.007438719999999999))*pp + ((-5.9489000000000006e-05)*phi*phi +   (0.0007089799999999999)*phi +    (0.03122960000000001));
                    if(pp < 4.75){dp = dp +             ((-9.6494e-05)*phi*phi +  (-0.0005152900000000001)*phi +               (0.0157187))*pp*pp +             ((0.000636499)*phi*phi +               (0.0035138)*phi +              (-0.109173))*pp +            ((-0.000981846)*phi*phi +              (-0.0057827)*phi +               (0.173592));}
                    else{         dp = dp +  ((1.3182999999999997e-06)*phi*phi +  (5.7112000000000005e-05)*phi +               (-0.002674))*pp*pp + ((-1.6734000000000006e-05)*phi*phi +             (-0.00085909)*phi +   (0.039538000000000004))*pp +  ((5.4850000000000005e-05)*phi*phi +              (0.00317812)*phi +   (-0.14055099999999998));}
                    dp =               dp +  ((3.1499999999999614e-08)*phi*phi +              (3.3905e-06)*phi +             (-0.00020291))*pp*pp + ((-1.1739999999999993e-06)*phi*phi +             (-4.1231e-05)*phi +              (0.0027257))*pp +  ((2.0700000000000124e-06)*phi*phi +             (7.87011e-05)*phi +  (-0.006728600000000001));
                }
                if(sec == 6){
                    dp =                     ((-7.158700000000002e-07)*phi*phi +             (3.31516e-05)*phi +  (-8.785999999999993e-05))*pp*pp +  ((1.2120100000000004e-05)*phi*phi + (-0.00022941599999999997)*phi +   (0.001413000000000001))*pp +  ((1.2220000000000012e-05)*phi*phi +  (0.00015444900000000008)*phi +              (0.011577));
                    if(pp < 4.75){dp = dp +              ((5.3238e-05)*phi*phi +            (-0.000348957)*phi +              (-0.0047347))*pp*pp +             ((-0.00043499)*phi*phi +              (0.00241282)*phi +               (0.037824))*pp +   ((0.0008483200000000001)*phi*phi +  (-0.0038117999999999997)*phi +             (-0.072263));}
                    else{         dp = dp + ((-2.6884999999999998e-06)*phi*phi +             (1.43847e-05)*phi +  (-0.0019304699999999999))*pp*pp +               ((4.033e-05)*phi*phi +            (-0.000158156)*phi +               (0.028948))*pp +            ((-0.000147228)*phi*phi +             (0.000288167)*phi +  (-0.10448299999999999));}
                    dp =               dp +             ((-4.3485e-06)*phi*phi +             (1.53884e-05)*phi +               (0.0002808))*pp*pp +  ((5.2077300000000004e-05)*phi*phi +            (-0.000211223)*phi +             (-0.0040935))*pp + ((-0.00013267900000000002)*phi*phi +             (0.000691386)*phi +  (0.013362470000000001));
                }
                
            }
            
        }
        //=======================//     Electron Corrections (End)     //=======================//
        
        
        //=======================//           π+ Corrections           //=======================//
        if(ivec == 1 && corPip != 0){
            if(corPip == 1){ // Fall 2018 - Pass 1 Corrections
                if(sec == 1){
                    dp =   ((-1.7334e-06)*phi*phi +  (1.45112e-05)*phi +  (0.00150721))*pp*pp +    ((6.6234e-06)*phi*phi + (-4.81191e-04)*phi +  (-0.0138695))*pp + ((-3.23625e-06)*phi*phi +   (2.79751e-04)*phi + (0.027726));
                }
                if(sec == 2){
                    dp = ((-4.475464e-06)*phi*phi + (-4.11573e-05)*phi +  (0.00204557))*pp*pp +  ((2.468278e-05)*phi*phi +   (9.3590e-05)*phi +   (-0.015399))*pp + ((-1.61547e-05)*phi*phi +   (-2.4206e-04)*phi + (0.0231743));
                }
                if(sec == 3){
                    dp =   ((-8.0374e-07)*phi*phi +   (2.8728e-06)*phi +  (0.00152163))*pp*pp +    ((5.1347e-06)*phi*phi +  (3.71709e-04)*phi +  (-0.0165735))*pp +   ((4.0105e-06)*phi*phi + (-5.289869e-04)*phi + (0.02175395));
                }
                if(sec == 4){
                    dp =   ((-3.8790e-07)*phi*phi + (-4.78445e-05)*phi + (0.002324725))*pp*pp +   ((6.80543e-06)*phi*phi +  (5.69358e-04)*phi +  (-0.0199162))*pp + ((-1.30264e-05)*phi*phi +  (-5.91606e-04)*phi + (0.03202088));
                }
                if(sec == 5){
                    dp =  ((2.198518e-06)*phi*phi + (-1.52535e-05)*phi + (0.001187761))*pp*pp + ((-1.000264e-05)*phi*phi +  (1.63976e-04)*phi + (-0.01429673))*pp +   ((9.4962e-06)*phi*phi +  (-3.86691e-04)*phi + (0.0303695));
                }
                if(sec == 6){
                    dp =  ((-3.92944e-07)*phi*phi +  (1.45848e-05)*phi +  (0.00120668))*pp*pp +    ((3.7899e-06)*phi*phi + (-1.98219e-04)*phi +  (-0.0131312))*pp +  ((-3.9961e-06)*phi*phi +  (-1.32883e-04)*phi + (0.0294497));
                }
            }
            
            if(corPip == 2){ // Fall 2018 - Pass 2 Corrections
                if(sec == 1){
                    dp =        ((1.1191099999999999e-06)*phi*phi +  (-4.555820000000001e-05)*phi + (0.00035314000000000016))*pp*pp + ((-4.847950000000002e-06)*phi*phi +              (0.00021902)*phi + (-0.004359200000000002))*pp +   ((8.018599999999996e-06)*phi*phi + (-0.00010882800000000002)*phi + (0.011117099999999998));
                    if(pp < 5){dp = dp +    ((2.9639e-06)*phi*phi +             (-1.2731e-04)*phi +             (6.8325e-04))*pp*pp +            ((-2.0769e-05)*phi*phi +              (6.8741e-04)*phi +            (-0.0025813))*pp +              ((1.4977e-05)*phi*phi +             (-6.5156e-04)*phi +            (0.0035485));}
                    else{      dp = dp +   ((-1.0211e-06)*phi*phi +             (-1.2476e-04)*phi +             (-0.0016665))*pp*pp +             ((1.3635e-05)*phi*phi +               (0.0017859)*phi +              (0.017797))*pp +             ((-5.4549e-05)*phi*phi +              (-0.0061437)*phi +            (-0.039813));}
                    dp =            dp +   ((-1.3461e-06)*phi*phi +              (1.2220e-05)*phi +             (4.0453e-04))*pp*pp +             ((1.1275e-05)*phi*phi +             (-7.2334e-05)*phi +            (-0.0037741))*pp +             ((-1.2556e-05)*phi*phi +              (3.6897e-05)*phi +            (0.0047898));
                }
                if(sec == 2){
                    dp =       ((-1.4109400000000002e-06)*phi*phi +            (-3.19128e-05)*phi +             (0.00090075))*pp*pp +           ((1.357953e-05)*phi*phi +  (0.00011084560000000001)*phi + (-0.010751109999999998))*pp + ((-1.9769400000000004e-05)*phi*phi +   (5.487899999999999e-05)*phi + (0.018924899999999998));
                    if(pp < 5){dp = dp +    ((3.3582e-06)*phi*phi +             (-1.6123e-04)*phi +              (0.0014661))*pp*pp +            ((-1.7191e-05)*phi*phi +              (8.7685e-04)*phi +            (-0.0076688))*pp +              ((1.9961e-05)*phi*phi +             (-8.5011e-04)*phi +              (0.00578));}
                    else{      dp = dp +    ((7.2850e-06)*phi*phi +              (1.9919e-04)*phi +             (-0.0023838))*pp*pp +            ((-9.4438e-05)*phi*phi +              (-0.0024354)*phi +              (0.029087))*pp +              ((2.9427e-04)*phi*phi +               (0.0071514)*phi +            (-0.079795));}
                    dp =            dp +    ((8.8958e-07)*phi*phi +              (1.1594e-05)*phi +            (-2.1837e-04))*pp*pp +            ((-6.5454e-06)*phi*phi +             (-4.0303e-05)*phi +             (0.0015182))*pp +              ((2.0679e-06)*phi*phi +             (-9.0161e-05)*phi +          (-1.5056e-04));
                }
                if(sec == 3){
                    dp =         ((9.851100000000002e-07)*phi*phi +   (6.053000000000001e-05)*phi +  (0.0005047900000000002))*pp*pp +  ((5.664999999999987e-07)*phi*phi +            (-0.000356055)*phi +            (-0.0086655))*pp + ((-3.8054999999999994e-06)*phi*phi +            (-0.000213476)*phi + (0.018339600000000008));
                    if(pp < 5){dp = dp +   ((-8.6830e-07)*phi*phi +              (5.9703e-05)*phi +              (0.0013032))*pp*pp +             ((6.0055e-06)*phi*phi +             (-3.6019e-04)*phi +            (-0.0062311))*pp +             ((-1.0980e-05)*phi*phi +              (4.6372e-04)*phi +            (0.0053207));}
                    else{      dp = dp +   ((-4.2567e-06)*phi*phi +              (1.2239e-04)*phi +             (-0.0023899))*pp*pp +             ((4.4797e-05)*phi*phi +              (-0.0016063)*phi +              (0.028418))*pp +             ((-1.2146e-04)*phi*phi +               (0.0050973)*phi +            (-0.074403));}
                    dp =            dp +    ((3.7278e-07)*phi*phi +             (-1.0391e-05)*phi +            (-7.7620e-05))*pp*pp +            ((-5.3408e-06)*phi*phi +              (1.0286e-04)*phi +            (6.0048e-04))*pp +              ((8.4599e-06)*phi*phi +             (-1.9268e-04)*phi +           (5.4090e-05));
                }
                if(sec == 4){
                    dp =                   ((-2.0053e-06)*phi*phi +             (4.72363e-05)*phi +             (0.00081723))*pp*pp +            ((1.32423e-05)*phi*phi + (-0.00020967799999999998)*phi + (-0.007274300000000003))*pp + ((-1.2887599999999998e-05)*phi*phi +  (-0.0004639299999999999)*phi + (0.012234000000000005));
                    if(pp < 4.5){dp = dp +  ((5.2229e-06)*phi*phi +              (1.9574e-04)*phi +             (2.2872e-04))*pp*pp +            ((-2.3170e-05)*phi*phi +             (-9.5521e-04)*phi +            (-0.0034589))*pp +              ((1.5039e-05)*phi*phi +              (9.0872e-04)*phi +            (0.0051232));}
                    else{        dp = dp + ((-2.5188e-06)*phi*phi +              (5.6253e-05)*phi +             (-0.0031624))*pp*pp +             ((2.8721e-05)*phi*phi +             (-7.7158e-04)*phi +              (0.037865))*pp +             ((-7.2393e-05)*phi*phi +               (0.0025854)*phi +             (-0.10784));}
                    dp =              dp +  ((1.0263e-06)*phi*phi +              (4.5781e-06)*phi +            (-1.3922e-04))*pp*pp +            ((-6.6979e-06)*phi*phi +             (-5.3840e-05)*phi +            (8.4739e-04))*pp +              ((4.3399e-06)*phi*phi +              (7.9334e-05)*phi +           (5.5832e-04));
                }
                if(sec == 5){
                    dp =         ((9.726859999999998e-07)*phi*phi + (-1.3775299999999998e-05)*phi +  (0.0005220700000000001))*pp*pp +           ((-4.69928e-06)*phi*phi +  (1.8265999999999984e-05)*phi +            (-0.0075102))*pp +             ((1.41712e-05)*phi*phi + (-0.00017345900000000002)*phi + (0.011935100000000004));
                    if(pp < 4.5){dp = dp + ((-5.8188e-06)*phi*phi +              (1.6693e-04)*phi +              (0.0010372))*pp*pp +             ((2.6977e-05)*phi*phi +             (-7.0556e-04)*phi +            (-0.0039761))*pp +             ((-2.8235e-05)*phi*phi +              (5.2835e-04)*phi +             (0.002592));}
                    else{        dp = dp +  ((3.9910e-06)*phi*phi +              (1.1435e-04)*phi +             (-0.0022152))*pp*pp +            ((-4.6227e-05)*phi*phi +              (-0.0013773)*phi +              (0.026904))*pp +              ((1.1754e-04)*phi*phi +               (0.0041633)*phi +            (-0.073689));}
                    dp =              dp + ((-4.3369e-07)*phi*phi +              (4.7403e-06)*phi +             (5.7200e-05))*pp*pp +             ((5.0509e-06)*phi*phi +             (-9.4318e-05)*phi +            (-0.0011244))*pp +             ((-1.0369e-05)*phi*phi +              (2.4561e-04)*phi +            (0.0025475));
                }
                if(sec == 6){
                    dp =                   ((1.90451e-06)*phi*phi +  (3.6626999999999997e-06)*phi +  (0.0006271600000000001))*pp*pp + ((-4.469499999999999e-06)*phi*phi +   (3.548999999999999e-05)*phi +            (-0.0093912))*pp +             ((-2.5483e-06)*phi*phi +             (-0.00041356)*phi + (0.022508600000000007));
                    if(pp < 4.5){dp = dp +  ((2.4397e-07)*phi*phi +             (-5.3125e-06)*phi +             (-0.0016639))*pp*pp +             ((1.7518e-06)*phi*phi +             (-7.1887e-05)*phi +             (0.0065102))*pp +              ((1.4443e-06)*phi*phi +              (1.1702e-04)*phi +           (-0.0059638));}
                    else{        dp = dp +  ((2.0755e-06)*phi*phi +             (-1.4409e-04)*phi +             (2.8350e-05))*pp*pp +            ((-2.6072e-05)*phi*phi +               (0.0017069)*phi +            (-0.0027976))*pp +              ((6.8458e-05)*phi*phi +               (-0.004796)*phi +             (0.020065));}
                    dp =              dp +  ((4.2948e-08)*phi*phi +              (1.8318e-05)*phi +            (-2.2139e-04))*pp*pp +            ((-2.0052e-07)*phi*phi +             (-1.2731e-04)*phi +             (0.0015941))*pp +             ((-5.6875e-06)*phi*phi +              (1.9029e-04)*phi +          (-5.6956e-04));
                }
                
            }
        }
        //=======================//        π+ Corrections (End)        //=======================//
        
        
        //=======================//           π- Corrections           //=======================//
        if(ivec == 2 && corPim != 0){
            if(corPim == 1){
                if(sec == 1){ // Fall 2018 - Pass 1 Corrections (Only)
                    dp = ((2.7123584594392597e-06)*phi*phi +  (-5.468601175954242e-05)*phi +   (0.002313330256974031))*pp*pp +  ((-8.039703360516874e-06)*phi*phi +  (0.00044464879674067275)*phi +  (-0.02546911446157775))*pp + ((3.5973669277966655e-06)*phi*phi + (-0.0003856844699023182)*phi + (0.05496480659602064) - 0.015);
                }
                if(sec == 2){
                    dp = ((1.9081500905303347e-06)*phi*phi +   (3.310647986349362e-05)*phi + (-0.0003264357817968204))*pp*pp + ((-1.2306311457915714e-05)*phi*phi +  (-6.404982516446639e-05)*phi +  (-0.01287404671840319))*pp +  ((9.746651642120768e-06)*phi*phi +    (6.1503461629194e-05)*phi + (0.04249861359511857) - 0.015);
                }
                if(sec == 3){
                    dp =  ((3.467960715633796e-06)*phi*phi + (-0.00011427345789836184)*phi +   (0.004780571116355615))*pp*pp + ((-1.2639455891842017e-05)*phi*phi +  (0.00044737258600913664)*phi +  (-0.03827009444373719))*pp + ((5.8243648992776484e-06)*phi*phi + (-0.0004240381542174731)*phi + (0.06589846610477122) - 0.015);
                }
                if(sec == 4){
                    dp =  ((-7.97757466039691e-06)*phi*phi + (-0.00011075801628158914)*phi +   (0.006505144041475733))*pp*pp +   ((3.570788801587046e-05)*phi*phi +   (0.0005835525352273808)*phi + (-0.045031773715754606))*pp + ((-3.223327114068019e-05)*phi*phi + (-0.0006144362450858762)*phi + (0.07280937684254037) - 0.015);
                }
                if(sec == 5){
                    dp =  ((1.990802625607816e-06)*phi*phi +   (7.057771450607931e-05)*phi +   (0.005399025205722829))*pp*pp +  ((-7.670376562908147e-06)*phi*phi + (-0.00032508260930191955)*phi + (-0.044439500813069875))*pp +  ((7.599354976329091e-06)*phi*phi +  (0.0002562152836894338)*phi + (0.07195292224032898) - 0.015);
                }
                if(sec == 6){
                    dp = ((1.9247834787602347e-06)*phi*phi +   (7.638857332736951e-05)*phi +   (0.005271258583881754))*pp*pp + ((-2.7349724034956845e-06)*phi*phi + (-0.00016130256163798413)*phi +  (-0.03668300882287307))*pp +   ((7.40942843287096e-07)*phi*phi + (-5.785254680184232e-05)*phi + (0.06282320712979896) - 0.015);
                }
            } 
            if(corPim == 2){

                if(sec ==1){
                    dp = (1.0983E-06*phi*phi + 1.5037E-04*phi + 1.9341E-03)*pp*pp +(-2.2527E-06*phi*phi + -6.8555E-04*phi + -8.1855E-03)*pp + 6.5232E-07*phi*phi + 2.9397E-04*phi + 3.4917E-02;
                } 
                if(sec ==2) {
                    dp =(2.5237E-06*phi*phi + 1.1512E-04*phi + 1.7644E-03)*pp*pp +(-9.9950E-06*phi*phi + -5.4096E-04*phi + -1.1687E-02)*pp  + 1.1094E-05*phi*phi + 1.2634E-04*phi + 3.6410E-02;
                }
                if(sec ==3) {
                    dp =(-1.6384E-06*phi*phi + -4.1317E-05*phi + 2.2341E-03)*pp*pp +(8.6412E-06*phi*phi + 2.2777E-04*phi + -1.3125E-02)*pp  + -5.6625E-06*phi*phi + -2.5952E-04*phi + 3.7961E-02;
                }
                if(sec ==4) {
                    dp =(8.6286E-07*phi*phi + 2.3389E-05*phi + 1.5474E-03)*pp*pp +(1.3660E-06*phi*phi + -4.7140E-05*phi + -8.9792E-03)*pp  + -4.6200E-06*phi*phi + -8.9496E-05*phi + 3.9814E-02;
                }
                if(sec ==5) {
                    dp =(-4.9591E-06*phi*phi + 5.2169E-05*phi + 4.4994E-03)*pp*pp +(2.4705E-05*phi*phi + -3.2781E-04*phi + -2.6733E-02)*pp  + -1.8959E-05*phi*phi + 1.0880E-04*phi + 5.1970E-02;
                }
                if(sec ==6) {
                    dp =(-1.5152E-08*phi*phi + 6.7025E-05*phi + 1.8863E-03)*pp*pp +(3.3589E-06*phi*phi + -2.6816E-04*phi + -1.1131E-02)*pp  + 9.5490E-07*phi*phi + 7.3840E-05*phi + 3.5431E-02;
                }
            }
        }
        //=======================//        π- Corrections (End)        //=======================//

        return dp/pp;
    }

    /**
    * Apply momentum correction to a given particle.
    * @param p
    * @return new_p
    */
    protected DecayProduct applyMomentumCorrection(DecayProduct p) {

        // Set PID and sector
        int pid = p.pid();
        int sec = p.sector();

        // Set correction version
        int corVersion = 0;
        if (!this._outbending) {
            if (this._dataset=="Fall2018" && this._pass==1) corVersion = 1;
            if (this._dataset=="Fall2018" && this._pass==2 && pid==-211) corVersion = 2;
            if (this._dataset=="Spring2019" && this._pass==2) corVersion = 2;
            if (this._dataset=="Fall2018" && this._pass==2 && (pid==11 || pid==211)) corVersion = 3;
        } else {
            if (this._dataset=="Fall2018" && this._pass==1 && (pid==11 || pid==211 || pid==-211)) corVersion = 1;
            if (this._dataset=="Fall2018" && this._pass==2 && (pid==11 || pid==211 || pid==-211)) corVersion = 2;
        }

        // Check if you have a valid correction version
        if (corVersion==0) return p;

        // Set ivec and correction version
        int ivec; int corEl; int corPip; int corPim; int corPro;
        double f_pip_loss = 1.0;
        switch (pid) {
            case 11:
                ivec =  0;
                corEl  = corVersion;
                break;
            case 211:
                ivec =  1;
                corPip = corVersion;
                f_pip_loss = this.f_pip_loss(p, p.detector(), this._outbending);
                break;
            case -211:
                ivec =  2;
                corPim = corVersion;
                break;
            case 2212:
                ivec =  3;
                corPro = corVersion;
                break;
            default:
                ivec = -1;
                break;
        }

        // Compute momentum corrections
        double f = 1.0;
        if (!this._outbending) {
            f += this.dppC_inbending((float)p.px(), (float)p.py(), (float)p.pz(), sec, ivec, corEl, corPip, corPim, corPro);
        } else {
            f += this.dppC_outbending((float)p.px(), (float)p.py(), (float)p.pz(), sec, ivec, corEl, corPip, corPim, corPro);
        }

        // Reset momentum vector
        DecayProduct new_p = p.clone();
        new_p.setPxPyPzM(f_pip_loss*f*p.px(), f_pip_loss*f*p.py(), f_pip_loss*f*p.pz(), p.m());
        return new_p;
        
    }

} // public class MomentumCorrections {
