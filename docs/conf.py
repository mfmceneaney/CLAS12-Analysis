# Configuration file for the Sphinx documentation builder.
#
# For the full list of built-in configuration values, see the documentation:
# https://www.sphinx-doc.org/en/master/usage/configuration.html
import os, subprocess

# Check if we're running on Read the Docs' servers
read_the_docs_build = os.environ.get('READTHEDOCS', None) == 'True'

if read_the_docs_build or True:

    # Install groovy
    print("INFO: Installing sdk...")
    subprocess.call('curl -s "https://get.sdkman.io" | bash', shell=True)#NOTE: Check download instructions on SDK page.
    print("INFO: Installing groovy with sdk...")
    subprocess.call('source "$HOME/.sdkman/bin/sdkman-init.sh; sdk install groovy', shell=True)

    # Run `groovydoc`
    input_files = '../app/src/main/groovy/org/jlab/analysis/*.groovy'
    output_dir = '_static'
    print("INFO: Creating groovy docs...")
    subprocess.call('groovydoc -d '+os.path.join(output_dir)+' '+os.path.join(input_files), shell=True)

# -- Project information -----------------------------------------------------
# https://www.sphinx-doc.org/en/master/usage/configuration.html#project-information

project = 'CLAS12-Analysis'
copyright = '2025, Matthew F. McEneaney'
author = 'Matthew F. McEneaney'
release = '1.0.0'

# -- General configuration ---------------------------------------------------
# https://www.sphinx-doc.org/en/master/usage/configuration.html#general-configuration

extensions = [
    'sphinx.ext.mathjax',
    'sphinx.ext.duration',
    'sphinx.ext.doctest',
    'sphinx.ext.autodoc',
    'sphinx.ext.autosummary',
    'sphinx.ext.intersphinx',
    'myst_parser',
    ]

intersphinx_mapping = {
    'python': ('https://docs.python.org/3/', None),
    'sphinx': ('https://www.sphinx-doc.org/en/master/', None),
}
intersphinx_disabled_domains = ['std']

templates_path = ['_templates']
exclude_patterns = ['_build', 'Thumbs.db', '.DS_Store']

# myst-parser configuration
myst_enable_extensions = [
    "dollarmath",
    "amsmath",
]

# -- Options for HTML output -------------------------------------------------
# https://www.sphinx-doc.org/en/master/usage/configuration.html#options-for-html-output

html_theme = 'sphinx_rtd_theme'
html_static_path = ['_static']
# html_logo = 'clas12_analysis_'+version+'_logo.png'
html_theme_options = {"logo_only": True, "sticky_navigation": False}
