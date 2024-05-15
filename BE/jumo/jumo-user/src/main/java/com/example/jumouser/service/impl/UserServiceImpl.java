package com.example.jumouser.service.impl;

import com.example.domain.dto.user.LoginRequestDto;
import com.example.domain.dto.user.SignUpRequestDto;
import com.example.domain.dto.user.UserInfoDto;
import com.example.domain.dto.user.UserProfileResponseDto;
import com.example.domain.entity.FcmToken;
import com.example.domain.entity.PhoneValidation;
import com.example.domain.entity.User;
import com.example.domain.repo.TokenRepo;
import com.example.domain.repo.UserRepo;
import com.example.domain.repo.ValidationRepo;
import com.example.jumouser.factory.UserFactory;
import com.example.jumouser.service.UserService;
import com.example.jumouser.util.NotificationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final TokenRepo tokenRepo;
    private final ValidationRepo validationRepo;
    private final UserFactory userFactory;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String drawingStorage;
    private DefaultMessageService messageService;
    @Value("${Cool-API-KEY}")
    private String Cool_API_KEY;

    @Value("${Cool-SECRET}")
    private String Cool_SECRET;
    private final Storage storage;

    public boolean emailCheck(String email){
        Optional<User> user = userRepo.findByEmail(email);
        if(!user.isPresent()){
            return true;
        }
        return false;
    }

    @PostConstruct
    public void initMessageService(){
        messageService = NurigoApp
                .INSTANCE
                .initialize(Cool_API_KEY, Cool_SECRET, "https://api.coolsms.co.kr");
    }

    @Transactional
    public Optional<User> signUp(SignUpRequestDto requestDto){
        User user = new User(requestDto);
        userRepo.save(user);
        Optional<User> sign_user = userRepo.findByEmail(requestDto.getEmail()); // 수정할 것
        if(sign_user.isPresent()){
            return sign_user;
        }else{
            return Optional.of(new User());
        }
    }


    public UserProfileResponseDto getUserProfile(Long user_id){

        Map<String,Object> result =  userRepo.selectProfileByUserId(user_id);

        ObjectMapper mapper = new ObjectMapper();
        UserProfileResponseDto responseDto = mapper.convertValue(result,UserProfileResponseDto.class);

        return responseDto;
    }

    @Transactional
    @Override
    public String updateProfileImg(Long user_id, MultipartFile file) throws IOException {
        Optional<User> user = userRepo.findById(user_id);
        if(!user.isEmpty()){
            if(file.isEmpty()) {
                user.get().updateProfileImg("");
                userRepo.save(user.get());
                return "";
            }else {
                String uuid = UUID.randomUUID().toString();
                String type = file.getContentType();

                BlobInfo blobInfo = storage.create(
                        BlobInfo.newBuilder(drawingStorage, uuid)
                                .setAcl(new ArrayList<>(Arrays.asList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))))
                                .setContentType(type)
                                .build(),
                        file.getInputStream()
                );
                String url = "https://storage.googleapis.com/bucket_lunamires/" + uuid;

                user.get().updateProfileImg(url);
                userRepo.save(user.get());
                return url;
            }
        }

        else{


        }
        return null;
    }

    @Override
    public Boolean updateFcmToken(Long user_id, String fcm_token) {

        Optional <FcmToken> token = tokenRepo.findById(user_id);
        Boolean flag= true;
        if(!token.isEmpty()){
            System.out.println(tokenRepo.findById(user_id).get().getToken());
            if(!token.get().getToken().equals(fcm_token)){
                Map<String,String> data = new HashMap<>();
                data.put("user_id",Long.toString(user_id));
                data.put("type",Integer.toString(0));

                NotificationUtil.sendNotification(data);
                flag= false;
            }
        }
        tokenRepo.save(new FcmToken(user_id,fcm_token));
        return flag;
    }

    @Override
    @Transactional
    public Boolean sendAuthMessage(String phone) {
        Message message = new Message();

        message.setFrom("01083623107");
        message.setTo(phone);

        String code="";
        var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        for(int i = 0; i < 6; i++) {
            int ranNum = (int) (Math.random()*36);

            code += possible.charAt(ranNum);
        }
        message.setText("안녕하세요. 주차장의 모든것 인증번호는 [ " +code + "] 입니다.");


        SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
        validationRepo.save(new PhoneValidation(phone, code, true));


        return true;
    }

    @Override
    public Boolean certificatePhone(String phone, String code) {
        Optional<PhoneValidation> phoneValidation = validationRepo.findById(phone);
        if(phoneValidation.isPresent()){
            if(phoneValidation.get().getValidation_value().equals(code)){
                validationRepo.save(phoneValidation.get().changeFlag());
                return true;
            }else{

                return false;
            }
        }
        return false;
    }

    @Override
    public UserInfoDto getUserInfo(LoginRequestDto requestDto) {
        return userFactory.loginSelector(requestDto.getType()).getUserInfo(requestDto);
    }

    @Override
    public Optional<User> checkUser(UserInfoDto userInfoDto, LoginRequestDto requestDto) {
        return  userFactory.loginSelector(requestDto.getType()).checkUser(userInfoDto);
    }
}
