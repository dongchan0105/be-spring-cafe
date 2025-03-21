package codesquad.codestagram.service;

import codesquad.codestagram.dto.ArticleDto;
import codesquad.codestagram.entity.Article;
import codesquad.codestagram.entity.User;
import codesquad.codestagram.repository.ArticleRepository;
import codesquad.codestagram.repository.UserRepositoryV2;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepositoryV2 userRepository;
    private List<Article> articles = new ArrayList<>();

    public ArticleService(ArticleRepository articleRepository, UserRepositoryV2 userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    public List<Article> getArticles() {
        return articles;
    }

    // 기존 오류 수정: title이 아니라 content가 들어가도록 수정
    public List<Article> getArticlesV2() {
        return articleRepository.findAll().stream()
                .map(article -> new Article(article.getTitle(), article.getContent(), article.getUser()))
                .collect(Collectors.toList());
    }

    public void addArticle(Article article) {
        articles.add(article);
    }

    // 사용자를 지정해서 게시글 추가
    public void addArticleV2(ArticleDto request, Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("해당 ID의 사용자가 존재하지 않습니다.");
        }
        User user = userOptional.get();
        articleRepository.save(new Article(request.getTitle(), request.getContent(), user));
    }

    @PostConstruct
    public void init() {
        User defaultUser = userRepository.findById(1L).orElse(null);
        if (defaultUser == null) {
            defaultUser = new User("홍길동", "user1@example.com", "user1", "password123");
            userRepository.save(defaultUser);
        }

        articles.add(new Article("InitializingBean implements afterPropertiesSet() 호출되지 않는 문제.",
                "A 에 의존성을 가지는 B라는 클래스가 있습니다.\n\n" +
                        "B라는 클래스는 InitializingBean 을 상속하고 afterPropertiesSet을 구현하고 있습니다. 서버가 가동되면서 bean들이 초기화되는 시점에 B라는 클래스의 afterPropertiesSet 메소드는\n\n" +
                        "A라는 클래스의 특정 메소드인 afunc()를 호출하고 있습니다.", defaultUser));

        articles.add(new Article("두 번째 게시글", "내용 2", defaultUser));
    }

    public Optional<Article> findArticleById(Long articleId) {
       return articleRepository.findArticleById(articleId);
    }
}
