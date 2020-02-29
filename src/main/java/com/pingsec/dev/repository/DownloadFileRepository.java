package com.pingsec.dev.repository;

import com.pingsec.dev.domain.DownloadFile;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the DownloadFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DownloadFileRepository extends MongoRepository<DownloadFile, String> {

}
