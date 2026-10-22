@echo off
chcp 65001 >nul
title 渝境智旅 - 一键启动

set "JAVA_HOME=C:\Users\FC\devtools\jdk-17.0.20.1+1"
set "NODE_HOME=C:\Users\FC\.workbuddy\binaries\node\versions\22.22.2"
set "PATH=%JAVA_HOME%\bin;%NODE_HOME%;%PATH%"
cd /d "%~dp0"

echo [1/3] 检查 MySQL 服务...
sc query MySQL80 | findstr /i "RUNNING" >nul
if errorlevel 1 (
  echo       MySQL80 未运行，尝试启动（需要管理员权限）...
  net start MySQL80 >nul 2>&1
) else (
  echo       MySQL80 已在运行
)

echo [2/3] 启动后端 8080 ...
start "yujing-backend" cmd /c "cd /d ""%~dp0backend"" && java -jar target\yujing-travel-backend-1.0.0.jar"

echo       等待后端就绪...
timeout /t 12 /nobreak >nul

echo [3/3] 启动前端 5173 ...
start "yujing-frontend" cmd /c "cd /d ""%~dp0frontend"" && ""%NODE_HOME%\node.exe"" ""%NODE_HOME%\node_modules\npm\bin\npm-cli.js"" run dev"

timeout /t 6 /nobreak >nul
echo.
echo 完成：前端 http://localhost:5173   后端 http://localhost:8080
echo 管理后台 http://localhost:5173/admin/login  (admin / admin123)
echo.
pause
