package com.Parsing.controller;

import com.Parsing.service.IZipAnalyseService;
import com.Parsing.service.LostAssetsService;
import com.Parsing.utils.MenaceInfoTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.bind.annotation.RestController;

import java.util.Timer;

@RestController
public class MenaceInfoController implements CommandLineRunner {
    @Autowired
    private LostAssetsService lostAssetsService;
    @Autowired
    private IZipAnalyseService zipAnalyseService;
    @Autowired
    private RedisController redisController;

    @Override
    public void run(String... args) {
        MenaceInfoTask task = new MenaceInfoTask(lostAssetsService, zipAnalyseService, redisController);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task,0,1000*60*60&24);
    }
}
