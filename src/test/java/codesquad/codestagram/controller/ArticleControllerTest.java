package codesquad.codestagram.controller;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.service.ArticleService;
import codesquad.codestagram.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ArticleController.class)
public class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;

    @MockBean
    private UserService userService;

    @Test
    void viewQuestions() throws Exception {
        mockMvc.perform(get("/qna"))
                .andExpect(status().isOk());
    }

    @Test
    void viewArticle() throws Exception {
        when(articleService.findArticleById(any())).thenReturn(java.util.Optional.of(new Article(A,A,A)));
        mockMvc.perform(get("/qna/articles/1"))
                .andExpect(status().isOk());
    }

    @Test
    void showForm() throws Exception {
        mockMvc.perform(get("/qna/form"))
                .andExpect(status().isOk());
    }
}
