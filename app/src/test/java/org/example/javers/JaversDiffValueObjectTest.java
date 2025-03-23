package org.example.javers;

import org.example.DBExtension;
import org.example.event_field.Amount;
import org.javers.core.Javers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

@ExtendWith(DBExtension.class)
class JaversDiffValueObjectTest {
    private static final Logger logger = LoggerFactory.getLogger(JaversDiffValueObjectTest.class);

    private final Javers javers;

    public JaversDiffValueObjectTest(Javers javers) {
        this.javers = javers;
    }

    @Test
    void valueObjectCompare_differentValues_Test() {
        final var amount100EUR = Amount.builder().value(BigDecimal.valueOf(100)).ccy("EUR").build();
        final var amount200USD = Amount.builder().value(BigDecimal.valueOf(200)).ccy("USD").build();

        final var diff = javers.compare(amount100EUR, amount200USD);
        logger.info(diff.prettyPrint());
        logger.info(diff.getChanges().devPrint());
    }

    @Test
    void valueObjectCompare_sameValues_Test() {
        final var amount1 = Amount.builder().value(BigDecimal.valueOf(100)).ccy("EUR").build();
        final var amount2 = Amount.builder().value(BigDecimal.valueOf(100)).ccy("EUR").build();

        logger.info(javers.compare(amount1, amount2).prettyPrint());
    }
}
