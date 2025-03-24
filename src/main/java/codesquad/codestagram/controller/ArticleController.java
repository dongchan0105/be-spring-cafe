package codesquad.codestagram.controller;

import codesquad.codestagram.annotation.Login;
import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.request.ArticleWriteRequest;
import codesquad.codestagram.service.ArticleService;
import codesquad.codestagram.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/qna")
public class ArticleController {

    private final ArticleService articleService;
    private final UserService userService;

    public ArticleController(ArticleService articleService, UserService userService) {
        this.articleService = articleService;
        this.userService = userService;
    }

    // 모든 게시글 조회
    @GetMapping()
    public String viewQuestions(Model model) {
        List<Article> articles = articleService.getArticlesV2();
        model.addAttribute("articles", articles);
        return "qna/list";
    }

    // 특정 게시글 조회
    @GetMapping("/articles/{articleId}")
    public String viewArticle(@PathVariable("articleId") Long articleId, Model model) {
        Optional<Article> article = articleService.findArticleById(articleId);
        if (article.isEmpty()) {
            return "redirect:/qna";
        }
        model.addAttribute("article", article.get());
        return "qna/show";
    }

    // 게시글 작성 폼 페이지
    @GetMapping("/form")
    public String showForm(@Login User user, RedirectAttributes redirectAttributes) {

        if (user == null) {
            // 로그인되지 않은 경우 메시지와 함께 로그인 페이지로 리다이렉트
            redirectAttributes.addFlashAttribute("loginMessage", "게시글 작성을 위해 로그인이 필요합니다.");
            return "redirect:/login";
        }

        return "qna/form";
    }

    // 게시글 작성
    @PostMapping("/write")
    public String submitArticle(@Login User user, @ModelAttribute ArticleWriteRequest request) {
        request.setUser(user);

        if (user == null) {
            return "redirect:/qna?error=userNotFound";
        }

        if (request.getContent() == null || request.getContent().isEmpty()) {
            return "redirect:/qna?error=contentRequired";
        }

        articleService.addArticleV2(request);
        return "redirect:/qna";
    }


}
