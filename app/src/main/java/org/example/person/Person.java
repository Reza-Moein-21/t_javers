package org.example.person;

import lombok.Builder;
import org.example.inf.AbstractEntity;
import org.javers.core.metamodel.annotation.Entity;
import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.TypeName;


@Builder(toBuilder = true)
@Entity
@TypeName("Person")
public record Person(@Id Long id, String fullName, long version) implements AbstractEntity<Long> {
    public Person {
        if (version == 0) version = 1;
    }
}