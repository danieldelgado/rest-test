package com.bci.reactive.web;

import com.bci.reactive.service.UserService;
import com.bci.reactive.webdto.request.CreateUserRequest;
import com.bci.reactive.webdto.request.UpdateUserRequest;
import com.bci.reactive.webdto.response.BaseWebResponse;
import com.bci.reactive.webdto.response.CreateUserResponse;
import com.bci.reactive.webdto.response.GetUserResponse;
import com.bci.reactive.webdto.response.UpdateUserResponse;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/api/user")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class UserRestController {

    @Autowired
    private UserService userService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,

            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Single<ResponseEntity<BaseWebResponse<CreateUserResponse>>> create(
            @Valid @RequestBody CreateUserRequest req) {
        log.info("create user");
        return userService.create(req).subscribeOn(Schedulers.io())
                .map(response -> ResponseEntity.ok(BaseWebResponse.successWithData(response)));
    }


    @PutMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Single<ResponseEntity<BaseWebResponse<UpdateUserResponse>>> update(
            @RequestBody UpdateUserRequest req) {
        log.info("update user");
        return userService.update(req).subscribeOn(Schedulers.io())
                .map(response -> ResponseEntity.ok(BaseWebResponse.successWithData(response)));
    }

    @GetMapping(
            path="/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public Single<ResponseEntity<BaseWebResponse<GetUserResponse>>>  get(
            @PathVariable(name="id") String id) {
        log.info("get user : {}", id);
        return userService.getUserById(Long.valueOf(id)).subscribeOn(Schedulers.io())
                .map(response -> ResponseEntity.ok(BaseWebResponse.successWithData(response)));
    }




}
