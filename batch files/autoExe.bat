@ECHO off
CLS

REM R�cup�ration des diff�rents param�tres
REM SET /p dir= Dossier o�  se trouvent les images : 
REM SET /p interval= Intrevale entre chaque capture (en s) : 
REM SET /p durTot= Temps d'acquisition total (en s) : 
REM SET /p quality= Qualit� de la conversion en JPEG (en %%) : 
REM SET /p interVid= Faire une nouvelle vid�o toute les ... (en s) : 
REM SET /p durCapt= Temps de capture utilis� pour la vid�o : 
REM SET /p durVid= Dur�e de la vid�o (en s) : 
SET interval=%1
SET durTot=%2
SET quality=%3
SET preview=%4
SET interVid=%5
SET durCapt=%6
SET durVid=%7

SET /a nbMaxVid= (durTot - durCapt) / interVid
ECHO %nbMaxVid% seront r�alis�es
SET cmpt=0

REM Cr�ation des dossiers de travail
IF NOT EXIST ".\Video" (MKDIR ".\Video")
IF NOT EXIST ".\tmp.txt" (ECHO. 2>".\tmp.txt")
IF NOT EXIST ".\list.txt" (ECHO. 2>".\list.txt")

REM On lance la capture
START autoCapture %durTot% %interval% %quality% %preview%
ECHO Capture lanc�e

REM TIMEOUT %durCapt%
SET /a durCaptms=(durCapt*1000)+5
ping 192.0.2.2 -n 1 -w %durCaptms% > nul

SET /a interVidms=interVid*1000

FOR /L %%G IN (0,1,%nbMaxVid%) DO (
	START mencoderByInterval %interval% %durCapt% %durVid% %%G
	ECHO Encodage de la vid�o %%G lanc�
	REM TIMEOUT %interVid%
	ping 192.0.2.2 -n 1 -w %interVidms%*1000 > nul
)

PAUSE
EXIT