pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh './gradlew publish'
            }
        }

    }
    post {
        always {
            archiveArtifacts artifacts: '*.jar', fingerprint: true
           cleanWs()
        }
    }
}