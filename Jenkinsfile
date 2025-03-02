pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'sahar', url: 'https://github.com/Sahargaiche23/Devops.git'
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

        stage('Package') {
            steps {
                script {
                    sh 'mvn package'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    def scannerHome = tool 'scanner'
                    withSonarQubeEnv('sonar') {  // Ensure 'sonar' matches the configuration in Jenkins
                        sh """
                            ${scannerHome}/bin/sonar-scanner \
                            -Dsonar.projectKey=devops \
                            -Dsonar.projectName=devops \
                            -Dsonar.projectVersion=1.0 \
                            -Dsonar.host.url=http://localhost:9000 \
                            -Dsonar.token=squ_3a43c806c917653c92994306ab3eacadabd36b6c
                        """
                    }
                }
            }
        }
    }
}
