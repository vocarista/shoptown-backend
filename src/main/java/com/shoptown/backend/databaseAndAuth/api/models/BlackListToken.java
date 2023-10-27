package com.shoptown.backend.databaseAndAuth.api.models;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "blackList")
public class BlackListToken {
    @Id
    private String _id;
    private String token;
}
