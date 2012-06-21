@echo off
rem This file runs the empire.jad/jar file in the emulator.

cd ..\..\..\

set CLASSPATH=lib\kvem.jar;lib\kenv.zip;lib\lime.jar;apps\empire\bin\empire.jar

java -Dkvem.home=. com.sun.kvem.midp.Main DefaultGrayPhone -descriptor apps\empire\bin\empire.jad

cd apps\empire\bin
