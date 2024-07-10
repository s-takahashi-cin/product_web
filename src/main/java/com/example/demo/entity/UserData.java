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


// @Entity
// @Table(name = "admins")
// @Data
// public class UserData implements UserDetails {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;
//     private String lastName;
//     private String firstName;
//     private String email;
//     private String phone;

//     @Column(name = "position_id", insertable = false, updatable = false)
//     private Long positionId;

//     @Column(name = "authority_id", insertable = false, updatable = false)
//     private Long authorityId;

//     @Column(name = "store_id", insertable = false, updatable = false)
//     private Long storeId;

//     private String password;

//     @ManyToOne(fetch = FetchType.EAGER)
//     @JoinColumn(name = "store_id", referencedColumnName = "id")
//     private StoreData store;

//     @Transient
//     private String storeName;

//     @ManyToOne(fetch = FetchType.EAGER)
//     @JoinColumn(name = "position_id", referencedColumnName = "id")
//     private PositionData position;

//     @Transient
//     private String positionName;

//     @ManyToOne(fetch = FetchType.EAGER)
//     @JoinColumn(name = "authority_id", referencedColumnName = "id")
//     private AuthorityData authority;

//     @Transient
//     private String authorityName;

//     public String getEmail() {
//         return email;
//     }

//     public void setEmail(String email) {
//         this.email = email;
//     }

//     public String getPhone() {
//         return phone;
//     }

//     public void setPhone(String phone) {
//         this.phone = phone;
//     }

//     public String getPassword() {
//         return password;
//     }

//     public void setPassword(String password) {
//         this.password = password;
//     }

//     public Long getAuthorityId() {
//         return authorityId;
//     }

//     public void setAuthorityId(Long authorityId) {
//         this.authorityId = authorityId;
//     }

//     public Long getPositionId() {
//         return positionId;
//     }

//     public void setPositionId(Long positionId) {
//         this.positionId = positionId;
//     }

//     public String getStoreName() {
//         return store != null ? store.getName() : null;
//     }

//     public void setStoreName(String storeName) {
//         this.storeName = storeName;
//     }

//     public String getPositionName() {
//         return position != null ? position.getName() : null;
//     }

//     public void setPositionName(String positionName) {
//         this.positionName = positionName;
//     }

//     public String getAuthorityName() {
//         return authority != null ? authority.getName() : null;
//     }

//     public void setAuthorityName(String authorityName) {
//         this.authorityName = authorityName;
//     }


//     @Override
//     public Collection<? extends GrantedAuthority> getAuthorities() {
//         return Collections.emptyList();
//     }

//     @Override
//     public String getUsername() {
//         return this.email;
//     }

//     @Override
//     public boolean isAccountNonExpired() {
//         return true;
//     }

//     @Override
//     public boolean isAccountNonLocked() {
//         return true;
//     }

//     @Override
//     public boolean isCredentialsNonExpired() {
//         return true;
//     }

//     @Override
//     public boolean isEnabled() {
//         return true;
//     }
// }
