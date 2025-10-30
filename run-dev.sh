#!/bin/bash
echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
echo "     MELI OS - Profile Selector  "
echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
echo "Choose a profile to run:"
echo "1) dev"
echo "2) test"
echo "3) prod"
read -p "Enter option (1-3): " option

case $option in
  1) PROFILE="dev" ;;
  2) PROFILE="test" ;;
  3) PROFILE="prod" ;;
  *) echo "[ERROR] Invalid option"; exit 1 ;;
esac

echo "Starting with profile: $PROFILE"
echo

# Verify Maven
if ! command -v mvn &> /dev/null; then
  echo "[ERROR] Maven not found in PATH."
  exit 1
fi

# Verify Java
if ! command -v java &> /dev/null; then
  echo "[ERROR] Java not found in PATH."
  exit 1
fi

# Update .env file
if [ -f ".env" ]; then
  grep -v "^SPRING_PROFILES_ACTIVE=" .env > .env.tmp
  echo "SPRING_PROFILES_ACTIVE=$PROFILE" >> .env.tmp
  mv .env.tmp .env
  echo "[INFO] .env updated: SPRING_PROFILES_ACTIVE=$PROFILE"
else
  echo "SPRING_PROFILES_ACTIVE=$PROFILE" > .env
  echo "[INFO] .env file created with SPRING_PROFILES_ACTIVE=$PROFILE"
fi
echo

# Run application
if ! mvn clean spring-boot:run -Dspring-boot.run.profiles=$PROFILE; then
  echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
  echo "[ERROR] Application failed to start."
  echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
  exit 1
fi

echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
echo "APPLICATION STOPPED SUCCESSFULLY"
echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
