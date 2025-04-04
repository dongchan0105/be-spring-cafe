package codesquad.codestagram.service;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.request.ArticleUpdateRequest;
import codesquad.codestagram.repository.ArticleRepository;
import codesquad.codestagram.repository.UserRepositoryV2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private UserRepositoryV2 userRepository;

    @InjectMocks
    private ArticleService articleService;

    private Article testArticle;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("테스트유저", "test@test.com", "testUser", "password");
        testUser.setId(1L);

        testArticle = new Article("테스트 제목", "테스트 내용", testUser);
        testArticle.setId(1L);
    }

    @Test
    void deleteArticleById_ShouldSetDeletedTrue() {
        // Given
        when(articleRepository.findArticleById(1L)).thenReturn(Optional.of(testArticle));

        // When
        articleService.deleteArticleById(1L);

        // Then
        assertThat(testArticle.getDeleted()).isTrue();
        verify(articleRepository).deleteById(1L);
    }

    @Test
    void getArticles_ShouldExcludeDeletedArticles() {
        // Given
        Article deletedArticle = new Article("삭제된 글", "삭제된 내용", testUser);
        deletedArticle.setDeleted(true);

        when(articleRepository.findAll()).thenReturn(Collections.singletonList(deletedArticle));

        // When
        Page<Article> result = articleService.getArticles(1);

        // Then
        assertThat(result).isEmpty();
        verify(articleRepository).findAll();
    }

    @Test
    void findArticleById_ShouldNotFindDeletedArticle() {
        // Given
        testArticle.setDeleted(true);
        when(articleRepository.findArticleById(1L)).thenReturn(Optional.empty());

        // When
        Optional<Article> result = articleService.findArticleById(1L);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void findByIdIncludingDeleted_ShouldFindDeletedArticle() {
        // Given
        testArticle.setDeleted(true);
        when(articleRepository.findByIdIncludingDeleted(1L)).thenReturn(Optional.of(testArticle));

        // When
        Optional<Article> result = articleRepository.findByIdIncludingDeleted(1L);

        // Then
        assertThat(result)
                .isPresent()
                .hasValueSatisfying(article ->
                        assertThat(article.getDeleted()).isTrue()
                );
    }

    @Test
    void updateArticle_ShouldNotAffectDeletedStatus() {
        // Given
        when(articleRepository.findArticleById(1L)).thenReturn(Optional.of(testArticle));
        ArticleUpdateRequest request = new ArticleUpdateRequest("수정 제목", "수정 내용");

        // When
        articleService.updateArticleById(1L, request);

        // Then
        assertThat(testArticle)
                .satisfies(article -> {
                    assertThat(article.getDeleted()).isFalse();
                    assertThat(article.getTitle()).isEqualTo("수정 제목");
                    assertThat(article.getContent()).isEqualTo("수정 내용");
                });
    }

    @Test
    void deleteNonExistentArticle_ShouldThrowException() {
        // Given
        when(articleRepository.findArticleById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> articleService.deleteArticleById(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 게시글이 존재하지 않습니다");
    }
}
