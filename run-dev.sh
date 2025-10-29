#!/bin/bash

# Exit immediately if any command fails
set -e

# Header
print_banner() {
  echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
  echo "🚀 Starting MELI Order System with profile: ${SPRING_PROFILE^^}"
  echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
}

# Checks the environment
check_requirements() {
  # Check for Java
  if ! command -v java &> /dev/null; then
    echo "❌ Java not found. Please install Java 17+ before continuing."
    exit 1
  fi

  # Check for Maven or Wrapper
  if ! command -v mvn &> /dev/null && [ ! -f "./mvnw" ]; then
    echo "❌ Maven not found. Please install Maven or include Maven Wrapper (mvnw)."
    exit 1
  fi
}

# Runs Spring boot
run_app() {
  # Detect Windows vs Unix
  if [[ "$OS" == "Windows_NT" ]]; then
    echo "🪟 Detected Windows environment."
    if [ -f "mvnw.cmd" ]; then
      mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=$SPRING_PROFILE"
    else
      mvn spring-boot:run "-Dspring-boot.run.profiles=$SPRING_PROFILE"
    fi
  else
    echo "🐧 Detected Unix-like environment."
    if [ -f "./mvnw" ]; then
      ./mvnw spring-boot:run -Dspring-boot.run.profiles=$SPRING_PROFILE
    else
      mvn spring-boot:run -Dspring-boot.run.profiles=$SPRING_PROFILE
    fi
  fi
}


# Main functionality
# Get selected profile from argument or default to 'dev'
SPRING_PROFILE=${1:-dev}

print_banner
check_requirements
run_app

echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
echo "✅ Application stopped or terminated manually."
echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
