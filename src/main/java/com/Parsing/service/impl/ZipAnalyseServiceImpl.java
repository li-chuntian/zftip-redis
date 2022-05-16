package com.Parsing.service.impl;

import com.Parsing.entity.ExcelColumnHeaderEntity;
import com.Parsing.service.IZipAnalyseService;
import com.Parsing.utils.ZipUtil;
import net.lingala.zip4j.exception.ZipException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Service
public class ZipAnalyseServiceImpl implements IZipAnalyseService {
    @Override
    public  List<List<Map<String, Object>>> getZipFileContent(String zipPath, String dest, String passwd) throws ZipException, IOException, InterruptedException {
        File zipFilePath = new File(zipPath);
        if (!zipFilePath.exists()) {
            Thread.sleep(10000);
        }
        if (!zipFilePath.exists()) {
            return null;
        }

        File[] files = ZipUtil.deCompress(zipFilePath, dest, passwd);
        List<List<Map<String, Object>>> workBookData = null;
        for (File file : files) {
            String substring = "";
            String name = file.getName();
            String[] split = name.split("\\.");
            if (split.length > 1) {
                substring = split[split.length - 1];
            }
            FileInputStream excelFile = new FileInputStream(file);
            Workbook workBook = null;
            if ("xlsx".equals(substring)) {
                workBook = new XSSFWorkbook(excelFile);
            }else  {
                workBook = new HSSFWorkbook(excelFile);
            }
            excelFile.close();
            workBookData = getWorkBookData(workBook);
            System.out.println(workBookData.toString());
        }
        return workBookData;
    }

    private  List<List<Map<String, Object>>> getWorkBookData(Workbook workBook) {
        List<List<Map<String, Object>>> workBookList = new ArrayList<>();
        // 便利sheet
        for (int i = 0; i < workBook.getNumberOfSheets(); i++) {
            List<Map<String, Object>> sheetList = new ArrayList<>();
            Sheet sheetAt = workBook.getSheetAt(i);
            Iterator<Row> rowIterator = sheetAt.rowIterator();
            // 便利行
            while(rowIterator.hasNext()) {
                Map<String, Object> rowMap = new HashMap<>();
                List<String> labelList = new ArrayList<>();
                Row row = rowIterator.next();
                // 便利列
                Cell domainCell = row.getCell(ExcelColumnHeaderEntity.domain);
                String ipValue =  domainCell == null ? "" : domainCell.toString();
                rowMap.put("domain", ipValue);
                Cell spiteTypeCell = row.getCell(ExcelColumnHeaderEntity.spiteType);
                Cell familyGangCell = row.getCell(ExcelColumnHeaderEntity.familyGang);
                String spiteTypeValue =  spiteTypeCell == null ? "" : spiteTypeCell.toString();
                String familyGangValue =  familyGangCell == null ? "" : familyGangCell.toString();
                labelList.add(spiteTypeValue);
                labelList.add(familyGangValue);
                rowMap.put("label", labelList);
                sheetList.add(rowMap);
            }
            workBookList.add(sheetList);
        }
        return workBookList;
    }


}
