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
                         ${SONAR_SCANNER}/bin/sonar-scanner \
                             -Dsonar.projectKey=devopsSecure \
                             -Dsonar.sources=. \
                             -Dsonar.java.binaries=target/classes \
                             -Dsonar.host.url=http://localhost:9000 \
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

        stage('Deploy Monitoring') {
            steps {
                script {
                    parallel {
                        stage('Prometheus') {
                            steps {
                                timeout(time: 2, unit: 'MINUTES') {  // Increased timeout
                                    sh '''
                                        docker-compose up -d prometheus
                                        # Wait for Prometheus to be healthy
                                        until curl -s http://prometheus:9090/-/ready; do
                                            sleep 5
                                            echo "Waiting for Prometheus to start..."
                                        done
                                    '''
                                    echo "Prometheus running at http://192.168.56.10:9090"
                                }
                            }
                        }
                        stage('Grafana') {
                            steps {
                                timeout(time: 2, unit: 'MINUTES') {  // Increased timeout
                                    sh '''
                                        docker-compose up -d grafana
                                        # Wait for Grafana to be healthy
                                        until curl -s http://grafana:3000/api/health; do
                                            sleep 5
                                            echo "Waiting for Grafana to start..."
                                        done
                                    '''
                                    echo "Grafana running at http://192.168.56.10:3000"
                                    echo "Default credentials: admin/admin"
                                }
                            }
                        }
                    }
                }
            }
        }

        post {
            always {
                // Optional: Add health checks
                script {
                    try {
                        sh 'curl -s http://192.168.56.10:9090/-/healthy > prometheus-health.txt'
                        sh 'curl -s http://192.168.56.10:3000/api/health > grafana-health.txt'
                    } catch (err) {
                        echo "Monitoring health check failed: ${err}"
                    }
                }
                cleanWs()
            }
        }
    }
}
