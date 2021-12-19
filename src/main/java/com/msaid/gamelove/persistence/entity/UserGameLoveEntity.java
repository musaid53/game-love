package com.msaid.gamelove.persistence.entity;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;

@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user_game_love")
@Getter
@Setter
@Table(indexes = @Index(name = "user_game_love_idx" , columnList = ("user_id, game_id"), unique = true))
public class UserGameLoveEntity extends BaseEntity {
    private static final String TABLE_NAME = "user_game_love";
    private static final String SEQUENCE_GEN_NAME = TABLE_NAME + "_gen";
    private static final String SEQUENCE_NAME = TABLE_NAME + "_seq";

    @Id
    @SequenceGenerator(name = SEQUENCE_GEN_NAME, allocationSize = 1, sequenceName = SEQUENCE_NAME)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_GEN_NAME)
    private Long id;

    private boolean love;

    @ManyToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
    @JoinColumn( name = "user_id", nullable = false)
    @ToString.Exclude
    private UserEntity user;

    @ManyToOne(targetEntity = GameEntity.class, fetch = FetchType.EAGER)
    @JoinColumn( name = "game_id", nullable = false)
    @ToString.Exclude
    private GameEntity game;

}
