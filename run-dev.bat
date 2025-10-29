@echo off

setlocal enabledelayedexpansion

:: Header
echo ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
echo 🚀 Starting MELI Order System
echo ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

:: Profile selection

if "%~1"=="" (
    set "SPRING_PROFILE=dev"
) else (
    set "SPRING_PROFILE=%~1"
)
echo Using profile: %SPRING_PROFILE%
echo.

:: Environment checks

:: Check Java
where java >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo ❌ Java not found. Please install Java 17 or higher.
    goto end
)

:: Check Maven
where mvn >nul 2>nul
if %ERRORLEVEL% equ 0 (
    set "MAVEN_CMD=mvn"
) else if exist mvnw.cmd (
    set "MAVEN_CMD=mvnw.cmd"
) else (
    echo ❌ Maven not found. Please install Maven or include mvnw.cmd in your project.
    goto end
)

:: Run application

echo 🧠 Launching Spring Boot with profile "%SPRING_PROFILE%"...
echo ----------------------------------------------------------
%MAVEN_CMD% clean spring-boot:run -Dspring-boot.run.profiles=%SPRING_PROFILE%
if %ERRORLEVEL% neq 0 (
    echo ❌ Error: Application failed to start.
    goto end
)

echo ----------------------------------------------------------
echo ✅ Application stopped or terminated manually.
echo ----------------------------------------------------------

:end
endlocal
pause
