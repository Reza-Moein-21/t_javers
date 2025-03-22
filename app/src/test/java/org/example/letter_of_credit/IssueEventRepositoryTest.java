package org.example.letter_of_credit;

import org.apache.ibatis.session.SqlSessionFactory;
import org.example.DBExtension;
import org.example.event_field.Amount;
import org.javers.core.Javers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(DBExtension.class)
class IssueEventRepositoryTest {
    private static final Logger logger = LoggerFactory.getLogger(IssueEventRepositoryTest.class);

    private final SqlSessionFactory factory;
    private final Javers javers;

    public IssueEventRepositoryTest(SqlSessionFactory factory, Javers javers) {
        this.factory = factory;
        this.javers = javers;
    }

    @Test
    void findTest() {
        try (final var session = factory.openSession()) {
            final var issueRepo = session.getMapper(IssueEventRepository.class);
            final var i = issueRepo.findIssueEventById(1L).orElseThrow();
            logger.info("issue loaded, {}", i);
            assertEquals(Amount.builder().value(new BigDecimal("1238348.23")).ccy("EUR").build(), i.getAmount());

            assertNotNull(i.getMaster());
            logger.info("master loaded");
            assertNotNull(i.getApplicant());
            logger.info("applicant loaded");
            assertNotNull(i.getApplicant().getCustomer());
            logger.info("applicant as customer loaded");
            assertNotNull(i.getBeneficiary());
            logger.info("beneficiary loaded");
            assertNotNull(i.getBeneficiary().getCustomer());
            logger.info("beneficiary as customer loaded");


        }
    }
}

