package com.bci.reactive.repository;

import com.bci.reactive.entity.Phones;
import com.bci.reactive.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhoneRepository extends JpaRepository<Phones, Long> {

    List<Phones> findByUserId(Long userId);
    Phones findByCountrycodeAndNumber(String countrycode, String number );

}