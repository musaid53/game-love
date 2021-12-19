package com.msaid.gamelove.persistence.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
@Table(indexes = @Index(name = "users_username_idx" , columnList = ("user_name"), unique = true))
@Getter
@Setter
public class UserEntity extends BaseEntity {
    private static final String TABLE_NAME = "users";
    private static final String SEQUENCE_GEN_NAME = TABLE_NAME + "_gen";
    private static final String SEQUENCE_NAME = TABLE_NAME + "_seq";

    @Id
    @SequenceGenerator( name = SEQUENCE_GEN_NAME, allocationSize = 1, sequenceName = SEQUENCE_NAME)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_GEN_NAME)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @JsonIgnore
    @ToString.Exclude
    private String password;

    @OneToMany(mappedBy = "user", targetEntity = RoleEntity.class,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<RoleEntity> roles = new HashSet<>();


    public UserDetails toUser(){
        String[] roles = this.roles.stream().map(roleEntity -> roleEntity.getRole().name()).toArray(String[]::new);
        return User.builder()
                .username(userName)
                .roles(roles)
                .password(password)
                .authorities(roles).build();
    }

}
