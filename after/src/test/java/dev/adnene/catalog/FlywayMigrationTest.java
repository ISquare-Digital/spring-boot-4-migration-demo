package dev.adnene.catalog;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Filet de sécurité de l'étape 2 : si l'auto-configuration Flyway disparaît
 * (flyway-core sans spring-boot-starter-flyway en 4.x), la table n'existe pas et ce test échoue.
 */
@SpringBootTest
class FlywayMigrationTest {

    @Autowired
    ProductRepository repository;

    @Test
    void migrationsHaveBeenApplied() {
        assertThat(repository.findAll()).hasSize(3);
        assertThat(repository.findById(1)).get()
                .extracting(Product::name).isEqualTo("Clavier mécanique");
    }
}
