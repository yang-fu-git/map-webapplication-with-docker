./gradlew clean shadowJar

java -Xmx6g -cp build/libs/MapServer-0.0.1-SNAPSHOT-all.jar org.programmierprojekt.server.MapServerApplication $1
