# --- Stage 1: Build ---
FROM gradle:8.10-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle installDist --no-daemon

# --- Stage 2: Runtime ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/install/simpus /app
ENTRYPOINT ["/app/bin/simpus"]
