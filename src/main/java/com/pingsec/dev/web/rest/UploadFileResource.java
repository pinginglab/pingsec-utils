package com.pingsec.dev.web.rest;

import com.pingsec.dev.service.UploadFileService;
import com.pingsec.dev.web.rest.errors.BadRequestAlertException;
import com.pingsec.dev.service.dto.UploadFileDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.pingsec.dev.domain.UploadFile}.
 */
@RestController
@RequestMapping("/api")
public class UploadFileResource {

    private final Logger log = LoggerFactory.getLogger(UploadFileResource.class);

    private static final String ENTITY_NAME = "utilsUploadFile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UploadFileService uploadFileService;

    public UploadFileResource(UploadFileService uploadFileService) {
        this.uploadFileService = uploadFileService;
    }

    /**
     * {@code POST  /upload-files} : Create a new uploadFile.
     *
     * @param uploadFileDTO the uploadFileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uploadFileDTO, or with status {@code 400 (Bad Request)} if the uploadFile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/upload-files")
    public ResponseEntity<UploadFileDTO> createUploadFile(@RequestBody UploadFileDTO uploadFileDTO) throws URISyntaxException {
        log.debug("REST request to save UploadFile : {}", uploadFileDTO);
        if (uploadFileDTO.getId() != null) {
            throw new BadRequestAlertException("A new uploadFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UploadFileDTO result = uploadFileService.save(uploadFileDTO);
        return ResponseEntity.created(new URI("/api/upload-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /upload-files} : Updates an existing uploadFile.
     *
     * @param uploadFileDTO the uploadFileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uploadFileDTO,
     * or with status {@code 400 (Bad Request)} if the uploadFileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uploadFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/upload-files")
    public ResponseEntity<UploadFileDTO> updateUploadFile(@RequestBody UploadFileDTO uploadFileDTO) throws URISyntaxException {
        log.debug("REST request to update UploadFile : {}", uploadFileDTO);
        if (uploadFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UploadFileDTO result = uploadFileService.save(uploadFileDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uploadFileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /upload-files} : get all the uploadFiles.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uploadFiles in body.
     */
    @GetMapping("/upload-files")
    public ResponseEntity<List<UploadFileDTO>> getAllUploadFiles(Pageable pageable) {
        log.debug("REST request to get a page of UploadFiles");
        Page<UploadFileDTO> page = uploadFileService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /upload-files/:id} : get the "id" uploadFile.
     *
     * @param id the id of the uploadFileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uploadFileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/upload-files/{id}")
    public ResponseEntity<UploadFileDTO> getUploadFile(@PathVariable String id) {
        log.debug("REST request to get UploadFile : {}", id);
        Optional<UploadFileDTO> uploadFileDTO = uploadFileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uploadFileDTO);
    }

    /**
     * {@code DELETE  /upload-files/:id} : delete the "id" uploadFile.
     *
     * @param id the id of the uploadFileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/upload-files/{id}")
    public ResponseEntity<Void> deleteUploadFile(@PathVariable String id) {
        log.debug("REST request to delete UploadFile : {}", id);
        uploadFileService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
