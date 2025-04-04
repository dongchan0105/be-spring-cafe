package codesquad.codestagram.service;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.request.ArticleUpdateRequest;
import codesquad.codestagram.dto.request.ArticleWriteRequest;
import codesquad.codestagram.repository.ArticleRepository;
import codesquad.codestagram.repository.UserRepositoryV2;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepositoryV2 userRepository;

    public ArticleService(ArticleRepository articleRepository, UserRepositoryV2 userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }


    /*public List<Article> getArticles() {
        return articleRepository.findAll();
    }*/

    public Page<Article> getArticles(int page) {
        return articleRepository.findAllActiveOrderByCreatedDateDesc(
                PageRequest.of(page - 1, 15, Sort.by("createdDate").descending())
        );
    }

    public Map<String, Object> getPaginationData(Page<Article> page) {
        int currentPage = page.getNumber() + 1;
        int totalPages = page.getTotalPages();
        int groupSize = 5;

        int startPage = ((currentPage - 1) / groupSize) * groupSize + 1;
        int endPage = Math.min(startPage + groupSize - 1, totalPages);

        return Map.of(
                "currentPage", currentPage,
                "startPage", startPage,
                "endPage", endPage,
                "totalPages", totalPages,
                "hasPrev", startPage > 1,
                "hasNext", endPage < totalPages
        );
    }


    @PostConstruct
    public void init() {
        User defaultUser = userRepository.findById(1L).orElse(null);
        if (defaultUser == null) {
            defaultUser = new User("홍길동", "user1@example.com", "user1", "password123");
            userRepository.save(defaultUser);
        }

        User administrator = new User("김동찬","codesquad@gmail.com","hello","world!");
        userRepository.save(administrator);

        // 초기 테스트 게시글 2개
        articleRepository.save(new Article("InitializingBean implements afterPropertiesSet() 호출되지 않는 문제.",
                "A 에 의존성을 가지는 B라는 클래스가 있습니다.\n\n" +
                        "B라는 클래스는 InitializingBean 을 상속하고 afterPropertiesSet을 구현하고 있습니다. 서버가 가동되면서 bean들이 초기화되는 시점에 B라는 클래스의 afterPropertiesSet 메소드는\n\n" +
                        "A라는 클래스의 특정 메소드인 afunc()를 호출하고 있습니다.", defaultUser));

        articleRepository.save(new Article("두 번째 게시글", "내용 2", administrator));

        // 추가 테스트 게시글 20개 생성
        for (int i = 3; i <= 22; i++) {
            Article article = new Article(
                    "테스트 게시글 " + i,
                    "이 게시글은 테스트용 샘플 컨텐츠입니다. (" + i + "번 게시글)\n" +
                            "Spring Boot 페이징 기능 개발을 위한 더미 데이터입니다.",
                    defaultUser
            );
            articleRepository.save(article);
        }
    }
    // 사용자를 지정해서 게시글 추가

    public void addArticle(ArticleWriteRequest request) {
        Article article = new Article(request.getTitle(), request.getContent(),request.getUser());
        articleRepository.save(article);
    }

    public Optional<Article> findArticleById(Long articleId) {
       return articleRepository.findArticleById(articleId);
    }

    public void updateArticleById(Long articleId, ArticleUpdateRequest request){

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. ID: " + articleId));

        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        //영속성 컨텍스트 생각해보자
    }

    public void deleteArticleById(Long articleId) {
        articleRepository.deleteById(articleId);
    }
}
