# CLAS12-Analysis
Generic analysis for CLAS12 in groovy to read HIPO events selecting all unique final state particle combinations corresponding to a given topology,
compute generic event-level kinematics commonly used for SIDIS analyses, and output the selected combinations and their respective kinematics to ROOT TNTuples.

The main purpose of this tool is to to compute event-level kinematics for all unique combinations of a 
given set of particles (e.g. proton pion pairs) without the hassle of rewriting your code and plugging in different configurations every time.

This tool only reads HIPO files (see [CLAS12-Offline-Software](https://github.com/JeffersonLab/clas12-offline-software)) and outputs to 
[ROOT](https://root.cern) files.

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

Note that this project is under active development and has only been tested for jdk/17.0.2 and groovy 4.0.3.  On ifarm you may need to module load this java version.  You may also need to update the library versions in `gradle/libs.versions.toml` and the java version in `app/build.gradle`.

## Installation
Begin by cloning the repository and running the setup script
```bash
git clone --recurse-submodules https://github.com/mfmceneaney/CLAS12-Analysis.git
cd CLAS12-Analysis
source bin/setup.sh
```

Then source the environment script from your startup script:
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

Finally, you might have to run the following so that gradle can SLURM jobs can read your gradle files:
```bash
chmod +r ~/.gradle/daemon/<version>/registry.bin
```

### Setting your CLASSPATH
To set your `$CLASSPATH` manually add the following to your startup script (borrowed from `bin/clara-shell` script in `$CLARA_HOME`):

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
and run from `$C12ANALYSIS/bin/run.sh`.

### If you already have J2ROOT and CLASQADB setup
Assuming your `$CLASSPATH` is configured correctly, you can proceed without further setup.

## Getting Started
You should now be able to run the application with `$C12ANALYSIS/bin/run.sh` which runs directly from groovy.
Some version info will pop up if you do not supply any arguments.
Use the `-h` or  `--help` option to see a detailed list of all available command line options and arguments.

As a legacy option, you may also run the application with `$C12ANALYSIS/bin/an-groovy`, which will automatically use the correct `$CLASSPATH`.  However, you can run into thread permissions errors running gradle from this script in SLURM jobs so it is better to just set your `$CLASSPATH` manually and use `$C12ANALYSIS/bin/run.sh` instead.

Although this tool is primarily intended to have a generic application and primarily be used via execution of `$C12ANALYSIS/bin/run.sh` from the command line, you may also configure and run an analysis with more direct control over the configuration process by using a groovy script.  See the `examples` folder for, well, examples!

Please note, the `Constants` and `ExtendedConstants` classes arrre not necessarily up to date, and you should also be especially careful to correctly set the beam energy and target mass.

## Documentation
Check out our [Read The Docs](https://clas12-analysis.readthedocs.io/en/latest/) page for the full groovy docs!

#

Contact: matthew.mceneaney@duke.edu
