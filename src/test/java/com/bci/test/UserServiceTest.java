package com.bci.test;

import com.bci.reactive.entity.Phones;
import com.bci.reactive.entity.User;
import com.bci.reactive.repository.PhoneRepository;
import com.bci.reactive.repository.UserRepository;
import com.bci.reactive.service.RoleService;
import com.bci.reactive.service.UserService;
import com.bci.reactive.webdto.request.CreatePhoneRequest;
import com.bci.reactive.webdto.request.CreateUserRequest;
import com.bci.reactive.webdto.request.UpdatePhoneRequest;
import com.bci.reactive.webdto.request.UpdateUserRequest;
import com.bci.reactive.webdto.response.CreateUserResponse;
import com.bci.reactive.webdto.response.UpdateUserResponse;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class UserServiceTest {

    private UserService userService;

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleService roleService;
    private PhoneRepository phoneRepository;

    @Before
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        roleService = Mockito.mock(RoleService.class);
        phoneRepository = Mockito.mock(PhoneRepository.class);
        userService = new UserService(userRepository, passwordEncoder, roleService, phoneRepository);
    }

    @Test
    public void create_user() throws Exception {

        User user = new User();
        user.setId(1L);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail("usuario03@bcitest.com");
        createUserRequest.setPassword("pass1235");
        createUserRequest.setName("test2");
        List<CreatePhoneRequest> phones = new ArrayList<>();
        CreatePhoneRequest createPhoneRequest = new CreatePhoneRequest();
        createPhoneRequest.setNumber("987456129");
        createPhoneRequest.setCountrycode("cw");
        createPhoneRequest.setCitycode("45");
        phones.add(createPhoneRequest);
        createUserRequest.setPhones(phones);

        Single<CreateUserResponse> createUserEvent = userService.create(createUserRequest);

        TestObserver<CreateUserResponse> testObserver = createUserEvent.test();

        testObserver.assertComplete()
                .assertNoErrors()
                .assertValue(createUserResponse -> {
                    return createUserResponse.getId().longValue() == 1;
                });

    }

    @Test
    public void create_user_email_duplicated() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setEmail("usuario03@bcitest.com");
        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(user);

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail("usuario03@bcitest.com");
        createUserRequest.setPassword("pass1235");
        createUserRequest.setName("test2");
        List<CreatePhoneRequest> phones = new ArrayList<>();
        CreatePhoneRequest createPhoneRequest = new CreatePhoneRequest();
        createPhoneRequest.setNumber("987456129");
        createPhoneRequest.setCountrycode("cw");
        createPhoneRequest.setCitycode("45");
        phones.add(createPhoneRequest);
        createUserRequest.setPhones(phones);

        Single<CreateUserResponse> createUserEvent = userService.create(createUserRequest);

        TestObserver<CreateUserResponse> testObserver = createUserEvent.test();

        testObserver.assertError(NonUniqueResultException.class);

    }


    @Test
    public void create_user_phone_duplicated() throws Exception {

        User user = new User();
        user.setId(1L);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        Phones phonestest = new Phones();
        phonestest.setNumber("987456129");
        phonestest.setCountrycode("cw");
        Mockito.when(phoneRepository.findByCountrycodeAndNumber(Mockito.any(), Mockito.any())).thenReturn(phonestest);

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail("usuario03@bcitest.com");
        createUserRequest.setPassword("pass1235");
        createUserRequest.setName("test2");
        List<CreatePhoneRequest> phones = new ArrayList<>();
        CreatePhoneRequest createPhoneRequest = new CreatePhoneRequest();
        createPhoneRequest.setNumber("987456129");
        createPhoneRequest.setCountrycode("cw");
        createPhoneRequest.setCitycode("45");
        phones.add(createPhoneRequest);
        createUserRequest.setPhones(phones);

        Single<CreateUserResponse> createUserEvent = userService.create(createUserRequest);

        TestObserver<CreateUserResponse> testObserver = createUserEvent.test();

        testObserver.assertError(NonUniqueResultException.class);


    }


    @Test
    public void update_user() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setEmail("usuario03@bcitest.com");
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));

        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setEmail("usuario03@bcitest.com");
        updateUserRequest.setPassword("pass1235");
        updateUserRequest.setName("test2");
        List<UpdatePhoneRequest> phones = new ArrayList<>();
        UpdatePhoneRequest updatePhoneRequest = new UpdatePhoneRequest();
        updatePhoneRequest.setNumber("987456129");
        updatePhoneRequest.setCountrycode("cw");
        updatePhoneRequest.setCitycode("45");
        phones.add(updatePhoneRequest);
        updateUserRequest.setPhones(phones);

        Single<UpdateUserResponse> userResponseSingle = userService.update(updateUserRequest);

        TestObserver<UpdateUserResponse> testObserver = userResponseSingle.test();

        testObserver.assertComplete()
                .assertNoErrors()
                .assertValue(createUserResponse -> {
                    return createUserResponse.getId().longValue() == 1;
                });

    }


    @Test
    public void update_user_not_exists() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setEmail("usuario032@bcitest.com");
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setEmail("usuario03@bcitest.com");
        updateUserRequest.setPassword("pass1235");
        updateUserRequest.setName("test2");
        List<UpdatePhoneRequest> phones = new ArrayList<>();
        UpdatePhoneRequest updatePhoneRequest = new UpdatePhoneRequest();
        updatePhoneRequest.setNumber("987456129");
        updatePhoneRequest.setCountrycode("cw");
        updatePhoneRequest.setCitycode("45");
        phones.add(updatePhoneRequest);
        updateUserRequest.setPhones(phones);

        Single<UpdateUserResponse> userResponseSingle = userService.update(updateUserRequest);

        TestObserver<UpdateUserResponse> testObserver = userResponseSingle.test();

        testObserver.assertError(EntityNotFoundException.class);


    }


    @Test
    public void update_user_email_exists() throws Exception {

        User user2 = new User();
        user2.setId(1L);
        user2.setEmail("usuario03@bcitest.com");
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user2));

        User user = new User();
        user.setId(2L);
        user.setEmail("usuario03@bcitest.com");
        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(user);

        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setEmail("usuario03@bcitest.com");
        updateUserRequest.setPassword("pass1235");
        updateUserRequest.setName("test2");
        List<UpdatePhoneRequest> phones = new ArrayList<>();
        UpdatePhoneRequest updatePhoneRequest = new UpdatePhoneRequest();
        updatePhoneRequest.setNumber("987456129");
        updatePhoneRequest.setCountrycode("cw");
        updatePhoneRequest.setCitycode("45");
        phones.add(updatePhoneRequest);
        updateUserRequest.setPhones(phones);

        Single<UpdateUserResponse> userResponseSingle = userService.update(updateUserRequest);

        TestObserver<UpdateUserResponse> testObserver = userResponseSingle.test();

        testObserver.assertError(NonUniqueResultException.class);
    }

    @Test
    public void update_user_phone_exists() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setEmail("usuario03@bcitest.com");
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));

        Phones phonesTest = Phones.builder()
                .userId(2L)
                .number("987456129")
                .citycode("45")
                .countrycode("cw")
                .build();
        Mockito.when(phoneRepository.findByCountrycodeAndNumber(Mockito.any(), Mockito.any())).thenReturn(phonesTest);

        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setId(1L);
        updateUserRequest.setEmail("usuario03@bcitest.com");
        updateUserRequest.setPassword("pass1235");
        updateUserRequest.setName("test2");
        List<UpdatePhoneRequest> phones = new ArrayList<>();
        UpdatePhoneRequest updatePhoneRequest = new UpdatePhoneRequest();
        updatePhoneRequest.setNumber("987456129");
        updatePhoneRequest.setCountrycode("cw");
        updatePhoneRequest.setCitycode("45");
        phones.add(updatePhoneRequest);
        updateUserRequest.setPhones(phones);

        Single<UpdateUserResponse> userResponseSingle = userService.update(updateUserRequest);

        TestObserver<UpdateUserResponse> testObserver = userResponseSingle.test();

        testObserver.assertError(NonUniqueResultException.class);
    }

}