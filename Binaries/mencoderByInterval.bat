@ECHO off

SET interCapt=%1
ECHO interCapt : %interCapt%
SET durCapt=%2
ECHO durCapt : %durCapt%
SET durVid=%3
ECHO durVid : %durVid%
SET numVid=%4
ECHO numVid : %numVid%

REM R�cup�ration du nombre total d'images dans le dossier
SET nbTot=0
FOR %%G IN (".\*.jpg") DO (
	SET /a nbTot+=1
)
ECHO nbTot : %nbTot%

REM D�finition du nombre d'images � utiliser pour r�aliser une vid�o
SET /a nbImages=durCapt/interCapt
ECHO %nbImages% seront utilis�es pour la vid�o

REM D�termination du nombre d'images par secondes pour la vid�o
SET /a fps=nbImages/durVId
ECHO fps : %fps%

REM On r�cup�re l'index de la premi�re image � utilis�es en fonction du num�ro de la vid�o
REM SET /a nbImages*=numVid
IF %numVid%==0 (SET nbImages=0) ELSE (SET /a nbImages=nbTot-nbImages)
ECHO index deb : %nbImages%

REM On cr�e la liste des images � utiliser pour l'encodage
REM dans tmp.txt on met la liste des images tri�e par ordre croissant
REM puis on cr�e list.txt commen�ant � l'index d�fini pr�c�dement
DIR /OD /B ".\*.jpg">".\tmp.txt"
MORE +%nbImages% ".\tmp.txt">".\list.txt"

REM On proc�de � l'encodage
REM COPY mencoder.exe ".\%dir%\mencoder.exe"
mencoder mf://@list.txt -mf w=640:h=480:fps=%fps%:type=jpg -ovc copy -oac copy -o ".\Video\%numVid%.avi"
REM DEL mencoder.exe
REM DEL list.txt
REM CD ..

PAUSE
EXIT