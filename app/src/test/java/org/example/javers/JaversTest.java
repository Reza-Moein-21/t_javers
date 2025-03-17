package org.example.javers;

import org.example.DBExtension;
import org.example.inf.JaversConfig;
import org.example.mappers.Person;
import org.javers.core.Javers;
import org.javers.repository.jql.QueryBuilder;
import org.javers.repository.sql.DialectName;
import org.javers.repository.sql.SqlRepositoryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.sql.DataSource;

@ExtendWith(DBExtension.class)
public class JaversTest {

    //    private static Javers javers;
    private Javers javers;
    private final DataSource dataSource;

    public JaversTest(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @BeforeEach
    void setUp() {


        javers = new JaversConfig().applicationJavers(SqlRepositoryBuilder.sqlRepository()
                .withDialect(DialectName.H2)
                .withConnectionProvider(dataSource::getConnection)
                .build());

    }


    @Test
    void javersTest() {
        final var query = QueryBuilder.byInstanceId(23L, Person.class).build();
//
//        Person ali = new Person(1L, "Ali");
////        javers.commit("user", ali);
////        Person reza = new Person(1L, "Reza");
////        javers.commit("user", reza);
//        javers.commitShallowDelete("user",ali);
        Person person = new Person(23L, "Person 2");
        javers.commit("user", person);


        var shadows = javers.findChanges(query);
        shadows.forEach(System.out::println);

    }
}
