package com.shoptown.backend.databaseAndAuth.api.models;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchRequest {
    private String keyword;
}
