package codesquad.codestagram.service;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.request.ArticleWriteRequest;
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

    public ArticleService(ArticleRepository articleRepository, UserRepositoryV2 userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }


    public List<Article> getArticlesV2() {
        return articleRepository.findAll().stream()
                .map(article -> new Article(article.getTitle(), article.getContent(), article.getUser()))
                .collect(Collectors.toList());
    }


    // 사용자를 지정해서 게시글 추가
    public void addArticleV2(ArticleWriteRequest request) {
        Article article = new Article(request.getTitle(), request.getContent(),request.getUser());
        articleRepository.save(article);
    }

    @PostConstruct
    public void init() {
        User defaultUser = userRepository.findById(1L).orElse(null);
        if (defaultUser == null) {
            defaultUser = new User("홍길동", "user1@example.com", "user1", "password123");
            userRepository.save(defaultUser);
        }

        articleRepository.save(new Article("InitializingBean implements afterPropertiesSet() 호출되지 않는 문제.",
                "A 에 의존성을 가지는 B라는 클래스가 있습니다.\n\n" +
                        "B라는 클래스는 InitializingBean 을 상속하고 afterPropertiesSet을 구현하고 있습니다. 서버가 가동되면서 bean들이 초기화되는 시점에 B라는 클래스의 afterPropertiesSet 메소드는\n\n" +
                        "A라는 클래스의 특정 메소드인 afunc()를 호출하고 있습니다.", defaultUser));

        articleRepository.save(new Article("두 번째 게시글", "내용 2", defaultUser));
    }

    public Optional<Article> findArticleById(Long articleId) {
       return articleRepository.findArticleById(articleId);
    }
}
