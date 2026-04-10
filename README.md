# 📦 OpenInventory API

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.13-brightgreen)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange)](https://www.oracle.com/java/technologies/downloads/)
[![Docker](https://img.shields.io/badge/Docker-Enabled-blue)](https://www.docker.com/)

O **OpenInventory** é uma API robusta para gerenciamento de estoque e inventário, desenvolvida para oferecer controle preciso sobre entradas, saídas e autenticação segura de usuários.

## 🚀 Tecnologias Utilizadas

* **Java 21** (LTS)
* **Spring Boot 3.5**
* **Spring Security & JWT** (Autenticação Stateless)
* **Spring Data JPA** (Persistência)
* **PostgreSQL 15** (Banco de Dados Relacional)
* **Docker & Docker Compose** (Containerização)
* **Hibernate** (ORM)

## 🛠 Configurações de Ambiente

A aplicação está configurada para rodar em containers, otimizando o uso de memória com os seguintes parâmetros de JVM:
* `MaxRAMPercentage=75.0`: Garante que a JVM utilize até 75% da RAM do container.
* `UseG1GC`: Coletor de lixo focado em performance e baixa latência.

## ⚙️ Como Rodar o Projeto

### Pré-requisitos
* Docker e Docker Compose instalados.

### Passo a Passo
1. Clone o repositório:
   ```bash
   git clone [https://github.com/seu-usuario/OpenInventory.git](https://github.com/seu-usuario/OpenInventory.git)
