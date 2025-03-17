package org.example.inf;

import java.util.List;

public record PageResult<T>(
        List<T> data,
        long totalCount,
        int currentPage,
        int pageSize) {

}