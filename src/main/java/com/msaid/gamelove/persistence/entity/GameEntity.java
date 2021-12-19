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
@Entity(name = "games")
@Table(indexes = @Index(name = "games_gamename_idx" , columnList = ("game_name"), unique = true))
@Getter
@Setter
public class GameEntity extends BaseEntity {
    private static final String TABLE_NAME = "games";
    private static final String SEQUENCE_GEN_NAME = TABLE_NAME + "_gen";
    private static final String SEQUENCE_NAME = TABLE_NAME + "_seq";

    @Id
    @SequenceGenerator( name = SEQUENCE_GEN_NAME, allocationSize = 1, sequenceName = SEQUENCE_NAME)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_GEN_NAME)
    private Long id;

    @Column(name = "game_name", nullable = false)
    private String gameName;


}
