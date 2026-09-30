# Journal de migration : before (3.5) → after (4.1.1)

Chaque section correspond à une étape de l'article. Les commandes marquées « à exécuter » n'ont pas encore
été lancées sur ce dépôt : le résultat sera ajouté ici une fois obtenu.

## Étape 0 — préparer (sur 3.5)

- Passer à la dernière 3.5.x publiée (le `pom.xml` de `before/` part de 3.5.0).
- `-Xlint:deprecation` est activé dans le `maven-compiler-plugin` : la compilation des tests signale `@MockBean`.
- Test de contrat JSON en place : `ProductControllerContractTest`.

## Étape 1 — OpenRewrite (à exécuter)

```bash
cp -r before rewrite-run && cd rewrite-run
mvn -U org.openrewrite.maven:rewrite-maven-plugin:run \
  -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-spring:RELEASE \
  -Drewrite.activeRecipes=org.openrewrite.java.spring.boot4.UpgradeSpringBoot_4_0 \
  -Drewrite.exportDatatables=true
git diff --no-index ../before . > ../openrewrite.diff
```

À comparer ensuite avec `after/` : ce que la recette a fait, ce qu'elle a laissé.

## Étape 2 — starters

```diff
-  <artifactId>spring-boot-starter-web</artifactId>
+  <artifactId>spring-boot-starter-webmvc</artifactId>

-  <groupId>org.flywaydb</groupId>
-  <artifactId>flyway-core</artifactId>
+  <groupId>org.springframework.boot</groupId>
+  <artifactId>spring-boot-starter-flyway</artifactId>
```

## Étape 3 — Jackson 3

Aucun code Jackson explicite dans ce service : seul le test de contrat protège le format de sortie.

## Étape 4 — propriétés

```bash
cd after && mvn -Pmigration spring-boot:run   # lire les avertissements au démarrage, puis retirer le profil
```

## Étape 5 — tests

```diff
-import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
-import org.springframework.boot.test.mock.mockito.MockBean;
+import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
+import org.springframework.test.context.bean.override.mockito.MockitoBean;
@@
-    @MockBean
+    @MockitoBean
     ProductRepository repository;
```

Et, dans le `pom.xml`, le starter de test `spring-boot-starter-webmvc-test`.

## Résultats de vérification

| Date | Projet | JDK | Résultat |
|---|---|---|---|
| – | before | – | à exécuter |
| – | after | – | à exécuter |
