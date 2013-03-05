@ECHO off

SET quality=%1

FOR %%F IN (*.bmp) DO (
	convert ".\%%~nxF" -quality %quality% ".\%%~nF.jpg"
	DEL %%F & echo converting to %%~nF.jpg
)