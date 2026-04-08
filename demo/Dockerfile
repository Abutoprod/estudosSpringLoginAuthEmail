# 1. Definir a versão do Java usado
FROM eclipse-temurin:21-jdk-jammy

# 2. Definimos que dentro da "caixa", vamos trabalhar na pasta /app
WORKDIR /app
# 3. Copiamos o Maven (ferramenta de build) para dentro da caixa
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# 4. Baixamos as dependências (sem rodar o código ainda)
RUN ./mvnw dependency:resolve

# 5. Copiamos o seu código fonte para dentro
COPY src ./src

# 6. Comando para rodar o projeto
CMD ["./mvnw", "spring-boot:run"]