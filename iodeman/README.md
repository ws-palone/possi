#Iodeman
Ce projet représente le l'API de l'application POSSI. Il a été fait avec Spring Boot vous pourrez retrouver l'ensemble de ses dépendences dans le **pom.xml**<br>

##Installation
####Pré-réquis
- Java 8
- Maven
- PostgreSQL

####Étapes
- Commencer par cloner le projet `git clone https://github.com/ws-palone/possi.git`.
**Très important :** Mettez-vous dans le repertoire `possi/iodeman`. Ceci est très important avant de continuer car c'est dans ce dossier que se trouve le projet maven en question.
- Assurez-vous d'avoir créé la base de données dans PostgreSQL en lui donnant le nom de **possi**
- Ouvrez le fichier de configuration `src/main/resources/application.properties` <br>
Modifier ces lignes en fonction de votre environnement<br>
`spring.datasource.username=<votre_username>`<br>
`spring.datasource.password=<votre_mot_passe>`
- exécutez la commande `mvn compile spring-boot:run` normalement tout devrait bien se passer.
