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
                sh "gradle test -PdnamVersion=${params.DNAM_VERSION}"
            }
            post {
                always {
//                    junit '**/build/test-results/junit-platform/TEST-*.xml'
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
                sh "export TEST_PACKAGE=layer1"
                sh "docker-compose " +
                        "-f 1-test-impl-layer/docker-compose-layer1-test-fixture.yml " +
                        "-f 1-test-impl-layer/docker-compose-dnam-func-acc-test.yml up " +
                        "--abort-on-container-exit"
                sh "docker-compose " +
                        "-f 1-test-impl-layer/docker-compose-layer1-test-fixture.yml " +
                        "-f 1-test-impl-layer/docker-compose-dnam-func-acc-test.yml rm -fv"
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
                sh "export TEST_PACKAGE=layer2"
                sh "docker-compose " +
                        "-f 1-test-impl-layer/docker-compose-layer1-test-fixture.yml " +
                        "-f 1-test-impl-layer/docker-compose-dnam-func-acc-test.yml up " +
                        "--abort-on-container-exit"
                sh "docker-compose " +
                        "-f 1-test-impl-layer/docker-compose-layer1-test-fixture.yml " +
                        "-f 1-test-impl-layer/docker-compose-dnam-func-acc-test.yml rm -fv"
            }
            post {
                always {
//                    junit '**/build/test-results/integrationTest/TEST-*.xml'
                    cleanWs()
                }
            }
        }
    }
}
