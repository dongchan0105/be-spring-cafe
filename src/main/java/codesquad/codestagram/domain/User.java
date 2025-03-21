package codesquad.codestagram.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 자동 증가 ID

    @NotEmpty
    @Column(nullable = false, unique = true)
    private String loginId;

    @NotEmpty
    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotEmpty
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user" ,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Article> articles = new ArrayList<>();

    // 기본 생성자
    public User() {
    }

    // 생성자
    public User(String name, String email, String loginId, String password) {
        this.name = name;
        this.email = email;
        this.loginId = loginId;
        this.password = password;
    }

    public void addArticle(Article article) {
        this.articles.add(article);
        article.setUser(this);
    }

    // Getter & Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
