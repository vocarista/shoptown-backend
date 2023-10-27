package com.shoptown.backend.databaseAndAuth.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String country;
    private String fullName;
    private String mobileNumber;
    private String pincode;
    private String houseNumber;
    private String street;
    private String area;
    private String city;
    private String state;
    private String landmark;
    private boolean defaultAddress;
}
