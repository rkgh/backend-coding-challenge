package com.engagetech.challenge.expenses.backend.controller;

import com.engagetech.challenge.expenses.backend.model.ModelTestFactory;
import com.engagetech.challenge.expenses.backend.model.VatRate;
import com.engagetech.challenge.expenses.backend.service.VatService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(VatRestController.class)
public class VatRestControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private VatService vatService;


    @Test
    public void getCurrentVatRate() throws Exception {
        VatRate vatRate = ModelTestFactory.defaultVatRate();
        given(vatService.getCurrentVat()).willReturn(vatRate);

        mvc.perform(get("/app/vat")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("20"));
    }

}