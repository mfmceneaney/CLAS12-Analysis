# CLAS12-Analysis
Generic analysis for CLAS12 in groovy

## Dependencies
* [CLAS12-Offline-Software](https://github.com/JeffersonLab/clas12-offline-software) Make sure your ```$CLASSPATH``` is properly set.
* [CLASQADB](https://github.com/JeffersonLab/clasqaDB)
* [J2ROOT](https://github.com/drewkenjo/j2root)

## Installation
Use the following commands in bash (csh scripts also provided) to setup the repository
```bash
git clone https://github.com/mfmceneaney/CLAS12-Analysis.git
cd CLAS12-Analysis
source setup/setup.sh
```
Then add the following to your startup script:
```bash
pushd /path/to/CLAS12-Analysis >> /dev/null
source env.sh
popd >> /dev/null
``` 

#

Contact: matthew.mceneaney@duke.edu
