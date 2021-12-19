package com.msaid.gamelove.service;

import com.msaid.gamelove.config.GameLoveProperties;
import com.msaid.gamelove.dto.UserRequestDto;
import com.msaid.gamelove.exception.BaseException;
import com.msaid.gamelove.config.DBThreadPoolExecutor;
import com.msaid.gamelove.persistence.entity.RoleEntity;
import com.msaid.gamelove.persistence.entity.UserEntity;
import com.msaid.gamelove.persistence.repository.RoleRepository;
import com.msaid.gamelove.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.ReactiveUserDetailsPasswordService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomUserDetailService implements ReactiveUserDetailsService, ReactiveUserDetailsPasswordService {
    private final UserRepository userRepository;
    private final DBThreadPoolExecutor dbThreadPoolExecutor;
    private final RoleRepository roleRepository;
    private final GameLoveProperties gameLoveProperties;

    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        log.info("initializing users from properties");
        roleRepository.deleteAll();
        userRepository.deleteAll();
        List<UserEntity> baseUsers = gameLoveProperties.getUsers().stream().map(userProperties -> {
            UserEntity userEntity = UserEntity.builder()
                    .userName(userProperties.getUsername())
                    .password(passwordEncoder.encode(userProperties.getPassword()))
                    .build();
            Set<RoleEntity> roles = userProperties.getRoles().stream()
                    .map(userRole -> RoleEntity.fromUserRole(userRole, userEntity)).collect(Collectors.toSet());
            userEntity.setRoles(roles);
            return userEntity;
        }).collect(Collectors.toList());
        userRepository.saveAll(baseUsers);
        log.info("users created ..");
    }


    @Override
    public Mono<UserDetails> updatePassword(UserDetails user, String newPassword) {
        return Mono.fromFuture(findByUserOrElseThrow(user.getUsername()))
                .flatMap(userEntity -> {
                    userEntity.setPassword(newPassword);
                    return saveUser(userEntity);
                }).map(userEntitySaved -> {
                    log.info("User pwd saved : {}", userEntitySaved.toString());
                    return withNewPassword(user, newPassword);
                });
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return Mono.fromFuture(findByUserOrElseThrow(username))
                .map(UserEntity::toUser);
    }

    private UserDetails withNewPassword(UserDetails userDetails, String newPassword) {
        return User.withUserDetails(userDetails).password(newPassword).build();
    }

    public Mono<UserEntity> saveUser(UserEntity userEntity) {
        return Mono.fromFuture(CompletableFuture.supplyAsync(() -> userRepository.save(userEntity), dbThreadPoolExecutor));
    }

    public CompletableFuture<UserEntity> findById(long userId) {
        return CompletableFuture.supplyAsync(() -> userRepository.findById(userId)
                .orElseThrow(() -> new BaseException("User not found")), dbThreadPoolExecutor);
    }

    public CompletableFuture<UserEntity> saveUser(UserRequestDto userRequestDto) {
        return userRepository.findByUserName(userRequestDto.getUsername())
                .thenApply(userEntOpt -> {
                    if (userEntOpt.isPresent())
                        throw new BaseException("User with same username exists");
                    UserEntity userEntity = UserEntity.builder()
                            .userName(userRequestDto.getUsername())
                            .password(passwordEncoder.encode(userRequestDto.getPassword()))
                            .build();
                    Set<RoleEntity> roles = userRequestDto.getRoles().stream()
                            .map(userRole -> RoleEntity.fromUserRole(userRole, userEntity)).collect(Collectors.toSet());
                    userEntity.setRoles(roles);
                    return userRepository.save(userEntity);
                });
    }

    public CompletableFuture<UserEntity> findByUserOrElseThrow(String userName) {
        return userRepository.findByUserName(userName)
                .thenApply(userEntity -> userEntity.orElseThrow(() -> new BaseException("User not found")));
    }
}
