package org.jlab.analysis;

// Groovy Imports
import groovy.transform.CompileStatic;

// Java Imports
import java.util.HashMap;

/**
* Sets the Lund pid to particle mass/charge/name hashmaps and other analysis constants.
* As you might expect the pid to mass hashmap is somewhat more extensive.
*
* @version 1.0
* @author  Matthew McEneaney
*/

@CompileStatic
public class ExtendedConstants extends Constants {

    // Lund PIDs //
    // Quarks & Leptons
    protected static int _pid_u, _pid_d, _pid_s, _pid_c, _pid_b, _pid_t, _pid_b_, _pid_t_, _pid_l, _pid_h;
    protected static int /*_pid_e,*/ _pid_nu_e, _pid_mu, _pid_nu_mu, _pid_tau, _pid_nu_tau, _pid_tau_, _pid_nu_tau_;
    // Fundamental Bosons
    protected static int _pid_gluon, /*_pid_g,*/ _pid_Z0, _pid_WP, _pid_H_10, _pid_Z_, _pid_Z__, _pid_W_, _pid_H_20, _pid_H_30, _pid_HP;
    // Diquarks
    protected static int _pid_dd1, _pid_ud0, _pid_ud1, _pid_uu1, _pid_sd0, _pid_sd1;
    protected static int _pid_su0, _pid_su1, _pid_ss1, _pid_cd0, _pid_cd1, _pid_cu0;
    protected static int _pid_cu1, _pid_cs0, _pid_cs1, _pid_cc1, _pid_bd0, _pid_bd1;
    protected static int _pid_bu0, _pid_bu1, _pid_bs0, _pid_bs1, _pid_bc0, _pid_bc1, _pid_bb1;
    // Atomic Nuclei
    protected static int _pid_deuterium, _pid_tritium, _pid_alpha, _pid_HE3;
    // J^{p} = 1/2+ Baryons
    protected static int _pid_L0, _pid_Lc, _pid_Lb0, _pid_SP, _pid_S0, _pid_SM;
    protected static int _pid_ScPP, _pid_ScP, _pid_Sc0, _pid_SbP, _pid_SbM, _pid_X0;
    protected static int _pid_XM, _pid_XcP, _pid_Xc0, _pid_XcP_, _pid_Xc0_, _pid_XccPP;
    protected static int _pid_Xb0, _pid_XbM, _pid_Oc0, _pid_ObM;
    // J^{p} = 3/2+ Baryons
    protected static int _pid_DeltaPP, _pid_DeltaP, _pid_Delta0, _pid_DeltaM;
    protected static int _pid_SStarP, _pid_SStar0, _pid_SStarM, _pid_SStarcPP;
    protected static int _pid_SStarcP, _pid_SStarc0, _pid_SStarbP, _pid_SStarbM;
    protected static int _pid_XStar0, _pid_XStarM, _pid_XStarcP, _pid_XStarc0;
    protected static int _pid_XStarb0, _pid_OM, _pid_OStarc0; 
    // Pseudoscalar Mesons
    protected static int _pid_pi0, _pid_eta, _pid_eta_, _pid_etac, _pid_etab;
    protected static int _pid_k0, _pid_ks0, _pid_kl0, _pid_D, _pid_D0, _pid_Ds;
    protected static int _pid_B, _pid_B0, _pid_Bs0, _pid_Bc;
    // Vector Mesons
    protected static int _pid_r, _pid_r0, _pid_w, _pid_phi, _pid_Jpsi, _pid_U;
    protected static int _pid_KStar, _pid_KStar0, _pid_DStar, _pid_DStar0, _pid_DStars;
    protected static int _pid_BStar, _pid_BStar0, _pid_BStars0;

    // Masses //
    // Quarks & Leptons
    protected static int _m_u, _m_d, _m_s, _m_c, _m_b, _m_t, _m_b_, _m_t_, _m_l, _m_h;
    protected static int /*_m_e,*/ _m_nu_e, _m_mu, _m_nu_mu, _m_tau, _m_nu_tau, _m_tau_, _m_nu_tau_;
    // Fundamental Bosons
    protected static int _m_gluon, /*_m_g,*/ _m_Z0, _m_WP, _m_H_10, _m_Z_, _m_Z__, _m_W_, _m_H_20, _m_H_30, _m_HP;
    // Diquarks
    protected static int _m_dd1, _m_ud0, _m_ud1, _m_uu1, _m_sd0, _m_sd1;
    protected static int _m_su0, _m_su1, _m_ss1, _m_cd0, _m_cd1, _m_cu0;
    protected static int _m_cu1, _m_cs0, _m_cs1, _m_cc1, _m_bd0, _m_bd1;
    protected static int _m_bu0, _m_bu1, _m_bs0, _m_bs1, _m_bc0, _m_bc1, _m_bb1;
    // Atomic Nuclei
    protected static double _m_deuterium, _m_tritium, _m_alpha, _m_HE3;
    // J^{p} = 1/2+ Baryons
    protected static double _m_L0, _m_Lc, _m_Lb0, _m_SP, _m_S0, _m_SM, _m_ScPP, _m_ScP;
    protected static double _m_Sc0, _m_SbP, _m_SbM, _m_X0, _m_XM, _m_XcP, _m_Xc0, _m_XcP_;
    protected static double _m_Xc0_, _m_XccPP, _m_Xb0, _m_XbM, _m_Oc0, _m_ObM;
    // J^{p} = 3/2+ Baryons
    protected static double _m_DeltaPP, _m_DeltaP, _m_Delta0, _m_DeltaM, _m_SStarP, _m_SStar0;
    protected static double _m_SStarM, _m_SStarcPP, _m_SStarcP, _m_SStarc0, _m_SStarbP;
    protected static double _m_SStarbM, _m_XStar0, _m_XStarM, _m_XStarcP, _m_XStarc0;
    protected static double _m_XStarb0, _m_OM, _m_OStarc0; 
    // Pseudoscalar Mesons
    protected static double _m_pi0, _m_eta, _m_eta_, _m_etac, _m_etab, _m_k0, _m_ks0, _m_kl0;
    protected static double _m_D, _m_D0, _m_Ds, _m_B, _m_B0, _m_Bs0, _m_Bc;
    // Vector Mesons
    protected static double _m_r, _m_r0, _m_w, _m_phi, _m_Jpsi, _m_U, _m_KStar, _m_KStar0;
    protected static double _m_DStar, _m_DStar0, _m_DStars, _m_BStar, _m_BStar0, _m_BStars0; 

