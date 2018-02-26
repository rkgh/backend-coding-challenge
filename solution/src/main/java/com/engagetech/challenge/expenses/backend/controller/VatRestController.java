package com.engagetech.challenge.expenses.backend.controller;

import com.engagetech.challenge.expenses.backend.service.VatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/vat")
@CrossOrigin(origins = "http://localhost:8080")
public class VatRestController {

    @Autowired
    private VatService vatService;

    @GetMapping
    public Byte getCurrentVatRate() {
        return vatService.getCurrentVat().getRate();
    }
}
