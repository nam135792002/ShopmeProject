package com.shopme.admin.report;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReportRestControllerTests {
    @Autowired private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user1",password = "pass1",authorities = {"Salesperson"})
    public void testGetReportDateLast7Days() throws Exception {
        String requestURL = "/reports/sales_by_date/last_7_days";

        mockMvc.perform(get(requestURL)).andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithMockUser(username = "user1",password = "pass1",authorities = {"Salesperson"})
    public void testGetReportDateLast6Months() throws Exception {
        String requestURL = "/reports/sales_by_date/last_6_months";

        mockMvc.perform(get(requestURL)).andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithMockUser(username = "user1",password = "pass1",authorities = {"Salesperson"})
    public void testGetReportDateByDateRange() throws Exception {
        String startDate = "2023-10-21";
        String endDate = "2023-10-27";
        String requestURL = "/reports/sales_by_date/" + startDate + "/" + endDate;

        mockMvc.perform(get(requestURL)).andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithMockUser(username = "user1",password = "pass1",authorities = {"Salesperson"})
    public void testGetReportDateByCategory() throws Exception {
        String requestURL = "/reports/category/last_7_days";

        mockMvc.perform(get(requestURL)).andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithMockUser(username = "user1",password = "pass1",authorities = {"Salesperson"})
    public void testGetReportDateByProduct() throws Exception {
        String requestURL = "/reports/product/last_7_days";

        mockMvc.perform(get(requestURL)).andExpect(status().isOk()).andDo(print());
    }
}
