package org.example.inf;

public record PageRequest(
        int pageNumber,
        int pageSize) {
    public PageRequest {
        if (pageNumber < 1) {
            throw new IllegalStateException("Page number must be granter then 0");
        }

        if (pageSize < 1) {
            throw new IllegalStateException("Page size must be granter then 0");
        }
    }

    public static PageRequest ofSize(int pageSize) {
        return new PageRequest(1, pageSize);
    }

    public int offset() {
        return (pageNumber - 1) * pageSize;
    }

    public int limit() {
        return pageSize;
    }
}