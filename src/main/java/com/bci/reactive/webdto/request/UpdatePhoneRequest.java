package com.bci.reactive.webdto.request;

import lombok.*;

import javax.validation.constraints.Pattern;

@Builder
@AllArgsConstructor
@ToString
@Getter
@Setter
@NoArgsConstructor
public class UpdatePhoneRequest {

    @Pattern(regexp = "^[0-9]{6,15}$")
    private String number;

    @Pattern(regexp = "^[A-Z]{3}$")
    private String citycode;

    @Pattern(regexp = "^[A-Z]{3}$")
    private String countrycode;

}
