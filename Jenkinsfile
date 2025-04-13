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
                    withSonarQubeEnv('SonarQubeScanner') { // Assurez-vous que "SonarQube" est bien configuré dans Jenkins
                        sh "${SONAR_SCANNER}/bin/sonar-scanner \
                            -Dsonar.projectKey=devopsSecure \
                            -Dsonar.sources=. \
                            -Dsonar.java.binaries=**/target/classes \
                            -Dsonar.host.url=http://localhost:9000 \
                            -Dsonar.login=squ_d7839e2d6a74c10227370756390d4ef41333b2d1"
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
