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
    def dockerCmd = 'docker stop java-maven-app; docker rm java-maven-app; docker run -d -p8080:8080 --name java-maven-app raffey/java-maven-app:1.1'
    sshagent(['ec2-server-key']) {
        sh "ssh -o StrictHostKeyChecking=no ec2-user@34.245.238.143 ${dockerCmd}"
    }
} 

return this
