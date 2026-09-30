package dev.adnene.catalog;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test de contrat JSON (étape 0 de l'article) : il fige la forme des réponses
 * pour détecter un changement silencieux de sérialisation lors du passage à Jackson 3.
 */
@WebMvcTest(ProductController.class)
class ProductControllerContractTest {

    @Autowired
    MockMvc mvc;

    @MockBean // déprécié depuis 3.4, supprimé en 4.0
    ProductRepository repository;

    @Test
    void listKeepsTheJsonContract() throws Exception {
        given(repository.findAll()).willReturn(List.of(
                new Product(1, "Clavier mécanique", 8990, LocalDate.of(2025, 1, 15))));

        mvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Clavier mécanique"))
                .andExpect(jsonPath("$[0].priceCents").value(8990))
                // date ISO-8601, pas un tableau [2025,1,15] ni un timestamp
                .andExpect(jsonPath("$[0].releasedOn").value("2025-01-15"));
    }

    @Test
    void unknownProductReturns404() throws Exception {
        given(repository.findById(42)).willReturn(Optional.empty());

        mvc.perform(get("/api/products/42"))
                .andExpect(status().isNotFound());
    }
}
