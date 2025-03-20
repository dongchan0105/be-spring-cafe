package codesquad.codestagram.dto;

public class ArticleDto {

    private String title;  // 게시글 제목
    private String content; // 게시글 내용

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
