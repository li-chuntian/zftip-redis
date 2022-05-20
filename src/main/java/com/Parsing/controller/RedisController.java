package com.Parsing.controller;

import com.Parsing.service.impl.CacheService;
import com.Parsing.utils.MenaceInfoTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class RedisController {

    @Autowired
    private CacheService cacheService;
    private final static Logger logger = LoggerFactory.getLogger(MenaceInfoTask.class);
    public void saveZipDataToRedis(List<List<Map<String, Object>>> zipContent) {
        for (List<Map<String, Object>> content : zipContent) {
            if (content.size() > 0) {
                for (Map<String, Object> map : content) {
                    logger.info(map.toString());
                    cacheService.addSetCache("tip360_domain", map);
                }
            }
        }
    }
}

