package org.example.party;

import lombok.*;
import org.example.inf.TrackableEntity;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.TypeName;

import java.time.LocalDateTime;

@TypeName("Party")
@Getter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Party implements TrackableEntity<Long> {
    @Id
    @EqualsAndHashCode.Include
    private Long id;
    private Long version;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;

    private PartyType type;
    private Long key29;
    @DiffIgnore
    @ToString.Exclude
    private Customer customer;
    @DiffIgnore
    @ToString.Exclude
    private Bank bank;


}
