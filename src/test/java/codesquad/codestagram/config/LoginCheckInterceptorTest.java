package codesquad.codestagram.config;

import codesquad.codestagram.interceptor.LoginCheckInterceptor;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginCheckInterceptorTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("로그인하지 않은 상태에서 보호된 경로에 접근하면 로그인 페이지로 리다이렉트되어야 함")
    void shouldRedirectToLoginWhenNotLoggedIn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/1/edit"))
                .andExpect(status().isFound()); // 리다이렉트로 인해 302 상태 코드가 반환됩니다.
    }

    @Test
    @DisplayName("로그인 경로에 대한 요청은 인터셉터가 동작하지 않아야 함")
    void shouldExcludeLoginPath() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("루트 경로에 대한 요청은 인터셉터가 동작하지 않아야 함")
    void shouldExcludeRootPath() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Q&A 경로에 대한 요청은 인터셉터가 동작하지 않아야 함")
    void shouldExcludeQnaPaths() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/qna"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/qna/articles/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("// 로그인한 상태에서 보호된 경로에 접근할 수 있어야 함")
    void shouldAllowAccessWhenLoggedIn() throws Exception {
        HttpSession session = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("username", "testUser")
                        .param("password", "testPassword"))
                .andExpect(status().isOk())
                .andReturn().getRequest().getSession();

        session.setAttribute(LoginCheckInterceptor.LOGIN_USER, "testUser");

        mockMvc.perform(MockMvcRequestBuilders.get("/users/form"))
                .andExpect(status().isOk());
    }
}


