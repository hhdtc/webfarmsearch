@echo OFF
REM javac -d ..\bin ..\src\*.java
javadoc -d ..\docs -author -version ..\src\main\java\db\MySQL.java
REM java -cp "..\bin" HW3.src.BB_gui