package com.testing.users.controller;

import com.testing.users.algorithm.Algorithm;
import com.testing.users.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@RestController
public class UsersController {


        @Autowired
        private Algorithm algorithm;

        @GetMapping("/**")
        public ResponseEntity<String> call(HttpServletRequest request) {

                System.out.println();
                Map<String, Map<String, String>> methodDetails = algorithm.resolveMethod(request.getRequestURI());
                return ResponseEntity.ok(methodDetails.toString());
        }

}
