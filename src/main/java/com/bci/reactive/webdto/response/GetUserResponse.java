package com.bci.reactive.webdto.response;


import com.bci.reactive.webdto.request.UpdatePhoneRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class GetUserResponse {

    private Long id;

    private String name;

    private String email;

    private String password;

    private List<UpdatePhoneRequest> phones;

    private Instant created;

    private Instant modified;

    private Instant lastLogin;

    private String keyAccess;

}
