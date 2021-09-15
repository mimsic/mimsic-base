package com.github.mimsic.base.common.utility;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.nio.charset.StandardCharsets;

public class FileUtilTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtilTest.class);
    private static final String FILE_PATH = "file-test.txt";

    @Test
    public void testReadByClassloader() {
        try {
            String data = FileUtil.readFile(this.getClass(), FILE_PATH, StandardCharsets.UTF_8);
            Assertions.assertEquals("contents", data);
        } catch (Exception ex) {
            LOGGER.error("File not found", ex);
            Assertions.fail();
        }
    }

    @Test
    public void testReadByPath() {
        try {
            URL url = this.getClass().getClassLoader().getResource(FILE_PATH);
            assert url != null;
            String data = FileUtil.readFile(url.toURI().getPath(), StandardCharsets.UTF_8);
            Assertions.assertEquals("contents", data);
        } catch (Exception ex) {
            LOGGER.error("File not found", ex);
            Assertions.fail();
        }
    }
}
