package com.example.jumouser.provider.impl;


import com.example.domain.dto.user.LoginRequestDto;
import com.example.domain.dto.user.UserInfoDto;
import com.example.domain.entity.User;
import com.example.domain.repo.UserRepo;
import com.example.jumouser.provider.LoginProvider;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public class KakaoLogin implements LoginProvider {

    private final UserRepo userRepo;
    public KakaoLogin(UserRepo userRepo){
        this.userRepo=userRepo;
    }
    public static KakaoLogin getInstance(UserRepo userRepo){

        return new KakaoLogin(userRepo);
    }

    @Value("${KAKAO-KEY}")
    String key;

    @Value("${REDIRECT.URI}")
    String redirect;

    @Override
    public UserInfoDto getUserInfo(LoginRequestDto requestDto) {
        String reqURL = "https://kapi.kakao.com";
        try {
            WebClient webClient = WebClient.create(reqURL);
            ResponseEntity<String> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/v2/user/me").build())
                    .header("Authorization", "Bearer "+requestDto.getAccessToken())
                    .retrieve().toEntity(String.class).block();
            ObjectMapper objMapper = new ObjectMapper();
            Map<String,Object> obj = objMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>(){});

            String kakao_id = Long.toString((Long)obj.get("id"));
            Map<String,Object> account = (Map<String, Object>) obj.get("kakao_account");
            Map<String,Object> profile = (Map<String, Object>) account.get("profile");
            String image = (String)profile.get("profile_image_url");

            UserInfoDto userInfo = UserInfoDto.builder()
                    .type("kakao")
                    .social_id(requestDto.getSocial_id())
                    .profile_image(image)
                    .build();

            return userInfo;
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println("not authorized");
        }
        return null;

    }

    public Optional<User> checkUser(UserInfoDto userInfoDto){


        Optional<User> user = userRepo.findBySocialId(userInfoDto.getSocial_id());
        if(user.isEmpty()){
            return Optional.of(User.builder()
                            .type("kakao")
                            .profileImg(userInfoDto.getProfile_image())
                            .socialId(userInfoDto.getSocial_id())
                    .build()
            );
        }else{
            return user;
        }
    }

}
