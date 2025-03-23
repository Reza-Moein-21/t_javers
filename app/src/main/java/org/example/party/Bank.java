package org.example.party;

import lombok.*;
import org.example.inf.TrackableEntity;
import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.TypeName;

import java.time.LocalDateTime;

@TypeName("Bank")
@Getter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Bank implements TrackableEntity<Long> {
    @Id
    @EqualsAndHashCode.Include
    private Long id;
    private Long version;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;

    private String title;
}
