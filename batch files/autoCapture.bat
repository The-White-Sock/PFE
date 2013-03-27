@ECHO off

SET duration=%1
ECHO duration : %duration%
SET interval=%2
ECHO interval : %interval%
SET quality=%3
ECHO quality : %quality%
SET preview=%4
ECHO preview : %preview%

SET /a duration*=1000
SET /a interval*=1000
SET /a nbFrames=duration/interval

ECHO nbFrames : %nbFrames%

SET command="convertjpg %quality%"

IF "%preview%"=="true" (
	RobotEyez /preview /delay 3000 /period %interval% /frames %nbFrames% /command %command% /number_files /bmp
) ELSE (
	RobotEyez /delay 3000 /period %interval% /frames %nbFrames% /command %command% /number_files /bmp
)

PAUSE
EXIT