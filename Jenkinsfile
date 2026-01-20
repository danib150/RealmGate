pipeline {
    agent any

    tools {
        jdk 'Corretto25'
    }

    environment {
        JAVA_HOME = "${tool 'Corretto25'}"
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
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
                sh '''
                  java -version
                  ./gradlew clean build
                '''
            }
        }
    }
}
