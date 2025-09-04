package com.tripcut.domain.tour.controller;

import com.tripcut.core.controller.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tripcut.global.common.api.ApiPath.BASE_URL;

@RestController
@RequiredArgsConstructor
@RequestMapping(BASE_URL + "/tour")
public class TourController extends BaseController {
}
