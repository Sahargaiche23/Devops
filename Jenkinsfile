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


        stage('Test') {
            steps {
                script {
                    sh 'mvn test'
                }
            }
        }

       stage('Build') {
            steps {
                script {
                    sh 'mvn clean package -DskipTests'
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

    }
}




