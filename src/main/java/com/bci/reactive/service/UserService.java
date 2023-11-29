package com.bci.reactive.service;

import com.bci.reactive.entity.Phones;
import com.bci.reactive.entity.Role;
import com.bci.reactive.exception.EmailNonUniqueResultException;
import com.bci.reactive.exception.PhoneNonUniqueResultException;
import com.bci.reactive.repository.PhoneRepository;
import com.bci.reactive.webdto.request.CreatePhoneRequest;
import com.bci.reactive.webdto.request.CreateUserRequest;
import com.bci.reactive.entity.User;
import com.bci.reactive.repository.UserRepository;
import com.bci.reactive.webdto.request.UpdatePhoneRequest;
import com.bci.reactive.webdto.response.CreateUserResponse;
import com.bci.reactive.webdto.request.UpdateUserRequest;
import com.bci.reactive.webdto.response.GetUserResponse;
import com.bci.reactive.webdto.response.UpdateUserResponse;
import io.reactivex.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class UserService {

    private static String ROLE_USER = "ROLE_USER";

    private UserRepository userRepository;
    private PhoneRepository phoneRepository;
    private PasswordEncoder passwordEncoder;
    private RoleService roleService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService, PhoneRepository phoneRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.phoneRepository = phoneRepository;
    }

    private void addRoleToUser(User user, String roleStr) {
        Role role = roleService.findByRole(roleStr);
        if (role == null) {
            role = roleService.saveRole(roleStr);
        }
        if(Objects.isNull(user.getRoles()) || user.getRoles().isEmpty()){
            user.setRoles(new ArrayList<>());
        }
        user.getRoles().add(role);
    }

    private String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Single<CreateUserResponse> create(CreateUserRequest req) {

        return Single.just(req).map(createUserRequest -> {
            User user = userRepository.findByEmail(req.getEmail());

            if (Objects.nonNull(user) ) {
                throw new EmailNonUniqueResultException("Exists email");
            }

            conflictsPhone(req.getPhones());

            user = User.builder()
                    .created(Instant.now())
                    .modified(Instant.now())
                    .name(req.getName())
                    .email(req.getEmail())
                    .password( hashPassword(req.getPassword()))
                    .active(Boolean.TRUE)
                    .build();
            addRoleToUser(user, ROLE_USER);
            user = userRepository.save(user);

            List<Phones> listPhones = getPhonesRequest(user.getId(), req);

            phoneRepository.saveAll(listPhones);

            CreateUserResponse createUserResponse = new CreateUserResponse();
            createUserResponse.setId(user.getId());
            return createUserResponse;
        });
    }

    private void conflictsPhone(List<CreatePhoneRequest> phones) {
        if(Objects.isNull(phones) && phones.isEmpty()){
            return;
        }
        for (CreatePhoneRequest phoneRequest : phones) {
            if(Objects.nonNull(phoneRepository.findByCountrycodeAndNumber(phoneRequest.getCountrycode(),phoneRequest.getNumber()))){
                throw new PhoneNonUniqueResultException("Number and contrycode exists :" + phoneRequest.getCountrycode()+"-"+phoneRequest.getNumber());
            }
        }
    }

    private void conflictsPhone(Long userId, List<UpdatePhoneRequest> phones) {
        if(Objects.isNull(phones) && phones.isEmpty()){
            return;
        }
        for (UpdatePhoneRequest phoneRequest : phones) {
            Phones phone = phoneRepository.findByCountrycodeAndNumber(phoneRequest.getCountrycode(),phoneRequest.getNumber());
            if(Objects.nonNull(phone) && (phone.getUserId().longValue() != userId.longValue()) ){
                throw new PhoneNonUniqueResultException("Number and contrycode exists :" + phoneRequest.getCountrycode()+"-"+phoneRequest.getNumber());
            }
        }
    }

    public void updateUser(User dbUser) {
        userRepository.save(dbUser);
    }

    public Single<UpdateUserResponse> update(UpdateUserRequest req) {

        return Single.just(req).map(updateUserRequest -> {

            Optional<User> dbUserOptional = userRepository.findById(updateUserRequest.getId());
            if(!dbUserOptional.isPresent()){
                throw new EntityNotFoundException("User not found");
            }

            User dbUser = dbUserOptional.get();
            User userEmail = userRepository.findByEmail(req.getEmail());
            if(Objects.nonNull(userEmail) && !userEmail.getId().equals(dbUser.getId())){
                throw new EmailNonUniqueResultException("Exists email");
            }

            conflictsPhone(dbUser.getId(), req.getPhones());

            dbUser.setModified(Instant.now());
            dbUser.setName(req.getName());
            dbUser.setEmail(req.getEmail());
            if(Objects.nonNull(req.getPassword()) && !req.getPassword().isEmpty()){
                dbUser.setPassword( hashPassword(req.getPassword()));
            }
            dbUser.setActive(Boolean.TRUE);

            userRepository.save(dbUser);

            phoneRepository.deleteAll(phoneRepository.findByUserId(dbUser.getId()));

            List<Phones> listPhones = getListPhonesUpdate(dbUser.getId(), updateUserRequest.getPhones());
            phoneRepository.saveAll(listPhones);

            UpdateUserResponse updateUserResponse = new UpdateUserResponse();
            updateUserResponse.setId(dbUser.getId());
            return updateUserResponse;
        });
    }


    public Single<GetUserResponse>  getUserById(Long id) {
        return Single.just(id).map(userId -> {
            Optional<User> dbUserOptional = userRepository.findById(userId);
            if(dbUserOptional.isPresent()){

                GetUserResponse getUserResponse = new GetUserResponse();
                User u = dbUserOptional.get();
                getUserResponse.setId(u.getId());
                getUserResponse.setName(u.getName());
                getUserResponse.setEmail(u.getEmail());
                getUserResponse.setCreated(u.getCreated());
                getUserResponse.setModified(u.getModified());
                getUserResponse.setLastLogin(u.getLastLogin());
                getUserResponse.setKeyAccess(u.getKeyAccess());

                List<Phones> listPhones = phoneRepository.findByUserId(u.getId());

                getUserResponse.setPhones(getPhoneResponse(listPhones));
                return getUserResponse;
            }
            throw new EntityNotFoundException("User not found");
        });

    }

    private static List<UpdatePhoneRequest> getPhoneResponse(List<Phones> listPhones) {
        return listPhones.stream().map(response ->
                UpdatePhoneRequest.builder()
                        .number(response.getNumber())
                        .citycode(response.getCitycode())
                        .countrycode(response.getCountrycode())
                        .build()
        ).collect(Collectors.toList());
    }

    private static List<Phones> getListPhonesUpdate(Long userId, List<UpdatePhoneRequest> phones) {
        if(Objects.isNull(phones)){
            return new ArrayList<>();
        }
        return phones.stream().map(updatePhoneRequest ->
                Phones.builder()
                        .userId(userId)
                        .number(updatePhoneRequest.getNumber())
                        .citycode(updatePhoneRequest.getCitycode())
                        .countrycode(updatePhoneRequest.getCountrycode())
                        .build()
        ).collect(Collectors.toList());
    }

    private static List<Phones> getPhonesRequest(Long userId, CreateUserRequest req) {
        if(Objects.isNull(req.getPhones())){
            return new ArrayList<>();
        }
        return req.getPhones().stream().map(createPhoneRequest ->
                Phones.builder()
                        .userId(userId)
                        .number(createPhoneRequest.getNumber())
                        .citycode(createPhoneRequest.getCitycode())
                        .countrycode(createPhoneRequest.getCountrycode())
                        .build()
        ).collect(Collectors.toList());
    }


}