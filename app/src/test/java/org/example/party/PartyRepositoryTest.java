package org.example.party;

import org.apache.ibatis.session.SqlSessionFactory;
import org.example.DBExtension;
import org.javers.core.Javers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(DBExtension.class)
class PartyRepositoryTest {
    private static final Logger logger = LoggerFactory.getLogger(PartyRepositoryTest.class);


    private final SqlSessionFactory factory;
    private final Javers javers;

    public PartyRepositoryTest(SqlSessionFactory factory, Javers javers) {
        this.factory = factory;
        this.javers = javers;
    }

    @Test
    void findTest() {
        try (final var session = factory.openSession()) {
            final var partyRepo = session.getMapper(PartyRepository.class);
            final var party = partyRepo.findPartyById(1L).orElseThrow();
            assertNotNull(party);

        }
    }
}

