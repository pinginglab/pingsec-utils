package com.pingsec.dev.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class DownloadFileMapperTest {

    private DownloadFileMapper downloadFileMapper;

    @BeforeEach
    public void setUp() {
        downloadFileMapper = new DownloadFileMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        String id = "id2";
        assertThat(downloadFileMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(downloadFileMapper.fromId(null)).isNull();
    }
}
