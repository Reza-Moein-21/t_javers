package org.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.inf.MyBatisConfig;
import org.junit.jupiter.api.extension.*;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

public class DBExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {
    private static final HikariDataSource DATA_SOURCE;

    static {

        var dbProperties = new Properties();
        try {
            dbProperties.load(DBExtension.class.getResourceAsStream("/db.properties"));
            final var dbType = dbProperties.get("db.type");
            var hikariConfig = switch (dbType) {
                case String dt when dt.equals("ora") -> getOracleHikariConfig(dbProperties);
                case String dt when dt.equals("h2") -> getH2HikariConfig(dbProperties);
                case String dt when dt.equals("pg") -> getPGHikariConfig(dbProperties);
                case null, default -> throw new IllegalStateException("Invalid db type " + dbType);
            };
            DATA_SOURCE = new HikariDataSource(hikariConfig);
        } catch (IOException e) {
            throw new RuntimeException("invalid db.properties");
        }

    }

    private static HikariConfig getOracleHikariConfig(Properties properties) {
        var c = new HikariConfig();
        c.setJdbcUrl(properties.getProperty("ora.url"));
        c.setUsername(properties.getProperty("ora.username"));
        c.setPassword(properties.getProperty("ora.password"));
        return c;
    }

    private static HikariConfig getPGHikariConfig(Properties properties) {
        var c = new HikariConfig();
        c.setJdbcUrl(properties.getProperty("pg.url"));
        c.setUsername(properties.getProperty("pg.username"));
        c.setPassword(properties.getProperty("pg.password"));
        return c;
    }

    private static HikariConfig getH2HikariConfig(Properties properties) {
        var c = new HikariConfig();
        c.setJdbcUrl(properties.getProperty("h2.url"));
        c.setUsername(properties.getProperty("h2.username"));
        c.setPassword(properties.getProperty("h2.password"));
        return c;
    }


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
                parameterContext.getParameter().getType().equals(DataSource.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        final var sqlSessionFactory = new MyBatisConfig().createSqlSessionFactory(DATA_SOURCE);

        if (parameterContext.getParameter().getType().equals(SqlSessionFactory.class))
            return sqlSessionFactory;

        if (parameterContext.getParameter().getType().equals(DataSource.class))
            return DATA_SOURCE;

        throw new IllegalStateException("Not supported Parameter");
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