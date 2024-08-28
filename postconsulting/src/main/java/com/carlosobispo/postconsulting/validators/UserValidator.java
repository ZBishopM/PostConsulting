package com.carlosobispo.postconsulting.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.carlosobispo.postconsulting.models.Role;
import com.carlosobispo.postconsulting.models.User;
import com.carlosobispo.postconsulting.services.RoleService;
import com.carlosobispo.postconsulting.services.UserService;

@Component
public class UserValidator implements Validator {
    private final UserService userService;
    private final RoleService roleService;

    public UserValidator(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    @SuppressWarnings("null")
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }
    @SuppressWarnings("null")
    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        assignUserRoles(user);
        if (userService.existsByEmail(user.getEmail())) {
            errors.rejectValue("email", "USER_EMAIL_ALREADY_REGISTERED");
            return;
        }
        //TODO: validate password missmatch
        if (passwordsMismatch(user)) {
            errors.rejectValue("passwordConfirmation", "USER_PASSWORDS_MISMATCH");
            return;
        }
    }

    public boolean passwordsMismatch(User user) {
        return user.getPassword().equals(user.getPasswordConfirmation()) == false;
    }

    private void assignUserRoles(User user) {
        Role role;
        role = roleService.findByName("ROLE_USER");
        user.setRole(role);
    }
}
