pipeline {
    agent any

    tools {
        jdk 'Corretto25'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Inject private JAR') {
            steps {
                sh '''
                  mkdir -p libs
                  cp /private-jars/HytaleServer.jar libs/
                '''
            }
        }

        stage('Build') {
            steps {
                sh './gradlew clean build'
            }
        }
    }
}
