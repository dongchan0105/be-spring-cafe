package codesquad.codestagram.controller;

import codesquad.codestagram.annotation.Login;
import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.Comment;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.service.ArticleService;
import codesquad.codestagram.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
//@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final ArticleService articleService;

    public CommentController(CommentService commentService, ArticleService articleService) {
        this.commentService = commentService;
        this.articleService = articleService;
    }
    //댓글 등록
    @PostMapping("/articles/{articleId}")
    public String addComment(@Login User loginUser, @PathVariable("articleId") Long articleId,
                           @RequestParam("content") String content,
                           HttpSession session) {
        Article article = articleService.findArticleById(articleId)
                .orElseThrow();
        
        Comment comment = new Comment(content, article, loginUser);
        commentService.leaveComment(comment);
        
        return "redirect:/qna/articles/" + articleId;
    }

    @PostMapping("/{articleId}/{commentId}/edit")
    public String editComment(@Login User user, @PathVariable("articleId") Long articleId, @PathVariable("commentId") Long commentId, @RequestParam("content") String content) {
        if(!user.equals(commentService.findCommentByCommentId(commentId).getUser())){
            throw new IllegalStateException("댓글 수정 권한이 없습니다.");
        }
        commentService.updateComment(commentId, content);
        return "redirect:/qna/articles/" + articleId;
    }

    @PostMapping("/{articleId}/{commentId}/delete")
    public String deleteComment(@Login User user , @PathVariable("articleId") Long articleId, @PathVariable("commentId") Long commentId) {
        if(!user.equals(commentService.findCommentByCommentId(commentId).getUser())){
            throw new IllegalStateException("댓글 삭제 권한이 없습니다.");
        }
        commentService.deleteComment(commentId);
        return "redirect:/qna/articles/" + articleId;
    }
}