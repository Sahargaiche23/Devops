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
                    withSonarQubeEnv('SonarQube') { // Assurez-vous que "SonarQube" est bien configuré dans Jenkins
                        sh "${SONAR_SCANNER}/bin/sonar-scanner \
                            -Dsonar.projectKey=devopsSecure \
                            -Dsonar.sources=. \
                            -Dsonar.host.url=http://localhost:9000 \
                            -Dsonar.login=sqp_f2eaf381d07b589d399fa861e0001af1617f3df4"
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
