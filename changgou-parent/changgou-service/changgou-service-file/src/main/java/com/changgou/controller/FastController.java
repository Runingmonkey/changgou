package com.changgou.controller;

import com.changgou.file.FastDFSFile;
import com.changgou.util.FastDFSClient;
import com.changgou.util.Result;
import com.changgou.util.StatusCode;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/7/3 10:08
 */
@RestController
@CrossOrigin
public class FastController {

    @PostMapping("/upload")
    public Result upload(@RequestParam(value = "file")MultipartFile file){
        try {
            // 把文件封装成   上传文件对象
            FastDFSFile fastDFSFile = new FastDFSFile(file.getOriginalFilename(),
                    file.getBytes(),
                    StringUtils.getFilenameExtension(file.getOriginalFilename()),
                    null, null);
            String[] upload = FastDFSClient.upload(fastDFSFile);
            return new Result(true, StatusCode.OK ,"文件上传成功",upload);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(false,StatusCode.ERROR ,"文件上传失败");
    }

}
