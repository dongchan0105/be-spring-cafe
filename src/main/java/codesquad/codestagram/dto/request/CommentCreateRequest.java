package codesquad.codestagram.dto.request;

import jakarta.validation.constraints.NotBlank;

// 댓글 생성 요청 DTO
public record CommentCreateRequest(
        @NotBlank(message = "댓글 내용을 입력해주세요.")
        String comment // 필드명을 'comment'로 사용합니다.
) {}