package com.pingsec.dev.service.mapper;

import com.pingsec.dev.domain.*;
import com.pingsec.dev.service.dto.UploadFileDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link UploadFile} and its DTO {@link UploadFileDTO}.
 */
@Mapper(componentModel = "spring", uses = {DownloadFileMapper.class})
public interface UploadFileMapper extends EntityMapper<UploadFileDTO, UploadFile> {

    @Mapping(source = "name.id", target = "nameId")
    UploadFileDTO toDto(UploadFile uploadFile);

    @Mapping(source = "nameId", target = "name")
    UploadFile toEntity(UploadFileDTO uploadFileDTO);

    default UploadFile fromId(String id) {
        if (id == null) {
            return null;
        }
        UploadFile uploadFile = new UploadFile();
        uploadFile.setId(id);
        return uploadFile;
    }
}
