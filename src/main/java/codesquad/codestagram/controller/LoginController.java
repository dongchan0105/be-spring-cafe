package codesquad.codestagram.controller;

import codesquad.codestagram.domain.LoginForm;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.service.LoginService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class LoginController {


    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }
        User loginUser = loginService.login(form.getLoginId(), form.getPassword());
        if (loginUser == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        session.setAttribute("loginUser", loginUser);

        //로그인 성공 처리 TODO
        return "redirect:/users";
    }
}
