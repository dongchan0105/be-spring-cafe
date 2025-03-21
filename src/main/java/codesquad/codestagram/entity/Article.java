package codesquad.codestagram.entity;

import jakarta.persistence.*;

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;        // 게시글 id

    @Column(nullable = false)
    private String title;  // 게시글 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 게시글 내용

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // 외래 키 설정
    private User user;  // 작성자 (기존 코드 유지)

    protected Article() {
    }

    // 생성자
    public Article(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    // Getter & Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
