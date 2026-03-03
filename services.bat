@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

:menu
cls
echo ========================================
echo   服务管理脚本
echo ========================================
echo.
echo   1. 启动所有服务
echo   2. 停止所有服务
echo   3. 重启所有服务
echo   4. 查看服务状态
echo   5. 启动单个服务
echo   6. 停止单个服务
echo   0. 退出
echo.
echo ========================================
set /p choice=请选择操作 (0-6):

if "%choice%"=="1" goto start_all
if "%choice%"=="2" goto stop_all
if "%choice%"=="3" goto restart_all
if "%choice%"=="4" goto status
if "%choice%"=="5" goto start_single
if "%choice%"=="6" goto stop_single
if "%choice%"=="0" exit

echo 无效选择，请重新选择
timeout /t 2 >nul
goto menu

:start_all
echo.
echo 正在启动所有服务...
echo.
echo [1/3] 启动 MySQL...
net start MySQL80 >nul 2>&1
net start MySQL >nul 2>&1
echo       MySQL: 已启动或已运行

echo [2/3] 启动 Redis...
net start Redis >nul 2>&1
redis-server --daemonize yes 2>nul
echo       Redis: 已启动或已运行

echo [3/3] 启动 Nginx...
start /b nginx >nul 2>&1
echo       Nginx: 已启动或已运行

echo.
echo 所有服务启动完成！
pause
goto menu

:stop_all
echo.
echo 正在停止所有服务...
echo.
echo [1/3] 停止 Nginx...
taskkill /F /IM nginx.exe >nul 2>&1
echo       Nginx: 已停止

echo [2/3] 停止 Redis...
taskkill /F /IM redis-server.exe >nul 2>&1
redis-cli shutdown 2>nul
echo       Redis: 已停止

echo [3/3] 停止 MySQL...
net stop MySQL80 >nul 2>&1
net stop MySQL >nul 2>&1
echo       MySQL: 已停止

echo.
echo 所有服务已停止！
pause
goto menu

:restart_all
echo.
call :stop_all
timeout /t 2 >nul
call :start_all
goto menu

:status
cls
echo ========================================
echo   服务状态
echo ========================================
echo.

echo [MySQL]
net start | findstr /i "MySQL" >nul
if %errorlevel%==0 (
    echo   状态: 运行中
) else (
    echo   状态: 未运行
)

echo.
echo [Redis]
net start | findstr /i "Redis" >nul
if %errorlevel%==0 (
    echo   状态: 运行中
) else (
    tasklist | findstr /i "redis-server" >nul
    if %errorlevel%==0 (
        echo   状态: 运行中
    ) else (
        echo   状态: 未运行
    )
)

echo.
echo [Nginx]
tasklist | findstr /i "nginx.exe" >nul
if %errorlevel%==0 (
    echo   状态: 运行中
) else (
    echo   状态: 未运行
)

echo.
pause
goto menu

:start_single
cls
echo ========================================
echo   启动单个服务
echo ========================================
echo.
echo   1. MySQL
echo   2. Redis
echo   3. Nginx
echo   0. 返回
echo.
set /p svc_choice=请选择服务 (0-3):

if "%svc_choice%"=="1" net start MySQL80 & goto menu
if "%svc_choice%"=="2" net start Redis & goto menu
if "%svc_choice%"=="3" start /b nginx & goto menu
if "%svc_choice%"=="0" goto menu
goto start_single

:stop_single
cls
echo ========================================
echo   停止单个服务
echo ========================================
echo.
echo   1. MySQL
echo   2. Redis
echo   3. Nginx
echo   0. 返回
echo.
set /p svc_choice=请选择服务 (0-3):

if "%svc_choice%"=="1" net stop MySQL80 & goto menu
if "%svc_choice%"=="2" taskkill /F /IM redis-server.exe & goto menu
if "%svc_choice%"=="3" taskkill /F /IM nginx.exe & goto menu
if "%svc_choice%"=="0" goto menu
goto stop_single
