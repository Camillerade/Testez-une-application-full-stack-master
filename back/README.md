# Yoga App - Backend

Il s'agit du backend de l'application Yoga App. Il est construit avec Spring Boot et comprend des fonctionnalités telles que l'authentification, la validation et l'intégration avec une base de données MySQL. L'application inclut également JaCoCo pour le suivi de la couverture de code.

## Prérequis

Avant d'exécuter le projet, assurez-vous d'avoir les outils suivants installés sur votre système :

- **Java 8** ou version ultérieure
- **Maven** 3.6 ou version ultérieure
- **MySQL** (ou une base de données compatible)
- **IDE** (par exemple, IntelliJ IDEA, Eclipse) pour le développement (facultatif)

## Configuration

### Cloner le dépôt

git clone https://github.com/Camillerade/Testez-une-application-full-stack-master/tree/main/back

cd yoga-app



## Configuration de la base de données

Assurez-vous que MySQL est en cours d'exécution sur votre machine et créez une base de données pour l'application. 

Mettez à jour le fichier application.properties (ou application.yml) dans le dossier src/main/resources avec vos identifiants MySQL et le nom de la base de données.



## properties

spring.datasource.url=jdbc:mysql://localhost:3306/votre-nom-de-base-de-données

spring.datasource.username=vos-identifiants-de-base-de-données

spring.datasource.password=vos-mots-de-passe-de-base-de-données

spring.jpa.hibernate.ddl-auto=update



## Installation des dépendances

### Installez les dépendances requises en exécutant la commande Maven suivante :

mvn install


### Pour démarrer l'application, exécutez :

mvn spring-boot:run

L'application sera accessible par défaut à l'adresse http://localhost:8080.

## Tests avec couverture de code Jacoco

### Pour exécuter les tests et générer les rapports de couverture de code Jacoco, exécutez :

mvn clean test



### Cette commande effectuera les actions suivantes :

Exécutera tous les tests dans le projet.

Générera un rapport de couverture de code Jacoco.

Les rapports de couverture de code seront disponibles dans le répertoire target/site/jacoco/index.html. Ouvrez ce fichier dans votre navigateur pour visualiser les résultats.



## Remarques
Assurez-vous que votre base de données MySQL est en cours d'exécution et que les informations de connexion sont correctes.
Si vous rencontrez des problèmes lors de l'exécution des tests ou de la génération des rapports de couverture de code, vérifiez les configurations de votre environnement et les logs générés pour identifier les erreurs.
