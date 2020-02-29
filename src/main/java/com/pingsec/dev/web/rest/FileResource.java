package com.pingsec.dev.web.rest;


import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.pingsec.dev.service.GridfsService;
import com.pingsec.dev.service.dto.UploadFileRespDTO;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api")
public class FileResource {

    private final Logger log = LoggerFactory.getLogger(FileResource.class);

    private GridfsService gridfsService;

    @Autowired
    public FileResource(GridfsService gridfsService) {
        this.gridfsService = gridfsService;
    }
//    public FileResource(FileService fileService, GridfsService gridfsService) {
//        this.fileService = fileService;
//        this.gridfsService = gridfsService;
//    }

    @GetMapping("/download/:id")
    @Timed
    public ResponseEntity<Object> downloadFile(@PathVariable Long id) {
        return null;
    }

    @GetMapping("/upload")
    @Timed
    public ResponseEntity<Object> uploadFile(@RequestBody Object object) {
        return null;
    }

    @PostMapping(value = "/file/upload")
    public List<UploadFileRespDTO> uploadData(@RequestParam(value = "files") MultipartFile[] files) {

        List<UploadFileRespDTO> resp = new ArrayList<>();

        for (MultipartFile file: files) {
            GridFSInputFile inputFile = gridfsService.save(file);
            UploadFileRespDTO uploadFileRespDTO = new UploadFileRespDTO();
            uploadFileRespDTO.setId(inputFile.getId().toString());
            uploadFileRespDTO.setMd5(inputFile.getMD5());
            uploadFileRespDTO.setLength(inputFile.getLength());
            uploadFileRespDTO.setName(inputFile.getFilename());
            resp.add(uploadFileRespDTO);
        }

        return resp;
    }

    @PostMapping(value = "/file/upload-one")
    public UploadFileRespDTO uploadData(@RequestPart(value = "file") MultipartFile file) {

        UploadFileRespDTO resp = new UploadFileRespDTO();


        GridFSInputFile inputFile = gridfsService.save(file);

        resp.setId(inputFile.getId().toString());
        resp.setMd5(inputFile.getMD5());
        resp.setLength(inputFile.getLength());
        resp.setName(inputFile.getFilename());


        return resp;
    }


    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping(value = "/file/delete")
    public Object deleteFile(@RequestParam(value = "id") String id) {

//        删除文件
        gridfsService.remove(id);
        return "delete ok";
    }


    /**
     * 下载文件
     * @param id
     * @param response
     */
    @GetMapping(value = "/file/{id}")
    public void getFile(@PathVariable String id, HttpServletResponse response) {
        GridFSDBFile file = gridfsService.getById(new ObjectId(id));

        if (file == null) {
            responseFail("404 not found",response);
            return;
        }

        OutputStream os = null;

        try {
            os = response.getOutputStream();
            response.addHeader("Content-Disposition", "attachment;filename=" + file.getFilename());
            response.addHeader("Content-Length", "" + file.getLength());
            response.setContentType("application/octet-stream");
            file.writeTo(os);
            os.flush();
            os.close();

        } catch (Exception e) {
            try{
                if (os != null) {
                    os.close();
                }
            }catch (Exception e2){}
            e.printStackTrace();
        }

    }

    @GetMapping(value = "/file/view/{id}")
    public void viewFile(@PathVariable String id, HttpServletResponse response) {

        GridFSDBFile file = gridfsService.getById(new ObjectId(id));

        if (file == null) {
            responseFail("404 not found",response);
            return;
        }

        OutputStream os = null;

        try {
            os = response.getOutputStream();
            response.addHeader("Content-Disposition", "attachment;filename=" + file.getFilename());
            response.addHeader("Content-Length", "" + file.getLength());
            response.setContentType(file.getContentType());
            file.writeTo(os);
            os.flush();
            os.close();

        } catch (Exception e) {
            try{
                if (os != null) {
                    os.close();
                }
            } catch (Exception e2){}
            e.printStackTrace();
        }

    }

    private void responseFail(String msg, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        ObjectMapper mapper = new ObjectMapper();
        try{
            String res = mapper.writeValueAsString(msg);
            out = response.getWriter();
            out.append(res);
        } catch (Exception e) {
            try {
                if (out != null) {
                    out.close();
                }
            }catch (Exception e2) {}
        }
    }
}
