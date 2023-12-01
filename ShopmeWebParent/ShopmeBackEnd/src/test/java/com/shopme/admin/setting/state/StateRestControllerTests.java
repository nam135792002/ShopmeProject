package com.shopme.admin.setting.state;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StateRestControllerTests {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @Autowired private CountryRepository countryRepository;

    @Autowired private StateRepository stateRepository;

    @Test
    @WithMockUser(username = "phuongnama32112002@gmail.com",password = "123456789",roles = "ADMIN")
    public void testListByCountries() throws Exception {
        Integer countryId = 2;
        String url = "/states/list_by_country/" + countryId;

        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        State[] states = objectMapper.readValue(jsonResponse,State[].class);
        Assertions.assertThat(states).hasSizeGreaterThan(1);
    }

    @Test
    @WithMockUser(username = "phuongnama32112002@gmail.com",password = "123456789",roles = "ADMIN")
    public void testCreateState() throws Exception {
        String url = "/states/save";
        Integer countryId = 2;
        Country country = countryRepository.findById(countryId).get();
        State state = new State("Vinh Long",country);

        MvcResult result = mockMvc.perform(get(url).contentType("application/json")
                .content(objectMapper.writeValueAsString(state))
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Integer stateId = Integer.parseInt(response);
        Optional<State> findById = stateRepository.findById(stateId);
        Assertions.assertThat(findById.isPresent());
    }

    @Test
    @WithMockUser(username = "phuongnama32112002@gmail.com",password = "123456789",roles = "ADMIN")
    public void testUpdateState() throws Exception {
        String url = "/states/save";
        Integer stateId = 11;
        String stateName = "Tra Vinh";

        State state = stateRepository.findById(stateId).get();
        state.setName(stateName);

        mockMvc.perform(get(url).contentType("application/json")
                .content(objectMapper.writeValueAsString(state))
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(stateId)));

        Optional<State> findById = stateRepository.findById(stateId);
        Assertions.assertThat(findById.isPresent());

        State updatedState = findById.get();
        Assertions.assertThat(updatedState.getName()).isEqualTo(stateName);
    }

    @Test
    @WithMockUser(username = "phuongnama32112002@gmail.com",password = "123456789",roles = "ADMIN")
    public void testDeleteState() throws Exception {
        Integer stateId = 11;
        String url = "/states/delete/" + stateId;
        mockMvc.perform(get(url)).andExpect(status().isOk());

        Optional<State> findById = stateRepository.findById(stateId);
        Assertions.assertThat(findById).isNotPresent();
    }
}

