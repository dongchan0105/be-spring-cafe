package codesquad.codestagram.config;

import codesquad.codestagram.interceptor.LoginCheckInterceptor;
import jakarta.servlet.http.HttpSession;
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
    void shouldRedirectToLoginWhenNotLoggedIn() throws Exception {
        // 로그인하지 않은 상태에서 보호된 경로에 접근하면 로그인 페이지로 리다이렉트되어야 함
        mockMvc.perform(MockMvcRequestBuilders.get("/users/form"))
                .andExpect(status().isFound()); // 리다이렉트로 인해 302 상태 코드가 반환됩니다.
    }

    @Test
    void shouldExcludeLoginPath() throws Exception {
        // 로그인 경로에 대한 요청은 인터셉터가 동작하지 않아야 함
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldExcludeRootPath() throws Exception {
        // 루트 경로에 대한 요청은 인터셉터가 동작하지 않아야 함
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldExcludeQnaPaths() throws Exception {
        // Q&A 경로에 대한 요청은 인터셉터가 동작하지 않아야 함
        mockMvc.perform(MockMvcRequestBuilders.get("/qna"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/qna/articles/1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAllowAccessWhenLoggedIn() throws Exception {
        // 로그인한 상태에서 보호된 경로에 접근할 수 있어야 함
        HttpSession session = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("username", "testUser")
                        .param("password", "testPassword"))
                .andExpect(status().isFound())
                .andReturn().getRequest().getSession();

        session.setAttribute(LoginCheckInterceptor.LOGIN_MEMBER, "testUser");

        mockMvc.perform(MockMvcRequestBuilders.get("/users/form"))
                .andExpect(status().isOk());
    }
}


