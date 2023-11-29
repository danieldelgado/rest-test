package com.bci.reactive.webdto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class CreatePhoneRequest {

    @Pattern(regexp = "^[0-9]{6,15}$")
    private String number;

    @Pattern(regexp = "^[A-Z]{3}$")
    private String citycode;

    @Pattern(regexp = "^[A-Z]{3}$")
    private String countrycode;

}
