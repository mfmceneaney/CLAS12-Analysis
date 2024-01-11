# CLAS12-Analysis
Generic analysis for CLAS12 in groovy

## Dependencies
* [CLAS12-Offline-Software](https://github.com/JeffersonLab/clas12-offline-software)
* [CLASQADB](https://github.com/JeffersonLab/clasqaDB)
* [J2ROOT](https://github.com/drewkenjo/j2root)

## Pre-requisites
To build the project you will need the following additional tools:
* groovy
* gradle
* scons
* maven

Note that this project is undere active development and has only been tested for jdk/21.0.1.  On ifarm you may need to module load this java version.

## Installation
Use the following commands in bash (csh scripts also provided) to setup the repository
```bash
git clone --recurse-submodules https://github.com/mfmceneaney/CLAS12-Analysis.git
cd CLAS12-Analysis
source bin/setup.sh
```

Then add the following to your startup script:
```bash
pushd /path/to/CLAS12-Analysis >> /dev/null
#Uncomment this line if using csh: cd j2root; source setup.csh; cd ..;
source env.sh
popd >> /dev/null
``` 
### Notes for installing on ifarm

Currently the default scons version 2.5 on ifarm does not support python 2.3, so just run the following before sourcing the setup script to get around this.
```bash
# For interactive use
alias scons "/usr/bin/env python2 `which scons`"

# For jobs
function scons {
    /usr/bin/env python2 `which scons`
}
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

However, you can run into thread permissions errors running gradle from `an-groovy` in jobs so it is better to just set `$CLASSPATH` manually like so (borrowed from `bin/clara-shell` script in `$CLARA_HOME`):

```bash
# set default classpath
if [ -z "${CLASSPATH}" ]; then
    CLASSPATH="${CLARA_HOME}/lib/*"

    # Add every plugin
    for plugin in "${plugins_dir}"/*/; do
        plugin=${plugin%*/}
        if [ "${plugin##*/}" = "clas12" ]; then # COAT has special needs
            CLASSPATH+=":${plugin}/lib/clas/*:${plugin}/lib/services/*"
        else
            CLASSPATH+=":${plugin}/services/*:${plugin}/lib/*"
        fi
    done

    CLASSPATH+=":${CLARA_HOME}/services/*"
    export CLASSPATH
fi
```

Then you can just use `$C12ANALYSIS/bin/run.sh` which runs directly from groovy.

## Getting Started
You should now be able to run the ```an-groovy``` from the command line and see some version info pop up.
Use ```an-groovy -h/--help``` to see a list of available options with basic descriptions.

The main purpose of this library is being able to compute kinematics for all unique combinations of a 
given set of particles (e.g. proton pion pairs) without the hassle of rewriting your analysis and plugging in different numbers every time.

This tool only reads HIPO files (see [CLAS12-Offline-Software](https://github.com/JeffersonLab/clas12-offline-software)) and outputs to 
[ROOT](https://root.cern) files.

You can also follow the examples to see how you might add customized kinematic variables to
an analysis or manually select other options.  Please note the Constants class is not necessarily up to date and you should be especially careful to correctly set the beam energy and target mass.

### If you already have J2ROOT and CLASQADB setup
Assuming your ```$CLASSPATH``` is setup correctly you can instead run:
```bash
$C12ANALYSIS/bin/run.sh --help
```

#

Contact: matthew.mceneaney@duke.edu
