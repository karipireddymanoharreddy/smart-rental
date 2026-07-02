
package com.example.rental_platform.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponse<T> {

    // =========================
    // CONTENT
    // =========================
    private List<T> content;

    // =========================
    // CURRENT PAGE
    // =========================
    private int currentPage;

    // =========================
    // PAGE SIZE
    // =========================
    private int pageSize;

    // =========================
    // TOTAL PAGES
    // =========================
    private int totalPages;

    // =========================
    // TOTAL ELEMENTS
    // =========================
    private long totalElements;

    // =========================
    // FIRST PAGE
    // =========================
    private boolean first;

    // =========================
    // LAST PAGE
    // =========================
    private boolean last;

}