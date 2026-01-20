pipeline {
    agent any

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
                sh '''
                  java -version
                  ./gradlew clean build
                '''
            }
        }
    }
}
