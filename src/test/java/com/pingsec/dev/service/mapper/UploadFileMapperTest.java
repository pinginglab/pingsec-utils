package com.pingsec.dev.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class UploadFileMapperTest {

    private UploadFileMapper uploadFileMapper;

    @BeforeEach
    public void setUp() {
        uploadFileMapper = new UploadFileMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        String id = "id2";
        assertThat(uploadFileMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(uploadFileMapper.fromId(null)).isNull();
    }
}
