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
                        withSonarQubeEnv('scanner')
                            sh 'mvn sonar:sonar -Dsonar.projectKey=devopsSecure -Dsonar.host.url=http://localhost:9000 -Dsonar.token=sqp_375f28f81a4294199a2f8bb973b74d7e03c55c5e'
                        }
                    }
                }
            }
        }
    }
