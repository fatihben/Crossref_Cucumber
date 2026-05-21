pipeline {

    agent any

    tools {
        maven 'Maven'
        jdk 'JDK21'
    }

    stages {

        stage('Checkout') {

            steps {

                git 'YOUR_GITHUB_REPO_URL'
            }
        }

        stage('Build & Test') {

            steps {

                bat 'mvn clean test'
            }
        }

        stage('Generate Report') {

            steps {

                cucumber buildStatus: 'UNSTABLE',
                        fileIncludePattern: '**/cucumber.json',
                        jsonReportDirectory: 'target/json-reports',
                        sortingMethod: 'ALPHABETICAL'
            }
        }
    }

    post {

        always {

            archiveArtifacts artifacts: 'target/**/*.*',
                    fingerprint: true
        }
    }
}