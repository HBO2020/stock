package com.enedis.habilitation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.enedis.habilitation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RattachementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rattachement.class);
        Rattachement rattachement1 = new Rattachement();
        rattachement1.setId(1L);
        Rattachement rattachement2 = new Rattachement();
        rattachement2.setId(rattachement1.getId());
        assertThat(rattachement1).isEqualTo(rattachement2);
        rattachement2.setId(2L);
        assertThat(rattachement1).isNotEqualTo(rattachement2);
        rattachement1.setId(null);
        assertThat(rattachement1).isNotEqualTo(rattachement2);
    }
}
