package com.bespoke.Bespoke.service;

import com.bespoke.Bespoke.models.FileModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileService {

    public FileModel uploadVideo(String path, MultipartFile file) throws IOException{
        return null;
    }


    public InputStream getVideoFile(String path, String fileName, int id)
            throws FileNotFoundException{


        return null;
    }
}
