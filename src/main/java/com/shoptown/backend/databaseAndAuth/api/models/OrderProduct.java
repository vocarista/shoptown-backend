package com.shoptown.backend.databaseAndAuth.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProduct {
    private String productId;
    private String qty;
    private Date orderDate;
    private Date arrivalDate;
}
