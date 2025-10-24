# Runtime stage only
FROM openjdk:17-slim
WORKDIR /app

# Install curl for healthcheck
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Copy pre-built jar
COPY tripcut-0.0.1-SNAPSHOT.jar app.jar

# Create log directory
RUN mkdir -p /var/log/spring-boot

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
