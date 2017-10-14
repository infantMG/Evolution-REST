package evolution.controller;

import evolution.common.GenderEnum;
import evolution.common.UserRoleEnum;
import evolution.data.UserDataService;
import evolution.manager.UserManager;
import evolution.model.User;
import evolution.model.UserAdditionalData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by Infant on 29.09.2017.
 */
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserRestController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final UserManager userManager;

    @Autowired
    public UserRestController(UserManager userManager) {
        this.userManager = userManager;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity findById(@PathVariable Long id) {
        Optional<User> optional = userManager.findOne(id);
        if (optional.isPresent()) {
            return ResponseEntity.ok(optional.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}/admin")
    public ResponseEntity findByIdAdmin(@PathVariable Long id) {
        Optional<User> optional = userManager.findOneInitializeLazy(id);
        if (optional.isPresent()) {
            return ResponseEntity.ok(optional.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/admin")
    public ResponseEntity findAllLazy() {
        List<User> list = this.userManager.findAllInitializeLazy();
        if (list.isEmpty())
            return ResponseEntity.noContent().build();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity findAll() {
        List<User> list = this.userManager.findAll();
        if (list.isEmpty())
            return ResponseEntity.noContent().build();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    @GetMapping(value = "/save/test")
    public ResponseEntity save() {

        for (int i = 1; i < 10; i++) {
            User user = new User();
            user.setFirstName("TEST_USER_" + i);
            user.setLastName("TEST_USER_" + i);
            user.setNickname("TEST_USER_" + i);
            user.setRole(UserRoleEnum.USER);

            UserAdditionalData userAdditionalData = new UserAdditionalData();
            userAdditionalData.setActive(true);
            userAdditionalData.setPassword("user_" + i);
            userAdditionalData.setGenderEnum(GenderEnum.MALE);
            userAdditionalData.setRegistrationDate(new Date());
            userAdditionalData.setUsername("user" + i + "@mail.ru");


            user.setUserAdditionalData(userAdditionalData);

            userManager.save(user);
        }

        return ResponseEntity.ok().build();
    }


}
