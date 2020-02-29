package com.pingsec.dev.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.pingsec.dev.web.rest.TestUtil;

public class UploadFileTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UploadFile.class);
        UploadFile uploadFile1 = new UploadFile();
        uploadFile1.setId("id1");
        UploadFile uploadFile2 = new UploadFile();
        uploadFile2.setId(uploadFile1.getId());
        assertThat(uploadFile1).isEqualTo(uploadFile2);
        uploadFile2.setId("id2");
        assertThat(uploadFile1).isNotEqualTo(uploadFile2);
        uploadFile1.setId(null);
        assertThat(uploadFile1).isNotEqualTo(uploadFile2);
    }
}