    // Charges //
    // Quarks & Leptons
    protected static int _q_u, _q_d, _q_s, _q_c, _q_b, _q_t, _q_b_, _q_t_, _q_l, _q_h;
    protected static int /*_q_e,*/ _q_nu_e, _q_mu, _q_nu_mu, _q_tau, _q_nu_tau, _q_tau_, _q_nu_tau_;
    // Fundamental Bosons
    protected static int _q_gluon, /*_q_g,*/ _q_Z0, _q_WP, _q_H_10, _q_Z_, _q_Z__, _q_W_, _q_H_20, _q_H_30, _q_HP;
    // Diquarks
    protected static int _q_dd1, _q_ud0, _q_ud1, _q_uu1, _q_sd0, _q_sd1;
    protected static int _q_su0, _q_su1, _q_ss1, _q_cd0, _q_cd1, _q_cu0;
    protected static int _q_cu1, _q_cs0, _q_cs1, _q_cc1, _q_bd0, _q_bd1;
    protected static int _q_bu0, _q_bu1, _q_bs0, _q_bs1, _q_bc0, _q_bc1, _q_bb1;
    // Atomic Nuclei
    protected static int _q_deuterium, _q_tritium, _q_alpha, _q_HE3;
    // J^{p} = 1/2+ Baryons
    protected static int _q_L0, _q_Lc, _q_Lb0, _q_SP, _q_S0, _q_SM;
    protected static int _q_ScPP, _q_ScP, _q_Sc0, _q_SbP, _q_SbM, _q_X0;
    protected static int _q_XM, _q_XcP, _q_Xc0, _q_XcP_, _q_Xc0_, _q_XccPP;
    protected static int _q_Xb0, _q_XbM, _q_Oc0, _q_ObM;
    // J^{p} = 3/2+ Baryons
    protected static int _q_DeltaPP, _q_DeltaP, _q_Delta0, _q_DeltaM;
    protected static int _q_SStarP, _q_SStar0, _q_SStarM, _q_SStarcPP;
    protected static int _q_SStarcP, _q_SStarc0, _q_SStarbP, _q_SStarbM;
    protected static int _q_XStar0, _q_XStarM, _q_XStarcP, _q_XStarc0;
    protected static int _q_XStarb0, _q_OM, _q_OStarc0; 
    // Pseudoscalar Mesons
    protected static int _q_pi0, _q_eta, _q_eta_, _q_etac, _q_etab;
    protected static int _q_k0, _q_ks0, _q_kl0, _q_D, _q_D0, _q_Ds;
    protected static int _q_B, _q_B0, _q_Bs0, _q_Bc;
    // Vector Mesons
    protected static int _q_r, _q_r0, _q_w, _q_phi, _q_Jpsi, _q_U;
    protected static int _q_KStar, _q_KStar0, _q_DStar, _q_DStar0, _q_DStars;
    protected static int _q_BStar, _q_BStar0, _q_BStars0;

    // Names //
    // Quarks & Leptons
    protected static int _n_u, _n_d, _n_s, _n_c, _n_b, _n_t, _n_b_, _n_t_, _n_l, _n_h;
    protected static int /*_n_e,*/ _n_nu_e, _n_mu, _n_nu_mu, _n_tau, _n_nu_tau, _n_tau_, _n_nu_tau_;
    // Fundamental Bosons
    protected static int _n_gluon, /*_n_g,*/ _n_Z0, _n_WP, _n_H_10, _n_Z_, _n_Z__, _n_W_, _n_H_20, _n_H_30, _n_HP;
    // Diquarks
    protected static int _n_dd1, _n_ud0, _n_ud1, _n_uu1, _n_sd0, _n_sd1;
    protected static int _n_su0, _n_su1, _n_ss1, _n_cd0, _n_cd1, _n_cu0;
    protected static int _n_cu1, _n_cs0, _n_cs1, _n_cc1, _n_bd0, _n_bd1;
    protected static int _n_bu0, _n_bu1, _n_bs0, _n_bs1, _n_bc0, _n_bc1, _n_bb1;
    // Atomic Nuclei
    protected static String _n_deuterium, _n_tritium, _n_alpha, _n_HE3;
    // J^{p} = 1/2+ Baryons
    protected static String _n_L0, _n_Lc, _n_Lb0, _n_SP, _n_S0, _n_SM;
    protected static String _n_ScPP, _n_ScP, _n_Sc0, _n_SbP, _n_SbM, _n_X0;
    protected static String _n_XM, _n_XcP, _n_Xc0, _n_XcP_, _n_Xc0_, _n_XccPP;
    protected static String _n_Xb0, _n_XbM, _n_Oc0, _n_ObM;
    // J^{p} = 3/2+ Baryons
    protected static String _n_DeltaPP, _n_DeltaP, _n_Delta0, _n_DeltaM;
    protected static String _n_SStarP, _n_SStar0, _n_SStarM, _n_SStarcPP;
    protected static String _n_SStarcP, _n_SStarc0, _n_SStarbP, _n_SStarbM;
    protected static String _n_XStar0, _n_XStarM, _n_XStarcP, _n_XStarc0;
    protected static String _n_XStarb0, _n_OM, _n_OStarc0; 
    // Pseudoscalar Mesons
    protected static String _n_pi0, _n_eta, _n_eta_, _n_etac, _n_etab;
    protected static String _n_k0, _n_ks0, _n_kl0, _n_D, _n_D0, _n_Ds;
    protected static String _n_B, _n_B0, _n_Bs0, _n_Bc;
    // Vector Mesons
    protected static String _n_r, _n_r0, _n_w, _n_phi, _n_Jpsi, _n_U;
    protected static String _n_KStar, _n_KStar0, _n_DStar, _n_DStar0, _n_DStars;
    protected static String _n_BStar, _n_BStar0, _n_BStars0;


