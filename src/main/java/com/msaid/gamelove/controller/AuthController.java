package com.msaid.gamelove.controller;

import com.msaid.gamelove.dto.LoginResponse;
import com.msaid.gamelove.dto.UserRequestDto;
import com.msaid.gamelove.persistence.entity.UserEntity;
import com.msaid.gamelove.security.JwtUtil;
import com.msaid.gamelove.service.CustomUserDetailService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final JwtUtil tokenProvider;

    private final ReactiveAuthenticationManager authenticationManager;

    private final CustomUserDetailService customUserDetailService;

    @PostMapping("/login")
    @ApiOperation("Login with existing user")
    public CompletableFuture<ResponseEntity<LoginResponse>> login(@Valid @RequestBody UserRequestDto authRequest) {

        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()))
                .map(tokenProvider::createToken)
                .map(jwtToken ->{
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);
                    return new ResponseEntity<>(LoginResponse.builder()
                            .accessToken(jwtToken.getFirst())
                            .expiryDate(jwtToken.getSecond().toString())
                            .build(), httpHeaders, HttpStatus.OK);
                }).toFuture();

    }
    @PutMapping("/register")
    @ApiOperation("Reqister with new user")
    public CompletableFuture<ResponseEntity<UserEntity>> saveUser(@RequestBody @Valid  UserRequestDto userRequest){
        return customUserDetailService.saveUser(userRequest)
                .thenApply(ResponseEntity::ok);
    }

}