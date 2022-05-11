package com.Parsing.controller;

import com.Parsing.entity.FebsResponse;
import com.Parsing.service.LostAssetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LostAssetsController {

    @Autowired
    private LostAssetsService lostAssetsService;

    @RequestMapping("/postTaskAndDownload")
    public FebsResponse postTaskAndDownload(@RequestParam("areaName") String areaName,
                                            @RequestParam("startTime") String queryStartTime,
                                            @RequestParam("endTime") String queryEndTime) {
        System.out.println("start===============================");
        lostAssetsService.fetchLostAssetsResult(areaName, queryStartTime, queryEndTime);
        return new FebsResponse().message("下载完成");
    }
}
