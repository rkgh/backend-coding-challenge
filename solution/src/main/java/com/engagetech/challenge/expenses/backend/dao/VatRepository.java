package com.engagetech.challenge.expenses.backend.dao;

import com.engagetech.challenge.expenses.backend.model.VatRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VatRepository extends JpaRepository<VatRate, Long>
{
    VatRate findFirstByOrderByFinishDateDesc();
}
