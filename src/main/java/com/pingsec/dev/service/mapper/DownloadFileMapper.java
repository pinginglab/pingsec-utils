package com.pingsec.dev.service.mapper;

import com.pingsec.dev.domain.*;
import com.pingsec.dev.service.dto.DownloadFileDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link DownloadFile} and its DTO {@link DownloadFileDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DownloadFileMapper extends EntityMapper<DownloadFileDTO, DownloadFile> {



    default DownloadFile fromId(String id) {
        if (id == null) {
            return null;
        }
        DownloadFile downloadFile = new DownloadFile();
        downloadFile.setId(id);
        return downloadFile;
    }
}
