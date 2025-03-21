package codesquad.codestagram.controller;

import codesquad.codestagram.entity.User;
import codesquad.codestagram.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원 가입 폼 페이지
    @GetMapping("/form")
    public String showUserForm(Model model) {
        model.addAttribute("user", new User());
        return "user/form";
    }

    // 회원 가입 처리
    @PostMapping()
    public String registerUser(@Validated @ModelAttribute("user") User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/form"; // 유효성 검증 실패 시 회원가입 폼으로 돌아감
        }
        userService.registerUser(user.getName(), user.getEmail(), user.getLoginId(), user.getPassword());
        return "redirect:/users";
    }

    // 회원 목록 조회
    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user/list";
    }

    // 특정 회원 프로필 조회
    @GetMapping("/{userId}")
    public String userProfile(@PathVariable("userId") Long userId, Model model) {
        Optional<User> user = userService.findUserById(userId);
        if (user.isEmpty()) {
            return "redirect:/users"; // 존재하지 않는 사용자 처리
        }
        model.addAttribute("user", user.get());
        return "user/profile";
    }

    // 회원 프로필 수정 페이지
    @GetMapping("/{userId}/edit")
    public String editUserProfile(@PathVariable("userId") Long userId, Model model, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        if (!loginUser.getId().equals(userId)) {
            return "redirect:/users";
        }

        Optional<User> user = userService.findUserById(userId);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user/profile-edit";
        } else {
            return "redirect:/users";
        }
    }

    // 회원 정보 업데이트
    @PutMapping("/{userId}/form")
    public String updateUserProfile(@PathVariable("userId") Long userId,
                                    @RequestParam("password") String password,
                                    @RequestParam("name") String name,
                                    @RequestParam("email") String email) {
        userService.updateUserProfile(userId, password, name, email);
        return "redirect:/users/" + userId;
    }

    @GetMapping("/users/form")
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        return "user/form";
    }
}
