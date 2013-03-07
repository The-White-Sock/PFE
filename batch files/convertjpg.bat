@ECHO off

SET quality=%1

FOR %%F IN (*.bmp) DO (
	convertto ".\%%~nxF" -quality %quality% ".\%%~nF.jpg"
	DEL %%F
	ECHO converting to %%~nF.jpg
)