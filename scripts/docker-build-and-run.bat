@echo off

REM Check if docker is available
docker --version > nul 2>&1
if errorlevel 1 goto error

REM Check if docker-compose is available
docker-compose --version > nul 2>&1
if errorlevel 1 goto error

REM Navigate to the root directory where docker-compose.yml resides
cd ..

REM Stop and remove the existing containers
docker-compose down

REM Build app image
docker build . -t contact_book:latest

REM Run the services using docker-compose
docker-compose up -d

echo Services are now up and running!
exit /b

:error
echo Docker or Docker Compose is not installed. Aborting.
exit /b 1