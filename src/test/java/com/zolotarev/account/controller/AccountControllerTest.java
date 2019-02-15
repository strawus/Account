package com.zolotarev.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zolotarev.account.controller.dto.AccountDto;
import com.zolotarev.account.domain.Account;
import com.zolotarev.account.mapper.AccountMapper;
import com.zolotarev.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static com.zolotarev.account.domain.Currency.RUB;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for {@link AccountController}
 */
@TestExecutionListeners(MockitoTestExecutionListener.class)
@WebMvcTest(value = AccountController.class, includeFilters = @Filter(type = ASSIGNABLE_TYPE, classes = AccountMapper.class))
public class AccountControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private AccountMapper accountMapper;
    @MockBean
    private AccountService accountService;

    @DataProvider
    public static Object[][] testAccounts() {
        return new Object[][]{{new AccountDto(1, BigDecimal.valueOf(1000L, 2), RUB.name(), 0)}};
    }

    /**
     * Checking calling GET /api/accounts/{accountId} on produced and consumed content type, headers, status code and returned value
     */
    @Test(dataProvider = "testAccounts")
    public void getAccountTest(AccountDto testAccountDto) throws Exception {
        when(accountService.getById(testAccountDto.getId())).thenReturn(accountMapper.toEntity(testAccountDto));

        mockMvc.perform(get("/api/accounts/{accountId}", testAccountDto.getId())
                .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().json(mapper.writeValueAsString(testAccountDto)));
    }

    /**
     * Checking calling POST /api/accounts/ on produced and consumed content type, headers, status code and returned value
     */
    @Test(dataProvider = "testAccounts")
    public void createAccountTest(AccountDto testAccountDto) throws Exception {
        final AccountDto testAccountDtoToCreate = testAccountDto.withId(null).withVersion(null);
        when(accountService.create(accountMapper.toEntity(testAccountDtoToCreate))).thenReturn(accountMapper.toEntity(testAccountDto));

        mockMvc.perform(post("/api/accounts/")
                .content(mapper.writeValueAsString(testAccountDtoToCreate))
                .contentType(APPLICATION_JSON_UTF8)
                .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().json(mapper.writeValueAsString(testAccountDto)));
    }

    /**
     * Checking calling PUT /api/accounts/ on produced and consumed content type, headers, status code and returned value
     */
    @Test(dataProvider = "testAccounts")
    public void updateAccountTest(AccountDto testAccountDto) throws Exception {
        final Account account = accountMapper.toEntity(testAccountDto);
        when(accountService.update(account)).thenReturn(account);

        mockMvc.perform(put("/api/accounts/")
                .content(mapper.writeValueAsString(testAccountDto))
                .contentType(APPLICATION_JSON_UTF8)
                .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().json(mapper.writeValueAsString(testAccountDto)));
    }

    /**
     * Checking calling DELETE /api/accounts/{accountId} on produced and consumed content type, headers and status code
     */
    @Test(dataProvider = "testAccounts")
    public void deleteAccountTest(AccountDto testAccountDto) throws Exception {
        doNothing().when(accountService).delete(testAccountDto.getId());

        mockMvc.perform(delete("/api/accounts/{accountId}", testAccountDto.getId())
                .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    /**
     * Checking calling GET /api/accounts/ on produced and consumed content type, headers, status code and returned values
     */
    @Test(dataProvider = "testAccounts")
    public void getAllAccountsTest(AccountDto testAccountDto) throws Exception {
        when(accountService.getAll()).thenReturn(Arrays.asList(accountMapper.toEntity(testAccountDto)));

        mockMvc.perform(get("/api/accounts/")
                .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().json(mapper.writeValueAsString(Arrays.asList(testAccountDto))));
    }
}
