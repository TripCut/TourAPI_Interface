package com.tripcut.core.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/v1")
public abstract class BaseController {

    // 공통 응답 메시지
    protected static final String SUCCESS_MESSAGE = "요청이 성공적으로 처리되었습니다.";
    protected static final String NOT_FOUND_MESSAGE = "요청한 리소스를 찾을 수 없습니다.";
    protected static final String UNAUTHORIZED_MESSAGE = "인증이 필요합니다.";
    protected static final String FORBIDDEN_MESSAGE = "접근 권한이 없습니다.";
    protected static final String VALIDATION_ERROR_MESSAGE = "입력 데이터가 올바르지 않습니다.";

    // 공통 응답 코드
    protected static final String SUCCESS_CODE = "SUCCESS";
    protected static final String ERROR_CODE = "ERROR";
    protected static final String NOT_FOUND_CODE = "NOT_FOUND";
    protected static final String UNAUTHORIZED_CODE = "UNAUTHORIZED";
    protected static final String FORBIDDEN_CODE = "FORBIDDEN";
    protected static final String VALIDATION_ERROR_CODE = "VALIDATION_ERROR";

    // 성공 응답 (info)
    protected <T> ResponseEntity<Map<String, Object>> success(T data) {
        Map<String, Object> info = new HashMap<>();
        info.put("code", SUCCESS_CODE);
        info.put("message", SUCCESS_MESSAGE);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("value", data);
        info.put("data", dataMap);
        return ResponseEntity.ok(info);
    }

    // 성공 응답 (list)
    protected <T> ResponseEntity<Map<String, Object>> success(List<T> data) {
        Map<String, Object> list = new HashMap<>();
        list.put("code", SUCCESS_CODE);
        list.put("message", SUCCESS_MESSAGE);
        list.put("count", data != null ? data.size() : 0);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("items", data);
        list.put("data", dataMap);
        return ResponseEntity.ok(list);
    }

    // 에러 응답
    protected ResponseEntity<Map<String, Object>> error(String code, String message, HttpStatus status) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("message", message);
        return ResponseEntity.status(status).body(map);
    }

    // Not Found 응답
    protected ResponseEntity<Map<String, Object>> notFound() {
        return error(NOT_FOUND_CODE, NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);
    }

    // 인증/인가 에러 응답
    protected ResponseEntity<Map<String, Object>> unauthorized() {
        return error(UNAUTHORIZED_CODE, UNAUTHORIZED_MESSAGE, HttpStatus.UNAUTHORIZED);
    }
    protected ResponseEntity<Map<String, Object>> forbidden() {
        return error(FORBIDDEN_CODE, FORBIDDEN_MESSAGE, HttpStatus.FORBIDDEN);
    }

    // 입력값 검증 에러 응답
    protected ResponseEntity<Map<String, Object>> validationError(String message) {
        return error(VALIDATION_ERROR_CODE, message != null ? message : VALIDATION_ERROR_MESSAGE, HttpStatus.BAD_REQUEST);
    }

    // 페이징 응답
    protected <T> ResponseEntity<Map<String, Object>> pagedSuccess(List<T> data, int page, int size, long total) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", SUCCESS_CODE);
        map.put("message", SUCCESS_MESSAGE);
        map.put("page", page);
        map.put("size", size);
        map.put("total", total);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("items", data);
        map.put("data", dataMap);
        return ResponseEntity.ok(map);
    }
}