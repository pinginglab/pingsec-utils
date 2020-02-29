package com.pingsec.dev.web.rest;

import com.pingsec.dev.UtilsApp;
import com.pingsec.dev.config.SecurityBeanOverrideConfiguration;
import com.pingsec.dev.domain.UploadFile;
import com.pingsec.dev.repository.UploadFileRepository;
import com.pingsec.dev.service.UploadFileService;
import com.pingsec.dev.service.dto.UploadFileDTO;
import com.pingsec.dev.service.mapper.UploadFileMapper;
import com.pingsec.dev.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;


import java.util.List;

import static com.pingsec.dev.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link UploadFileResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, UtilsApp.class})
public class UploadFileResourceIT {

    private static final String DEFAULT_UPLOAD_TIME = "AAAAAAAAAA";
    private static final String UPDATED_UPLOAD_TIME = "BBBBBBBBBB";

    @Autowired
    private UploadFileRepository uploadFileRepository;

    @Autowired
    private UploadFileMapper uploadFileMapper;

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restUploadFileMockMvc;

    private UploadFile uploadFile;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UploadFileResource uploadFileResource = new UploadFileResource(uploadFileService);
        this.restUploadFileMockMvc = MockMvcBuilders.standaloneSetup(uploadFileResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UploadFile createEntity() {
        UploadFile uploadFile = new UploadFile()
            .uploadTime(DEFAULT_UPLOAD_TIME);
        return uploadFile;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UploadFile createUpdatedEntity() {
        UploadFile uploadFile = new UploadFile()
            .uploadTime(UPDATED_UPLOAD_TIME);
        return uploadFile;
    }

    @BeforeEach
    public void initTest() {
        uploadFileRepository.deleteAll();
        uploadFile = createEntity();
    }

    @Test
    public void createUploadFile() throws Exception {
        int databaseSizeBeforeCreate = uploadFileRepository.findAll().size();

        // Create the UploadFile
        UploadFileDTO uploadFileDTO = uploadFileMapper.toDto(uploadFile);
        restUploadFileMockMvc.perform(post("/api/upload-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(uploadFileDTO)))
            .andExpect(status().isCreated());

        // Validate the UploadFile in the database
        List<UploadFile> uploadFileList = uploadFileRepository.findAll();
        assertThat(uploadFileList).hasSize(databaseSizeBeforeCreate + 1);
        UploadFile testUploadFile = uploadFileList.get(uploadFileList.size() - 1);
        assertThat(testUploadFile.getUploadTime()).isEqualTo(DEFAULT_UPLOAD_TIME);
    }

    @Test
    public void createUploadFileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = uploadFileRepository.findAll().size();

        // Create the UploadFile with an existing ID
        uploadFile.setId("existing_id");
        UploadFileDTO uploadFileDTO = uploadFileMapper.toDto(uploadFile);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUploadFileMockMvc.perform(post("/api/upload-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(uploadFileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UploadFile in the database
        List<UploadFile> uploadFileList = uploadFileRepository.findAll();
        assertThat(uploadFileList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllUploadFiles() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList
        restUploadFileMockMvc.perform(get("/api/upload-files?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uploadFile.getId())))
            .andExpect(jsonPath("$.[*].uploadTime").value(hasItem(DEFAULT_UPLOAD_TIME)));
    }
    
    @Test
    public void getUploadFile() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get the uploadFile
        restUploadFileMockMvc.perform(get("/api/upload-files/{id}", uploadFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(uploadFile.getId()))
            .andExpect(jsonPath("$.uploadTime").value(DEFAULT_UPLOAD_TIME));
    }

    @Test
    public void getNonExistingUploadFile() throws Exception {
        // Get the uploadFile
        restUploadFileMockMvc.perform(get("/api/upload-files/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateUploadFile() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        int databaseSizeBeforeUpdate = uploadFileRepository.findAll().size();

        // Update the uploadFile
        UploadFile updatedUploadFile = uploadFileRepository.findById(uploadFile.getId()).get();
        updatedUploadFile
            .uploadTime(UPDATED_UPLOAD_TIME);
        UploadFileDTO uploadFileDTO = uploadFileMapper.toDto(updatedUploadFile);

        restUploadFileMockMvc.perform(put("/api/upload-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(uploadFileDTO)))
            .andExpect(status().isOk());

        // Validate the UploadFile in the database
        List<UploadFile> uploadFileList = uploadFileRepository.findAll();
        assertThat(uploadFileList).hasSize(databaseSizeBeforeUpdate);
        UploadFile testUploadFile = uploadFileList.get(uploadFileList.size() - 1);
        assertThat(testUploadFile.getUploadTime()).isEqualTo(UPDATED_UPLOAD_TIME);
    }

    @Test
    public void updateNonExistingUploadFile() throws Exception {
        int databaseSizeBeforeUpdate = uploadFileRepository.findAll().size();

        // Create the UploadFile
        UploadFileDTO uploadFileDTO = uploadFileMapper.toDto(uploadFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUploadFileMockMvc.perform(put("/api/upload-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(uploadFileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UploadFile in the database
        List<UploadFile> uploadFileList = uploadFileRepository.findAll();
        assertThat(uploadFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteUploadFile() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        int databaseSizeBeforeDelete = uploadFileRepository.findAll().size();

        // Delete the uploadFile
        restUploadFileMockMvc.perform(delete("/api/upload-files/{id}", uploadFile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UploadFile> uploadFileList = uploadFileRepository.findAll();
        assertThat(uploadFileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
