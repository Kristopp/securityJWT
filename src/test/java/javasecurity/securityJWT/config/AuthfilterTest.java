package javasecurity.securityJWT.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthfilterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @BeforeEach
    public void setUp() {
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("user", "password"));
    }

    @AfterEach
    public void cleanUp() {
        SecurityContextHolder.clearContext();
    }

    private String generateTestToken() {
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn("test@example.com");

        // Assuming you have a real JwtService bean in your test context
        return jwtService.generateToken(mockUserDetails);
    }

    @Test
    public void testInvalidAuthorizationHeader() throws Exception {
        mockMvc.perform(get("/dummyUrl")).andExpect(status().isForbidden());
    }

    @Test
    public void testValidAuthorizationHeader() throws Exception {
        String token = "Bearer " + generateTestToken();

        mockMvc.perform(get("/dummyUrl").header("Authorization", token))
                .andExpect(status().isOk());
    }

}
