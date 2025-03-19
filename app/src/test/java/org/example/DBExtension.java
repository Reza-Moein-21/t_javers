package org.example;

import org.apache.ibatis.session.SqlSessionFactory;
import org.example.inf.JaversConfig;
import org.example.inf.MyBatisConfig;
import org.javers.core.Javers;
import org.javers.repository.sql.DialectName;
import org.javers.repository.sql.SqlRepositoryBuilder;
import org.junit.jupiter.api.extension.*;

import javax.sql.DataSource;

import static org.example.TestDB.DATA_SOURCE;

public class DBExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {
    @Override
    public void beforeEach(ExtensionContext context) {
        executeQuery(TestUtils.getFileContent("/schema-drop.sql"), TestUtils.getFileContent("/schema-init.sql"));
    }

    @Override
    public void afterEach(ExtensionContext context) {
        executeQuery(TestUtils.getFileContent("/schema-drop.sql"));
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(SqlSessionFactory.class) ||
                parameterContext.getParameter().getType().equals(DataSource.class) ||
                parameterContext.getParameter().getType().equals(Javers.class)
                ;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        final var sqlSessionFactory = new MyBatisConfig().createSqlSessionFactory(DATA_SOURCE);

        if (parameterContext.getParameter().getType().equals(SqlSessionFactory.class))
            return sqlSessionFactory;

        if (parameterContext.getParameter().getType().equals(Javers.class))
            return getJavers();

        if (parameterContext.getParameter().getType().equals(DataSource.class))
            return DATA_SOURCE;

        throw new IllegalStateException("Not supported Parameter");
    }

    private Javers getJavers() {
        final var dialect = switch (DATA_SOURCE.getCatalog()) {
            case "h2" -> DialectName.H2;
            case "ora" -> DialectName.ORACLE;
            case "pg" -> DialectName.POSTGRES;
            default -> throw new IllegalStateException("");
        };
        return new JaversConfig().applicationJavers(SqlRepositoryBuilder.sqlRepository()
                .withDialect(dialect)
                .withConnectionProvider(() -> TestDB.DB_CONNECTION)
                .withSchemaManagementEnabled(false)
                .build());
    }


    private void executeQuery(String... query) {
        try (final var con = DATA_SOURCE.getConnection();
             final var stm = con.createStatement()) {
            for (String q : query) {
                stm.execute(q);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could setup database" + e.getMessage());
        }
    }
}