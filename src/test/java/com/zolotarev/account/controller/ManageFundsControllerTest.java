package com.zolotarev.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zolotarev.account.controller.dto.AccountDto;
import com.zolotarev.account.controller.dto.ManageFundsDto;
import com.zolotarev.account.controller.dto.TransferDto;
import com.zolotarev.account.domain.Currency;
import com.zolotarev.account.mapper.AccountMapper;
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
import org.testng.annotations.Test;

import static com.zolotarev.account.domain.Currency.EUR;
import static java.math.BigDecimal.TEN;
import static org.mockito.Mockito.when;
import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for {@link ManageFundsController}
 */
@TestExecutionListeners(MockitoTestExecutionListener.class)
@WebMvcTest(value = ManageFundsController.class, includeFilters = @Filter(type = ASSIGNABLE_TYPE, value = AccountMapper.class))
public class ManageFundsControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private AccountMapper accountMapper;
    @MockBean
    private ManageFundsService service;

    private final ManageFundsDto testManageFundsDto = new ManageFundsDto(1, TEN, EUR.name());
    private final TransferDto testTransferDto = new TransferDto(1, 2, TEN, EUR.name());
    private final AccountDto testAccountDto = new AccountDto(1, TEN, EUR.name(), 0);

    /**
     * Checking calling POST /api/deposits/ on produced and consumed content type, headers, status code and returned value
     */
    @Test
    public void depositTest() throws Exception {
        when(service.deposit(testManageFundsDto.getAccountId(), testManageFundsDto.getAmount(), Currency.parse(testManageFundsDto.getCurrencyCode())))
                .thenReturn(accountMapper.toEntity(testAccountDto));

        mockMvc.perform(post("/api/deposits/")
                .content(mapper.writeValueAsString(testManageFundsDto))
                .contentType(APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().json(mapper.writeValueAsString(testAccountDto)));
    }

    /**
     * Checking calling POST /api/withdraws/ on produced and consumed content type, headers, status code and returned value
     */
    @Test
    public void withdrawTest() throws Exception {
        when(service.withdraw(testManageFundsDto.getAccountId(), testManageFundsDto.getAmount(), Currency.parse(testManageFundsDto.getCurrencyCode())))
                .thenReturn(accountMapper.toEntity(testAccountDto));

        mockMvc.perform(post("/api/withdraws/")
                .content(mapper.writeValueAsString(testManageFundsDto))
                .contentType(APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().json(mapper.writeValueAsString(testAccountDto)));
    }

    /**
     * Checking calling POST /api/transfers/ on produced and consumed content type, headers, status code and returned value
     */
    @Test
    public void transferTest() throws Exception {
        when(service.transfer(testTransferDto.getSourceAccountId(), testTransferDto.getTargetAccountId(), testTransferDto.getAmount(), Currency.parse(testTransferDto.getCurrencyCode())))
                .thenReturn(accountMapper.toEntity(testAccountDto));

        mockMvc.perform(post("/api/transfers/")
                .content(mapper.writeValueAsString(testTransferDto))
                .contentType(APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().json(mapper.writeValueAsString(testAccountDto)));
    }
}
