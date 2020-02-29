package com.pingsec.dev.service.dto;
import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.pingsec.dev.domain.DownloadFile} entity.
 */
public class DownloadFileDTO implements Serializable {

    private String id;

    private String name;

    private Instant downloadTime;

    private Integer count;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(Instant downloadTime) {
        this.downloadTime = downloadTime;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DownloadFileDTO downloadFileDTO = (DownloadFileDTO) o;
        if (downloadFileDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), downloadFileDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DownloadFileDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", downloadTime='" + getDownloadTime() + "'" +
            ", count=" + getCount() +
            "}";
    }
}
