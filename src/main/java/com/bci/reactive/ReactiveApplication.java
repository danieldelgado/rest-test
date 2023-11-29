package com.bci.reactive;

import com.bci.reactive.service.UserService;
import com.bci.reactive.webdto.request.CreatePhoneRequest;
import com.bci.reactive.webdto.request.CreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;


@EnableSwagger2
@SpringBootApplication
public class ReactiveApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveApplication.class, args);
    }

    private UserService userService;

    @Autowired
    public ReactiveApplication(PasswordEncoder passwordEncoder, UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... strings) {

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail("usuario02@bcitest.com");
        createUserRequest.setPassword("pass1235");
        createUserRequest.setName("test2");
        List<CreatePhoneRequest> phones = new ArrayList<>();
        CreatePhoneRequest createPhoneRequest = new CreatePhoneRequest();
        createPhoneRequest.setNumber("987456123");
        createPhoneRequest.setCountrycode("CW");
        createPhoneRequest.setCitycode("45");
        phones.add(createPhoneRequest);
        createUserRequest.setPhones(phones);
        userService.create(createUserRequest).blockingGet();

    }
}
