def checkoutSRC() {
    echo 'Checking out source code!' 
    echo "Building applicaiton version : ${params.BRANCH_NAME}"
    git "${GIT_URL}"
}
def deployApp() {
    echo 'Deployment deploying the application' 
    sh 'oc create deployment infordata-poc-app --image=us.icr.io/gs-cda-dev-ns/myimage:1'
}
def exposeService() {
    echo 'Exposing Service' 
    sh 'oc expose deployment infordata-poc-app --type="NodePort" --port=3000'
}
return this
