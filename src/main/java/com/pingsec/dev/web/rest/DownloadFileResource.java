package com.pingsec.dev.web.rest;

import com.pingsec.dev.service.DownloadFileService;
import com.pingsec.dev.web.rest.errors.BadRequestAlertException;
import com.pingsec.dev.service.dto.DownloadFileDTO;

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
 * REST controller for managing {@link com.pingsec.dev.domain.DownloadFile}.
 */
@RestController
@RequestMapping("/api")
public class DownloadFileResource {

    private final Logger log = LoggerFactory.getLogger(DownloadFileResource.class);

    private static final String ENTITY_NAME = "utilsDownloadFile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DownloadFileService downloadFileService;

    public DownloadFileResource(DownloadFileService downloadFileService) {
        this.downloadFileService = downloadFileService;
    }

    /**
     * {@code POST  /download-files} : Create a new downloadFile.
     *
     * @param downloadFileDTO the downloadFileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new downloadFileDTO, or with status {@code 400 (Bad Request)} if the downloadFile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/download-files")
    public ResponseEntity<DownloadFileDTO> createDownloadFile(@RequestBody DownloadFileDTO downloadFileDTO) throws URISyntaxException {
        log.debug("REST request to save DownloadFile : {}", downloadFileDTO);
        if (downloadFileDTO.getId() != null) {
            throw new BadRequestAlertException("A new downloadFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DownloadFileDTO result = downloadFileService.save(downloadFileDTO);
        return ResponseEntity.created(new URI("/api/download-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /download-files} : Updates an existing downloadFile.
     *
     * @param downloadFileDTO the downloadFileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated downloadFileDTO,
     * or with status {@code 400 (Bad Request)} if the downloadFileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the downloadFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/download-files")
    public ResponseEntity<DownloadFileDTO> updateDownloadFile(@RequestBody DownloadFileDTO downloadFileDTO) throws URISyntaxException {
        log.debug("REST request to update DownloadFile : {}", downloadFileDTO);
        if (downloadFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DownloadFileDTO result = downloadFileService.save(downloadFileDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, downloadFileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /download-files} : get all the downloadFiles.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of downloadFiles in body.
     */
    @GetMapping("/download-files")
    public ResponseEntity<List<DownloadFileDTO>> getAllDownloadFiles(Pageable pageable) {
        log.debug("REST request to get a page of DownloadFiles");
        Page<DownloadFileDTO> page = downloadFileService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /download-files/:id} : get the "id" downloadFile.
     *
     * @param id the id of the downloadFileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the downloadFileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/download-files/{id}")
    public ResponseEntity<DownloadFileDTO> getDownloadFile(@PathVariable String id) {
        log.debug("REST request to get DownloadFile : {}", id);
        Optional<DownloadFileDTO> downloadFileDTO = downloadFileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(downloadFileDTO);
    }

    /**
     * {@code DELETE  /download-files/:id} : delete the "id" downloadFile.
     *
     * @param id the id of the downloadFileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/download-files/{id}")
    public ResponseEntity<Void> deleteDownloadFile(@PathVariable String id) {
        log.debug("REST request to delete DownloadFile : {}", id);
        downloadFileService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
