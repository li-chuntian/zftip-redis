package com.Parsing.service;

public interface LostAssetsService  {

    /**
     * 根据地区，起止时间下载压缩包
     * @param areaName 地区
     * @param onceQueryStartTime 查询开始时间
     * @param onceQueryEndTime 查询结束时间
     * @return
     */
    String fetchLostAssetsResult(String areaName, String onceQueryStartTime, String onceQueryEndTime, Integer searchWaitTime);
}
