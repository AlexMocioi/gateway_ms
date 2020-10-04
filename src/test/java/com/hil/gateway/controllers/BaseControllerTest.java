package com.hil.gateway.controllers;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.hil.gateway.Application;
import com.hil.gateway.entity.dao.User;
import com.hil.gateway.enums.RoleName;
import com.hil.gateway.repositories.UserRepository;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class BaseControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    protected String authHeader;

    protected String dataMSCall = "http://localhost:8080/gateway/data/";

    protected Map<String, JSONObject> dataMSRequests = new HashMap<>();

    @Before
    public void setAuthHeader() throws Exception {

        User user = new User();
        user.setName("Adam");
        user.setEmail("adam@hil.com");
        user.setUsername("adamhil");
        user.setPassword(passwordEncoder.encode("123456"));
        user.getRoles().add(RoleName.ADMIN);

        userRepository.save(user);
        if (authHeader == null) {
            authHeader = mockMvc.perform(post("/api/auth/signin")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"username\" : \"adamtgs\", \"password\" : \"123456\"}"))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();
            JSONObject jsonObject = new JSONObject(authHeader);
            String accessToken = jsonObject.get("token").toString();
            String tokenType = "Bearer";
            authHeader = tokenType + " " + accessToken;
        }
    }

    @Before
    public void dataMSBody() throws Exception {

        JSONObject orderJSON = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(getProduct("Coca Cola", "Juice", 3.33));
        jsonArray.put(getProduct("Fanta", "Juice", 3.33));

        orderJSON.put("address", "Calea Victoriei");
        orderJSON.put("userId", 2);
        orderJSON.put("products", jsonArray);

        dataMSRequests.put("createOrder", orderJSON);
        dataMSRequests.put("createProduct", getProduct("Coca Cola", "Juice", 3.33));

        JSONObject orderUpdate = getOrder(2L, "Veronica Micle", 2L);
        orderUpdate.put("products", jsonArray);
        dataMSRequests.put("updateOrder", orderUpdate);

        JSONObject productUpdate = getUpdateProduct(2L, "Pepsi", "Juice", 2.22);
        dataMSRequests.put("updateProduct", productUpdate);
    }

    private JSONObject getProduct(String name, String description, Double price) throws Exception {

        JSONObject product = new JSONObject();
        product.put("name", name);
        product.put("description", description);
        product.put("price", price);
        return product;
    }

    private JSONObject getOrder(Long id, String address, Long userId) throws Exception {

        JSONObject order = new JSONObject();
        order.put("id", id);
        order.put("address", address);
        order.put("userId", userId);

        return order;
    }

    private JSONObject getUpdateProduct(Long id, String name, String description, Double price) throws Exception {

        JSONObject product = new JSONObject();
        product.put("id", id);
        product.put("name", name);
        product.put("description", description);
        product.put("price", price);
        return product;
    }
}
