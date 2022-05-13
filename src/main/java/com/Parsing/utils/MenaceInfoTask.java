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

        private final List<String> areaNameList = Arrays.asList("changde","chongqing","jiangsu","guizhou","henan","jingmen","yunnan","guangdong","kunming","hunan","hebei","sichuan","guiyang");
//    private final List<String> areaNameList = Arrays.asList("changde", "chongqing");

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
        FebsConstant.UNCOMPRESS = "./cache/" + DateUtil.format(new Date(), new SimpleDateFormat("yyyy-MM-dd")) + "/";
//        FebsConstant.UNCOMPRESS = "D:\\cache\\" + DateUtil.format(new Date(), new SimpleDateFormat("yyyy-MM-dd-hhmm")) + "\\";

        ArrayList<String> filePathList = new ArrayList<>();
        for (String areaName : areaNameList) {
//            1.根据传递的信息获取压缩包
            String filePath = lostAssetsService.fetchLostAssetsResult(areaName, queryStartTime, queryEndTime);
            if (filePath != null && !filePath.isEmpty()) {
                filePathList.add(filePath);
            }
        }
        for (String filePath : filePathList) {
            // 解压文件目录
            String uncompress = FebsConstant.UNCOMPRESS;
            //        2.将压缩包解压，并返回压缩包内容
            List<List<Map<String, Object>>> zipFileContent = zipAnalyseService.getZipFileContent(filePath, uncompress, FebsConstant.QIHOO_ASSETS_API_KEY);
            System.out.println("导入数据: " + zipFileContent);
            //        3.将数据存储绕redis
            if (zipFileContent != null && !zipFileContent.isEmpty()) {
                logger.info(zipFileContent.toString());
                redisController.saveZipDataToRedis(zipFileContent);
            }
        }


    }
}
