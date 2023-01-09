package org.example.pp.controller;

import org.example.pp.model.Role;
import org.example.pp.model.User;
import org.example.pp.service.RoleServiceImp;
import org.example.pp.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleServiceImp roleService;
    public AdminController(UserService userService, RoleServiceImp roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String showAllUsers(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("authUser", user);
        model.addAttribute("roles",  roleService.getAllRoles());
        return "admin";
    }

    @PostMapping("delete")
    public String deleteUser(@RequestParam long id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }

    @PostMapping(value = "save")
    public String saveUser(@RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam Integer age,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String role) {

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(roleService.getRole(role));

        userService.saveUser(new User(firstName, lastName, age, email, password, userRoles));
        return "redirect:/admin";
    }

    @PostMapping(value = "edit/{id}")
    public String updateUser(@RequestParam String firstName,
                             @RequestParam String lastName,
                             @RequestParam Integer age,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam String role,
                             @RequestParam Long id) {

        User user = userService.findUserById(id);
        user.getRoles().clear();


        Set<Role> userRoles = new HashSet<>();
        userRoles.add(roleService.getRole(role));

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAge(age);
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(userRoles);

        userService.updateUser(user);

        return "redirect:/admin";
    }
}
