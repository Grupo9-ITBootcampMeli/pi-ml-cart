FROM openjdk:11
MAINTAINER group9
VOLUME /tmp
EXPOSE 8082
ADD target/cart-0.0.1-SNAPSHOT.jar cart.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/cart.jar"]
