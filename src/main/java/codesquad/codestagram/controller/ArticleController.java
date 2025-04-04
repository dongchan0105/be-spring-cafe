package codesquad.codestagram.controller;

import codesquad.codestagram.annotation.Login;
import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.request.ArticleUpdateRequest;
import codesquad.codestagram.dto.request.ArticleWriteRequest;
import codesquad.codestagram.service.ArticleService;
import codesquad.codestagram.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Slf4j
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
    /*@GetMapping()
    public String viewQuestions(Model model) {
        List<Article> articles = articleService.getArticles();
        model.addAttribute("articles", articles);
        return "qna/list";
    }*/

    @GetMapping()
    public String viewArticles(
            @RequestParam(name = "page",defaultValue = "1") int page,
            Model model
    ) {
        Page<Article> articlePage = articleService.getArticles(page);
        model.addAttribute("articles", articlePage.getContent());
        model.addAttribute("pageInfo", articleService.getPaginationData(articlePage));
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
            return "redirect:/login";
        }


        articleService.addArticle(request);
        return "redirect:/qna";
    }

    //게시글 수정 입력폼노출
    @GetMapping("/articles/{articleId}/edit")
        public String showUpdateArticleForm(@Login User user, @PathVariable("articleId")Long articleId,Model model){

        Optional<Article> article = articleService.findArticleById(articleId);

        if (article.isEmpty()) {
            return "redirect:/qna";
        }

        if(!article.get().getUser().equals(user) || user==null){
            return "redirect:/login";
        }

        model.addAttribute("article", article.get());
        return "qna/edit";
    }

    // 게시글 수정
    @PostMapping("/articles/{articleId}/edit")
    public String updateArticle(@PathVariable("articleId") Long articleId, @ModelAttribute ArticleUpdateRequest request) {
        articleService.updateArticleById(articleId, request);
        return "redirect:/qna";
    }

    //게시글 삭제
    @PostMapping("/articles/{articleId}/delete")
    public String deleteArticle(@PathVariable("articleId") Long articleId) {
        // 게시글 조회
        Article article = articleService.findArticleById(articleId).orElseThrow(() -> new IllegalArgumentException("Article not found"));

        // 댓글이 없는 경우 바로 삭제 가능
        if (article.getComments().isEmpty()) {
            articleService.deleteArticleById(articleId);
            return "redirect:/qna";
        }

        // 댓글 작성자와 게시글 작성자가 모두 같은지 확인
        boolean allCommentsByAuthor = article.getComments().stream()
                .allMatch(comment -> comment.getUser().equals(article.getUser()));

        if (allCommentsByAuthor) {
            // 모든 댓글 작성자가 게시글 작성자인 경우 삭제 가능
            articleService.deleteArticleById(articleId);
            return "redirect:/qna";
        }

        // 조건에 맞지 않으면 삭제 불가능
        return "redirect:/articles";
    }

}
