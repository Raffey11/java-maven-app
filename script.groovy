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
    def shellCmd = 'bash ./shell-cmds.sh'
    def ec2Instance = 'ec2-user@34.245.238.143'
    sshagent(['ec2-server-key']) {
        sh "scp -o StrictHostKeyChecking=no shell-cmds.sh ${ec2Instance}:~"
        sh "scp -o StrictHostKeyChecking=no docker-compose.yaml ${ec2Instance}:~"
        sh "ssh -o StrictHostKeyChecking=no ${ec2Instance} ${shellCmd}"
    }
} 

return this
