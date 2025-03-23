package org.example.letter_of_credit;

import org.apache.ibatis.session.SqlSessionFactory;
import org.example.DBExtension;
import org.example.inf.PageRequest;
import org.example.inf.PageResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(DBExtension.class)
class IssueMasterRepositoryTest {
    private static final Logger logger = LoggerFactory.getLogger(IssueMasterRepositoryTest.class);

    private final SqlSessionFactory factory;

    public IssueMasterRepositoryTest(SqlSessionFactory factory) {
        this.factory = factory;
    }

    @Test
    void pagingTest() {
        try (final var session = this.factory.openSession()) {
            final var issueMasterRepository = session.getMapper(IssueMasterRepository.class);

            final var pageRequest = PageRequest.ofSize(10);

            final var searchParam = new IssueMasterSearchParam(Set.of("1001"), "ILC");
            final var items = issueMasterRepository.searchIssueMasterBySearchParam(searchParam, pageRequest.offset(), pageRequest.limit());
            final var total = issueMasterRepository.totalIssueMasterBySearchParam(searchParam);

            final var pageResult = new PageResult<>(items, total, pageRequest.pageNumber(), pageRequest.pageSize());

            assertNotNull(pageResult.data());
            assertEquals(pageRequest.pageNumber(), pageResult.currentPage());
            assertEquals(pageRequest.pageSize(), pageResult.pageSize());
            assertEquals(total, pageResult.totalCount());
            logger.info("{}", pageResult);
        }

    }
}

