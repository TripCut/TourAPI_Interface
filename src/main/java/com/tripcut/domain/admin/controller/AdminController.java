package com.tripcut.domain.admin.controller;

import com.tripcut.core.controller.BaseController;
import com.tripcut.domain.admin.dto.AdminDto;
import com.tripcut.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.tripcut.global.common.api.ApiPath.BASE_URL;

@RestController
@RequiredArgsConstructor
@RequestMapping(BASE_URL + "/admins")
public class AdminController extends BaseController {

    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<List<AdminDto>> listAdmins() {
        return ResponseEntity.ok(adminService.getAdmins());
    }
}
