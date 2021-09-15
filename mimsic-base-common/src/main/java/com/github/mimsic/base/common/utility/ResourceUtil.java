package com.github.mimsic.base.common.utility;

import java.io.FileNotFoundException;
import java.nio.charset.Charset;

public class ResourceUtil {

    public ResourceUtil() {
    }

    public static <T> String readResource(Class<T> callerClass, String path, Charset charset) throws Exception {
        try {
            return FileUtil.readFile(path, charset);
        } catch (Exception ex) {
            if (ex instanceof FileNotFoundException) {
                return FileUtil.readFile(callerClass, path, charset);
            } else {
                throw ex;
            }
        }
    }
}
