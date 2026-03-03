@echo off
echo Checking services...
echo.

echo [1] MySQL on port 3306...
netstat -an | findstr ":3306 " | findstr "LISTENING"
if %errorlevel%==0 (echo   OK) else (echo   NOT RUNNING)

echo.
echo [2] Redis on port 6379...
netstat -an | findstr ":6379 " | findstr "LISTENING"
if %errorlevel%==0 (echo   OK) else (echo   NOT RUNNING)

echo.
echo [3] Spring Boot on port 8080...
netstat -an | findstr ":8080 " | findstr "LISTENING"
if %errorlevel%==0 (echo   OK) else (echo   NOT RUNNING)

echo.
pause
