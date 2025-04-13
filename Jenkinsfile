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
          withCredentials([usernamePassword(credentialsId: 'nexus', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
              sh '''
                  mvn deploy -DskipTests -Dusername=$USERNAME -Dpassword=$PASSWORD \
                      -Dnexus.username=$USERNAME -Dnexus.password=$PASSWORD
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
