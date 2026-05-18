#!/bin/bash

# Diretório onde os backups serão salvos
BACKUP_DIR="./backups"

# Configurações do Banco de Dados
DB_CONTAINER_NAME="estudosspringloginauthemail-db-1"
DB_NAME="estoque_db"
DB_USER="admin"

# Cria o diretório de backup se ele não existir
mkdir -p "$BACKUP_DIR"

# Gera o nome do arquivo com a data e hora atual (Formato: backup_estoque_db_YYYYMMDD_HHMM.sql)
FILENAME="backup_${DB_NAME}_$(date +'%Y%m%d_%H%M').sql"

echo "Iniciando backup do banco de dados..."

# Executa o pg_dump dentro do container Docker e salva no arquivo local
# Nota: No Linux usamos -i em vez de -t no docker exec para evitar problemas com cronjobs/scripts automatizados
docker exec -i "$DB_CONTAINER_NAME" pg_dump -U "$DB_USER" "$DB_NAME" > "$BACKUP_DIR/$FILENAME"

echo ""
echo "Backup concluído com sucesso!"
echo "Arquivo gerado: $BACKUP_DIR/$FILENAME"
echo ""