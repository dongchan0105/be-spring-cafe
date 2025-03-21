package codesquad.codestagram.controller;

import codesquad.codestagram.dto.ArticleDto;
import codesquad.codestagram.domain.Article;
import codesquad.codestagram.dto.request.ArticleWriteRequest;
import codesquad.codestagram.service.ArticleService;
import codesquad.codestagram.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String showForm() {
        return "qna/form";
    }

    // 게시글 작성
    @PostMapping("/write")
    public String submitArticle(@RequestBody ArticleWriteRequest request) {

        if (userService.findUserById(request.getUser().getId()).isEmpty()) {
            return "redirect:/qna?error=userNotFound";
        }

        articleService.addArticleV2(request);
        return "redirect:/qna";
    }


}
