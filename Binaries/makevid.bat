REM nettoie l'écran et désactive l'affichage des commandes
@echo off
cls

:begin
	REM on récupère le nom du dossier
	echo Entrez le nom du dossier contenant les images :
	set /p directory=
	
	REM test pour savoir si le dossier existe, s'il n'existe pas on redemande à l'utilisateur
	IF NOT EXIST ".\%directory%" (echo Le dossier n'existe pas ! & goto begin)
	
	REM on initialise le nbPictures
	set nbPictures=0
	REM boucle pour chacque jpg contenu dans le répertoire on incrémente nbPictures
	FOR %%i in (".\%directory%\*.jpg") DO (set /a nbPictures+=1)
	
	REM s'il n'y a pas d'images dans le dossier on redemande à l'utilisateur de spécifier un dossier
	IF %nbPictures%==0 (echo Il n'y a pas de fichiers jpg dans ce dossier & goto begin)
	echo Il y a %nbPictures% images dans %directory%
	
	REM Ici on récupère le nombre de déimals pour faciliter l'utilisation de ffmpeg
	echo Entrez le nombre de décimals composant le nom des images (ex: image_0152 =^> 4) :
	set /p nbdigits=

:duration
	REM on récupère la durée de la vidéo
	echo Entrez la durée souhaitée de la vidéo (en secondes) :
	set /p duration=
	
	REM si la durée est <= à 0 on la redemande
	IF %duration% LEQ 0 (echo La durée doit-être superieure . 0 & goto duration)
	set /a frameRate= %nbPictures%/%duration%
	REM affichage des fps
	echo La vidéo aura un frame rate de %frameRate%fps

:name
	REM récupèration du nom de la vidéo
	echo Entrez le nom de la vidéo :
	set /p vidName=
	
	REM si le dossier vidéo n'existe pas on le crée
	IF NOT EXIST ".\%directory%\Video" (mkdir ".\%directory%\Video")

REM on lance la commande ffmpeg
ffmpeg -f image2 -r %frameRate% -i ".\%directory%\frame%%0%nbdigits%d.jpg" -vcodec libx264 ".\%directory%\Video\%vidName%.mp4"
REM puis on lance la vidéo
".\%directory%\Video\%vidName%.mp4"

pause