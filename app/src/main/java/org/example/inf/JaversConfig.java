package org.example.inf;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.repository.sql.JaversSqlRepository;

public class JaversConfig {

    public Javers applicationJavers(JaversSqlRepository javersSqlRepository) {
        return JaversBuilder
                .javers()
                .registerJaversRepository(javersSqlRepository)
                .build();
    }


}
