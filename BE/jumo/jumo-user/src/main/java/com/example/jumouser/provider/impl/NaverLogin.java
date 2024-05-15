package com.example.jumouser.provider.impl;

import com.example.domain.dto.user.LoginRequestDto;
import com.example.domain.dto.user.UserInfoDto;
import com.example.domain.entity.User;
import com.example.domain.repo.UserRepo;
import com.example.jumouser.provider.LoginProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

public class NaverLogin implements LoginProvider {

    private final UserRepo userRepo;

    public NaverLogin(UserRepo userRepo){
        this.userRepo =userRepo;
    }
    public static NaverLogin getInstance(UserRepo userRepo){
        return new NaverLogin(userRepo);
    }
    @Override
    public UserInfoDto getUserInfo(LoginRequestDto requestDto) {
        return UserInfoDto.builder()
                .type("naver")
                .social_id(requestDto.getSocial_id())
                .name(requestDto.getName())
                .profile_img(requestDto.getProfile_img())
                .build();
    }


    @Override
    public Optional<User> checkUser(UserInfoDto userInfoDto){

        Optional<User> user = userRepo.findBySocialId(userInfoDto.getSocial_id());

        if(user.isEmpty()){
            return Optional.of(User.builder()
                            .socialId(userInfoDto.getSocial_id())
                            .name(userInfoDto.getName())
                            .profileImg(userInfoDto.getProfile_img())
                            .type("naver")
                    .build());
        }else{
            return user;
        }
    }


}
