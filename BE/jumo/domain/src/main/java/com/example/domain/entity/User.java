package com.example.domain.entity;


import javax.persistence.*;

import com.example.domain.dto.user.LoginResponseDto;
import com.example.domain.dto.user.SignUpRequestDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    private String name;
    private String type;
    @Column(name="social_id")
    private String socialId;
    private String phone;
    private String email;
    private String password;
    @Column(name="fcm_token")
    private String fcmToken;
    @Column(name="profile_img")
    private String profileImg;
    @Column(name="pt_has")
    private int ptHas;


    public User(SignUpRequestDto requestDto){
        this.email = requestDto.getEmail();
        this.name = requestDto.getName();
        this.type = requestDto.getType();
        this.phone = requestDto.getPhone();
        this.profileImg = requestDto.getProfile_image();
        this.password=requestDto.getPassword();
        this.socialId=requestDto.getSocial_id();
    }

    public void addPoint(int point){
        this.ptHas += point;
    }

    public void subtractPoint(int point) {
        this.ptHas -= point;
    }

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CarInfo> carInfoList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
    private List<Ticket> tickets = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Transaction> transactions = new ArrayList<>();

    @Override
    public String toString() {
        return "name : "+ name + " social_id : " +socialId;
    }

    public void updateProfileImg(String url){
        this.profileImg = url;
    }

    public LoginResponseDto toLoginRepsonse(String token){
        return LoginResponseDto.builder()
                .user_id(this.getUserId())
                .name(this.getName())
                .email(this.getEmail())
                .phone(this.getPhone())
                .profile_img(this.getProfileImg())
                .ptHas(this.getPtHas())
                .type(this.getType())
                .social_id(this.getSocialId())
                .fcm_token(token)
                .build();
    }
}
