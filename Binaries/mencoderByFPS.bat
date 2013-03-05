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

copy mencoder.exe ".\%directory%\mencoder.exe"
cd .\%directory%
mencoder mf://*.jpg -mf w=640:h=480:fps=%fps%:type=jpg -ovc copy -oac copy -o ".\Video\%vidname%.avi"
del mencoder.exe
cd ..

:end