package com.zolotarev.account.controller;

import com.zolotarev.account.mapper.AccountMapper;
import com.zolotarev.account.domain.Account;
import com.zolotarev.account.service.ManageFundsService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test suite for {@link ManageFundsController}
 */
@TestExecutionListeners(MockitoTestExecutionListener.class)
@WebMvcTest(value = ManageFundsController.class, includeFilters = @Filter(value = AccountMapper.class, type = ASSIGNABLE_TYPE))
public class ManageFundsControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ManageFundsService service;

    @BeforeClass
    public void setUp() {
        when(service.deposit(anyInt(), any(), any())).then(mock -> new Account(mock.getArgument(0), mock.getArgument(1), mock.getArgument(2)));
        when(service.withdraw(anyInt(), any(), any())).then(mock -> new Account(mock.getArgument(0), mock.getArgument(1), mock.getArgument(2)));
        when(service.transfer(anyInt(), anyInt(), any(), any())).then(mock -> new Account(mock.getArgument(0), mock.getArgument(2), mock.getArgument(3)));
    }

    /**
     * Checking calling POST /api/deposits/ on produced and consumed content type, headers, status code and returned value
     */
    @Test
    public void depositTest() throws Exception {
        final Integer id = 1;
        final String amount = "100.0";
        final String currency = "RUB";
        mockMvc.perform(post("/api/deposits/")
                .content("{\"accountId\":\"" + id + "\",\"amount\":\"" + amount + "\",\"currencyCode\":\"" + currency + "\"}")
                .contentType(APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.amount").value(amount))
                .andExpect(jsonPath("$.currencyCode").value(currency));
    }

    /**
     * Checking calling POST /api/withdraws/ on produced and consumed content type, headers, status code and returned value
     */
    @Test
    public void withdrawTest() throws Exception {
        final Integer id = 1;
        final String amount = "100.0";
        final String currency = "RUB";
        mockMvc.perform(post("/api/withdraws/")
                .content("{\"accountId\":\"" + id + "\",\"amount\":\"" + amount + "\",\"currencyCode\":\"" + currency + "\"}")
                .contentType(APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.amount").value(amount))
                .andExpect(jsonPath("$.currencyCode").value(currency));
    }

    /**
     * Checking calling POST /api/transfers/ on produced and consumed content type, headers, status code and returned value
     */
    @Test
    public void transferTest() throws Exception {
        final Integer source = 1;
        final Integer dest = 2;
        final String amount = "100.0";
        final String currency = "RUB";
        mockMvc.perform(post("/api/transfers/")
                .content("{\"sourceAccountId\":" + source + ",\"targetAccountId\":" + dest + ",\"amount\":" + amount + ",\"currencyCode\":\"" + currency + "\"}")
                .contentType(APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(source))
                .andExpect(jsonPath("$.amount").value(amount))
                .andExpect(jsonPath("$.currencyCode").value(currency));
    }
}
