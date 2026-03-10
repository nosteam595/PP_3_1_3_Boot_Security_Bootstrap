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
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String getAllUsers(ModelMap modelMap) {
        modelMap.addAttribute("users", userService.allUsers());
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

    @GetMapping("/add")
    public String addNewUser(ModelMap modelMap) {
        modelMap.addAttribute("user", new User());
        modelMap.addAttribute("allRoles", roleService.getAllRoles());
        return "userAdd";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") @Valid User user,
                           BindingResult bindingResult,
                           @RequestParam(value = "listRoles", required = false) List<Long> roleIds,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", roleService.getAllRoles());
            return "userAdd";
        }
        userService.registerNewUser(user, roleIds);
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
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            User oldUser = userService.getUser(user.getId());
            user.setPassword(oldUser.getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (bindingResult.hasFieldErrors("firstName") ||
                bindingResult.hasFieldErrors("lastName") ||
                bindingResult.hasFieldErrors("email") ||
                bindingResult.hasFieldErrors("age")) {

            model.addAttribute("allRoles", roleService.getAllRoles());
            return "userEdit";
        }
        if (roleIds != null) {
            Set<Role> roles = new HashSet<>();
            for (Long roleId : roleIds) {
                roles.add(roleService.getRoleById(roleId));
            }
            user.setRoles(roles);
        }
        userService.updateUser(user);
        return "redirect:/users";
    }

    @PostMapping("/remove")
    public String removeUser(@ModelAttribute("user") User user) {
        userService.removeUser(user);
        return "redirect:/users";
    }
}
