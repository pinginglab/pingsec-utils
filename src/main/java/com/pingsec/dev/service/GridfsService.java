package com.pingsec.dev.service;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class GridfsService {

    private MongoDbFactory mongodbfactory;

    private MongoClient mongoClient;

    public GridfsService(MongoDbFactory mongodbfactory) {
        this.mongodbfactory = mongodbfactory;
    }

    /**
     * 插入文件
     * @param file
     * @return
     */
    public GridFSInputFile save(MultipartFile file){

        GridFS gridFS = new GridFS(mongodbfactory.getLegacyDb());

        try{

            InputStream in = file.getInputStream();

            String name = file.getOriginalFilename();

            GridFSInputFile gridFSInputFile = gridFS.createFile(in);

            gridFSInputFile.setFilename(name);

            gridFSInputFile.setContentType(file.getContentType());

            gridFSInputFile.save();

            return gridFSInputFile;
        } catch (Exception e){}

        return null;

    }

    /**
     * 据id返回文件
     */
//    @Autowired
    public GridFSDBFile getById(ObjectId id){

        GridFS gridFS = new GridFS(mongodbfactory.getLegacyDb());
        return gridFS.findOne(new BasicDBObject("_id", id));

    }


    /**
     * 删除
     * @param id
     */
    public void remove(String id) {
        GridFS gridFS = new GridFS(mongodbfactory.getLegacyDb());
        gridFS.remove(new ObjectId(id));
    }
}
