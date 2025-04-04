package codesquad.codestagram.controller;

import codesquad.codestagram.annotation.Login;
import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.Comment;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.request.CommentCreateRequest;
import codesquad.codestagram.dto.response.CommentResponse;
import codesquad.codestagram.service.ArticleService;
import codesquad.codestagram.service.CommentServiceV2;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.net.URI;

@RestController
@RequestMapping("/qna/comments")
public class CommentRestController {

    private final CommentServiceV2 commentService;
    private final ArticleService articleService;

    public CommentRestController(CommentServiceV2 commentService, ArticleService articleService) {
        this.commentService = commentService;
        this.articleService = articleService;
    }

    // 댓글 생성
    @PostMapping("/{articleId}")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable("articleId") Long articleId,
            @RequestBody @Valid CommentCreateRequest request,
            @Login User user) {

        Article article = articleService.findArticleById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다"));

        Comment comment = new Comment(
                request.comment(),
                article,
                user
        );

        Comment savedComment = commentService.leaveComment(comment);
        return ResponseEntity.created(URI.create("/api/comments/" + savedComment.getId()))
                .body(CommentResponse.from(savedComment));
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable("commentId") Long commentId,
            @Login User user) {

        commentService.deleteComment(commentId, user);
        return ResponseEntity.noContent().build();
    }

    // 댓글 목록 조회
    /*@GetMapping("/{articleId}")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long articleId) {
        List<Comment> comments = commentService.getCommentsByArticleId(articleId);
        return ResponseEntity.ok(
                comments.stream()
                        .map(CommentResponse::from)
                        .toList()
        );
    }*/

    @GetMapping("/{articleId}")
    public ResponseEntity<Page<CommentResponse>> getComments(
            @PathVariable("articleId") long articleId,
            @RequestParam(name = "page", defaultValue = "1") int page
    ) {
        // 1. 서비스 계층에서 댓글 페이지 조회
        Page<Comment> commentPage = commentService.getCommentByArticleId(articleId, page);

        // 2. Comment → CommentResponse 변환
        Page<CommentResponse> responsePage = commentPage.map(CommentResponse::from);

        // 3. 변환된 결과 반환
        return ResponseEntity.ok(responsePage);
    }
}


