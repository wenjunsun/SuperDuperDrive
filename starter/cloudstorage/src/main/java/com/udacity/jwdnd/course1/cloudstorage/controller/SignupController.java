package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/signup")
public class SignupController {
    private UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getSignupPage(Model model) {
        return "signup";
    }

    @PostMapping
    public String signup(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes) {
        // documentation for RedirectAttributes:
        // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/mvc/support/RedirectAttributes.html

        // From the documentation:
        // A RedirectAttributes model is empty when the method is called and is never used unless the method returns a redirect view name or a RedirectView.
        // After the redirect, flash attributes are automatically added to the model of the controller that serves the target URL.

        // somehow just returning "/login" view without this redirection stuff will return the login page, but the URL
        // still stays at "/signup", I think that is because controller only returns view without modifying the URL.

        String signupError = null;

        if (!userService.isUserNameAvailable(user.getUserName())) {
            signupError = "The username already exists. Please pick another username.";
        }

        if (signupError == null) {
            int rowsAdded = userService.addUser(user);
            if (rowsAdded < 0) {
                signupError = "There was an error signing you up. Please try again.";
            }
        }

        if (signupError == null) {
            redirectAttributes.addFlashAttribute("signupSuccess", true);
            return "redirect:/login";
        } else {
            model.addAttribute("signupError", signupError);
            return "signup";
        }

    }
}
