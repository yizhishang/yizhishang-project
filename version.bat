@echo off
set /p newVersion=请输入版本号：
echo 开始修改项目，请稍候......
echo --------------------------------------------

mvn versions:set -P !artifactory,!oss,!sonar -DnewVersion=%newVersion%
echo  恭喜您  创建成功  进入下一步......
pause