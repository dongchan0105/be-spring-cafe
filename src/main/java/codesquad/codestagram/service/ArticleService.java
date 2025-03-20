package codesquad.codestagram.service;

import codesquad.codestagram.dto.ArticleDto;
import codesquad.codestagram.entity.Article;
import codesquad.codestagram.repository.ArticleRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;
    private List<Article> articles = new ArrayList<>();

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public List<Article> getArticlesV2(){
        return articleRepository.findAll().stream()
                .map(article -> new Article(article.getId(),article.getTitle(),article.getTitle()))
                .collect(Collectors.toList());
    }

    public void addArticle(Article article) {
        articles.add(article);
    }

    public void addArticleV2(ArticleDto request){
        articleRepository.save(new Article(request.getTitle(),request.getContent()));
    }

    @PostConstruct
    public void init(){
        articles.add(new Article(1,"InitializingBean implements afterPropertiesSet() 호출되지 않는 문제."
        ,"A 에 의존성을 가지는 B라는 클래스가 있습니다.\n" +
                "\n" +
                "B라는 클래스는 InitializingBean 을 상속하고 afterPropertiesSet을 구현하고 있습니다. 서버가 가동되면서 bean들이 초기화되는 시점에 B라는 클래스의 afterPropertiesSet 메소드는\n" +
                "\n" +
                "A라는 클래스의 특정 메소드인 afunc()를 호출하고 있습니다."));
        articles.add(new Article(2, "두 번째 게시글", "내용 2"));
    }


}
