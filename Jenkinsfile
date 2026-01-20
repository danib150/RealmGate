pipeline {
    agent any

    triggers {
        githubPush()
    }

    environment {
        JAVA_HOME = "/opt/java/jdk-25.0.1+8"
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
                  cp /opt/jenkins-private-jars/HytaleServer.jar libs/
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

        stage('Collect JARs') {
            steps {
                sh '''
                  cp build/libs/*.jar libs/
                '''
            }
        }
    }

    post {
        success {
            archiveArtifacts artifacts: 'libs/*.jar',
                             excludes: 'libs/HytaleServer.jar',
                             fingerprint: true
        }
    }
}
