package com.kyrylomalyi.shuttertrackapi.web;

import com.kyrylomalyi.shuttertrackapi.dto.AnalyticsResponseDTO;
import com.kyrylomalyi.shuttertrackapi.service.AnalyticService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnalyticsController.class)
class AnalyticsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private AnalyticService analyticService;

    @Test
    @WithMockUser(username = "test-user", roles = {"USER"})
    void shouldReturnAnalytics() throws Exception {

        // given
        AnalyticsResponseDTO response = AnalyticsResponseDTO.builder()
                .totalUploads(3L)
                .averageShutterCount(200L)
                .mostFrequentCamera("Canon R8")
                .build();

        when(analyticService.getAnalysis()).thenReturn(response);

        // when + then
        mockMvc.perform(get("/analytics"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalUploads").value(3))
                .andExpect(jsonPath("$.averageShutterCount").value(200))
                .andExpect(jsonPath("$.mostFrequentCamera").value("Canon R8"));

    }


}