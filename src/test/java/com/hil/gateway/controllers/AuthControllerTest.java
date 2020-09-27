package com.hil.gateway.controllers;

import static junit.framework.TestCase.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.hil.gateway.Application;
import com.hil.gateway.entity.dao.User;
import com.hil.gateway.repositories.UserRepository;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;


    private void tearDown() throws Exception {
        Optional<User> userOptional = userRepository.findByUsername("test93");
        userOptional.ifPresent(user -> userRepository.delete(user));
    }

    @Test
    public void registerTest() {

        String userInfo = "{\n" +
                "\t\"name\":\"test10\",\n" +
                "\t\"username\":\"test93\",\n" +
                "\t\"email\":\"test93@yahoo.com\",\n" +
                "\t\"roles\":[\"ADMIN\",\"USER\"],\n" +
                "\t\"password\":\"123456\"\n" +
                "}";

        try {
            this.mockMvc.perform(post("/api/auth/signup").contentType(MediaType.APPLICATION_JSON).content(userInfo))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("name").value("test10"))
                    .andExpect(jsonPath("username").value("test93"))
                    .andExpect(jsonPath("email").value("test93@yahoo.com"))
                    .andExpect(jsonPath("roles").isArray())
                    .andExpect(jsonPath("roles").isNotEmpty())
                    .andExpect(jsonPath("token").isNotEmpty());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void logInTest() throws Exception {
        String userLoginInfo = "{\n" +
                "\t\"username\": \"test93\",\n" +
                "\t\"password\":\"123456\"\n" +
                "}";
        try {
            this.mockMvc.perform(post("/api/auth/signin").contentType(MediaType.APPLICATION_JSON).content(userLoginInfo))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("name").value("test10"))
                    .andExpect(jsonPath("username").value("test93"))
                    .andExpect(jsonPath("email").value("test93@yahoo.com"))
                    .andExpect(jsonPath("roles").isArray())
                    .andExpect(jsonPath("roles").isNotEmpty())
                    .andExpect(jsonPath("token").isNotEmpty());

            tearDown();

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
