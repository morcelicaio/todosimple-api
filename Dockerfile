# Baixa uma imagem (contâiner linux) com as seguintes configurações:
FROM maven:3.8.3-openjdk-17

ENV PROJECT_HOME /usr/src/todosimpleapp
ENV JAR_NAME todosimpleapp.jar

# Executa o comando para criar o diretório de destino
RUN mkdir -p $PROJECT_HOME
WORKDIR $PROJECT_HOME

# Copia tudo que está no WORKDIR para dentro do docker.
COPY . .

# Empacota a aplicação como um um arquivo .jar.
RUN mvn clean package -DskipTests

# Move o arquivo jar que é gerado dentro de /target.
RUN mv $PROJECT_HOME/target/$JAR_NAME $PROJECT_HOME/

# Comando que o docker executa quando o container é iniciado.
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "todosimpleapp.jar"]
