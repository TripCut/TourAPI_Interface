package com.tripcut.domain.admin.controller;

import com.tripcut.core.controller.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.tripcut.global.common.api.ApiPath.BASE_URL;

@Controller
@RequiredArgsConstructor
@RequestMapping(BASE_URL + "/admin")
public class AdminController extends BaseController {
}
