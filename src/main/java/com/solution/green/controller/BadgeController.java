package com.solution.green.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class BadgeController {

    // TODO - 내가 가진 뱃지 리턴하는 메소드
    // TODO - 현재 있는 뱃지 중 선택하는 메소드
    // TODO - 뱃지 고르는 메소드
    // TODO - 뱃지 변경하는 메소드
}
