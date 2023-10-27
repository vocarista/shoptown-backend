package com.shoptown.backend.databaseAndAuth.api.models;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.EnumNaming;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.processing.Generated;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Document(collection = "userDetails")
@Builder
public class User implements UserDetails {
    @Id
    private String _id;
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private List<WishlistProduct> wishlist;
    private List<OrderProduct> orderlist;
    private List<CartProduct> cartlist;
    private String role = "ROLE_USER";
    private String email;
    private String phone;
    private Date dob;
    private List<Address> shippingList;
    private Address billingAddress;

    public User(String firstname, String lastname, String username, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}