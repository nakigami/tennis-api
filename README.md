# Tennis API - BForbank Test Technique


**Avant de poursuivre la lecture de ce README, veuillez consulter l'image ci-dessus et la vidéo de démonstration.**

<img width="2233" height="555" alt="Architecture" src="https://github.com/user-attachments/assets/243777e2-aec4-4808-9800-5c9bf2fdb7fd" />

## Présentation

Cette API REST est conçue dans le cadre d'un test technique pour BForbank. Elle est appelée par le BFF disponible ici : [Tennis Score Tracker BFF](https://github.com/nakigami/tennis-score-tracker-bff) et sert de backend (d'une façon indirecte) pour l'application front-end React : [Tennis Score Tracker React App](https://github.com/nakigami/tennis-score-tracker-react-app/).

L'API est développée selon une architecture hexagonale (ports & adapters), permettant une séparation claire entre la logique métier et les détails d'infrastructure.

## Fonctionnalités

L'API permet de gérer les scores d'un match de tennis selon les règles suivantes :
- Gestion des points (0, 15, 30, 40)
- Gestion des avantages
- Détermination du vainqueur
- Historique des matchs

Après chaque appel API, le résultat est publié dans un topic Kafka et sauvegardé dans une base de données PostgreSQL.

## Endpoints exposés

- `GET /matches` : Récupère tous les matchs
- `GET /matches/{id}` : Récupère un match spécifique par son ID
- `POST /matches` : Crée un nouveau match à partir d'une séquence de points

## Exemple de réponse API

```json
{
    "id": "a73af541-b125-4ed9-a18b-683cb2626230",
    "playerAName": "A",
    "playerAPoints": 40,
    "playerAHasAdvantage": true,
    "playerBName": "B",
    "playerBPoints": 40,
    "playerBHasAdvantage": false,
    "status": "FINISHED",
    "winner": "Player A",
    "date": "2025-07-16T00:47:03+02:00"
}
```

## Services disponibles

- **API REST** : http://localhost:8080
  - Documentation Swagger : http://localhost:8080/swagger-ui/index.html
  - Health check : http://localhost:8080/actuator/health
- **Interface Kafka** : http://localhost:8081/

## Gestion des erreurs

L'API implémente une gestion globale des erreurs via `GlobalExceptionHandler` qui transforme les exceptions en réponses HTTP appropriées avec des messages d'erreur clairs et des codes d'état HTTP correspondants. Les erreurs sont formatées selon le modèle `ApiErrorResponse`.

## Tests

Le projet inclut plusieurs niveaux de tests :
- Tests unitaires pour les services et la logique métier
- Tests d'intégration avec Mock MVC pour tester les contrôleurs REST
- Tests BDD avec Cucumber pour valider les scénarios fonctionnels

## Intégration Kafka

La fonction `generateMatchLog` dans le service `ScoreService` est responsable de la génération du log de match qui est ensuite publié dans un topic Kafka via `MatchResultProducer`. Cela permet de notifier d'autres services des résultats de match en temps réel.

## Architecture Hexagonale

L'API est conçue selon les principes de l'architecture hexagonale (également connue sous le nom de "Ports & Adapters"), qui offre plusieurs avantages :

- **Séparation des préoccupations** : Le domaine métier est isolé des détails d'infrastructure
- **Testabilité améliorée** : La logique métier peut être testée indépendamment des adaptateurs externes
- **Flexibilité** : Les adaptateurs peuvent être remplacés sans modifier le cœur métier

La structure du projet reflète cette architecture :

- **Domain** : Contient la logique métier pure, les modèles et les règles du jeu de tennis
  - **model** : Les entités du domaine (Match, Player)
  - **service** : Les services métier (ScoreService)
  - **port** :
    - **input** : Interfaces utilisées par les adaptateurs primaires (API REST)
    - **output** : Interfaces utilisées par le domaine pour communiquer avec les adaptateurs secondaires (Repository, Kafka)
  - **exception** : Exceptions spécifiques au domaine

- **Infrastructure** : Contient les adaptateurs qui permettent au domaine de communiquer avec le monde extérieur
  - **adapter** :
    - **controller** : Adaptateurs primaires (REST API)
    - **repository** : Adaptateurs secondaires pour la persistance
    - **kafka** : Adaptateurs secondaires pour la messagerie
    - **exceptions** : Gestion globale des exceptions

Cette architecture permet une évolution indépendante du domaine métier et des technologies d'infrastructure.

## Versionnement de l'API

L'API suit une approche de versionnement pour garantir la compatibilité et faciliter l'évolution :

- **Versionnement des endpoints** : Les endpoints sont préfixés par la version (ex: `/api/v1/matches`)

Cette approche de versionnement permet :
- D'assurer la rétrocompatibilité pour les clients existants
- De faire évoluer l'API sans perturber les intégrations existantes
- De déployer plusieurs versions de l'API simultanément si nécessaire

## Démarrage des services

Le projet utilise Docker Compose pour orchestrer tous les services nécessaires (API, Kafka, PostgreSQL). Pour démarrer l'ensemble :

```bash
docker-compose up
```

## Note pour les évaluateurs

Si vous avez des questions concernant ce projet, n'hésitez pas à les garder pour l'entretien. Je serai ravi d'y répondre
