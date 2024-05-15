package com.example.jumouser.service;

import com.example.domain.dto.user.LoginRequestDto;
import com.example.domain.dto.user.SignUpRequestDto;
import com.example.domain.dto.user.UserInfoDto;
import com.example.domain.dto.user.UserProfileResponseDto;
import com.example.domain.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface UserService {

    public boolean emailCheck(String email);
    public Optional<User> signUp(SignUpRequestDto requestDto);
    public UserProfileResponseDto getUserProfile(Long user_id);

    public String updateProfileImg(Long user_id, MultipartFile file) throws IOException;

    Boolean updateFcmToken(Long user_id, String fcm_token);

    Boolean sendAuthMessage(String phone);

    Boolean certificatePhone(String phone,String code);

    UserInfoDto getUserInfo(LoginRequestDto requestDto);

    Optional<User> checkUser(UserInfoDto userInfoDto,LoginRequestDto requestDto);
}
