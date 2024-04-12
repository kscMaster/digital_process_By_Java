package com.nancal.api.utils;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import org.apache.http.entity.ContentType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @author hewei
 * @date 2022/8/4 13:20
 * @Description
 */
public class FileUtils {


    /**
     * 解压zip文件
     *
     * @param targetPath 解压路径
     * @param sourceFile 源文件
     * @throws ZipException
     */
    public static void unzipFiles(String targetPath, File sourceFile) throws ZipException, UnsupportedEncodingException{
        String charsetGbk = "GBK";
        String charsetUtf8 = "UTF8";
        ZipFile zipFile = new ZipFile(sourceFile);
        zipFile.setCharset(Charset.forName(charsetGbk));
        String extractedFile;
        for (FileHeader fileHeader : zipFile.getFileHeaders()) {
            extractedFile = fileHeader.getFileName();
            // 如果是fileHeader的文件名是utf-8且zipFile的编码不为空且zipFile的编码不是utf-8，则重置zipFile的编码为utf-8
            if (fileHeader.isFileNameUTF8Encoded() && zipFile.getCharset() != null
                    && !charsetUtf8.equalsIgnoreCase(zipFile.getCharset().name())) {
                // 转换文件名
                extractedFile = new String(extractedFile.getBytes(charsetGbk), charsetUtf8);
                // 重置zipFile的编码
                zipFile.setCharset(Charset.forName(charsetUtf8));
                // 重置fileHeader的文件名
                fileHeader.setFileName(extractedFile);
            } else if (zipFile.getCharset() != null && !charsetGbk.equalsIgnoreCase(zipFile.getCharset().name())) {
                zipFile.setCharset(Charset.forName(charsetGbk));
            }
            zipFile.extractFile(fileHeader, targetPath, extractedFile);
        }
    }

    public static File getMultipartFile(MultipartFile multipartFile){
        //文件上传前的名称
        String fileName = multipartFile.getOriginalFilename();
        File file = new File(fileName);
        try( OutputStream out = new FileOutputStream(file)){
            //获取文件流，以文件流的方式输出到新文件
            byte[] ss = multipartFile.getBytes();
            for(int i = 0; i < ss.length; i++){
                out.write(ss[i]);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return file;
    }

    public static MultipartFile getMultipartFile(File file){
        FileInputStream fileInputStream = null;
        MultipartFile multipartFile = null;
        try {
            fileInputStream = new FileInputStream(file);
            multipartFile = new MockMultipartFile(file.getName(),file.getName(), ContentType.APPLICATION_OCTET_STREAM.toString()
                    ,fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return multipartFile;
    }


    //删除方法
    public static void deleteDir(File files) {
        try {
            //获取File对象中的所有文件，并将其放在数组中
            File[] listFiles = files.listFiles();
            if (null != listFiles) {
                //循环遍历数组
                for (File file : listFiles) {
                    //如果是目录文件，则递归调用删除方法
                    if (file.isDirectory()) {
                        deleteDir(file);
                    }
                    //如果是文件，则删除
                    file.delete();
                }
                //删除文件夹内所有文件后，再删除文件夹
                files.delete();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
