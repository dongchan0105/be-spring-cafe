// 전역 변수
let ARTICLE_ID;
let currentPage = 0;
let totalComments = 0;
const PAGE_SIZE = 5;

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
    // ARTICLE_ID 초기화
    const loadMoreButton = document.getElementById('load-more-btn');
    if (loadMoreButton) {
        ARTICLE_ID = loadMoreButton.dataset.articleId;

        // 더보기 버튼 이벤트 등록
        loadMoreButton.addEventListener('click', loadComments);
    }

    // 댓글 작성 이벤트 등록
    const commentSubmitButton = document.getElementById('comment-submit-btn');
    if (commentSubmitButton) {
        commentSubmitButton.addEventListener('click', addComment);
    }

    // 초기 댓글 로드
    if (ARTICLE_ID) {
        loadComments();
    }
});

// 댓글 로딩 함수
async function loadComments() {
    try {
        const response = await fetch(`/qna/comments/${ARTICLE_ID}?page=${currentPage}`);
        if (!response.ok) throw new Error(`댓글 불러오기 실패: ${response.statusText}`);

        const { content, totalElements, last } = await response.json();
        content.forEach(comment => appendCommentToDOM(comment));

        totalComments = totalElements;
        currentPage++;
        updateCommentCount();
        updateLoadButton(last);

    } catch (error) {
        showError(error.message);
    }
}

// 댓글 추가 함수
async function addComment() {
    const commentInput = document.getElementById('comment-input');
    const content = commentInput.value.trim();

    if (!validateComment(content)) return;

    try {
        const response = await fetch(`/qna/comments/${ARTICLE_ID}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ comment: content })
        });

        if (!response.ok) throw new Error(`댓글 등록 실패: ${response.statusText}`);

        const newComment = await response.json();
        prependCommentToDOM(newComment);
        commentInput.value = '';
        totalComments++;
        updateCommentCount();

    } catch (error) {
        showError(error.message);
    }
}

// 댓글 삭제 함수
async function deleteComment(commentId) {
    if (!confirm('정말 삭제하시겠습니까?')) return;

    try {
        const response = await fetch(`/qna/comments/${commentId}`, { method: 'DELETE' });
        if (!response.ok) throw new Error('댓글 삭제 실패');

        document.getElementById(`comment-${commentId}`).remove();
        totalComments--;
        updateCommentCount();

    } catch (error) {
        showError(error.message);
    }
}

// DOM 조작 함수
function appendCommentToDOM(comment) {
    const commentHTML = createCommentHTML(comment);
    document.getElementById('comment-list').insertAdjacentHTML('beforeend', commentHTML);
}

function prependCommentToDOM(comment) {
    const commentHTML = createCommentHTML(comment);
    document.getElementById('comment-list').insertAdjacentHTML('afterbegin', commentHTML);
}

function createCommentHTML(comment) {
    return `
        <div class="comment-item" id="comment-${comment.id}">
            <div class="comment-header">
                <span class="comment-author">
                    <i class="bi bi-person-circle"></i>
                    ${comment.user}
                </span>
                <span class="comment-date">${comment.createdDate}</span>
            </div>
            <div class="comment-content">${comment.comment}</div>
            ${comment.isOwner ? `
            <div class="comment-actions">
                <button onclick="deleteComment(${comment.id})"
                        class="btn btn-sm btn-danger">
                    <i class="bi bi-trash"></i> 삭제
                </button>
            </div>` : ''}
        </div>
    `;
}

// 유틸리티 함수
function validateComment(content) {
    if (!content) {
        showError('댓글 내용을 입력해주세요.');
        return false;
    }
    if (content.length > 500) {
        showError('댓글은 500자를 초과할 수 없습니다.');
        return false;
    }
    return true;
}

function updateCommentCount() {
    const countElement = document.getElementById('comment-count');
    countElement.textContent = `(${totalComments})`;
}

function updateLoadButton(isLastPage) {
    const button = document.getElementById('load-more-btn');
    if (isLastPage) {
        button.disabled = true;
        button.innerHTML = '<i class="bi bi-check-circle"></i> 모든 댓글을 불러왔습니다';
    }
}

function showError(message) {
    alert(message);
}