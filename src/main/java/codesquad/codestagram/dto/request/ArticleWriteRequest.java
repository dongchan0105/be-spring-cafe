package codesquad.codestagram.dto.request;

import codesquad.codestagram.domain.User;

public class ArticleWriteRequest {

    private User user;
    private String title;
    private String content;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}
