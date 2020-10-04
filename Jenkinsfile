def gv
def image
pipeline {
    agent any 
    //Environment varialbles
    environment {
    REGISTRY = "de.icr.io/infordata_poc_ir/infordata-gs-poc"
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
        choice(name: 'BRANCH_NAME', choices:['DEV','QA','UAT','PROD'], description:'Select Your Deployment Option')
        booleanParam(name: 'executeTests', defaultValue: true, description:'Unselect the box to make it false')
    }
    tools{
        maven 'mvn'
    }
    stages {
        stage("Init") {
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
        stage("Create Container Image") {
            steps{
                script{
                    image=docker.build("infordata-gs-poc")
                    echo "$image"
                }
            }   
        }
        stage("Push To ICR") {
            steps{
                script{
                    docker.withRegistry('de.icr.io/infordata_poc_ir','credentials'){
                    image.push("${env.BUILD_NUMBER}")
                    echo "$image"
                    image.push("lastest")
                    echo "$image"
                }
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
                    params.BRANCH_NAME == 'DEV' || params.BRANCH_NAME == 'QA' || params.BRANCH_NAME == 'UAT'
                }
            }
            steps {
                script {
                    gv.deployApp()
                }
            }
        }
        stage("Create Service") {
            steps {
                script {
                    gv.exposeService()
                }
            }
        }
        stage("create Route") {
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
