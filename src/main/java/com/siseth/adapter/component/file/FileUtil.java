package com.siseth.adapter.component.file;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

public final class FileUtil {

    public static MultipartFile convert(File file, String fileName) {
        try{
            return new MockMultipartFile(fileName, new FileInputStream(file));
        } catch (Exception e) {
            System.out.println("Error when create MultipartFile from File by "+e.getMessage());
            return null;
        }

    }

    public static MultipartFile convert(byte[] bytes, String fileName) {
        try{
            return new MockMultipartFile(fileName, bytes);
        } catch (Exception e) {
            System.out.println("Error when create MultipartFile from File by "+e.getMessage());
            return null;
        }
    }

    public static MultipartFile convert(File file) {
        return FileUtil.convert(file, file.getName());
    }

    public static MultipartFile convert(String entryName, ByteArrayOutputStream byteArrayOutputStream) {
        try{
            return new MockMultipartFile(entryName, byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }
}
