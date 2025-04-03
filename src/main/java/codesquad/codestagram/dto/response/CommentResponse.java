package codesquad.codestagram.dto.response;

import codesquad.codestagram.domain.Comment;
import org.springframework.http.converter.json.GsonBuilderUtils;

import java.time.format.DateTimeFormatter;


public record CommentResponse(
        Long id,
        String comment,
        String user, // User 객체가 아닌 이름으로 변환
        String createdDate // 문자열로 변환된 날짜
) {

    public static CommentResponse from(Comment comment) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return new CommentResponse(
                comment.getId(),
                comment.getComment(),
                comment.getUser().getName(), // User에서 이름만 추출
                comment.getCreatedDate().format(formatter).toString() // 날짜를 문자열로 변환
        );
    }
}
