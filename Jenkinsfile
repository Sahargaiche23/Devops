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
                    withSonarQubeEnv('SonarQubeScanner') {
                        sh """
                            ${SONAR_SCANNER}/bin/sonar-scanner \\
                                -Dsonar.projectKey=devopsSecure \\
                                -Dsonar.sources=. \\
                                -Dsonar.java.binaries=target/classes \\
                                -Dsonar.host.url=http://localhost:9000 \\
                                -Dsonar.login=squ_d7839e2d6a74c10227370756390d4ef41333b2d1
                        """
                    }
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'nexus',
                        usernameVariable: 'NEXUS_USER',
                        passwordVariable: 'NEXUS_PASS'
                    )
                ]) {
                    sh '''
                        mvn deploy \
                        -DskipTests \
                        -DaltDeploymentRepository=deploymentRepo::default::http://${NEXUS_USER}:${NEXUS_PASS}@localhost:8081/repository/maven-releases/
                    '''
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

        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("${DOCKER_REGISTRY}/kaddem:${APP_VERSION}")
                }
            }
        }

 stage('Deploy Monitoring') {
            steps {
                script {
                    parallel {
                        stage('Prometheus') {
                            steps {
                                timeout(time: 1, unit: 'MINUTES') {
                                    sh 'docker-compose up -d prometheus'
                                    sleep 7
                                    echo "Prometheus running at http://192.168.1.18:9090"
                                }
                            }
                        }
                        stage('Grafana') {
                            steps {
                                timeout(time: 1, unit: 'MINUTES') {
                                    sh 'docker-compose up -d grafana'
                                    sleep 2
                                    echo "Grafana running at http://192.168.1.18:3000"
                                }
                            }
                        }
                    }
                }
            }
        }


    post {
        always {
            // Clean up workspace
            cleanWs()
        }
    }
}
