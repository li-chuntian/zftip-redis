package com.Parsing.service;

import net.lingala.zip4j.exception.ZipException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IZipAnalyseService {

    /**
     * 根据压缩文件路径和密码，解压文件
     * @param zipPath 压缩文件领
     * @param dest 解压地址
     * @param passwd 密码
     */
    List<List<Map<String, Object>>> getZipFileContent(String zipPath, String dest, String passwd) throws ZipException, IOException;
}
