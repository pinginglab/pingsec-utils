package com.pingsec.dev.web.rest;

import com.pingsec.dev.UtilsApp;
import com.pingsec.dev.config.SecurityBeanOverrideConfiguration;
import com.pingsec.dev.domain.DownloadFile;
import com.pingsec.dev.repository.DownloadFileRepository;
import com.pingsec.dev.service.DownloadFileService;
import com.pingsec.dev.service.dto.DownloadFileDTO;
import com.pingsec.dev.service.mapper.DownloadFileMapper;
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


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.pingsec.dev.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DownloadFileResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, UtilsApp.class})
public class DownloadFileResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_DOWNLOAD_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DOWNLOAD_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_COUNT = 1;
    private static final Integer UPDATED_COUNT = 2;

    @Autowired
    private DownloadFileRepository downloadFileRepository;

    @Autowired
    private DownloadFileMapper downloadFileMapper;

    @Autowired
    private DownloadFileService downloadFileService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restDownloadFileMockMvc;

    private DownloadFile downloadFile;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DownloadFileResource downloadFileResource = new DownloadFileResource(downloadFileService);
        this.restDownloadFileMockMvc = MockMvcBuilders.standaloneSetup(downloadFileResource)
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
    public static DownloadFile createEntity() {
        DownloadFile downloadFile = new DownloadFile()
            .name(DEFAULT_NAME)
            .downloadTime(DEFAULT_DOWNLOAD_TIME)
            .count(DEFAULT_COUNT);
        return downloadFile;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DownloadFile createUpdatedEntity() {
        DownloadFile downloadFile = new DownloadFile()
            .name(UPDATED_NAME)
            .downloadTime(UPDATED_DOWNLOAD_TIME)
            .count(UPDATED_COUNT);
        return downloadFile;
    }

    @BeforeEach
    public void initTest() {
        downloadFileRepository.deleteAll();
        downloadFile = createEntity();
    }

    @Test
    public void createDownloadFile() throws Exception {
        int databaseSizeBeforeCreate = downloadFileRepository.findAll().size();

        // Create the DownloadFile
        DownloadFileDTO downloadFileDTO = downloadFileMapper.toDto(downloadFile);
        restDownloadFileMockMvc.perform(post("/api/download-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(downloadFileDTO)))
            .andExpect(status().isCreated());

        // Validate the DownloadFile in the database
        List<DownloadFile> downloadFileList = downloadFileRepository.findAll();
        assertThat(downloadFileList).hasSize(databaseSizeBeforeCreate + 1);
        DownloadFile testDownloadFile = downloadFileList.get(downloadFileList.size() - 1);
        assertThat(testDownloadFile.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDownloadFile.getDownloadTime()).isEqualTo(DEFAULT_DOWNLOAD_TIME);
        assertThat(testDownloadFile.getCount()).isEqualTo(DEFAULT_COUNT);
    }

    @Test
    public void createDownloadFileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = downloadFileRepository.findAll().size();

        // Create the DownloadFile with an existing ID
        downloadFile.setId("existing_id");
        DownloadFileDTO downloadFileDTO = downloadFileMapper.toDto(downloadFile);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDownloadFileMockMvc.perform(post("/api/download-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(downloadFileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DownloadFile in the database
        List<DownloadFile> downloadFileList = downloadFileRepository.findAll();
        assertThat(downloadFileList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllDownloadFiles() throws Exception {
        // Initialize the database
        downloadFileRepository.save(downloadFile);

        // Get all the downloadFileList
        restDownloadFileMockMvc.perform(get("/api/download-files?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(downloadFile.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].downloadTime").value(hasItem(DEFAULT_DOWNLOAD_TIME.toString())))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)));
    }
    
    @Test
    public void getDownloadFile() throws Exception {
        // Initialize the database
        downloadFileRepository.save(downloadFile);

        // Get the downloadFile
        restDownloadFileMockMvc.perform(get("/api/download-files/{id}", downloadFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(downloadFile.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.downloadTime").value(DEFAULT_DOWNLOAD_TIME.toString()))
            .andExpect(jsonPath("$.count").value(DEFAULT_COUNT));
    }

    @Test
    public void getNonExistingDownloadFile() throws Exception {
        // Get the downloadFile
        restDownloadFileMockMvc.perform(get("/api/download-files/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateDownloadFile() throws Exception {
        // Initialize the database
        downloadFileRepository.save(downloadFile);

        int databaseSizeBeforeUpdate = downloadFileRepository.findAll().size();

        // Update the downloadFile
        DownloadFile updatedDownloadFile = downloadFileRepository.findById(downloadFile.getId()).get();
        updatedDownloadFile
            .name(UPDATED_NAME)
            .downloadTime(UPDATED_DOWNLOAD_TIME)
            .count(UPDATED_COUNT);
        DownloadFileDTO downloadFileDTO = downloadFileMapper.toDto(updatedDownloadFile);

        restDownloadFileMockMvc.perform(put("/api/download-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(downloadFileDTO)))
            .andExpect(status().isOk());

        // Validate the DownloadFile in the database
        List<DownloadFile> downloadFileList = downloadFileRepository.findAll();
        assertThat(downloadFileList).hasSize(databaseSizeBeforeUpdate);
        DownloadFile testDownloadFile = downloadFileList.get(downloadFileList.size() - 1);
        assertThat(testDownloadFile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDownloadFile.getDownloadTime()).isEqualTo(UPDATED_DOWNLOAD_TIME);
        assertThat(testDownloadFile.getCount()).isEqualTo(UPDATED_COUNT);
    }

    @Test
    public void updateNonExistingDownloadFile() throws Exception {
        int databaseSizeBeforeUpdate = downloadFileRepository.findAll().size();

        // Create the DownloadFile
        DownloadFileDTO downloadFileDTO = downloadFileMapper.toDto(downloadFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDownloadFileMockMvc.perform(put("/api/download-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(downloadFileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DownloadFile in the database
        List<DownloadFile> downloadFileList = downloadFileRepository.findAll();
        assertThat(downloadFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteDownloadFile() throws Exception {
        // Initialize the database
        downloadFileRepository.save(downloadFile);

        int databaseSizeBeforeDelete = downloadFileRepository.findAll().size();

        // Delete the downloadFile
        restDownloadFileMockMvc.perform(delete("/api/download-files/{id}", downloadFile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DownloadFile> downloadFileList = downloadFileRepository.findAll();
        assertThat(downloadFileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
