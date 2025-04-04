package codesquad.codestagram.repository;


import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findArticleById(Long id);

    Optional<Article> deleteArticleById(Long id);

    List<Article> findArticlesByUser(User user);


    // Soft Delete 적용 시
    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT a FROM Article a WHERE a.deleted = false ORDER BY a.createdDate DESC")
    Page<Article> findAllActiveOrderByCreatedDateDesc(Pageable pageable);

    // 삭제된 데이터도 포함하여 조회
    @Query("SELECT a FROM Article a WHERE a.id = :id")
    Optional<Article> findByIdIncludingDeleted(@Param("id") Long id);
}
