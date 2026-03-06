package nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.controllers;

import jakarta.validation.Valid;
import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.model.User;
import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getAllUsers(ModelMap modelMap) {
        modelMap.addAttribute("users", userService.allUsers());
        return "users";
    }

    @GetMapping("/user/details")
    public String getUser(@RequestParam long id, ModelMap model) {
        model.addAttribute("user", userService.getUser(id));
        return "userUpdateRemove";
    }

    @GetMapping("/add")
    public String addNewUser(ModelMap modelMap) {
        modelMap.addAttribute("user", new User());
        return "userAdd";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") @Valid User user,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Если есть ошибки, возвращаем ту же форму (НЕ редирект!)
            return "userAdd";
        }
        //user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.addUser(user);
        return "redirect:/users";
    }

    @PostMapping("/remove")
    public String removeUser(@ModelAttribute("user") User user) {
        userService.removeUser(user);
        return "redirect:/users";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "userUpdateRemove";
        }
        userService.updateUser(user);
        return "redirect:/users";
    }
}
