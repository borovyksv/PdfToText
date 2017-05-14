#! /bin/bash

#uncomment next lines if it's needed to install docker on server
CurDir=$PWD
#install docker
#wget -qO- https://get.docker.com/ | sh
#sudo usermod -aG docker $(whoami)
#sudo systemctl start docker.service

#install docker-compose
#sudo yum install epel-release
#sudo yum install -y python-pip
#sudo pip install docker-compose

#install MAVEN
#wget http://mirror.cc.columbia.edu/pub/software/apache/maven/maven-3/3.0.5/binaries/apache-maven-3.0.5-bin.tar.gz
#sudo tar xzf apache-maven-3.0.5-bin.tar.gz -C /usr/local
#cd /usr/local
#sudo ln -s apache-maven-3.0.5 maven
#export M2_HOME=/usr/local/maven
#export PATH=${M2_HOME}/bin:${PATH}


#create dir for input files
mkdir -p /tmp/original
#create dir for output files
mkdir -p /tmp/copy

#cd to project directory
cd $CurDir

#compile project
mvn clean package

#copy Dockerfile to compiled file and RUN
cp Dockerfile target/
cd target/
docker build -t borovyksv/pdf_to_text .

#run Docker-compose
cd ..
docker-compose up
