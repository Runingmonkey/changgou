package com.changgou.util;

import com.changgou.file.FastDFSFile;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

import java.io.*;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/7/3 8:56
 */
public class FastDFSClient {
    /**
     * @description: 获取tracker 信息
     * @author: QIXIANG LING
     * @date: 2020/7/3 9:00
     * @param: null
     * @return:
     */
    static {
        try {
            String filepath = new ClassPathResource("fdfs_client.conf").getPath();
            ClientGlobal.init(filepath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @description:  文件上传
     * @author: QIXIANG LING
     * @date: 2020/7/3 9:11
     * @param: file   要上传的文件封装 FastDFSFile
     * @return: String[]    [0] 文件上传的所存储的组名 ; [1] 文件存储路径
     */
    public static String[]  upload(FastDFSFile file){
        // 获取文件作者
        NameValuePair[] meta_list =  new NameValuePair[1];
        meta_list[0] = new NameValuePair(file.getAuthor());

        String[] uploadFile = null;
        try {
            // Tracker 客户端
            TrackerClient trackerClient = new TrackerClient();
            // 通过client 获取 Tracker 服务
            TrackerServer trackerServer = trackerClient.getConnection();
            // StorageClient;
            StorageClient storageClient = new StorageClient(trackerServer, null);
            // 文件上传  byte[] , 拓展名 , 附加信息
            String hostAddress = trackerServer.getInetSocketAddress().getAddress().getHostAddress();
            int trackerHttpPort = ClientGlobal.getG_tracker_http_port();
            uploadFile = storageClient.upload_file(file.getContent(), file.getExt(), meta_list);
            System.out.println(hostAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uploadFile;
    }

    /**
     * @description: 获取文件信息
     * @author: QIXIANG LING
     * @date: 2020/7/3 9:34
     * @param: groupName
     * @param: remoteFile
     * @return: org.csource.fastdfs.FileInfo
     */
    public static FileInfo getFile(String groupName, String remoteFile){
        FileInfo fileInfo = null;
        try {
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageClient storageClient = new StorageClient(trackerServer, null);
            fileInfo = storageClient.get_file_info(groupName, remoteFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  fileInfo;
    }


    /**
     * @description: 文件下载
     * @author: QIXIANG LING
     * @date: 2020/7/3 9:51
     * @param: groupName
     * @param: fileRemoteName
     * @return: java.io.InputStream
     */
    public static InputStream downloadFile(String groupName, String fileRemoteName){
        try {
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageClient storageClient = new StorageClient(trackerServer, null);
            byte[] bytes = storageClient.download_file(groupName, fileRemoteName);

            // 转成 输入 流
            return new ByteArrayInputStream(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }


    /**
     * @description: 文件删除
     * @author: QIXIANG LING
     * @date: 2020/7/3 9:56
     * @param: groupName
     * @param: fileRemoteName
     * @return: java.lang.Integer
     */
    public  static Integer deleteFile(String groupName , String fileRemoteName){
        try {
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageClient storageClient = new StorageClient(trackerServer, null);
            return storageClient.delete_file(groupName, fileRemoteName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }
    // redundant

    // 测试
    public static void main(String[] args) throws Exception {
        // 下载文件
        InputStream inputStream = downloadFile("group1", "M00/00/00/wKjThF7-nY6AOaikAAefbyuxJ7Q852.jpg");

        FileOutputStream fileOutputStream = new FileOutputStream(new File("D:/test/now.jpg"));

        byte[] buffer = new byte[1024];
        while (inputStream.read(buffer) != -1){
            fileOutputStream.write(buffer);
        }

        fileOutputStream.close();
        inputStream.close();

        // 删除文件
        //FastDFSClient.deleteFile("group1","M00/00/00/wKjThF7-nN6ATcOeAAZJFcT7kuU264.jpg");
    }
}
