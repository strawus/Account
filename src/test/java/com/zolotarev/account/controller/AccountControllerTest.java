package com.zolotarev.account.controller;

import com.zolotarev.account.mapper.AccountMapper;
import com.zolotarev.account.domain.Account;
import com.zolotarev.account.domain.Currency;
import com.zolotarev.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test suite for {@link AccountController}
 */
@TestExecutionListeners(MockitoTestExecutionListener.class)
@WebMvcTest(value = AccountController.class, includeFilters = @Filter(value = AccountMapper.class, type = ASSIGNABLE_TYPE))
public class AccountControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountService accountService;

    @BeforeClass
    public void setUp() {
        final Account testAccount = new Account(1, BigDecimal.TEN, Currency.RUB);
        when(accountService.getAll()).thenReturn(Arrays.asList(testAccount));
        when(accountService.getById(anyInt())).thenReturn(testAccount);
        when(accountService.create(any())).then(mock -> mock.getArgument(0));
    }

    /**
     * Checking calling GET /api/accounts/{accountId} on produced and consumed content type, headers, status code and returned value
     */
    @Test
    public void getAccountTest() throws Exception {
        mockMvc.perform(get("/api/accounts/{accountId}", 1)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(1));
    }

    /**
     * Checking calling POST /api/accounts/ on produced and consumed content type, headers, status code and returned value
     */
    @Test
    public void createAccountTest() throws Exception {
        final Integer id = 2;
        final String amount = "100.0";
        final String currency = "RUB";
        mockMvc.perform(post("/api/accounts/")
                .content("{\"id\":" + id + ",\"amount\":" + amount + ",\"currencyCode\":\"" + currency + "\"}")
                .contentType(APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.amount").value(amount))
                .andExpect(jsonPath("$.currencyCode").value(currency));
    }

    /**
     * Checking calling DELETE /api/accounts/{accountId} on produced and consumed content type, headers and status code
     */
    @Test
    public void deleteAccountTest() throws Exception {
        mockMvc.perform(delete("/api/accounts/{accountId}", 1)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    /**
     * Checking calling GET /api/accounts/ on produced and consumed content type, headers, status code and returned values
     */
    @Test
    public void getAllAccountsTest() throws Exception {
        mockMvc.perform(get("/api/accounts/")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").value(hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1));
    }
}
