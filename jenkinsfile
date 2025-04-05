pipeline {
    agent any
    tools {
        jdk 'JDK17'
        maven 'Maven'
    }
    stages {
        // ----------------------------------
        // Phase 1: Git Checkout
        // ----------------------------------
        stage('Checkout Git') {
            steps {
                checkout scm  // Automatically checks out the triggered branch
                sh 'git branch'  // Verify branch
            }
        }

        // ----------------------------------
        // Phase 2: Build & Unit Tests
        // ----------------------------------
        stage('Build & Test') {
            steps {
                sh 'mvn clean package'
                junit 'target/surefire-reports/*.xml'  // JUnit reports
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        // ----------------------------------
        // Phase 3: SonarQube Analysis
        // ----------------------------------
        stage('SonarQube Scan') {
            environment {
                SONAR_SCANNER_HOME = tool 'SonarScanner'
            }
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh """
                    mvn sonar:sonar \
                    -Dsonar.projectKey=kaddem \
                    -Dsonar.projectName=kaddem
                    """
                }
            }
        }

        // ----------------------------------
        // Phase 4: Nexus Upload
        // ----------------------------------
        stage('Upload to Nexus') {
            when {
                branch 'main'  // Only upload from main branch
            }
            steps {
                nexusArtifactUploader(
                    nexusVersion: 'nexus3',
                    protocol: 'http',
                    nexusUrl: 'http://nexus-ip:8081',
                    groupId: 'tn.esprit.spring',
                    version: '1.0',
                    repository: 'maven-releases',
                    credentialsId: 'nexus-creds',
                    artifacts: [
                        [artifactId: 'kaddem',
                         type: 'jar',
                         file: 'target/kaddem-*.jar']
                    ]
                )
            }
        }

        // ----------------------------------
        // Phase 5: Docker Build & Push
        // ----------------------------------
        stage('Docker Build') {
            environment {
                DOCKER_IMAGE = "your-dockerhub/kaddem:${env.BRANCH_NAME}-${env.BUILD_NUMBER}"
            }
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-creds') {
                        docker.build(DOCKER_IMAGE).push()
                    }
                }
            }
        }

        // ----------------------------------
        // Phase 6: Deploy with Docker Compose
        // ----------------------------------
        stage('Deploy') {
            when {
                branch 'main'  // Only deploy from main
            }
            steps {
                sh '''
                docker-compose down
                docker-compose up -d
                '''
            }
        }
    }

    // ----------------------------------
    // Post-Build Actions
    // ----------------------------------
    post {
        always {
            cleanWs()
        }
        success {
            slackSend channel: '#devops',
                     message: "Build ${env.BUILD_NUMBER} succeeded for ${env.BRANCH_NAME}"
        }
        failure {
            mail to: 'team@example.com',
                 subject: "Build Failed: ${env.JOB_NAME}",
                 body: "Check ${env.BUILD_URL}"
        }
    }
}