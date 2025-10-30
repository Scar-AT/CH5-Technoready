@echo off
title MELI OS Runner
setlocal enabledelayedexpansion

echo ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
echo      MELI OS - Profile Selector
echo ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
echo 1) dev
echo 2) test
echo 3) prod
set /p option="Enter option (1-3): "

if "%option%"=="1" set PROFILE=dev
if "%option%"=="2" set PROFILE=test
if "%option%"=="3" set PROFILE=prod

if not defined PROFILE (
  echo [ERROR] Invalid option. Exiting...
  exit /b 1
)

echo Starting with profile: %PROFILE%
echo.

:: Check Maven
where mvn >nul 2>nul
if %errorlevel% neq 0 (
  echo [ERROR] Maven not found in PATH.
  echo Please install Maven or add it to PATH.
  pause
  exit /b 1
)

:: Check Java
where java >nul 2>nul
if %errorlevel% neq 0 (
  echo [ERROR] Java not found in PATH.
  echo Please install Java 17+ or add it to PATH.
  pause
  exit /b 1
)

:: Update .env file
if exist .env (
  for /f "usebackq delims=" %%a in (`findstr /v "SPRING_PROFILES_ACTIVE=" .env`) do (
    echo %%a>>.env.tmp
  )
  echo SPRING_PROFILES_ACTIVE=%PROFILE%>>.env.tmp
  move /y .env.tmp .env >nul
  echo [INFO] .env updated: SPRING_PROFILES_ACTIVE=%PROFILE%
) else (
  echo SPRING_PROFILES_ACTIVE=%PROFILE%>.env
  echo [INFO] .env file created with SPRING_PROFILES_ACTIVE=%PROFILE%
)
echo.

:: Run application
echo Running Spring Boot application...
mvn clean spring-boot:run -Dspring-boot.run.profiles=%PROFILE%

if %errorlevel% neq 0 (
  echo [ERROR] Application failed to start.
  echo ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  echo APPLICATION STOPPED WITH ERRORS
  echo ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  pause
  exit /b 1
)

echo ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
echo APPLICATION STOPPED SUCCESSFULLY
echo ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
pause
endlocal
