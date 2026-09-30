package dev.adnene.catalog;

import java.time.LocalDate;

/**
 * Produit du catalogue. Le prix est en centimes pour que le contrat JSON ne dépende pas
 * de la sérialisation des décimaux.
 */
public record Product(long id, String name, long priceCents, LocalDate releasedOn) {
}
