package com.fflins.ecom.controllers;

import com.fflins.ecom.models.User;
import com.fflins.ecom.DTOs.UserDTO;
import com.fflins.ecom.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired private UserService userService;

    // Converte um User para UserDTO
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setLogin(user.getLogin());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());  // Ou o valor exato do seu Enum
        return dto;
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return users.stream().map(this::convertToDTO).toList();
    }

    @GetMapping("/{username}")
    public UserDTO getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return convertToDTO(user);
    }

    @GetMapping("/me")
    public UserDTO getLoggedInUser() {
        User user = userService.getLoggedInUser();
        return convertToDTO(user);
    }

    @GetMapping("/email/{email}")
    public UserDTO getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return user != null ? convertToDTO(user) : null;
    }

    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
    }
}
