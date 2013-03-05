REM nettoie l'�cran et d�sactive l'affichage des commandes
@echo off
cls

:begin
	REM on r�cup�re le nom du dossier
	echo Entrez le nom du dossier contenant les images :
	set /p directory=
	
	REM test pour savoir si le dossier existe, s'il n'existe pas on redemande � l'utilisateur
	IF NOT EXIST ".\%directory%" (echo Le dossier n'existe pas ! & goto begin)
	
	REM on initialise le nbPictures
	set nbPictures=0
	REM boucle pour chacque jpg contenu dans le r�pertoire on incr�mente nbPictures
	FOR %%i in (".\%directory%\*.jpg") DO (set /a nbPictures+=1)
	
	REM s'il n'y a pas d'images dans le dossier on redemande � l'utilisateur de sp�cifier un dossier
	IF %nbPictures%==0 (echo Il n'y a pas de fichiers jpg dans ce dossier & goto begin)
	echo Il y a %nbPictures% images dans %directory%
	
	REM Ici on r�cup�re le nombre de d�imals pour faciliter l'utilisation de ffmpeg
	echo Entrez le nombre de d�cimals composant le nom des images (ex: image_0152 =^> 4) :
	set /p nbdigits=

:duration
	REM on r�cup�re la dur�e de la vid�o
	echo Entrez la dur�e souhait�e de la vid�o (en secondes) :
	set /p duration=
	
	REM si la dur�e est <= � 0 on la redemande
	IF %duration% LEQ 0 (echo La dur�e doit-�tre superieure . 0 & goto duration)
	set /a frameRate= %nbPictures%/%duration%
	REM affichage des fps
	echo La vid�o aura un frame rate de %frameRate%fps

:name
	REM r�cup�ration du nom de la vid�o
	echo Entrez le nom de la vid�o :
	set /p vidName=
	
	REM si le dossier vid�o n'existe pas on le cr�e
	IF NOT EXIST ".\%directory%\Video" (mkdir ".\%directory%\Video")

REM on lance la commande ffmpeg
ffmpeg -f image2 -r %frameRate% -i ".\%directory%\frame%%0%nbdigits%d.jpg" -vcodec libx264 ".\%directory%\Video\%vidName%.mp4"
REM puis on lance la vid�o
".\%directory%\Video\%vidName%.mp4"

pause