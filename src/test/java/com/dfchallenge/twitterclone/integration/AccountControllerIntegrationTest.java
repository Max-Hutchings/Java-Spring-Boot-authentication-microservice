package com.dfchallenge.twitterclone.integration;


import com.dfchallenge.twitterclone.dao.AccountRepository;
import com.dfchallenge.twitterclone.entity.Account;
import org.aspectj.lang.annotation.After;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(AccountControllerIntegrationTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    public void setup(){
        accountRepository.deleteAll();
    }

    @AfterEach
    public void cleanup(){
        accountRepository.deleteAll();
    }

    String ENDPOINT_URL = "/authentication/create-account";


    @Test
    @DisplayName("Integration Test for Account Creation")
    public void accountCreationIntegrationTest() throws Exception {

        String jsonRequest = """
        {
            "username": "best_username_ever",
            "fName": "Jason",
            "lName": "Borne",
            "email": "jason@gmail.com",
            "password": "PassWord233##!",
            "role": "USER"
        }
        """;


//        mockMvc.perform(post(ENDPOINT_URL)
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonRequest))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.username").value("best_username_ever"))
//                .andExpect(jsonPath("$.email").value("jason@gmail.com"))
//                .andExpect(jsonPath("$.id", Matchers.anything()))
//                .andExpect(jsonPath("$.fName").value("Jason"))
//                .andExpect(jsonPath("$.lName").value("Borne"))
//                .andExpect(jsonPath("$.authorities[0].authority").value("USER"));

        ResultActions resultActions = mockMvc.perform(post(ENDPOINT_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest));


        MockHttpServletResponse response = resultActions.andReturn().getResponse();
        System.out.println("Response: " + response.getContentAsString());


        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("best_username_ever"))
                .andExpect(jsonPath("$.email").value("jason@gmail.com"))
                .andExpect(jsonPath("$.id", Matchers.anything()))
                .andExpect(jsonPath("$.fName").value("Jason"))
                .andExpect(jsonPath("$.lName").value("Borne"))
                .andExpect(jsonPath("$.authorities[0].authority").value("USER"));

    }


}
