package com.example.demo.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "admins")
@Data
public class UserData implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String lastName;
    private String firstName;
    private String email;
    private String phone;

    @Column(name = "position_id")
    private Long positionId;

    @Column(name = "authority_id")
    private Long authorityId;

    @Column(name = "store_id")
    private Long storeId;

    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "store_id", insertable = false, updatable = false)
    private StoreData store;

    @Transient
    private String storeName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "position_id", insertable = false, updatable = false)
    private PositionData position;

    @Transient
    private String positionName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "authority_id", insertable = false, updatable = false)
    private AuthorityData authority;

    @Transient
    private String authorityName;

    // Getters and Setters...

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getUsername() {
        return this.email;
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
