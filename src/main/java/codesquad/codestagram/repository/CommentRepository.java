package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    Optional<Comment> findByArticleIdAndId(Long articleId, Long id);

    Page<Comment> findByArticleIdOrderByCreatedDateDesc(Long articleId, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.id = :id")
    Optional<Comment> findByIdIncludingDeleted(@Param("id") Long id);
}
