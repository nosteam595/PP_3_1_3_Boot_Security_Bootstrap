package nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.controllers;

import jakarta.validation.Valid;
import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.model.Role;
import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.model.User;
import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.security.PersonDetails;
import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.services.RoleService;
import nosteam.IdeaProjects.PP_3_1_2_Boot_Security_new.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.allUsers());
        model.addAttribute("newUser", new User());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "users";
    }

    @GetMapping("/user/details")
    public String showUserProfile(@RequestParam(value = "id", required = false) Long id,
                                  @AuthenticationPrincipal PersonDetails personDetails,
                                  Model model) {
        User userToShow = userService.getUserForProfile(personDetails.getUser(), id);
        model.addAttribute("user", userToShow);
        return "userProfile";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("newUser") @Valid User user,
                           BindingResult bindingResult,
                           @RequestParam(value = "listRoles", required = false) List<Long> roleIds,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.allUsers());
            model.addAttribute("allRoles", roleService.getAllRoles());
            return "users";
        }
        userService.addUser(user, roleIds);
        return "redirect:/users";
    }

    @GetMapping("/edit")
    public String editUserPage(@RequestParam("id") Long id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "userEdit";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult,
                             @RequestParam(value = "listRoles", required = false) List<Long> roleIds,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", roleService.getAllRoles());
            return "userEdit";
        }
        userService.updateUser(user, roleIds);
        return "redirect:/users";
    }

    @PostMapping("/remove")
    public String removeUser(@ModelAttribute("user") User user) {
        userService.removeUser(user);
        return "redirect:/users";
    }
}
