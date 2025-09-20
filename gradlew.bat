@echo off

REM ##########################################################################
REM
REM  Gradle start up script for Windows
REM
REM ##########################################################################

REM ...existing code for gradlew.bat script...
REM This is a standard script, you can regenerate it with 'gradle wrapper' if needed.

set WRAPPER_JAR=%~dp0\gradle\wrapper\gradle-wrapper.jar

java -jar "%WRAPPER_JAR%" %*

