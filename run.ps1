$env:JAVA_HOME = "D:\soft\jdk\jdk21"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
Set-Location "e:\pythonprojects\通用万能模板项目"
& "E:\maven\apache-maven-3.9.5\bin\mvn.cmd" spring-boot:run
