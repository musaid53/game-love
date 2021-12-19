package com.msaid.gamelove.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;

@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "roles")
@Getter
@Setter
public class RoleEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @ManyToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
    @JoinColumn( name = "user_id", nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private UserEntity user;

    public static RoleEntity fromUserRole(UserRole userRole, UserEntity userEntity){
        return RoleEntity.builder()
                .user(userEntity)
                .role(userRole)
                .build();
    }


}
