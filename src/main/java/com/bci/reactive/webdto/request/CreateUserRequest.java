package com.bci.reactive.webdto.request;


import com.bci.reactive.util.PasswordRegexValid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateUserRequest {


    @Pattern(regexp = "^[a-zA-Z0-9]{1,100}$")
    @NotBlank(message = "name is mandatory")
    private String name;

    @Email
    @NotBlank(message = "email is mandatory")
    private String email;

    //@Pattern(regexp = "^[a-zA-Z0-9]{6,10}$")
    @PasswordRegexValid
    @NotBlank(message = "password is mandatory")
    private String password;

    @Valid
    private List<CreatePhoneRequest> phones;

}
