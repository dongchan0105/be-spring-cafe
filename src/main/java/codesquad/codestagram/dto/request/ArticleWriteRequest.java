package codesquad.codestagram.dto.request;

import codesquad.codestagram.domain.User;

public class ArticleWriteRequest {

    private User user;
    private String title;
    private String content;

    public User getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
