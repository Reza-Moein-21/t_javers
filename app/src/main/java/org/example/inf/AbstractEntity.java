package org.example.inf;

public interface AbstractEntity<I> {
    I id();

     long version();
}
