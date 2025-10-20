# Dockerfile for building/running CLAS12-Analysis
# Base on Ubuntu LTS with common build tools
FROM docker.io/rootproject/root:6.36.00-ubuntu25.04

ENV DEBIAN_FRONTEND=noninteractive

# System dependencies (adjust if needed)
RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential \
    cmake \
    git \
    wget \
    curl \
    unzip \
    zip \
    gnupg \
    software-properties-common \
    openjdk-21-jdk \
    maven \
    python3 \
    python3-pip \
    scons \
    ca-certificates \
    locales \
    && rm -rf /var/lib/apt/lists/*

# Set JAVA_HOME environment variable
ENV JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
ENV PATH=$JAVA_HOME/bin:$PATH

# Define Groovy version
ENV GROOVY_VERSION=5.0.0

# Download Groovy binary distribution
RUN wget https://groovy.jfrog.io/artifactory/dist-release-local/groovy-zips/apache-groovy-binary-${GROOVY_VERSION}.zip \
    -O /tmp/groovy.zip && \
    unzip /tmp/groovy.zip -d /opt && \
    rm /tmp/groovy.zip

# Set GROOVY_HOME and update PATH
ENV GROOVY_HOME=/opt/groovy-${GROOVY_VERSION}
ENV PATH=$GROOVY_HOME/bin:$PATH

# Verify tools
RUN root --version
RUN groovy --version
RUN java -version

# Create workdir and copy source. Set C12ANALYSIS env so bin/run.sh uses correct path
WORKDIR /usr/src/clas12-analysis
COPY . /usr/src/clas12-analysis

ENV C12ANALYSIS=/usr/src/clas12-analysis

# Build j2root project
RUN ./bin/build_j2root.sh

# Build clasqaDB project
RUN ./bin/build_clasqaDB.sh

# Build gradle project
RUN ./bin/build_gradle.sh

# Copy jar file to installation directory
RUN mkdir -p /opt/clas12-analysis && \
    mv app/build/libs/*.jar /opt/clas12-analysis/

# Set environment variable for installation directory
ENV C12ANALYSIS_INSTALL_DIR=/opt/clas12-analysis/

# Ensure run.sh is executable
RUN chmod +x /usr/src/clas12-analysis/bin/run.sh || true

# Expose a default entrypoint which runs the groovy runner script
ENTRYPOINT ["/bin/bash", "-c", "source /usr/src/clas12-analysis/bin/env.sh && exec bash"]
