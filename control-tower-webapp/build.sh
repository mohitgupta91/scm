#!bash


npm install

bower update

compass compile

grunt sass
grunt build


scp -r src centos@10.41.92.108:SCMTower/src
scp -r dist centos@10.41.92.108:SCMTower/dist
ssh -t centos@10.41.92.108 'bash uitowerbuild.sh'