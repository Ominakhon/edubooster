FROM openjdk:21

ADD target/ricoin.jar ricoin.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "ricoin.jar"]