@echo off

set directory=%1
set fps=%2
set vidname=%3

IF NOT EXIST ".\%directory%" (echo Le dossier n'existe pas ! & goto end)

set nbPictures=0
FOR %%i in (".\%directory%\*.jpg") DO (set /a nbPictures+=1)

IF %nbPictures%==0 (echo Il n'y a pas de fichiers jpg dans ce dossier & goto end)
echo Il y a %nbPictures% images dans %directory%

IF NOT EXIST ".\%directory%\Video" (mkdir ".\%directory%\Video")

ffmpeg -f image2 -r %fps% -i ".\%directory%\frame%%04d.jpg" -vcodec libx264 ".\%directory%\Video\%vidName%.mp4"

:end