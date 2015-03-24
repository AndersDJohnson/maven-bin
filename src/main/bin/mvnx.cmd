@echo off

set ARG1=%1
set RESTVAR=

shift

:loop1

if "%1"=="" goto after_loop
set RESTVAR=%RESTVAR% %1
shift
goto loop1

:after_loop

set RESTVAR_ESC=%RESTVAR:"=\"%

REM echo ARG1 %ARG1%
REM echo RESTVAR %RESTVAR%
REM echo RESTVAR_ESC %RESTVAR_ESC%

mvn me.andrz:maven-executable:executable "-Dartifact=%ARG1%" "-Darguments=%RESTVAR_ESC%"

@echo on
