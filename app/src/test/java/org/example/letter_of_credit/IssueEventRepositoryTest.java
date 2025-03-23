package org.example.letter_of_credit;

import org.apache.ibatis.session.SqlSessionFactory;
import org.example.DBExtension;
import org.example.event_field.Amount;
import org.example.inf.JaversConfig;
import org.javers.core.diff.Diff;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(DBExtension.class)
class IssueEventRepositoryTest {
    private static final Logger logger = LoggerFactory.getLogger(IssueEventRepositoryTest.class);

    private final SqlSessionFactory factory;

    public IssueEventRepositoryTest(SqlSessionFactory factory) {
        this.factory = factory;
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

    // Note MyBatis lazy loading has conflict with javers for compare or commit, so for this demo, lazy loading is disabled
    @Test
    void updateWithAudit() {
        try (final var session = this.factory.openSession()) {
            // Javers use MyBatis session to have same transaction management
            final var javers = new JaversConfig().buildJaversFromSession(session);
            final var issueEventRepository = session.getMapper(IssueEventRepository.class);

            final var origin = issueEventRepository.findIssueEventById(2L).orElseThrow();
            final var forUpdate = origin.toBuilder().expiryDate(LocalDate.of(2020, 10, 10)).version(origin.getVersion()).build();

            // Check version
            if (!origin.getVersion().equals(forUpdate.getVersion())) {
                throw new IllegalStateException("Invalid version for update");
            }

            Diff diff = javers.compare(origin, forUpdate);
            if (!diff.hasChanges()) {
                logger.info("No CHANGE DETECTED");
                return;
            }

            logger.info(diff.prettyPrint());

            final var author = "Admin user";
            final var forSave = forUpdate.toBuilder().version(origin.getVersion() + 1).modifiedBy(author).build();
            logger.info("FOR UPDATE: {}", forSave);
            issueEventRepository.updateIssueEvent(forSave);
            javers.commit("Admin user", forUpdate);

            session.commit();
        }
    }
}

