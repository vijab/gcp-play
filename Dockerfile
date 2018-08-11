FROM frolvlad/alpine-scala
RUN apk add --no-cache curl
RUN \
  curl -L -o sbt-1.1.1.deb http://dl.bintray.com/sbt/debian/sbt-1.1.1.deb && \
  dpkg -i sbt-1.1.1.deb && \
  rm sbt-1.1.1.deb && \
  apt-get update && \
  apt-get install sbt && \
  sbt sbtVersion
# Set the working directory to /app
WORKDIR /app

# Copy the current directory contents into the container at /app
ADD . /app
RUN sbt assembly
EXPOSE 8888
RUN chmod +x target/scala-2.12/gcp-play-assembly-0.1.0-SNAPSHOT.jar
ENTRYPOINT java -cp target/scala-2.12/gcp-play-assembly-0.1.0-SNAPSHOT.jar main.ServerBootstrap
