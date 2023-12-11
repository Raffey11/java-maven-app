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
    sshagent(['ec2-server-key']) {
        sh "scp shell-cmds.sh ec2-user@34.245.238.143:~"
        sh "scp docker-compose.yaml ec2-user@34.245.238.143:~"
        sh "ssh -o StrictHostKeyChecking=no ec2-user@34.245.238.143 ${dockerComposeCmd}"
    }
} 

return this
