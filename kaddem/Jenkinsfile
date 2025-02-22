pipeline {
    agent any
    stages {
        stage('Install dependencies') {
            steps {
                script {
                    sh('mvn clean install -DskipTests') // Pour Maven
                    // ou sh('./gradlew dependencies') // Pour Gradle
                }
            }
        }
        stage('Unit Test') {
            steps {
                script {
                    sh('mvn test') // Pour Maven
                    // ou sh('./gradlew test') // Pour Gradle
                }
            }
        }
        stage('Build application') {
            steps {
                script {
                    sh('mvn package -DskipTests') // Pour Maven
                    // ou sh('./gradlew build -x test') // Pour Gradle
                }
            }
        }
    }
}