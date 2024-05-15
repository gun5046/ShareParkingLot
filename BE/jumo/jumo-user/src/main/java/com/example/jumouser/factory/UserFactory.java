package com.example.jumouser.factory;

import com.example.domain.repo.UserRepo;
import com.example.jumouser.provider.LoginProvider;
import com.example.jumouser.provider.impl.JumoLogin;
import com.example.jumouser.provider.impl.KakaoLogin;
import com.example.jumouser.provider.impl.NaverLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class UserFactory {

    private final UserRepo userRepo;
    private final Map<String, LoginProvider> loginProviders = new ConcurrentHashMap<>();

    public LoginProvider loginSelector(String type){
        return loginProviders.computeIfAbsent(type, t -> createLoginProvider(t));
    }

    private LoginProvider createLoginProvider(String type) {
        switch (type) {
            case "kakao":
                return new KakaoLogin(userRepo);
            case "naver":
                return new NaverLogin(userRepo);
            default:
                return new JumoLogin(userRepo);
        }
    }

}
