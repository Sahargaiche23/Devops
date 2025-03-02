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

        stage('Deploy') {
            steps {
                script {
                    sh 'scp target/*.jar user@server:/path/to/deploy/'
                }
            }
        }
    }
}
