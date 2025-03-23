package org.example.javers;

import org.example.DBExtension;
import org.example.event_field.Amount;
import org.example.letter_of_credit.IssueEvent;
import org.example.letter_of_credit.IssueMaster;
import org.example.party.Party;
import org.example.party.PartyType;
import org.javers.core.Javers;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.JqlQuery;
import org.javers.repository.jql.QueryBuilder;
import org.javers.shadow.Shadow;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;

@ExtendWith(DBExtension.class)
class JaversJQLTest {
    private static final Logger logger = LoggerFactory.getLogger(JaversJQLTest.class);

    private final Javers javers;

    public JaversJQLTest(Javers javers) {
        this.javers = javers;
    }

    /**
     * <h5>JQL (JaVers Query Language) is a simple, fluent API which allows you to query JaversRepository for changes of a given class, object or property.</h5>
     * <br>
     * <h6>Data history can be fetched from JaversRepository using javers.find*() methods in one of three views:</h6>
     * <br><b>Shadow</b> is a historical version of a domain object restored from a snapshot.
     * <br><b>Change</b> represents an atomic difference between two objects.
     * <br><b>Snapshot</b> is a historical state of a domain object captured as the property:value map.
     */
    @Test
    @Disabled
    void jqlDefinition() {
    }

    @Test
    void commit() {
        final var issueEventOrigin = buildMockIssueEvent(1L);
        logger.info("issueEventOrigin: {}", issueEventOrigin);
        javers.commit("User1", issueEventOrigin);

        final var issueEventUpdated = issueEventOrigin.toBuilder()
                .version(2L)
                .amount(new Amount(BigDecimal.valueOf(1000), "USD"))
                .applicant(Party.builder().id(1L).type(PartyType.CUSTOMER).key29(1L).build())
                .build();
        logger.info("issueEventUpdated: {}", issueEventUpdated);
        javers.commit("User2", issueEventUpdated);
    }

    @Test
    void findChanges_ByInstanceId() {
        final JqlQuery query = QueryBuilder.byInstanceId(1L, IssueEvent.class)
//                .skip(0).limit(2) // <- Filter For pagination
//                .byAuthor("User1")
//                .fromVersion(3)
//                .from(LocalDateTime.of(2025, Month.JANUARY, 1, 0, 0, 0))
//                .to(LocalDateTime.of(2025, Month.MARCH, 1, 0, 0, 0))
                .build();

        final var changes = javers.findChanges(query);
        logger.info("Number of changes: {}", changes.size());
        logger.info(changes.prettyPrint());
    }

    @Test
    void findSnapshots_ByInstanceId() {
        final JqlQuery query = QueryBuilder.byInstanceId(1L, IssueEvent.class).build();

        final var snapshots = javers.findSnapshots(query);
        for (CdoSnapshot snapshot : snapshots) {
            System.out.println(snapshot);
        }
    }

    @Test
    void findShadow_ByInstanceId() {
        final JqlQuery query = QueryBuilder.byInstanceId(1L, IssueEvent.class).build();

        final var shadows = javers.<IssueEvent>findShadows(query);
        for (Shadow<IssueEvent> shadow : shadows) {
            System.out.println("### SHADOW INFO ###");
            System.out.println("commitInfo: " + shadow.getCommitMetadata());
            System.out.println("snapshot: " + shadow.getCdoSnapshot());
            System.out.println("Actual Object: " + shadow.get());
            System.out.println();
        }
    }


    @Test
    void queryByClass() {
        final JqlQuery query = QueryBuilder.byClass(IssueEvent.class).build();
        System.out.println(javers.findChanges(query).prettyPrint());
    }

    private IssueEvent buildMockIssueEvent(Long id) {
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
                .id(id)
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
