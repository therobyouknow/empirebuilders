@echo off

rem This batch file builds and preverifies the code.
rem it then packages them in a JAR file appropriately.

echo *** Creating directories ***

mkdir ..\tmpclasses
mkdir ..\classes

echo *** Compiling source files ***

javac -bootclasspath ..\..\..\lib\midpapi.zip -d ..\tmpclasses -classpath ..\tmpclasses ..\src\empire\*.java

echo *** Verifying class files ***

rem WARNING: When running under windows 9x the JAR may be incomplete
rem due to a bug in windows 98. Simply place a pause statement between
rem the preverify and JAR stages and wait 5 seconds before continuing
rem the build.

..\..\..\bin\preverify -classpath ..\..\..\lib\midpapi.zip;..\tmpclasses -d ..\classes ..\tmpclasses

echo *** Jaring verified class files ***
jar cmf MANIFEST.MF empire.jar -C ..\classes\empire .

echo *** Jaring resource files ***
jar umf MANIFEST.MF empire.jar -C ..\res\empire .

echo *** Don't forget to update the JAR file size in the JAD file ***

