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
                git branch: 'SalmaEquipe', url: 'https://github.com/Sahargaiche23/Devops.git'
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
                                -Dsonar.login=squ_14fe63ab1aa93e6d117ed1165ae9fdfb64c65fb7
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
        stage('Build Docker Image') {
                                    steps {
                                        script {
                                            docker.build("${DOCKER_REGISTRY}/kaddem:0.0.1")
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

