@echo off
REM GitHub 推送脚本

echo ========================================
echo   GitHub 推送脚本
echo ========================================

REM 设置仓库信息
set REPO_NAME=通用web项目基座模板

REM 初始化Git（如果还没有）
if not exist .git (
    echo [1/5] 初始化Git仓库...
    git init
    git add .
    git commit -m "Initial commit: 企业级Java快速开发基座项目"
)

REM 添加远程仓库（请先在GitHub创建仓库）
echo [2/5] 添加远程仓库...
echo 请先在GitHub创建仓库，然后输入仓库URL
echo 例如: https://github.com/你的用户名/%REPO_NAME%.git
set /p REMOTE_URL=请输入仓库URL:

git remote add origin %REMOTE_URL%

REM 推送代码
echo [3/5] 推送到GitHub...
git push -u origin master

echo [4/5] 推送标签...
git tag -a v1.0.0 -m "Initial release"
git push origin v1.0.0

echo [5/5] 完成！
echo ========================================
echo 推送成功！
echo ========================================

pause
