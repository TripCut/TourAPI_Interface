//package com.tripcut.global.security.jwt.controller;
//
//import com.tripcut.domain.user.dto.request.UserRequestDto;
//import com.tripcut.domain.user.dto.response.BaseResponse;
//import com.tripcut.domain.user.dto.response.UserResponseDto;
//import com.tripcut.domain.user.entity.User;
//import com.tripcut.domain.user.util.UserConverter;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import com.tripcut.global.security.jwt.dto.LoginRequest;
//import com.tripcut.global.security.jwt.dto.LoginResponse;
//import com.tripcut.global.security.jwt.dto.SignupRequest;
//import com.tripcut.domain.user.service.AuthService;
//
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//
//@RestController
//@RequestMapping("")
//@RequiredArgsConstructor
//public class AuthController {
//    private final AuthService authService;
//
//    @PostMapping("/login")
//    public ResponseEntity<?> registerUser(@RequestBody UserRequestDto.LoginRequestDTO loginRequestDTO) {
//        return ResponseEntity.ok("로그인");
//    }
//
//    @PostMapping("/kakao/login")
//    public BaseResponse<UserResponseDto.JoinResultDTO> kakaoLogin(@RequestParam("code") String accessCode, HttpServletResponse httpServletResponse) {
//        User user = authService.oAuthLogin(accessCode, httpServletResponse);
//        return BaseResponse.onSuccess(UserConverter.toJoinResultDTO(user));
//    }
//}