    public ExtendedConstants() {
        /**
        * Constructor stub
        */

        // Quarks & Leptons
        this._pid_d       = 1;        // [Lund pid]
        this._pid_u       = 2;        // [Lund pid]
        this._pid_s       = 3;        // [Lund pid]
        this._pid_c       = 4;        // [Lund pid]
        this._pid_b       = 5;        // [Lund pid]
        this._pid_t       = 6;        // [Lund pid]
        this._pid_l       = 7;        // [Lund pid]
        this._pid_h       = 8;        // [Lund pid]
        //this._pid_e       = 11;       // [Lund pid]
        this._pid_nu_e    = 12;       // [Lund pid]
        this._pid_mu      = 13;       // [Lund pid]
        this._pid_nu_mu   = 14;       // [Lund pid]
        this._pid_tau     = 15;       // [Lund pid]
        this._pid_nu_tau  = 16;       // [Lund pid]
        this._pid_tau_    = 17;       // [Lund pid]
        this._pid_nu_tau_ = 18;       // [Lund pid]

        // Fundamental Bosons
		this._pid_gluon = 21;	//[ Lund pid]
		//this._pid_g     = 22; // [Lund pid]
		this._pid_Z0    = 23;	// [Lund pid] 
		this._pid_WP    = 24;	// [Lund pid] 
		this._pid_H_10  = 25;	// [Lund pid] 
		this._pid_Z_    = 32;	// [Lund pid] 
		this._pid_Z__   = 33;	// [Lund pid] 
		this._pid_W_    = 34;	// [Lund pid] 
		this._pid_H_20  = 35;	// [Lund pid] 
		this._pid_H_30  = 36;	// [Lund pid] 
		this._pid_HP    = 37;	// [Lund pid]

        // Diquarks 
		this._pid_dd1 = 1103;	// [Lund pid] 
		this._pid_ud0 = 2101;	// [Lund pid] 
		this._pid_ud1 = 2103;	// [Lund pid] 
		this._pid_uu1 = 2203;	// [Lund pid] 
		this._pid_sd0 = 3101;	// [Lund pid] 
		this._pid_sd1 = 3103;	// [Lund pid] 
		this._pid_su0 = 3201;	// [Lund pid] 
		this._pid_su1 = 3203;	// [Lund pid] 
		this._pid_ss1 = 3303;	// [Lund pid] 
		this._pid_cd0 = 4101;	// [Lund pid] 
		this._pid_cd1 = 4103;	// [Lund pid] 
		this._pid_cu0 = 4201;	// [Lund pid] 
		this._pid_cu1 = 4203;	// [Lund pid] 
		this._pid_cs0 = 4301;	// [Lund pid] 
		this._pid_cs1 = 4303;	// [Lund pid] 
		this._pid_cc1 = 4403;	// [Lund pid] 
		this._pid_bd0 = 5101;	// [Lund pid] 
		this._pid_bd1 = 5103;	// [Lund pid] 
		this._pid_bu0 = 5201;	// [Lund pid] 
		this._pid_bu1 = 5203;	// [Lund pid] 
		this._pid_bs0 = 5301;	// [Lund pid] 
		this._pid_bs1 = 5303;	// [Lund pid] 
		this._pid_bc0 = 5401;	// [Lund pid] 
		this._pid_bc1 = 5403;	// [Lund pid] 
		this._pid_bb1 = 5503;	// [Lund pid]

        // Atomic Nuclei
        this._pid_deuterium  = 700201;    // [Lund pid]
        this._pid_tritium    = 700301;    // [Lund pid]
        this._pid_alpha      = 700202;    // [Lund pid]
        this._pid_HE3        = 700302;    // [Lund pid]

        // J^{p} = 1/2+ Baryons
        this._pid_L0    = 3122;    // [Lund pid]
        this._pid_Lc    = 4122;    // [Lund pid]
        this._pid_Lb0   = 5122;    // [Lund pid]
        this._pid_SP    = 3222;    // [Lund pid]
        this._pid_S0    = 3212;    // [Lund pid]
        this._pid_SM    = 3112;    // [Lund pid]
        this._pid_ScPP  = 4222;    // [Lund pid]
        this._pid_ScP   = 4212;    // [Lund pid]
        this._pid_Sc0   = 4112;    // [Lund pid]
        this._pid_SbP   = 5222;    // [Lund pid]
        this._pid_SbM   = 5112;    // [Lund pid]
        this._pid_X0    = 3322;    // [Lund pid]
        this._pid_XM    = 3312;    // [Lund pid]
        this._pid_XcP   = 4232;    // [Lund pid]
        this._pid_Xc0   = 4112;    // [Lund pid]
        this._pid_XcP_  = 4322;    // [Lund pid]
        this._pid_Xc0_  = 4312;    // [Lund pid]
        this._pid_XccPP = 4422;    // [Lund pid]
        this._pid_Xb0   = 5232;    // [Lund pid]
        this._pid_XbM   = 5132;    // [Lund pid]
        this._pid_Oc0   = 4332;    // [Lund pid]
        this._pid_ObM   = 5332;    // [Lund pid]

        // J^{p} = 3/2+ Baryons
        this._pid_DeltaPP  = 2224;    // [Lund pid]
        this._pid_DeltaP   = 2214;    // [Lund pid]
        this._pid_Delta0   = 2114;    // [Lund pid]
        this._pid_DeltaM   = 1114;    // [Lund pid]
        this._pid_SStarP   = 3224;    // [Lund pid]
        this._pid_SStar0   = 3214;    // [Lund pid]
        this._pid_SStarM   = 3212;    // [Lund pid]
        this._pid_SStarcPP = 4224;    // [Lund pid]
        this._pid_SStarcP  = 4214;    // [Lund pid]
        this._pid_SStarc0  = 4114;    // [Lund pid]
        this._pid_SStarbP  = 5224;    // [Lund pid]
        this._pid_SStarbM  = 5114;    // [Lund pid]
        this._pid_XStar0   = 3324;    // [Lund pid]
        this._pid_XStarM   = 3314;    // [Lund pid]
        this._pid_XStarcP  = 4324;    // [Lund pid]
        this._pid_XStarc0  = 4314;    // [Lund pid]
        this._pid_XStarb0  = 5324;    // [Lund pid]    
        this._pid_OM       = 3334;    // [Lund pid]  
        this._pid_OStarc0  = 4334;    // [Lund pid]  

        // Pseudoscalar Mesons
        this._pid_pi0  = 111;    // [Lund pid]
        this._pid_eta  = 221;    // [Lund pid]
        this._pid_eta_ = 331;    // [Lund pid]
        this._pid_etac = 441;    // [Lund pid]
        this._pid_etab = 551;    // [Lund pid]
        this._pid_k0   = 311;    // [Lund pid]
        this._pid_ks0  = 310;    // [Lund pid]
        this._pid_kl0  = 130;    // [Lund pid]
        this._pid_D    = 411;    // [Lund pid]
        this._pid_D0   = 421;    // [Lund pid]
        this._pid_Ds   = 431;    // [Lund pid]
        this._pid_B    = 521;    // [Lund pid]
        this._pid_B0   = 511;    // [Lund pid]
        this._pid_Bs0  = 531;    // [Lund pid]
        this._pid_Bc   = 541;    // [Lund pid]

        // Vector Mesons
        this._pid_r       = 213;    // [Lund pid]
        this._pid_r0      = 113;    // [Lund pid]
        this._pid_w       = 223;    // [Lund pid]
        this._pid_phi     = 333;    // [Lund pid]
        this._pid_Jpsi    = 443;    // [Lund pid]
        this._pid_U       = 553;    // [Lund pid]
        this._pid_KStar   = 323;    // [Lund pid]
        this._pid_KStar0  = 313;    // [Lund pid]
        this._pid_DStar   = 413;    // [Lund pid]
        this._pid_DStar0  = 423;    // [Lund pid]
        this._pid_DStars  = 433;    // [Lund pid]
        this._pid_BStar   = 523;    // [Lund pid]
        this._pid_BStar0  = 513;    // [Lund pid]
        this._pid_BStars0 = 533;    // [Lund pid]

        // Atomic Nuclei
        this._m_deuterium  = 1.876123928;    // [GeV/c^2]
        this._m_tritium    = 2.809432042;    // [GeV/c^2]
        this._m_alpha      = 3.727379378;    // [GeV/c^2]
        this._m_HE3        = 2.809413506;    // [GeV/c^2]

        // J^{p} = (double) 1/2+ Baryons
        this._m_L0     = (double) 1.115683;    // [GeV/c^2]
        this._m_Lc     = (double) 2.28646;     // [GeV/c^2]
        this._m_Lb0    = (double) 5.6194;      // [GeV/c^2]
        this._m_SP     = (double) 1.18937;     // [GeV/c^2]
        this._m_S0     = (double) 1.192642;    // [GeV/c^2]
        this._m_SM     = (double) 1.197449;    // [GeV/c^2]
        this._m_ScPP   = (double) 2.45398;     // [GeV/c^2]
        this._m_ScP    = (double) 2.4529;      // [GeV/c^2]
        this._m_Sc0    = (double) 2.45374;     // [GeV/c^2]
        this._m_SbP    = (double) 5.8113;      // [GeV/c^2]
        this._m_SbM    = (double) 5.8155;      // [GeV/c^2]
        this._m_X0     = (double) 1.31486;     // [GeV/c^2]
        this._m_XM     = (double) 1.32171;     // [GeV/c^2]
        this._m_XcP    = (double) 2.4678;      // [GeV/c^2]
        this._m_Xc0    = (double) 2.47088;     // [GeV/c^2]
        this._m_XcP_   = (double) 2.5756;      // [GeV/c^2]
        this._m_Xc0_   = (double) 2.5779;      // [GeV/c^2]
        this._m_Xb0    = (double) 5.7878;      // [GeV/c^2]
        this._m_XbM    = (double) 5.7911;      // [GeV/c^2]
        this._m_Oc0    = (double) 2.6952;      // [GeV/c^2]
        this._m_ObM    = (double) 6.071;       // [GeV/c^2]

        // J^{p} = (double) 3/2+ Baryons
        this._m_DeltaPP  = (double) 1.232;      // [GeV/c^2]
        this._m_DeltaP   = (double) 1.232;      // [GeV/c^2]
        this._m_Delta0   = (double) 1.232;      // [GeV/c^2]
        this._m_DeltaM   = (double) 1.232;      // [GeV/c^2]
        this._m_SStarP   = (double) 1.3828;     // [GeV/c^2]
        this._m_SStar0   = (double) 1.3837;     // [GeV/c^2]
        this._m_SStarM   = (double) 1.3872;     // [GeV/c^2]
        this._m_SStarcPP = (double) 2.5179;     // [GeV/c^2]
        this._m_SStarcP  = (double) 2.5175;     // [GeV/c^2]
        this._m_SStarc0  = (double) 2.5188;     // [GeV/c^2]
        this._m_SStarbP  = (double) 5.8321;     // [GeV/c^2]
        this._m_SStarbM  = (double) 5.8351;     // [GeV/c^2]
        this._m_XStar0   = (double) 1.53180;    // [GeV/c^2]
        this._m_XStarM   = (double) 1.5350;     // [GeV/c^2]
        this._m_XStarcP  = (double) 2.6459;     // [GeV/c^2]
        this._m_XStarc0  = (double) 2.6459;     // [GeV/c^2]
        this._m_XStarb0  = (double) 5.9455;     // [GeV/c^2]    
        this._m_OM       = (double) 1.67245;    // [GeV/c^2]  
        this._m_OStarc0  = (double) 2.7659;     // [GeV/c^2]  

        // Pseudoscalar Mesons
        this._m_pi0   = (double) 0.1349766;    // [GeV/c^2]
        this._m_eta   = (double) 0.547862;     // [GeV/c^2]
        this._m_eta_  = (double) 0.95778;      // [GeV/c^2]
        this._m_etac  = (double) 0.29836;      // [GeV/c^2]
        this._m_etab  = (double) 0.93980;      // [GeV/c^2]
        this._m_k0    = (double) 0.497614;     // [GeV/c^2]
        this._m_ks0   = (double) 0.497614;     // [GeV/c^2]
        this._m_kl0   = (double) 0.497614;     // [GeV/c^2]
        this._m_D     = (double) 1.86961;      // [GeV/c^2]
        this._m_D0    = (double) 1.86484;      // [GeV/c^2]
        this._m_Ds    = (double) 1.96830;      // [GeV/c^2]
        this._m_B     = (double) 5.27926;      // [GeV/c^2]
        this._m_B0    = (double) 5.27958;      // [GeV/c^2]
        this._m_Bs0   = (double) 5.36677;      // [GeV/c^2]
        this._m_Bc    = (double) 6.2756;       // [GeV/c^2]

        // Vector Mesons
        this._m_r       = (double) 0.77511;     // [GeV/c^2]
        this._m_r0      = (double) 0.77526;     // [GeV/c^2]
        this._m_w       = (double) 0.78265;     // [GeV/c^2]
        this._m_phi     = (double) 1.019461;    // [GeV/c^2]
        this._m_Jpsi    = (double) 3.096916;    // [GeV/c^2]
        this._m_U       = (double) 9.46030;     // [GeV/c^2]
        this._m_KStar   = (double) 0.89166;     // [GeV/c^2]
        this._m_KStar0  = (double) 0.89581;     // [GeV/c^2]
        this._m_DStar   = (double) 2.01026;     // [GeV/c^2]
        this._m_DStar0  = (double) 2.00696;     // [GeV/c^2]
        this._m_DStars  = (double) 2.1121;      // [GeV/c^2]
        this._m_BStar   = (double) 5.3252;      // [GeV/c^2]
        this._m_BStar0  = (double) 5.3252;     // [GeV/c^2]
        this._m_BStars0 = (double) 5.4154;      // [GeV/c^2]

        // Atomic nuclei
        this._q_deuterium  = 1;    // [e]
        this._q_tritium    = 1;    // [e]
        this._q_alpha      = 2;    // [e]
        this._q_HE3        = 1;    // [e]

        // J^{p} = 1/2+ Baryons
        this._q_L0    = 0;     // [e]
        this._q_Lc    = 1;     // [e]
        this._q_Lb0   = 0;     // [e]
        this._q_SP    = 1;     // [e]
        this._q_S0    = 0;     // [e]
        this._q_SM    = -1;    // [e]
        this._q_ScPP  = 2;     // [e]
        this._q_ScP   = 1;     // [e]
        this._q_Sc0   = 0;     // [e]
        this._q_SbP   = 1;     // [e]
        this._q_SbM   = -1;    // [e]
        this._q_X0    = 0;     // [e]
        this._q_XM    = -1;    // [e]
        this._q_XcP   = 1;     // [e]
        this._q_Xc0   = 0;     // [e]
        this._q_XcP_  = 1;     // [e]
        this._q_Xc0_  = 0;     // [e]
        this._q_XccPP = 2;     // [e]
        this._q_Xb0   = 0;     // [e]
        this._q_XbM   = -1;    // [e]
        this._q_Oc0   = 0;     // [e]
        this._q_ObM   = -1;    // [e]

        // J^{p} = 3/2+ Baryons
        this._q_DeltaPP  = 2;     // [e]
        this._q_DeltaP   = 1;     // [e]
        this._q_Delta0   = 0;     // [e]
        this._q_DeltaM   = -1;    // [e]
        this._q_SStarP   = 1;     // [e]
        this._q_SStar0   = 0;     // [e]
        this._q_SStarM   = -1;    // [e]
        this._q_SStarcPP = 2;     // [e]
        this._q_SStarcP  = 1;     // [e]
        this._q_SStarc0  = 0;     // [e]
        this._q_SStarbP  = 1;     // [e]
        this._q_SStarbM  = -1;    // [e]
        this._q_XStar0   = 0;     // [e]
        this._q_XStarM   = -1;    // [e]
        this._q_XStarcP  = 1;     // [e]
        this._q_XStarc0  = 0;     // [e]
        this._q_XStarb0  = 0;     // [e]    
        this._q_OM       = -1;    // [e]  
        this._q_OStarc0  = 0;     // [e]  

        // Pseudoscalar Mesons
        this._q_pi0  = 0;    // [e]
        this._q_eta  = 0;    // [e]
        this._q_eta_ = 0;    // [e]
        this._q_etac = 0;    // [e]
        this._q_etab = 0;    // [e]
        this._q_k0   = 0;    // [e]
        this._q_ks0  = 0;    // [e]
        this._q_kl0  = 0;    // [e]
        this._q_D    = 1;    // [e]
        this._q_D0   = 0;    // [e]
        this._q_Ds   = 1;    // [e]
        this._q_B    = 1;    // [e]
        this._q_B0   = 0;    // [e]
        this._q_Bs0  = 0;    // [e]
        this._q_Bc   = 1;    // [e]

        // Vector Mesons
        this._q_r       = 1;    // [e]
        this._q_r0      = 0;    // [e]
        this._q_w       = 0;    // [e]
        this._q_phi     = 0;    // [e]
        this._q_Jpsi    = 0;    // [e]
        this._q_U       = 0;    // [e]
        this._q_KStar   = 1;    // [e]
        this._q_KStar0  = 0;    // [e]
        this._q_DStar   = 1;    // [e]
        this._q_DStar0  = 0;    // [e]
        this._q_DStars  = 1;    // [e]
        this._q_BStar   = 1;    // [e]
        this._q_BStar0  = 0;    // [e]
        this._q_BStars0 = 0;    // [e]

        // Atomic nuclei
        this._n_deuterium  = new String("deuterium");
        this._n_tritium    = new String("tritium");
        this._n_alpha      = new String("alpha");
        this._n_HE3        = new String("HE3");

        // J^{p} = new String("")/2+ Baryons
        this._n_L0    = new String("L0");
        this._n_Lc    = new String("Lc");
        this._n_Lb0   = new String("Lb0");
        this._n_SP    = new String("SP");
        this._n_S0    = new String("S0");
        this._n_SM    = new String("SM");
        this._n_ScPP  = new String("ScPP");
        this._n_ScP   = new String("ScP");
        this._n_Sc0   = new String("Sc0");
        this._n_SbP   = new String("SbP");
        this._n_SbM   = new String("SbM");
        this._n_X0    = new String("X0");
        this._n_XM    = new String("XM");
        this._n_XcP   = new String("XcP");
        this._n_Xc0   = new String("Xc0");
        this._n_XcP_  = new String("XcP_");
        this._n_Xc0_  = new String("Xc0");
        this._n_XccPP = new String("XccPP");
        this._n_Xb0   = new String("Xb0");
        this._n_XbM   = new String("XbM");
        this._n_Oc0   = new String("Oc0");
        this._n_ObM   = new String("ObM");

        // J^{p} = new String("")/2+ Baryons
        this._n_DeltaPP  = new String("DeltaPP");
        this._n_DeltaP   = new String("DeltaP");
        this._n_Delta0   = new String("Delta0");
        this._n_DeltaM   = new String("DeltaM");
        this._n_SStarP   = new String("SStarP");
        this._n_SStar0   = new String("SStar0");
        this._n_SStarM   = new String("SStarM");
        this._n_SStarcPP = new String("SStarcPP");
        this._n_SStarcP  = new String("SStarcP");
        this._n_SStarc0  = new String("SStarc0");
        this._n_SStarbP  = new String("SStarc0");
        this._n_SStarbM  = new String("SStarbM");
        this._n_XStar0   = new String("XStar0");
        this._n_XStarM   = new String("XStarM");
        this._n_XStarcP  = new String("XStarcP");
        this._n_XStarc0  = new String("XStarc0");
        this._n_XStarb0  = new String("XStarb0");    
        this._n_OM       = new String("OM");  
        this._n_OStarc0  = new String("OStarc0");  

        // Pseudoscalar Mesons
        this._n_pi0  = new String("pi0");
        this._n_eta  = new String("eta");
        this._n_eta_ = new String("eta_");
        this._n_etac = new String("etac");
        this._n_etab = new String("etab");
        this._n_k0   = new String("k0");
        this._n_ks0  = new String("ks0");
        this._n_kl0  = new String("kl0");
        this._n_D    = new String("D");
        this._n_D0   = new String("D0");
        this._n_Ds   = new String("Ds");
        this._n_B    = new String("B");
        this._n_B0   = new String("B0");
        this._n_Bs0  = new String("Bs0");
        this._n_Bc   = new String("Bc");

        // Vector Mesons
        this._n_r       = new String("r");
        this._n_r0      = new String("r0");
        this._n_w       = new String("w");
        this._n_phi     = new String("phi");
        this._n_Jpsi    = new String("Jpsi");
        this._n_U       = new String("U");
        this._n_KStar   = new String("KStar");
        this._n_KStar0  = new String("KStar0");
        this._n_DStar   = new String("DStar");
        this._n_DStar0  = new String("DStar0");
        this._n_DStars  = new String("DStars");
        this._n_BStar   = new String("BStar");
        this._n_BStar0  = new String("BStar0");
        this._n_BStars0 = new String("BStars0");

        // Add Masses //
        // Atomic nuclei
        this.addMMapEntry(this._pid_deuterium,this._m_deuterium);
        this.addMMapEntry(this._pid_tritium,this._m_tritium);
        this.addMMapEntry(this._pid_alpha,this._m_alpha);
        this.addMMapEntry(this._pid_HE3,this._m_HE3);

        // J^{p} = 1/2+ Baryons
        this.addMMapEntry(this._pid_L0,this._m_L0);
        this.addMMapEntry(this._pid_Lc,this._m_Lc);
        this.addMMapEntry(this._pid_Lb0,this._m_Lb0);
        this.addMMapEntry(this._pid_SP,this._m_SP);
        this.addMMapEntry(this._pid_S0,this._m_S0);
        this.addMMapEntry(this._pid_SM,this._m_SM);
        this.addMMapEntry(this._pid_ScPP,this._m_ScPP);
        this.addMMapEntry(this._pid_ScP,this._m_ScP);
        this.addMMapEntry(this._pid_Sc0,this._m_Sc0);
        this.addMMapEntry(this._pid_SbP,this._m_SbP);
        this.addMMapEntry(this._pid_SbM,this._m_SbM);
        this.addMMapEntry(this._pid_X0,this._m_X0);
        this.addMMapEntry(this._pid_XM,this._m_XM);
        this.addMMapEntry(this._pid_XcP,this._m_XcP);
        this.addMMapEntry(this._pid_Xc0,this._m_Xc0);
        this.addMMapEntry(this._pid_XcP_,this._m_XcP_);
        this.addMMapEntry(this._pid_Xc0_,this._m_Xc0_);
        this.addMMapEntry(this._pid_XccPP,this._m_XccPP);
        this.addMMapEntry(this._pid_Xb0,this._m_Xb0);
        this.addMMapEntry(this._pid_XbM,this._m_XbM);
        this.addMMapEntry(this._pid_Oc0,this._m_Oc0);
        this.addMMapEntry(this._pid_ObM,this._m_ObM);

        // J^{p} = 3/2+ Baryons
        this.addMMapEntry(this._pid_DeltaPP,this._m_DeltaPP);
        this.addMMapEntry(this._pid_DeltaP,this._m_DeltaP);
        this.addMMapEntry(this._pid_Delta0,this._m_Delta0);
        this.addMMapEntry(this._pid_DeltaM,this._m_DeltaM);
        this.addMMapEntry(this._pid_SStarP,this._m_SStarP);
        this.addMMapEntry(this._pid_SStar0,this._m_SStar0);
        this.addMMapEntry(this._pid_SStarM,this._m_SStarM);
        this.addMMapEntry(this._pid_SStarcPP,this._m_SStarcPP);
        this.addMMapEntry(this._pid_SStarcP,this._m_SStarcP);
        this.addMMapEntry(this._pid_SStarc0,this._m_SStarc0);
        this.addMMapEntry(this._pid_SStarbP,this._m_SStarbP);
        this.addMMapEntry(this._pid_SStarbM,this._m_SStarbM);
        this.addMMapEntry(this._pid_XStar0,this._m_XStar0);
        this.addMMapEntry(this._pid_XStarM,this._m_XStarM);
        this.addMMapEntry(this._pid_XStarcP,this._m_XStarcP);
        this.addMMapEntry(this._pid_XStarc0,this._m_XStarc0);
        this.addMMapEntry(this._pid_XStarb0,this._m_XStarb0);  
        this.addMMapEntry(this._pid_OM,this._m_OM);
        this.addMMapEntry(this._pid_OStarc0,this._m_OStarc0);  

        // Pseudoscalar Mesons
        this.addMMapEntry(this._pid_pi0,this._m_pi0);
        this.addMMapEntry(this._pid_eta,this._m_eta);
        this.addMMapEntry(this._pid_eta_,this._m_eta_);
        this.addMMapEntry(this._pid_etac,this._m_etac);
        this.addMMapEntry(this._pid_etab,this._m_etab);
        this.addMMapEntry(this._pid_k0,this._m_k0);
        this.addMMapEntry(this._pid_ks0,this._m_ks0);
        this.addMMapEntry(this._pid_kl0,this._m_kl0);
        this.addMMapEntry(this._pid_D,this._m_D);
        this.addMMapEntry(this._pid_D0,this._m_D0);
        this.addMMapEntry(this._pid_Ds,this._m_Ds);
        this.addMMapEntry(this._pid_B,this._m_B);
        this.addMMapEntry(this._pid_B0,this._m_B0);
        this.addMMapEntry(this._pid_Bs0,this._m_Bs0);
        this.addMMapEntry(this._pid_Bc,this._m_Bc);

        // Vector Mesons
        this.addMMapEntry(this._pid_r,this._m_r);
        this.addMMapEntry(this._pid_r0,this._m_r0);
        this.addMMapEntry(this._pid_w,this._m_w);
        this.addMMapEntry(this._pid_phi,this._m_phi);
        this.addMMapEntry(this._pid_Jpsi,this._m_Jpsi);
        this.addMMapEntry(this._pid_U,this._m_U);
        this.addMMapEntry(this._pid_KStar,this._m_KStar);
        this.addMMapEntry(this._pid_KStar0,this._m_KStar0);
        this.addMMapEntry(this._pid_DStar,this._m_DStar);
        this.addMMapEntry(this._pid_DStar0,this._m_DStar0);
        this.addMMapEntry(this._pid_DStars,this._m_DStars);
        this.addMMapEntry(this._pid_BStar,this._m_BStar);
        this.addMMapEntry(this._pid_BStar0,this._m_BStar0);
        this.addMMapEntry(this._pid_BStars0,this._m_BStars0);

        // Add charges //
        // Atomic nuclei
        this.addQMapEntry(this._pid_deuterium,this._q_deuterium);
        this.addQMapEntry(this._pid_tritium,this._q_tritium);
        this.addQMapEntry(this._pid_alpha,this._q_alpha);
        this.addQMapEntry(this._pid_HE3,this._q_HE3);

        // J^{p} = 1/2+ Baryons
        this.addQMapEntry(this._pid_L0,this._q_L0);
        this.addQMapEntry(this._pid_Lc,this._q_Lc);
        this.addQMapEntry(this._pid_Lb0,this._q_Lb0);
        this.addQMapEntry(this._pid_SP,this._q_SP);
        this.addQMapEntry(this._pid_S0,this._q_S0);
        this.addQMapEntry(this._pid_SM,this._q_SM);
        this.addQMapEntry(this._pid_ScPP,this._q_ScPP);
        this.addQMapEntry(this._pid_ScP,this._q_ScP);
        this.addQMapEntry(this._pid_Sc0,this._q_Sc0);
        this.addQMapEntry(this._pid_SbP,this._q_SbP);
        this.addQMapEntry(this._pid_SbM,this._q_SbM);
        this.addQMapEntry(this._pid_X0,this._q_X0);
        this.addQMapEntry(this._pid_XM,this._q_XM);
        this.addQMapEntry(this._pid_XcP,this._q_XcP);
        this.addQMapEntry(this._pid_Xc0,this._q_Xc0);
        this.addQMapEntry(this._pid_XcP_,this._q_XcP_);
        this.addQMapEntry(this._pid_Xc0_,this._q_Xc0_);
        this.addQMapEntry(this._pid_XccPP,this._q_XccPP);
        this.addQMapEntry(this._pid_Xb0,this._q_Xb0);
        this.addQMapEntry(this._pid_XbM,this._q_XbM);
        this.addQMapEntry(this._pid_Oc0,this._q_Oc0);
        this.addQMapEntry(this._pid_ObM,this._q_ObM);

        // J^{p} = 3/2+ Baryons
        this.addQMapEntry(this._pid_DeltaPP,this._q_DeltaPP);
        this.addQMapEntry(this._pid_DeltaP,this._q_DeltaP);
        this.addQMapEntry(this._pid_Delta0,this._q_Delta0);
        this.addQMapEntry(this._pid_DeltaM,this._q_DeltaM);
        this.addQMapEntry(this._pid_SStarP,this._q_SStarP);
        this.addQMapEntry(this._pid_SStar0,this._q_SStar0);
        this.addQMapEntry(this._pid_SStarM,this._q_SStarM);
        this.addQMapEntry(this._pid_SStarcPP,this._q_SStarcPP);
        this.addQMapEntry(this._pid_SStarcP,this._q_SStarcP);
        this.addQMapEntry(this._pid_SStarc0,this._q_SStarc0);
        this.addQMapEntry(this._pid_SStarbP,this._q_SStarbP);
        this.addQMapEntry(this._pid_SStarbM,this._q_SStarbM);
        this.addQMapEntry(this._pid_XStar0,this._q_XStar0);
        this.addQMapEntry(this._pid_XStarM,this._q_XStarM);
        this.addQMapEntry(this._pid_XStarcP,this._q_XStarcP);
        this.addQMapEntry(this._pid_XStarc0,this._q_XStarc0);
        this.addQMapEntry(this._pid_XStarb0,this._q_XStarb0);  
        this.addQMapEntry(this._pid_OM,this._q_OM);
        this.addQMapEntry(this._pid_OStarc0,this._q_OStarc0);  

        // Pseudoscalar Mesons
        this.addQMapEntry(this._pid_pi0,this._q_pi0);
        this.addQMapEntry(this._pid_eta,this._q_eta);
        this.addQMapEntry(this._pid_eta_,this._q_eta_);
        this.addQMapEntry(this._pid_etac,this._q_etac);
        this.addQMapEntry(this._pid_etab,this._q_etab);
        this.addQMapEntry(this._pid_k0,this._q_k0);
        this.addQMapEntry(this._pid_ks0,this._q_ks0);
        this.addQMapEntry(this._pid_kl0,this._q_kl0);
        this.addQMapEntry(this._pid_D,this._q_D);
        this.addQMapEntry(this._pid_D0,this._q_D0);
        this.addQMapEntry(this._pid_Ds,this._q_Ds);
        this.addQMapEntry(this._pid_B,this._q_B);
        this.addQMapEntry(this._pid_B0,this._q_B0);
        this.addQMapEntry(this._pid_Bs0,this._q_Bs0);
        this.addQMapEntry(this._pid_Bc,this._q_Bc);

        // Vector Mesons
        this.addQMapEntry(this._pid_r,this._q_r);
        this.addQMapEntry(this._pid_r0,this._q_r0);
        this.addQMapEntry(this._pid_w,this._q_w);
        this.addQMapEntry(this._pid_phi,this._q_phi);
        this.addQMapEntry(this._pid_Jpsi,this._q_Jpsi);
        this.addQMapEntry(this._pid_U,this._q_U);
        this.addQMapEntry(this._pid_KStar,this._q_KStar);
        this.addQMapEntry(this._pid_KStar0,this._q_KStar0);
        this.addQMapEntry(this._pid_DStar,this._q_DStar);
        this.addQMapEntry(this._pid_DStar0,this._q_DStar0);
        this.addQMapEntry(this._pid_DStars,this._q_DStars);
        this.addQMapEntry(this._pid_BStar,this._q_BStar);
        this.addQMapEntry(this._pid_BStar0,this._q_BStar0);
        this.addQMapEntry(this._pid_BStars0,this._q_BStars0);

        // Add names //
        // Atomic nuclei
        this.addNMapEntry(this._pid_deuterium,this._n_deuterium);
        this.addNMapEntry(this._pid_tritium,this._n_tritium);
        this.addNMapEntry(this._pid_alpha,this._n_alpha);
        this.addNMapEntry(this._pid_HE3,this._n_HE3);

        // J^{p} = 1/2+ Baryons
        this.addNMapEntry(this._pid_L0,this._n_L0);
        this.addNMapEntry(this._pid_Lc,this._n_Lc);
        this.addNMapEntry(this._pid_Lb0,this._n_Lb0);
        this.addNMapEntry(this._pid_SP,this._n_SP);
        this.addNMapEntry(this._pid_S0,this._n_S0);
        this.addNMapEntry(this._pid_SM,this._n_SM);
        this.addNMapEntry(this._pid_ScPP,this._n_ScPP);
        this.addNMapEntry(this._pid_ScP,this._n_ScP);
        this.addNMapEntry(this._pid_Sc0,this._n_Sc0);
        this.addNMapEntry(this._pid_SbP,this._n_SbP);
        this.addNMapEntry(this._pid_SbM,this._n_SbM);
        this.addNMapEntry(this._pid_X0,this._n_X0);
        this.addNMapEntry(this._pid_XM,this._n_XM);
        this.addNMapEntry(this._pid_XcP,this._n_XcP);
        this.addNMapEntry(this._pid_Xc0,this._n_Xc0);
        this.addNMapEntry(this._pid_XcP_,this._n_XcP_);
        this.addNMapEntry(this._pid_Xc0_,this._n_Xc0_);
        this.addNMapEntry(this._pid_XccPP,this._n_XccPP);
        this.addNMapEntry(this._pid_Xb0,this._n_Xb0);
        this.addNMapEntry(this._pid_XbM,this._n_XbM);
        this.addNMapEntry(this._pid_Oc0,this._n_Oc0);
        this.addNMapEntry(this._pid_ObM,this._n_ObM);

        // J^{p} = 3/2+ Baryons
        this.addNMapEntry(this._pid_DeltaPP,this._n_DeltaPP);
        this.addNMapEntry(this._pid_DeltaP,this._n_DeltaP);
        this.addNMapEntry(this._pid_Delta0,this._n_Delta0);
        this.addNMapEntry(this._pid_DeltaM,this._n_DeltaM);
        this.addNMapEntry(this._pid_SStarP,this._n_SStarP);
        this.addNMapEntry(this._pid_SStar0,this._n_SStar0);
        this.addNMapEntry(this._pid_SStarM,this._n_SStarM);
        this.addNMapEntry(this._pid_SStarcPP,this._n_SStarcPP);
        this.addNMapEntry(this._pid_SStarcP,this._n_SStarcP);
        this.addNMapEntry(this._pid_SStarc0,this._n_SStarc0);
        this.addNMapEntry(this._pid_SStarbP,this._n_SStarbP);
        this.addNMapEntry(this._pid_SStarbM,this._n_SStarbM);
        this.addNMapEntry(this._pid_XStar0,this._n_XStar0);
        this.addNMapEntry(this._pid_XStarM,this._n_XStarM);
        this.addNMapEntry(this._pid_XStarcP,this._n_XStarcP);
        this.addNMapEntry(this._pid_XStarc0,this._n_XStarc0);
        this.addNMapEntry(this._pid_XStarb0,this._n_XStarb0);  
        this.addNMapEntry(this._pid_OM,this._n_OM);
        this.addNMapEntry(this._pid_OStarc0,this._n_OStarc0);  

        // Pseudoscalar Mesons
        this.addNMapEntry(this._pid_pi0,this._n_pi0);
        this.addNMapEntry(this._pid_eta,this._n_eta);
        this.addNMapEntry(this._pid_eta_,this._n_eta_);
        this.addNMapEntry(this._pid_etac,this._n_etac);
        this.addNMapEntry(this._pid_etab,this._n_etab);
        this.addNMapEntry(this._pid_k0,this._n_k0);
        this.addNMapEntry(this._pid_ks0,this._n_ks0);
        this.addNMapEntry(this._pid_kl0,this._n_kl0);
        this.addNMapEntry(this._pid_D,this._n_D);
        this.addNMapEntry(this._pid_D0,this._n_D0);
        this.addNMapEntry(this._pid_Ds,this._n_Ds);
        this.addNMapEntry(this._pid_B,this._n_B);
        this.addNMapEntry(this._pid_B0,this._n_B0);
        this.addNMapEntry(this._pid_Bs0,this._n_Bs0);
        this.addNMapEntry(this._pid_Bc,this._n_Bc);

        // Vector Mesons
        this.addNMapEntry(this._pid_r,this._n_r);
        this.addNMapEntry(this._pid_r0,this._n_r0);
        this.addNMapEntry(this._pid_w,this._n_w);
        this.addNMapEntry(this._pid_phi,this._n_phi);
        this.addNMapEntry(this._pid_Jpsi,this._n_Jpsi);
        this.addNMapEntry(this._pid_U,this._n_U);
        this.addNMapEntry(this._pid_KStar,this._n_KStar);
        this.addNMapEntry(this._pid_KStar0,this._n_KStar0);
        this.addNMapEntry(this._pid_DStar,this._n_DStar);
        this.addNMapEntry(this._pid_DStar0,this._n_DStar0);
        this.addNMapEntry(this._pid_DStars,this._n_DStars);
        this.addNMapEntry(this._pid_BStar,this._n_BStar);
        this.addNMapEntry(this._pid_BStar0,this._n_BStar0);
        this.addNMapEntry(this._pid_BStars0,this._n_BStars0);
        
    } // constructor stub

} // class
