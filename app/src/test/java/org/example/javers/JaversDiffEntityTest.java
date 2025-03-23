package org.example.javers;

import org.example.DBExtension;
import org.example.event_field.Amount;
import org.example.letter_of_credit.IssueEvent;
import org.example.letter_of_credit.IssueMaster;
import org.example.party.Party;
import org.example.party.PartyType;
import org.javers.core.Javers;
import org.javers.core.diff.changetype.NewObject;
import org.javers.core.diff.changetype.ObjectRemoved;
import org.javers.core.diff.changetype.PropertyChange;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;

@ExtendWith(DBExtension.class)
class JaversDiffEntityTest {
    private static final Logger logger = LoggerFactory.getLogger(JaversDiffEntityTest.class);

    private final Javers javers;

    public JaversDiffEntityTest(Javers javers) {
        this.javers = javers;
    }

    /**
     * <b>There are three main types of Changes:</b>
     * <br><b>NewObject</b> — when an object is present only in the right graph,
     * <br><b>ObjectRemoved</b> — when an object is present only in the left graph,
     * <br><b>PropertyChange</b> — most common — a changed property (field or getter).
     */
    @Test
    void entityCompare_sameType_sameId_Test() {
        final var issueEventOrigin = buildMockIssueEvent();
        logger.info("issueEventOrigin: {}", issueEventOrigin);

        final var issueEventUpdated = issueEventOrigin.toBuilder()
                .version(2L)
                .amount(new Amount(BigDecimal.valueOf(1000), "USD"))
                .applicant(Party.builder().id(1L).type(PartyType.CUSTOMER).key29(1L).build())
                .build();
        logger.info("issueEventUpdated: {}", issueEventUpdated);

        final var diff = javers.compare(issueEventOrigin, issueEventUpdated);
        logger.info("Number of changes: {}", diff.getChanges().size());

        logger.info("Changes of PropertyChange");
        diff.getChangesByType(PropertyChange.class).forEach(System.out::println);

        logger.info("Changes of NewObject");
        diff.getChangesByType(NewObject.class).forEach(System.out::println);

        logger.info("Changes of ObjectRemoved");
        diff.getChangesByType(ObjectRemoved.class).forEach(System.out::println);


    }

    /**
     * JaVers matches only objects with the same <code>GlobalId</code>.
     */
    @Test
    void entityCompare_sameType_differentId_Test() {
        final var partyCus = Party.builder().id(1L).type(PartyType.CUSTOMER).build();
        final var partyBank = Party.builder().id(2L).type(PartyType.BANK).build();
        logger.info("{}", javers.compare(partyCus, partyBank));
    }

    /**
     * JaVers matches only objects with the same <code>GlobalId</code>.
     */
    @Test
    void entityCompare_differentType_sameId_Test() {
        final var partyCus = Party.builder().id(1L).type(PartyType.CUSTOMER).build();
        final var issueEvent = IssueEvent.builder().id(1L).applicantReference("ILC1001/040123435").build();
        logger.info("{}", javers.compare(partyCus, issueEvent));
    }


    private IssueEvent buildMockIssueEvent() {
        final var customerParty2 = Party.builder().id(2L).type(PartyType.CUSTOMER).key29(2L).build();
        final var bankParty3 = Party.builder().id(3L).type(PartyType.BANK).key29(3L).build();
        final var issueMaster = IssueMaster.builder()
                .id(1L)
                .version(1L)
                .inputBranch("1001")
                .behalfBranch("1001")
                .reference("ILC1001/041909090")
                .amount(new Amount(BigDecimal.valueOf(19998.09), "IRR"))
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2029, 3, 1))
                .applicant(bankParty3)
                .beneficiary(customerParty2)
                .build();

        return IssueEvent.builder()
                .id(1L)
                .version(1L)
                .applicantReference(issueMaster.getReference())
                .amount(issueMaster.getAmount())
                .issueDate(issueMaster.getIssueDate())
                .expiryDate(issueMaster.getExpiryDate())
                .master(issueMaster)
                .applicant(issueMaster.getApplicant())
                .beneficiary(issueMaster.getBeneficiary())
                .build();
    }
}
