# Stage 1: Build the Application
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Dependency ক্যাশ করার জন্য প্রথমে pom.xml কপি করা
COPY pom.xml .
COPY src ./src

# Spring Boot app বিল্ড করা (Test স্কিপ করা হচ্ছে দ্রুত বিল্ডের জন্য)
RUN mvn clean package -DskipTests

# Stage 2: Run the Application
FROM eclipse-temurin:17-jre
WORKDIR /app

# Stage 1 থেকে তৈরি হওয়া .jar ফাইল কপি করা
COPY --from=build /app/target/*.jar app.jar

# Spring Boot ডিফল্ট পোর্ট
EXPOSE 8080

# Application রান করা
ENTRYPOINT ["java", "-jar", "app.jar"]