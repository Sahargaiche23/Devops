pipeline {
    agent any

    tools {
        maven 'Maven' // Assurez-vous que Maven est installé dans Jenkins
    }

    environment {
        SONAR_SCANNER = tool 'SonarQubeScanner' // Nom de l'outil configuré dans Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'Mariem', url: 'https://github.com/Sahargaiche23/Devops.git'
            }
        }

        stage('Build') {
            steps {
                script {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    sh 'mvn test'
                }
            }
        }


        stage('SonarQube Analysis') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'SONAR_TOKEN', variable: 'SONAR_TOKEN')]) {
                        withSonarQubeEnv('SonarQubeScanner') {
                            sh "${SONAR_SCANNER}/bin/sonar-scanner \
                                -Dsonar.projectKey=devopsSecure \
                                -Dsonar.sources=. \
                                -Dsonar.java.binaries=**/target/classes \
                                -Dsonar.host.url=http://localhost:9000 \
                                -Dsonar.login=${SONAR_TOKEN}"
                        }
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                script {
                    timeout(time: 2, unit: 'MINUTES') { // Timeout pour éviter un blocage du pipeline
                        waitForQualityGate abortPipeline: true
                    }
                }
            }
        }

        stage('Package') {
            steps {
                script {
                    sh 'mvn package'
                }
            }
        }
    }
}
