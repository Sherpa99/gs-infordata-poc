# Spring Boot Application Using Postgres

## Jenkins Pipeline Workflow
![Screenshot](https://github.com/Sherpa99/gs-infordata-poc/blob/master/docs/images/PipelineWorkFlow.png)

### Jenksinsfile for pushing artifact to Nexus
* Following code build the artifact and pushes to Nexus artifactory.
```console
def checkoutSRC() {
  echo 'Checking out source code!'
  echo "Building applicaiton version : ${params.BRANCH_NAME}"
  git "${GIT_URL}"
}
def buildApp() {
  echo 'Building the application!'
  echo "Building applicaiton version : ${params.BRANCH_NAME}"
  sh 'mvn clean package -DskipTests=true'
}
def pushToNexus() {
  pom = readMavenPom file: "pom.xml"
  filesByGlob = findFiles(glob: "target/*.${pom.packaging}");
  echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
  artifactPath = filesByGlob[0].path;
  artifactExists = fileExists artifactPath;
  if(artifactExists) {
    nexusArtifactUploader(
        nexusVersion: NEXUS_VERSION,
        protocol: NEXUS_PROTOCOL,
        nexusUrl: NEXUS_URL,
        groupId: pom.groupId,
        version: pom.version,
        repository: NEXUS_REPOSITORY,
        credentialsId: NEXUS_CREDENTIAL_ID,
        artifacts: [
          [artifactId: pom.artifactId,
            classifier: '',
            file: artifactPath,
            type: pom.packaging],
          [artifactId: pom.artifactId,
            classifier: '',
            file: "pom.xml",
            type: "pom"]
        ]
        );
  } else {
    error "*** File: ${artifactPath}, could not be found";
  }
}
def deployApp() {
  echo 'Deployment deploying the application'
  sh 'oc create deployment infordata-poc-app --image=de.icr.io/infordata_poc_ir/infordata-gs-poc:v1'
}
def exposeService() {
  echo 'Exposing Service'
  sh 'oc expose deployment infordata-poc-app --type="NodePort" --port=8080'
}
return this

```

### Jenksinsfile for deploying application to OCP - OpenShift Container Platform
* Following code deploys application into a OCP - OpenShift Container Platform cluster
```console
def gv
pipeline {
    agent any 
    //Environment varialbles
    environment {
    REGISTRY_URL = ""
    DOCKER_IMAGE = ""
    // This is the git repository from where we fetch the code to build
    GIT_URL= "https://github.com/Sherpa99/gs-infordata-poc.git"
    // This can be nexus3 or nexus2
    NEXUS_VERSION = "nexus3"
    // This can be http or https
    NEXUS_PROTOCOL = "http"
    // Where your Nexus is running
    NEXUS_URL = "nexus-dev.gs-cda-poc-2bef1f4b4097001da9502000c44fc2b2-0000.us-east.containers.appdomain.cloud/"
    // Repository where we will upload the artifact
    NEXUS_REPOSITORY = "nexus-infordata"
    // Jenkins credential id to authenticate to Nexus OSS
    NEXUS_CREDENTIAL_ID = "nexus-credentials"
    }
    parameters {
        choice(name: 'BRANCH_NAME', choices:['DEV','QA','PROD'], description:'Select Your Options')
        booleanParam(name: 'executeTests', defaultValue: true, description:'Unselect the box to make it false')
    }
    tools{
        maven 'mvn'
    }
    stages {
        stage("init") {
            steps {
                script {
                    gv = load "script.groovy"
                }
            }
        }
        stage('Checkout Source') {
            steps {
                script {
                    gv.checkoutSRC()
                }
            }
        }
        stage("Create External Service") {
            steps {
                script {
                    gv.createExtSVC()
                }
            }
        }
        stage("Create External EndPoint") {
            steps {
                script {
                    gv.createExtEP()
                }
            }
        }
        stage("Deploy an Application") {
             when {
                expression{
                    params.BRANCH_NAME == 'DEV' || params.BRANCH_NAME == 'QA'
                }
            }
            steps {
                script {
                    gv.deployApp()
                }
            }
        }
        stage("Create Internal Service") {
            steps {
                script {
                    gv.exposeService()
                }
            }
        }
        stage("create Load Balancer") {
            steps {
                script {
                    gv.createRoute()
                }
            }
        }
        stage("Get Route") {
            steps {
                script {
                    gv.getRounte()
                }
            }
        }
    }
}
```


### Groovy Script file
* Following is the groovy code which called into jenkinsfile to carryout each activities.
```console
def checkoutSRC() {
	echo 'Checking out source code!'
	echo "Building applicaiton version : ${params.BRANCH_NAME}"
	git "${GIT_URL}"
}
def buildApp() {
	echo 'Building the application!'
	echo "Building applicaiton version : ${params.BRANCH_NAME}"
	sh 'mvn clean package -DskipTests=true'
}
def pushToNexus() {
	echo 'Publish to nexus!'
	// Read POM xml file using 'readMavenPom' step , this step 'readMavenPom' is included in: https://plugins.jenkins.io/pipeline-utility-steps
	pom = readMavenPom file: "pom.xml";
	// Find built artifact under target folder
	filesByGlob = findFiles(glob: "target/*.${pom.packaging}");
	// Print some info from the artifact found
	echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
	// Extract the path from the File found
	artifactPath = filesByGlob[0].path;
	// Assign to a boolean response verifying If the artifact name exists
	artifactExists = fileExists artifactPath;
	if(artifactExists) {
		echo "*** File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version ${pom.version}";
		nexusArtifactUploader(
				nexusVersion: NEXUS_VERSION,
				protocol: NEXUS_PROTOCOL,
				nexusUrl: NEXUS_URL,
				groupId: pom.groupId,
				version: pom.version,
				repository: NEXUS_REPOSITORY,
				credentialsId: NEXUS_CREDENTIAL_ID,
				artifacts: [
					// Artifact generated such as .jar, .ear and .war files.
					[artifactId: pom.artifactId,
						classifier: '',
						file: artifactPath,
						type: pom.packaging],
					// Lets upload the pom.xml file for additional information for Transitive dependencies
					[artifactId: pom.artifactId,
						classifier: '',
						file: "pom.xml",
						type: "pom"]
				]
				);
	} else {
		error "*** File: ${artifactPath}, could not be found";
	}
}

def deployApp() {
	echo 'Deploy an application'
	sh 'oc apply -f deployment.yaml'
}
def exposeService() {
	echo 'Exposing Service'
	sh 'oc apply -f svcapp.yaml'
}
def createRoute() {
	echo 'Create OCP Route - Load Balancer'
	sh 'oc apply -f routeapp.yaml'
}
def createExtSVC() {
	echo 'Creating OCP Routes - Load Balancer'
	sh 'oc apply -f svcoradb.yaml'
}
def createExtEP() {
	echo 'Create External Endpoint'
	sh 'oc apply -f eporadb.yaml'
}
def getRounte() {
	echo 'Getting Routes for accessing service'
	sh 'oc get route'
}
return this

```

### BlueOcean deployment result
![Screenshot](https://github.com/Sherpa99/gs-infordata-poc/blob/master/docs/images/blueoceanpipeline.png)

## Spring Boot Application  <a href=https://github.com/Sherpa99/gs-infordata-poc/blob/master/README.md> Link </a>
