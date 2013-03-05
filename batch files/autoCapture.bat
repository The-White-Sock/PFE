@ECHO off

SET duration=%1
ECHO duration : %duration%
SET period=%2
ECHO period : %period%
SET quality=%3
ECHO quality : %quality%

REM SET /p duration= Veuillez rentrer la durée de la capture (en s): 
SET /a duration*=1000

REM SET /p period= Veuillez rentrer l'intervale de temps entre chaque capture (en ms) : 
SET /a period*=1000

SET /a nbFrames=duration/period
ECHO nbFrames : %nbFrames%

REM SET /p quality= Rentrez la qualité de compression JPG (de 1 à 100) : 

SET command="convertjpg %quality%"

RobotEyez /preview /delay 3000 /period %period% /frames %nbFrames% /command %command% /number_files /bmp

PAUSE
EXIT