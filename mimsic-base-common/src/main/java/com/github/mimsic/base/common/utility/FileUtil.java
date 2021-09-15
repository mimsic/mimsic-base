package com.github.mimsic.base.common.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * If path does not start with "/", callerClass.getClassLoader() must be used instead of callerClass.getClass()
 */

public class FileUtil {

    public FileUtil() {
    }

    public static <T> String readFile(Class<T> callerClass, String path, String charset) throws Exception {
        return FileUtil.readFile(callerClass, path, Charset.forName(charset));
    }

    public static <T> String readFile(Class<T> callerClass, String path, Charset charset) throws Exception {
        try (InputStream inputStream = callerClass.getClassLoader().getResourceAsStream(path)) {
            if (inputStream != null) {
                return StreamUtil.readInputStream(inputStream, charset);
            }
            throw new FileNotFoundException();
        }
    }

    public static String readFile(String path, String charset) throws Exception {
        return FileUtil.readFile(path, Charset.forName(charset));
    }

    public static String readFile(String path, Charset charset) throws Exception {
        File file = new File(path);
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            return StreamUtil.readInputStream(fileInputStream, charset);
        }
    }

    public static String readFile(String path) throws Exception {
        File file = new File(path);
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            return StreamUtil.readInputStream(fileInputStream);
        }
    }
}
