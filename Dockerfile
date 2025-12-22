# ==============================
# Stage 1: Build
# ==============================
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Build application
COPY src ./src
RUN mvn clean package -DskipTests

# ==============================
# Stage 2: Runtime
# ==============================
FROM eclipse-temurin:17-jre-alpine

# Create non-root user
RUN addgroup -S app && adduser -S app -G app
USER app

WORKDIR /app

COPY --from=build /app/target/gateway-*.jar app.jar

EXPOSE 10010

ENV JAVA_OPTS="\
 -XX:MaxRAMPercentage=75 \
 -XX:+UseG1GC \
 -XX:+UseContainerSupport \
 -Djava.security.egd=file:/dev/./urandom"

HEALTHCHECK --interval=30s --timeout=5s --start-period=30s \
 CMD wget -qO- http://localhost:10010/actuator/health || exit 1

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
