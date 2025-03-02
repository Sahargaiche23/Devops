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
       steps{
       script {
       def scannerHome = tool 'scanner'
       withSonarQubeEnv {
       sh "${scannerHome}/bin/sonar-scanner"
       }
       }
       }
       }


    }
}
