package com.engagetech.challenge.expenses.backend.dao;

import com.engagetech.challenge.expenses.backend.model.VatRate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"/VatRepositoryTests.sql"})
public class VatRepositoryTests {

    @Autowired
    private VatRepository vatRepository;

    @Test
    public void findFirstByOrderByFinishDateDesc() throws Exception {
        VatRate currentVat = vatRepository.findFirstByOrderByFinishDateDesc();

        assertNotNull(currentVat);
        assertEquals(Byte.valueOf("20"), currentVat.getRate());
        assertEquals(LocalDate.of(2011, 01, 04), currentVat.getStartDate());
        assertNull(currentVat.getFinishDate());
    }

}