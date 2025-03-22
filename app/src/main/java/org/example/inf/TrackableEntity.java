package org.example.inf;

import java.time.LocalDateTime;

public interface TrackableEntity<I> {
    I getId();

    Long getVersion();

    LocalDateTime getCreatedAt();

    String getCreatedBy();

    LocalDateTime getModifiedAt();

    String getModifiedBy();

}
