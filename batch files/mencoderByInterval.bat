@ECHO off

SET interCapt=%1
ECHO interCapt : %interCapt%
SET durCapt=%2
ECHO durCapt : %durCapt%
SET durVid=%3
ECHO durVid : %durVid%
SET numVid=%4
ECHO numVid : %numVid%

REM Récupération du nombre total d'images dans le dossier
SET nbTot=0
FOR %%G IN (".\*.jpg") DO (
	SET /a nbTot+=1
)
ECHO nbTot : %nbTot%

REM Définition du nombre d'images à utiliser pour réaliser une vidéo
SET /a nbImages=durCapt/interCapt
ECHO %nbImages% seront utilisées pour la vidéo

REM Détermination du nombre d'images par secondes pour la vidéo
SET /a fps=nbImages/durVId
ECHO fps : %fps%

REM On récupère l'index de la première image à utilisées en fonction du numéro de la vidéo
REM SET /a nbImages*=numVid
IF %numVid%==0 (SET nbImages=0) ELSE (SET /a nbImages=nbTot-nbImages)
ECHO index deb : %nbImages%

REM On crée la liste des images à utiliser pour l'encodage
REM dans tmp.txt on met la liste des images triée par ordre croissant
REM puis on crée list.txt commençant à l'index défini précédement
DIR /OD /B ".\*.jpg">".\tmp.txt"
MORE +%nbImages% ".\tmp.txt">".\list.txt"

REM On procède à l'encodage
REM COPY mencoder.exe ".\%dir%\mencoder.exe"
mencoder mf://@list.txt -mf w=640:h=480:fps=%fps%:type=jpg -ovc copy -oac copy -o ".\Video\%numVid%.avi"
REM DEL mencoder.exe
REM DEL list.txt
REM CD ..

PAUSE
EXIT