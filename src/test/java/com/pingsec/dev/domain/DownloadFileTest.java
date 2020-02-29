package com.pingsec.dev.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.pingsec.dev.web.rest.TestUtil;

public class DownloadFileTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DownloadFile.class);
        DownloadFile downloadFile1 = new DownloadFile();
        downloadFile1.setId("id1");
        DownloadFile downloadFile2 = new DownloadFile();
        downloadFile2.setId(downloadFile1.getId());
        assertThat(downloadFile1).isEqualTo(downloadFile2);
        downloadFile2.setId("id2");
        assertThat(downloadFile1).isNotEqualTo(downloadFile2);
        downloadFile1.setId(null);
        assertThat(downloadFile1).isNotEqualTo(downloadFile2);
    }
}
