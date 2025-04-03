package codesquad.codestagram.service;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.Comment;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.ArticleRepository;
import codesquad.codestagram.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceV2 {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    // 댓글 작성
    public Comment leaveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    // 댓글 삭제 (작성자 확인 포함)
    public void deleteComment(Long commentId, User requester) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        if (!comment.getUser().getId().equals(requester.getId())) {
            throw new IllegalStateException("댓글 삭제 권한이 없습니다.");
        }

        comment.setDeleted(true); // 소프트 딜리트 처리
        commentRepository.save(comment);
    }

    // 게시글 ID로 댓글 목록 조회
    public List<Comment> getCommentsByArticleId(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        return article.getComments();
    }
}
