package com.Parsing.utils;

import cn.hutool.core.date.DateUtil;
import com.Parsing.controller.RedisController;
import com.Parsing.entity.FebsConstant;
import com.Parsing.service.IZipAnalyseService;
import com.Parsing.service.LostAssetsService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

public class MenaceInfoTask extends TimerTask {
    private final static Logger logger = LoggerFactory.getLogger(MenaceInfoTask.class);

    private final LostAssetsService lostAssetsService;

    private final IZipAnalyseService zipAnalyseService;

    private final RedisController redisController;

    private final List<String> areaNameList = Arrays.asList("changde", "chongqing", "jiangsu", "guizhou", "henan", "jingmen", "yunnan", "guangdong", "kunming", "hunan", "hebei", "sichuan", "guiyang");

    public MenaceInfoTask(LostAssetsService lostAssetsService, IZipAnalyseService zipAnalyseService, RedisController redisController) {
        this.lostAssetsService = lostAssetsService;
        this.zipAnalyseService = zipAnalyseService;
        this.redisController = redisController;
    }

    @SneakyThrows
    @Override
    public void run() {
        Date yesterday = DateUtil.parse(DateUtil.yesterday().toDateStr());
        String queryStartTime = DateUtil.beginOfDay(yesterday).toString();
        String queryEndTime = DateUtil.endOfDay(yesterday).toString();
        logger.info("任务开始, 本次查询时间段: " + queryStartTime + " - " + queryEndTime);
        // 创建本次任务压缩包存放目录--
        FebsConstant.UNCOMPRESS = "/home/zftip_redis//cache/" + DateUtil.format(new Date(), new SimpleDateFormat("yyyy-MM-dd")) + "/";
        ArrayList<String> filePathList = new ArrayList<>();
        for (String areaName : areaNameList) {
            // 根据传递的信息获取压缩包
            String filePath = lostAssetsService.fetchLostAssetsResult(areaName, queryStartTime, queryEndTime, 60 * 1000);
            if (filePath != null && !filePath.isEmpty()) {
                filePathList.add(filePath);
            }
        }
        for (String filePath : filePathList) {
            // 解压文件目录
            String uncompress = FebsConstant.UNCOMPRESS;
            // 将压缩包解压，并返回压缩包内容
            List<List<Map<String, Object>>> zipFileContent = zipAnalyseService.getZipFileContent(filePath, uncompress, FebsConstant.QIHOO_ASSETS_API_KEY);
            // 将数据存储绕redis
            int zipFileContentSize = 0;
            for (List<Map<String, Object>> content : zipFileContent) {
                zipFileContentSize += content.size();
            }
            logger.info("本次共导入数据: " + zipFileContentSize + "条, 文件地址为" + filePath);
            if (!zipFileContent.isEmpty()) {
                logger.info(zipFileContent.toString());
                redisController.saveZipDataToRedis(zipFileContent);
                logger.info("导入完成");
            }
        }
        logger.info("本次任务结束");
    }
}
