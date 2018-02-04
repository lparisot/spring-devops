## Installation of our DevOps servers in AWS

## Some configuration

You must create a file ~/.devops_pass.txt which contains your Ansible vault password.

You must create a new vault file in group_vars/all/vault, which contains
```
---
vault_aws_access_key_id: your aws key id
vault_aws_secret_access_key: your aws access key

vault_jenkins_admin_password: your jenkins admin account password

vault_db_root_password: the root password of the mysql database
vault_db_user_password: the user password of our database
```
To create your AWS access key, see https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_access-keys.html#Using_CreateAccessKey

You must change all keys with yours then encrypt the file with:
```
$ ansible-vault encrypt group_vars/all/vault
```
The file will be encrypted without asking any password, using the one found in ~/.devops_pass.txt file (this filename is defined in ansible.cfg file).

You must also change group_vars/all/vars file:
* The servername with your domain name.
* the ec2_vpc_default with your AWS vpc.
* the ec2_subnet with your subnet.
* the ec2_region with your region.
* github_user_name with your github user name.
* github_user_project with your github project name.

## Ansible playbooks

```
$ ansible-playbook site.yml
```

* site.yml: call app.yml and devops.yml
* devops.yml: create a jenkins server and an artifactory server
* app.yml: create a mysql server

This will run approximately for 15 minutes.

This playbook will create two instances in AWS. We use the CentOS 7 (x86_64) found in AWS market place.
Instance type is t2.micro with a volume type gp2 with 8GB.
With these values we are free tier eligible.

If you have some slowness problem with the artifactory instance, uncomment the ec2_instance_type: "t2.small" line in master_devops.yml file.

As we can see in dev file, we have devops-jenkins01 which is a jenkins server and devops-artifactory01 which is an artifactory server.
On application side, we have devops-mysql01 which is a mysql server and devops-webserver01 which contains the application.

* master.yml will create all AWS part (instances, security groups, ...).
* jenkins.yml will populate the jenkins server
* artifactory.yml will populate the artifactory server
* webserver.yml will populate the webserver with our application

The jenkins server will contains:
* java jdk
* git application
* an apache server waiting on port 80 and redirecting to port 8080
* a jenkins server running on port 8080

It will also pre-configure the jenkins server:
* with the admin password found in the vault as vault_jenkins_admin_password.
* generate an SSH public key which will be stored under ansible/env/id_rsa.pub. This key must be used in your github project settings as a deploy key.
* install a list of plugins which can be found in jenkins/defaults/main.yml (you can change them if needed).
* add a global credential using the private key found under ~jenkins/.ssh
* declare a new job named "Spring DevOps Project" which will wait for a push in the github project to launch a maven clean install.

The artifactory server will contains:
* docker
* an apache server waiting on port 80 and redirecting to port 8081
* an artifactory container accessible on port 8081

The docker image is the open source server docker.bintray.io/jfrog/artifactory-oss.

The artifactory data will be stored in three volumes under /var/opt/jfrog/artifactory/ in data, logs and etc folders.

The webserver will contains:
* java, the open source JDK
* an apache server waiting on port 80 and redirecting to port 8080
* the application jar file running a tomcat server and the application on port 8080

The application files will be stored under the centos account in /home/centos.

## Jenkins

See https://jenkins.io/

## Artifactory

See https://jfrog.com/artifactory/

First you must login admin to change immediately the default password (which is password).

Then you must launch the Quick Setup menu, selecting Maven repository. It will generate some artifacts.

Then you must update your settings.xml file with the new repository:
* select Artifacts
* select libs-release
* click on Set Me Up
* click on Generate Maven Settings
* click on Generate Settings
* copy and paste it in your settings.xml file
* replace username with admin

To get encrypted admin password:
* open admin profile
* enter current password to unlock
* copy encrypted password
* paste it in your settings.xml file

In your pom.xml file you must copy the distributionManagement repositories for libs-release-local and libs-snapshot-local:
* select Artifacts
* select libs-release-local
* select Set Me Up
* copy the deploy xml part to your pom.xml

Do the same for the libs-snapshot-local.

Then you can do a "clean install" maven command and a deploy maven command to retrieve and pull your artifact in Artifactory.

## Github project

On your github project:
* you must copy in the settings under "Deploy keys", the content of the ansible/env/id_rsa.pub file.
* you must create in the settings under "Webhooks", a new webhook with the Payload URL equals to http://your_jenkins_url/github-webhook, no secret, and selecting "Just the push event" as event.

Each time a push will be done, an event will be send to the jenkins server.

## Test of DevOps servers

At last, you can connect via your browser to the public DNS of the jenkins server with account admin.

You can also ssh directly to the server, using ssh devops-jenkins01.

This is done by the private access key generated to access the instances and stored in your ~/.ssh folder (jenkins-staging-private.pem), and the ~/.ssh/config file which is modify to map the devops-jenkins01 name on the AWS public DNS names.

On the jenkins server you can check the application by doing:
```
$ sudo less /etc/sysconfig/jenkins
$ sudo less /var/lib/jenkins/config.xml
$ sudo less /var/log/jenkins/jenkins.log
$ systemctl status jenkins
$ systemctl status httpd
```

## Application

On devops-webserver01 the application is launched as a service named springboot.

See /etc/systemd/system/springboot.service file for the description.
It will use the spring-core-devops jar file downloaded from artifactory and the application.properties file construct by Ansible.

You can connect with ssh to devops-webserver01 and check the status of the service with:
```bash
$ systemctl status springboot
```

Then you can browse the application on the webserver address on port 80.
