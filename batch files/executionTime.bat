
@echo off
rem -----------------------------------------------
rem mesure temps d'execution d'un batch
rem cree par Bigfish alias 3ddI7IHd
rem le 21-07-09
rem Modifié le 24-07-09
rem -----------------------------------------------

set /p cmd=Rentrez la commande à mesurer : 

set hdebut=%time:~,2%
set mdebut=%time:~3,2%
set sdebut=%time:~6,2%
set mlsdebut=%time:~9,2%

rem ************Lancement du bach ou de l'application externe************
start /wait cmd.exe /c "%cmd%"
rem ******************************************************************************

:fin
set hfin=%time:~,2%
set mfin=%time:~3,2%
set sfin=%time:~6,2%
set mlsfin=%time:~9,2%

Rem prise en compte du temps a cheval sur 2 heures
set /a htemps=hfin-hdebut
setlocal enableDelayedExpansion
if %htemps%==0 (
    set /a mtemps=mfin-mdebut
) else (
    set /a mtemps=60-mdebut
    set /a mtemps=!mtemps!+mfin
    if !mtemps! geq 60 (
        set /a mtemps=!mtemps!-60
    ) else (
        set /a htemps=!htemps!-1
    )
)

Rem prise en compte du temps a cheval sur 2 minutes
if !mtemps!==0 (
    set /a stemps=sfin-sdebut
) else (
    set /a stemps=60-sdebut
    set /a stemps=!stemps!+sfin
    if !stemps! geq 60 (
        set /a stemps=!stemps!-60
    ) else (
        set /a mtemps=!mtemps!-1
    )
)

Rem prise en compte du temps a cheval sur 2 secondes
if !stemps!==0 (
    set /a mlstemps=mlsfin-mlsdebut
) else (
    set /a mlstemps=100-mlsdebut
    set /a mlstemps=!mlstemps!+mlsfin
    if !mlstemps! geq 100 (
        set /a mlstemps=!mlstemps!-100
    ) else (
        set /a stemps=!stemps!-1
    )
)

rem preparation de l'ecriture du resultat
set htest=*%htemps%
set mtest=*%mtemps%
set stest=*%stemps%
set mlstest=*%mlstemps%

if %htest:~-2,1%==* set htemps=0%htemps%
if %mtest:~-2,1%==* set mtemps=0%mtemps%
if %stest:~-2,1%==* set stemps=0%stemps%
if %mlstest:~-2,1%==* set mlstemps=0%mlstemps%

rem ecriture du resultat
echo Temps d'execution: %htemps%:%mtemps%:%stemps%,%mlstemps%


echo.
echo (C)3ddI7IHd
echo.
pause
 