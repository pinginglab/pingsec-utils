package com.pingsec.dev.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.pingsec.dev.web.rest.TestUtil;

public class UploadFileDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UploadFileDTO.class);
        UploadFileDTO uploadFileDTO1 = new UploadFileDTO();
        uploadFileDTO1.setId("id1");
        UploadFileDTO uploadFileDTO2 = new UploadFileDTO();
        assertThat(uploadFileDTO1).isNotEqualTo(uploadFileDTO2);
        uploadFileDTO2.setId(uploadFileDTO1.getId());
        assertThat(uploadFileDTO1).isEqualTo(uploadFileDTO2);
        uploadFileDTO2.setId("id2");
        assertThat(uploadFileDTO1).isNotEqualTo(uploadFileDTO2);
        uploadFileDTO1.setId(null);
        assertThat(uploadFileDTO1).isNotEqualTo(uploadFileDTO2);
    }
}
