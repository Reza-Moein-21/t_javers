package org.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class TestDB {
    public static final Connection DB_CONNECTION;
    public static final HikariDataSource DATA_SOURCE;

    static {

        var dbProperties = new Properties();
        try {
            dbProperties.load(DBExtension.class.getResourceAsStream("/db.properties"));
            final var dbType = dbProperties.getProperty("db.type");
            DB_CONNECTION = DriverManager.getConnection(dbProperties.getProperty(dbType + ".url"), dbProperties.getProperty(dbType + ".username"), dbProperties.getProperty(dbType + ".password"));
            var c = new HikariConfig();
            c.setJdbcUrl(dbProperties.getProperty(dbType + ".url"));
            c.setUsername(dbProperties.getProperty(dbType + ".username"));
            c.setPassword(dbProperties.getProperty(dbType + ".password"));
            c.setCatalog(dbType);


            DATA_SOURCE = new HikariDataSource(c);
        } catch (IOException e) {
            throw new RuntimeException("invalid db.properties");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
