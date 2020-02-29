package com.pingsec.dev.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.pingsec.dev.web.rest.TestUtil;

public class DownloadFileDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DownloadFileDTO.class);
        DownloadFileDTO downloadFileDTO1 = new DownloadFileDTO();
        downloadFileDTO1.setId("id1");
        DownloadFileDTO downloadFileDTO2 = new DownloadFileDTO();
        assertThat(downloadFileDTO1).isNotEqualTo(downloadFileDTO2);
        downloadFileDTO2.setId(downloadFileDTO1.getId());
        assertThat(downloadFileDTO1).isEqualTo(downloadFileDTO2);
        downloadFileDTO2.setId("id2");
        assertThat(downloadFileDTO1).isNotEqualTo(downloadFileDTO2);
        downloadFileDTO1.setId(null);
        assertThat(downloadFileDTO1).isNotEqualTo(downloadFileDTO2);
    }
}
