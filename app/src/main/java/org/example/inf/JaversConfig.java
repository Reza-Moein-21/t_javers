package org.example.inf;

import org.apache.ibatis.session.SqlSession;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.repository.sql.DialectName;
import org.javers.repository.sql.JaversSqlRepository;
import org.javers.repository.sql.SqlRepositoryBuilder;

import java.io.IOException;
import java.util.Properties;

public class JaversConfig {

    public Javers applicationJavers(JaversSqlRepository javersSqlRepository) {
        return JaversBuilder
                .javers()
                .registerJaversRepository(javersSqlRepository)
                .build();
    }

    public Javers buildJaversFromSession(SqlSession session) {
        try {
            var dbProperties = new Properties();
            dbProperties.load(JaversConfig.class.getResourceAsStream("/db.properties"));
            final var dbType = dbProperties.getProperty("db.type");

            final var dialect = switch (dbType) {
                case "h2" -> DialectName.H2;
                case "ora" -> DialectName.ORACLE;
                case "pg" -> DialectName.POSTGRES;
                default -> throw new IllegalStateException("");
            };
            final var sqlRepository = SqlRepositoryBuilder.sqlRepository()
                    .withDialect(dialect)
                    .withConnectionProvider(session::getConnection)
                    .withSchemaManagementEnabled(false)
                    .build();

            return applicationJavers(sqlRepository);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
