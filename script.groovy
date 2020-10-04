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
def createContainerImage() {
	echo 'Create Container Image'
	dockerImage = docker.build 'de.icr.io/infordata_poc_ir/infordata-gs-poc' + ":$BUILD_NUMBER"
    echo "$dockerImage"
}
def UploadImageToICR() {
	echo 'Create DB Service - Load Balancer'
	docker.withRegistry( '', registryCredential ) { 
		dockerImage.push() 
	}
}
def createExtSVC() {
	echo 'Create DB Service - Load Balancer'
	sh 'oc apply -f svcoradb.yaml'
}
def createExtEP() {
	echo 'Create External Endpoint'
	sh 'oc apply -f eporadb.yaml'
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
def getRounte() {
	echo 'Getting Routes for accessing service'
	sh 'oc get route'
}
return this