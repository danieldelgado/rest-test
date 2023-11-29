package com.bci.reactive.entity;

import lombok.*;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "phone", uniqueConstraints={
        @UniqueConstraint(columnNames = {"number", "countrycode"})
})
public class Phones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private String number;

    @Column(name = "citycode")
    private String citycode;


    @Column(name = "countrycode")
    private String countrycode;

    private Long userId;


}