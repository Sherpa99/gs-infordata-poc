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
        booleanParam(name: 'executeTests', defaultValue: true, description:'Un select to make it false')
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
        stage("Get Route") {
            steps {
                script {
                    gv.getRounte()
                }
            }
        }
    }
}
