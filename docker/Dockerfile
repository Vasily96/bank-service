FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build

# Cache Maven dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source after dependencies are cached
COPY src ./src

# Build the application (skip tests for speed)
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copy JAR file (matches any SNAPSHOT or versioned jar)
COPY --from=build /build/target/transfer-money-*.jar app.jar

RUN chown -R appuser:appgroup /app
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]