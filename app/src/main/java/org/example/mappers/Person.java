package org.example.mappers;

import lombok.Builder;
import org.example.inf.AbstractEntity;


@Builder(toBuilder = true)
public record Person(Long id, String fullName) implements AbstractEntity<Long> {
}