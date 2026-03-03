@echo off
echo ========================================
echo   Docker镜像构建脚本
echo ========================================
echo.

echo [1/3] 检查Docker环境...
docker --version >nul 2>nul
if %errorlevel% neq 0 (
    echo [错误] 未安装Docker，请先安装Docker Desktop
    pause
    exit /b 1
)

echo [2/3] 构建并启动所有容器...
docker-compose up -d --build

echo.
echo [3/3] 查看容器状态...
docker-compose ps

echo.
echo ========================================
echo   启动完成!
echo ========================================
echo.
echo 访问地址:
echo   - 前端页面: http://localhost
echo   - API文档:   http://localhost/doc.html
echo.
echo 常用命令:
echo   docker-compose logs -f     查看日志
echo   docker-compose stop         停止
echo   docker-compose down         销毁
echo.
pause
