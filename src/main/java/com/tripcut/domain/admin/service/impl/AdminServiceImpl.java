package com.tripcut.domain.admin.service.impl;

import com.tripcut.domain.admin.dto.AdminDto;
import com.tripcut.domain.admin.repository.AdminRepository;
import com.tripcut.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public List<AdminDto> getAdmins() {
        return adminRepository.findAll().stream()
                .map(AdminDto::from)
                .collect(Collectors.toList());
    }
}

