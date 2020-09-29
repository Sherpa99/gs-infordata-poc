# Spring Boot Application Using Postgres

## Jenkins Pipeline Workflow
![Screenshot](https://github.com/Sherpa99/gs-infordata-poc/blob/master/docs/images/PipelineWorkFlow.png)

### Jenksinsfile for pushing artifact to Nexus
* Deployment yaml
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
* Deployment yaml
```console
def gv
pipeline {
    agent any 
    environment {
    // This is the git repository from where we fetch the code to build
    GIT_URL= "https://github.com/Sherpa99/gs-infordata-poc.git"
    }
    parameters {
        choice(name: 'BRANCH_NAME', choices:['DEV','QA','PROD'], description:'')
        booleanParam(name: 'executeTests', defaultValue: true, description:'')
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
        stage("deploy") {
             when {
                expression{
                    params.BRANCH_NAME=='DEV' || params.BRANCH_NAME=='QA'
                }
            }
            steps {
                script {
                    gv.deployApp()
                }
            }
        }
         stage("expose service") {
            steps {
                script {
                    gv.exposeService()
                }
            }
        }
    }
}
```


### Groovy Script file
* Deployment yaml
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
  echo 'Deployment deploying the application'
  sh 'oc create deployment infordata-poc-app --image=de.icr.io/infordata_poc_ir/infordata-gs-poc:v1'
}
def exposeService() {
  echo 'Exposing Service'
  sh 'oc expose deployment infordata-poc-app --type="NodePort" --port=8080'
}
return this

```

### BlueOcean deployment steps
![Screenshot](https://github.com/Sherpa99/gs-infordata-poc/blob/master/docs/images/blueoceanpipeline.png)

## Spring Boot Application  <a href=https://github.com/Sherpa99/gs-infordata-poc/blob/master/README.md> Link </a>
