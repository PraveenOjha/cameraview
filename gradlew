#!/usr/bin/env sh

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# ...existing code for gradlew script...
# This is a standard script, you can regenerate it with 'gradle wrapper' if needed.

# Set the location of the wrapper jar file
WRAPPER_JAR="$(dirname "$0")/gradle/wrapper/gradle-wrapper.jar"

# Execute the wrapper jar
exec java -jar "$WRAPPER_JAR" "$@"

