pipeline {
    agent none
    parameters {
        string(name: 'DNAM_VERSION', defaultValue: '30.0.0', description: 'DNAM-version for gradle dependencies')
    }
    triggers {
        pollSCM('H/5 * * * *')
    }
    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '10'))
        ansiColor('xterm')
        timeout(time: 1, unit: 'HOURS')
        skipDefaultCheckout()
    }
    stages {
        stage('Build') {
            agent {
                docker {
                    label 'docker && infinera'
                    image 'se-artif-prd.infinera.com/gradle:4.3.1'
                }
            }
            steps {
                checkout scm
                stash name: 'checkout', useDefaultExcludes: false
                sh "gradle assemble -PdnamVersion=${params.DNAM_VERSION}"
                stash name: 'assemble', useDefaultExcludes: false
            }
            post {
                always {
                    cleanWs()
                }
            }
        }
        stage('Test') {
            // parallelize unit tests / api tests?
            agent {
                docker {
                    label 'docker'
                    image 'se-artif-prd.infinera.com/gradle:4.3.1'
                }
            }
            steps {
                unstash 'assemble'
                sh 'gradle test'
            }
            post {
                always {
//                    junit '**/build/test-results/junit-platform/TEST-*.xml'
                    cleanWs()
                }
            }

        }
        stage('Integration smoke tests') {
            agent {
                label 'docker'
            }
            steps {
                sh "docker network rm \$(docker network ls | grep \"dnam_func_accept\" | awk '/ / { print \$1 }')"
                unstash 'assemble'
                sh('./gradlew integrationtest -PtestPackage=com.infinera.metro.test.acceptance.appdriver.dnam')
            }
            post {
                always {
                    junit '**/build/test-results/integrationTest/TEST-*.xml'
                    cleanWs()
                }
            }
        }
        stage('Functional Acceptance tests layer 1') {
            agent {
                label 'docker'
            }
            steps {
                unstash 'assemble'
                sh('./gradlew integrationTest -PtestPackage=com.infinera.metro.test.acceptance.layer1')
            }
            post {
                always {
//                    junit '**/build/test-results/integrationTest/TEST-*.xml'
                    cleanWs()
                }
            }
        }
        stage('Functional Acceptance tests layer 2') {
            agent {
                label 'docker'
            }
            steps {
                unstash 'assemble'
                sh('./gradlew integrationTest -PtestPackage=com.infinera.metro.test.acceptance.layer2')
            }
            post {
                always {
                    junit '**/build/test-results/integrationTest/TEST-*.xml'
                    cleanWs()
                }
            }
        }
//        stage('Publish') {
//            agent {
//                docker {
//                    label 'docker'
//                    image 'se-artif-prd.infinera.com/gradle:4.3.1'
//                }
//            }
//            steps {
//                unstash 'assemble'
//                script {
//                    GIT_COMMIT = sh (script: 'git --no-pager show -s --format=\'%h\'', returnStdout: true).trim().take(7)
//                    withEnv(["TAG=1.0.0-$GIT_COMMIT"]) {
//                        sh "./gradlew artifactoryPublish -PpartOfLatestCommitHash=$GIT_COMMIT"
//                    }
//                }
//            }
//            post {
//                always {
//                    cleanWs()
//                }
//            }
//        }
    }
}
