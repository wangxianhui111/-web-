@echo off
echo ========================================
echo   企业级快速开发平台 - 启动脚本
echo ========================================
echo.

REM 检查Nginx是否已安装
where nginx >nul 2>nul
if %errorlevel% neq 0 (
    echo [错误] 未找到Nginx，请先安装Nginx
    echo 下载地址: https://nginx.org/en/download.html
    echo.
    pause
    exit /b 1
)

REM 启动Nginx
echo [1/2] 启动Nginx...
start "Nginx" nginx -c "e:\pythonprojects\通用万能模板项目\nginx.conf"

REM 等待Nginx启动
timeout /t 2 /nobreak >nul

REM 启动Spring Boot
echo [2/2] 启动Spring Boot后端服务...
echo.
echo 服务启动完成后，访问: http://localhost
echo API文档: http://localhost/doc.html
echo.

REM 启动Spring Boot (在后台运行)
start "Spring Boot" cmd /k "cd /d e:\pythonprojects\通用万能模板项目 && mvn21 spring-boot:run"

echo.
echo ========================================
echo   启动完成!
echo ========================================
pause
