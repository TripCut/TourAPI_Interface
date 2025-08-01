package com.tripcut.domain.user.controller;

//import com.tripcut.domain.user.dto.request.UserRequestDto;
import com.tripcut.domain.user.dto.response.BaseResponse;
import com.tripcut.domain.user.dto.response.UserResponseDto;
import com.tripcut.domain.user.entity.User;
import com.tripcut.domain.user.service.AuthService;
import com.tripcut.domain.user.util.UserConverter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class AuthController {

    private final AuthService authService;

//    @PostMapping("/login")
//    public ResponseEntity<?> join(@RequestBody UserRequestDto.LoginRequestDTO loginRequestDTO) {
//        return null;
//    }

    @GetMapping("/auth/login/kakao")
    public BaseResponse<UserResponseDto.JoinResultDTO> kakaoLogin(@RequestParam("code") String accessCode, HttpServletResponse httpServletResponse) {
        User user = authService.oAuthLogin(accessCode, httpServletResponse);
        System.out.println("유저명 :" + user.getName());
        System.out.println("이메일 :" +user.getEmail());
        return BaseResponse.onSuccess(UserConverter.toJoinResultDTO(user));
    }
}
