// 댓글 등록 처리 함수
function addComment(articleId) {
    const commentInput = document.getElementById('comment-input');
    const commentValue = commentInput.value.trim();

    if (!commentValue) {
        alert('댓글 내용을 입력하세요.');
        return;
    }

    const data = { comment: commentValue };

    fetch(`/qna/comments/${articleId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error('댓글 등록 실패: ' + text);
                });
            }
            return response.json();
        })
        .then(comment => {
            appendCommentToDOM(comment);
            commentInput.value = ''; // 입력 필드 초기화
        })
        .catch(error => alert(error.message));
}

function deleteComment(commentId) {
    fetch(`/qna/comments/${commentId}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error('댓글 삭제 실패: ' + text);
                });
            }
            // 성공 시 DOM에서 댓글 제거
            document.getElementById(`comment-${commentId}`).remove();
        })
        .catch(error => alert(error.message));
}


// DOM에 새 댓글 추가하는 함수
function appendCommentToDOM(comment) {
    const commentHtml = `
        <div class="comment-item" id="comment-${comment.id}">
            <div class="comment-header">
                <span class="comment-author">${comment.user.name || comment.user}</span>
                <span class="comment-date">${comment.createdDate}</span>
            </div>
            <div class="comment-content">${comment.comment}</div>
            <button data-comment-id="${comment.id}" onclick="deleteComment(this.dataset.commentId)" class="btn btn-sm btn-outline-danger">삭제</button>
        </div>`;
    document.getElementById('comment-list').insertAdjacentHTML('beforeend', commentHtml);
}