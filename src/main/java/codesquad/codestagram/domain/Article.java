package codesquad.codestagram.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


import java.util.List;

@Entity
@Table(name = "Articles")
@SQLDelete(sql = "UPDATE articles SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        // 게시글 id

    @Column(nullable = false)
    private String title;  // 게시글 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 게시글 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "article", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id asc") // 댓글 정렬
    private List<Comment> comments;

    @Column(nullable = false)
    private Boolean deleted = false; // Soft Delete 여부

    protected Article() {
    }

    // 생성자
    public Article(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.deleted = false; // 기본값 설정
    }

    // Getter & Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
