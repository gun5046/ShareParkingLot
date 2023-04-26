package com.example.domainuser.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoDto {
    private String type;
    private String email;
    private String profile_image;

}
