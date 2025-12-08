package com.anas.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void testUserGettersAndSetters() {
        User user = new User();

        user.setId(1L);
        user.setUsername("testUser");
        user.setEmail("test@email.com");
        user.setPassword("secret");
        user.setFirstName("John");
        user.setCountry("USA");

        assertEquals(1L, user.getId());
        assertEquals("testUser", user.getUsername());
        assertEquals("test@email.com", user.getEmail());
        assertEquals("secret", user.getPassword());
        assertEquals("John", user.getFirstName());
        assertEquals("USA", user.getCountry());
    }

    @Test
    void testToString() {
        // Just checking that toString doesn't crash
        User user = new User();
        user.setUsername("printMe");
        assertNotNull(user.toString());
    }
}