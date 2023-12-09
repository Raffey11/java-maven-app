def buildJar() {
    echo "building the application..."
    sh 'mvn package'
} 

def buildImage() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'docker-credentials', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh 'docker build -t raffey/java-maven-app:1.1 .'
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh 'docker push raffey/java-maven-app:1.1'
    }
} 

def deployApp() {
    def dockerCmd = 'docker run -d -p3000:8080 raffey/java-maven-app:1.1'
    sshagent(['ec2-server-key']) {
        sh "ssh -o StrictHostKeyChecking=no ec2-user@3.250.6.221 ${dockerCmd}"
    }
} 

return this
