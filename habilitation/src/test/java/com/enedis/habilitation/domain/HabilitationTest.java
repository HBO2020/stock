package com.enedis.habilitation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.enedis.habilitation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HabilitationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Habilitation.class);
        Habilitation habilitation1 = new Habilitation();
        habilitation1.setId(1L);
        Habilitation habilitation2 = new Habilitation();
        habilitation2.setId(habilitation1.getId());
        assertThat(habilitation1).isEqualTo(habilitation2);
        habilitation2.setId(2L);
        assertThat(habilitation1).isNotEqualTo(habilitation2);
        habilitation1.setId(null);
        assertThat(habilitation1).isNotEqualTo(habilitation2);
    }
}
