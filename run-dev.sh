#!/bin/bash
echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
echo "Starting MELI OS - DEV Profile"
echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"

# Ensure Maven is in PATH and Java 17+ is available
mvn clean spring-boot:run -Dspring-boot.run.profiles=dev

echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
echo "APPLICATION STOPPED"
echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"