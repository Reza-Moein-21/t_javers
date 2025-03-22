package org.example.letter_of_credit;

import lombok.*;
import org.example.event_field.Amount;
import org.example.inf.TrackableEntity;
import org.example.party.Party;
import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.TypeName;

import java.time.LocalDate;
import java.time.LocalDateTime;

@TypeName("IssueMaster")
@Getter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class IssueMaster implements TrackableEntity<Long> {
    @Id
    @EqualsAndHashCode.Include
    private Long id;
    private Long version;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
    private String reference;
    private String inputBranch;
    private String behalfBranch;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private Amount amount;
    @ToString.Exclude
    private Party applicant;
    @ToString.Exclude
    private Party beneficiary;

}
