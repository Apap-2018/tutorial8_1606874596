package com.apap.tutorial8.controller;

import com.apap.tutorial8.model.UserRoleModel;
import com.apap.tutorial8.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/user")
public class UserRoleController {
    @Autowired
    private UserRoleService userService;

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    private String addUserSubmit(@ModelAttribute UserRoleModel user, Model model) {
        if (user.getPassword().length() < 8) {
            model.addAttribute("msg", "Password tidak boleh kurang dari 8 karakter!");
            return "home";
        } else {
            if (user.getPassword().matches(".*[a-zA-Z].*") && user.getPassword().matches(".*[0-9].*")) {
                userService.addUser(user);
                model.addAttribute("msg", "User baru berhasil ditambahkan");
                return "home";
            } else {
                model.addAttribute("msg", "Password harus mengandung angka dan huruf");
                return "home";
            }
        }
    }

    @RequestMapping(value = "/updatePassword/{username}", method = RequestMethod.GET)
    private String updatePassword(@PathVariable(value="username") String username, Model model) {
        UserRoleModel user = userService.getUser(username);
        model.addAttribute("user", user);
        model.addAttribute("username", username);
        model.addAttribute("msg", "");
        return "update-password";
    }

    @RequestMapping(value = "/updatePassword/{username}", method = RequestMethod.POST)
    private String updatePasswordSubmit(@PathVariable(value="username") String username, String oldpass, String newpass, String newpasskonfirmasi, Model model) {
        UserRoleModel user = userService.getUser(username);

        if (newpass.matches(".*[a-zA-Z].*") && newpass.matches(".*[0-9].*") && newpass.length() >= 8) {
            if (newpass.equals(newpasskonfirmasi) == false) {
                model.addAttribute("msg", "Konfirmasi password tidak sama!");
                return "update-password";
            } else {
                boolean valid = userService.validatePassword(user.getPassword(), oldpass);
                if (valid == true) {
                    userService.updatePass(username, newpass);
                    return "update-password-success";
                } else {
                    model.addAttribute("msg", "Password tidak sesuai!");
                    return "update-password";

                }
            }
        } else {
            model.addAttribute("msg", "Password harus mengandung huruf dan angka serta tidak boleh kurang dari 8 karakter!");
            return "update-password";
        }


    }
}
