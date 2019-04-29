pipeline {

    agent { label 'KEG-slave-89' }
    stages {
        stage('build') {
            steps {
                sh 'docker build -t socp.io/data-import/data-import:v1 .'
                sh 'docker run -id socp.io/data-import/data-import:v1 '
            }
        }

        stage('dockerpush'){
            steps{
                sh 'docker push socp.io/data-import/data-import:v1'
            }
        }

    }

}
