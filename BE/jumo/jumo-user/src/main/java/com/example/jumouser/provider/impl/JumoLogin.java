package com.example.jumouser.provider.impl;

import com.example.domain.dto.user.LoginRequestDto;
import com.example.domain.dto.user.UserInfoDto;
import com.example.domain.entity.User;
import com.example.domain.repo.UserRepo;
import com.example.jumouser.provider.LoginProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


public class JumoLogin implements LoginProvider  {

    private final UserRepo userRepo;

    public JumoLogin(UserRepo userRepo){
        this.userRepo=userRepo;
    }
    public static JumoLogin getInstance(UserRepo userRepo){
        System.out.println("LoginInstance");
        return new JumoLogin(userRepo);
    }

    @Override
    public UserInfoDto getUserInfo(LoginRequestDto requestDto) {
        return UserInfoDto.builder()
                .email(requestDto.getEmail())
                .password(requestDto.getPassword())
                .build();
    }


    @Override
    public Optional<User> checkUser(UserInfoDto userInfoDto) {
        System.out.println(userInfoDto.getEmail());
        System.out.println(userInfoDto.getPassword());
        Optional<User> user = userRepo.findByEmailAndPassword(userInfoDto.getEmail(),userInfoDto.getPassword());
        if(!user.isEmpty()){
            System.out.println(user.get().getEmail());
            return user;
        }else{
            return Optional.of(new User());
        }

    }


}
