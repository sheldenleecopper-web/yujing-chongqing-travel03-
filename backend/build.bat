@echo off
set "JAVA_HOME=C:\Users\FC\devtools\jdk-17.0.20.1+1"
set "MAVEN_HOME=C:\Users\FC\devtools\apache-maven-3.9.11"
set "PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%"
cd /d "%~dp0"
call mvn -DskipTests clean package
echo BUILD_EXIT=%ERRORLEVEL%
