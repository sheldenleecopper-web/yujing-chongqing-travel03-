@echo off
chcp 65001 >nul
set "MYSQL_BIN=C:\Program Files\MySQL\MySQL Server 8.0\bin"
set "BACKUP_DIR=%~dp0backups"
if not exist "%BACKUP_DIR%" mkdir "%BACKUP_DIR%"

set "STAMP=%date:~0,4%%date:~5,2%%date:~8,2%_%time:~0,2%%time:~3,2%"
set "STAMP=%STAMP: =0%"

"%MYSQL_BIN%\mysqldump.exe" -u root -p123456 -h 127.0.0.1 --protocol=TCP --default-character-set=utf8mb4 --databases yujing > "%BACKUP_DIR%\yujing_%STAMP%.sql"

if errorlevel 1 (
  echo 备份失败
) else (
  echo 备份完成：%BACKUP_DIR%\yujing_%STAMP%.sql
)
pause
