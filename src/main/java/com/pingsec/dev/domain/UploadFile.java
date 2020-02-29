package com.pingsec.dev.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;

/**
 * A UploadFile.
 */
@Document(collection = "upload_file")
public class UploadFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("upload_time")
    private String uploadTime;

    @DBRef
    @Field("name")
    private DownloadFile name;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public UploadFile uploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
        return this;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public DownloadFile getName() {
        return name;
    }

    public UploadFile name(DownloadFile downloadFile) {
        this.name = downloadFile;
        return this;
    }

    public void setName(DownloadFile downloadFile) {
        this.name = downloadFile;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UploadFile)) {
            return false;
        }
        return id != null && id.equals(((UploadFile) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "UploadFile{" +
            "id=" + getId() +
            ", uploadTime='" + getUploadTime() + "'" +
            "}";
    }
}
