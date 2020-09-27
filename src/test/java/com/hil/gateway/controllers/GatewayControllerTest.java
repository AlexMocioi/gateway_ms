package com.hil.gateway.controllers;


import static junit.framework.TestCase.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.hil.gateway.Application;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class GatewayControllerTest extends com.hil.gateway.controllers.BaseControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    public void testGetRoute() throws Exception {

        //Test to see if we get all orders
        String responseOrderList = mvc.perform(get(dataMSCall + "order/all").contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authHeader))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertTrue(!responseOrderList.isEmpty());
        System.out.println("All orders = " + responseOrderList);

        //Test to see if we get all orders by userId
        String responseOrderListByUserId = mvc.perform(get(dataMSCall + "order/getByUserId/2").contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authHeader))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertTrue(!responseOrderListByUserId.isEmpty());
        System.out.println("List searched by User ID = " + responseOrderListByUserId);

        //Test to see if we get all products
        String responseProductList = mvc.perform(get(dataMSCall + "product/all").contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authHeader))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertTrue(!responseProductList.isEmpty());
        System.out.println("All Products = " + responseProductList);

        userRepository.deleteAll();
    }


    @Test
    public void testPostAndDeleteRoute() throws Exception {

        //Testing Order Creation

        String responseOrder = mvc.perform(post(dataMSCall + "order/create").contentType(MediaType.APPLICATION_JSON).content(dataMSRequests.get("createOrder").toString())
                .header(HttpHeaders.AUTHORIZATION, authHeader))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONObject jsonObjectResponseOrder = new JSONObject(responseOrder);

        String deletedOrder = mvc.perform(delete(dataMSCall + "order/" + jsonObjectResponseOrder.get("id"))
                .header(HttpHeaders.AUTHORIZATION, authHeader))
                .andReturn().getResponse().getContentAsString();

        jsonObjectResponseOrder.remove("id");
        jsonObjectResponseOrder.getJSONArray("products").getJSONObject(0).remove("id");
        jsonObjectResponseOrder.getJSONArray("products").getJSONObject(1).remove("id");


        assertTrue(jsonObjectResponseOrder.toString().length() == (dataMSRequests.get("createOrder").toString().length()));
        assertTrue(deletedOrder.equals("true"));

        //Testing Product Creation

        String responseProduct = mvc.perform(post(dataMSCall + "product/create").contentType(MediaType.APPLICATION_JSON).content(dataMSRequests.get("createProduct").toString())
                .header(HttpHeaders.AUTHORIZATION, authHeader))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONObject jsonObjectResponseProduct = new JSONObject(responseProduct);

        String deletedProduct = mvc.perform(delete(dataMSCall + "product/" + jsonObjectResponseProduct.get("id"))
                .header(HttpHeaders.AUTHORIZATION, authHeader))
                .andReturn().getResponse().getContentAsString();

        jsonObjectResponseProduct.remove("id");

        assertTrue(jsonObjectResponseProduct.toString().equals(dataMSRequests.get("createProduct").toString()));
        assertTrue(deletedProduct.equals("true"));

        userRepository.deleteAll();
    }

    @Test
    public void testPutRoute() throws Exception {

        //Test to update an order
        String responseOrder = mvc.perform(put(dataMSCall + "order").contentType(MediaType.APPLICATION_JSON)
                .content(dataMSRequests.get("updateOrder").toString())
                .header(HttpHeaders.AUTHORIZATION, authHeader))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //The update will add the new order details and will add the product list that we giving
        //to the already list that order has in DB
        assertTrue(!responseOrder.isEmpty());
        System.out.println(responseOrder);


        //Test to update a product
        String responseProduct = mvc.perform(put(dataMSCall + "product").contentType(MediaType.APPLICATION_JSON)
                .content(dataMSRequests.get("updateProduct").toString())
                .header(HttpHeaders.AUTHORIZATION, authHeader))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertTrue(!responseOrder.isEmpty());
        System.out.println(responseProduct);

        userRepository.deleteAll();
    }
}
