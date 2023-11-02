package javasecurity.securityJWT.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void testUserCreation() {
        User user = User.builder()
                .id(1)
                .firstname("TestName")
                .lastname("TestLastName")
                .email("testEmail@test.com")
                .password("testPassword")
                .build();

        assertEquals(1, user.getId());
        assertEquals("TestName", user.getFirstname());
        assertEquals("TestLastName", user.getLastname());
        assertEquals("testEmail@test.com", user.getEmail());
        assertEquals("testPassword", user.getPassword());
    }

}
