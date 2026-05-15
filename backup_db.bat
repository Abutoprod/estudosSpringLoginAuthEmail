@echo off
set BACKUP_DIR=./backups
:: NOME CORRIGIDO AQUI EMBAIXO:
set DB_CONTAINER_NAME=estudosspringloginauthemail-db-1
set DB_NAME=estoque_db
set DB_USER=admin

if not exist "%BACKUP_DIR%" mkdir "%BACKUP_DIR%"

set FILENAME=backup_%DB_NAME%_%date:~-4%%date:~3,2%%date:~0,2%_%time:~0,2%%time:~3,2%.sql
set FILENAME=%FILENAME: =0%

echo Iniciando backup do banco de dados...
docker exec -t %DB_CONTAINER_NAME% pg_dump -U %DB_USER% %DB_NAME% > "%BACKUP_DIR%/%FILENAME%"

echo.
echo Backup concluido com sucesso! 
echo Arquivo gerado: %BACKUP_DIR%/%FILENAME%
echo.
pause