@echo off
set "JAVA_HOME=C:\Users\FC\devtools\jdk-17.0.20.1+1"
set "PATH=%JAVA_HOME%\bin;%PATH%"
cd /d "%~dp0"
if not defined DEEPSEEK_API_KEY set "DEEPSEEK_API_KEY=sk-test"
java -jar target\yujing-travel-backend-1.0.0.jar
