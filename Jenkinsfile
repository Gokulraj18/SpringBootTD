pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK21'
    }

    environment {
        IMAGE_NAME = "SpringbootTD"
        SPRING_DATASOURCE_URL = "jdbc:h2:mem:splunkdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
        SPRING_DATASOURCE_USERNAME = "sa"
        SPRING_DATASOURCE_PASSWORD = ""
        SPRING_DATASOURCE_DRIVER_CLASS_NAME = "org.h2.Driver"
    }

    stages {

        stage('Clone Repository & Build') {
            steps {
                checkout scm
                sh 'mvn clean compile'
            }
        }

        stage('Run Unit Tests') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package Application') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        stage('Stop Old Container') {
            steps {
                sh 'docker stop SpringbootTD || true'
                sh 'docker rm SpringbootTD || true'
            }
        }

        stage('Remove Old Image') {
            steps {
                sh 'docker rmi $IMAGE_NAME || true'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $IMAGE_NAME -f src/main/java/com/example/splunk/Dockerfile .'
            }
        }

        stage('Run Container') {
            steps {
                sh """
                docker run -d \
                --name SpringbootTD \
                -p 8081:8080 \
                $IMAGE_NAME
                """
            }
        }
    }

    post {
        success {
            echo 'CI/CD Pipeline Completed Successfully'
        }
        failure {
            echo 'Pipeline Failed'
        }
    }
}
