package com.pingsec.dev.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;

/**
 * A DownloadFile.
 */
@Document(collection = "download_file")
public class DownloadFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("download_time")
    private Instant downloadTime;

    @Field("count")
    private Integer count;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public DownloadFile name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getDownloadTime() {
        return downloadTime;
    }

    public DownloadFile downloadTime(Instant downloadTime) {
        this.downloadTime = downloadTime;
        return this;
    }

    public void setDownloadTime(Instant downloadTime) {
        this.downloadTime = downloadTime;
    }

    public Integer getCount() {
        return count;
    }

    public DownloadFile count(Integer count) {
        this.count = count;
        return this;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DownloadFile)) {
            return false;
        }
        return id != null && id.equals(((DownloadFile) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "DownloadFile{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", downloadTime='" + getDownloadTime() + "'" +
            ", count=" + getCount() +
            "}";
    }
}
