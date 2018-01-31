## Installation of our Jenkins server in AWS

## Some configuration

You must create a file ~/.devops_pass.txt which contains your ansible vault password.

You must create a new vault file in group_vars/all/vault, which contains
```
---
vault_aws_access_key_id: your aws key id
vault_aws_secret_access_key: your aws access key

vault_jenkins_admin_password: your jenkins admin account password
```
To create your AWS access key, see https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_access-keys.html#Using_CreateAccessKey

You must change all keys with yours then encrypt the file with:
```
$ ansible-vault encrypt group_vars/all/vault
```
The file will be encrypt without asking any password, using the one found in ~/.devops_pass.txt file (this filename is defined in ansible.cfg file).

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

This will run approximatively in 15 minutes.

This playbook will create one instance in AWS. We use the CentOS 7 (x86_64) found in AWS market place.
Instance type is t2.micro with a volume type gp2 with 8GB.
With these values we are free tier eligible.

So we have devops-jenkins01 which is a jenkins server.

They will also create a security group (opening port 22 and 80).

It will then install all required applications.

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

## Github project

On your github project:
* you must copy in the settings under "Deploy keys", the content of the ansible/env/id_rsa.pub file.
* you must create in the settings under "Webhooks", a new webhook with the Payload URL equals to http://your_jenkins_url/github-webhook, no secret, and selecting "Just the push event" as event.

Each time a push will be done, an event will be send to the jenkins server.

## Test

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
