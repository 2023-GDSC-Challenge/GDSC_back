package com.solution.green.controller.notUse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class CommonController {
    @GetMapping("")
    public String home() {
        return "This is index page!\n" +
                "다른 페이지로 접속하기 위해서는 http://35.216.34.93:8080/api 뒷부분에\n" +
                "notion api 페이지에 있는 endpoint를 붙여주세요!";
    }
    @GetMapping("/getTest")
    public String getTest() {
        return "Get Mapping Test Success!";
    }
    @PostMapping("/postTest")
    public String postTest(@RequestBody String name) {
        return "test name is: " + name;
    }
}
