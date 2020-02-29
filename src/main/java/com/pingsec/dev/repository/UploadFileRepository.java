package com.pingsec.dev.repository;

import com.pingsec.dev.domain.UploadFile;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the UploadFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UploadFileRepository extends MongoRepository<UploadFile, String> {

}
