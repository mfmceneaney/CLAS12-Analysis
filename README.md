# CLAS12-Analysis
Generic analysis for CLAS12 in groovy

## Dependencies
* [CLAS12-Offline-Software](https://github.com/JeffersonLab/clas12-offline-software)
* [CLASQADB](https://github.com/JeffersonLab/clasqaDB)
* [J2ROOT](https://github.com/drewkenjo/j2root)

## Installation
Use the following commands in bash (csh scripts also provided) to setup the repository
```bash
git clone https://github.com/mfmceneaney/CLAS12-Analysis.git
cd CLAS12-Analysis
source bin/setup.sh
```

Then add the following to your startup script:
```bash
pushd /path/to/CLAS12-Analysis >> /dev/null
source env.sh
popd >> /dev/null
``` 
### Notes for installing on ifarm

Currently the default scons version 2.5 on ifarm does not support python 2.3, so just run the following before sourcing the setup script to get around this.
```bash
alias scons "/usr/bin/env python2 `which scons`"
```

Depending on what java version you have, in particular for java 8 on ifarm, you need to edit the setup script by commenting and uncommenting these two lines:
```bash
jar -c -f clasqa.jar clasqa/*.groovy # Comment this
# jar cf clasqa.jar clasqa/*.groovy  # Uncomment this
```

Finally, you might have to run the following so that gradle can jobs can read your gradle files:
```bash
chmod +r ~/.gradle/daemon/<version>/registry.bin
```

## Getting Started
You should now be able to run the ```an-groovy``` from the command line and see some version info pop up.
Use ```an-groovy -h/--help``` to see a list of available options with basic descriptions.

The main purpose of this library is being able to compute kinematics for all unique combinations of a 
given set of particles (e.g. proton pion pairs) without the hassle of rewriting your analysis and plugging in different numbers every time.

This tool only reads HIPO files (see [CLAS12-Offline-Software](https://github.com/JeffersonLab/clas12-offline-software)) and outputs to 
[ROOT](https://root.cern) files.

You can also follow the examples to see how you might add customized kinematic variables to
an analysis or manually select other options.  Please note the Constants class is not necessarily up to date and you should be especially careful to correctly set the beam energy and target mass.

#

Contact: matthew.mceneaney@duke.edu
