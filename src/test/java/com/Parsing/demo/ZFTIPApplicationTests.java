package com.Parsing.demo;

//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import java.util.Date;

//@SpringBootTest
class ZFTIPApplicationTests {


    void contextLoads() throws InterruptedException {
        Date yesterday = DateUtil.parse(DateUtil.yesterday().toDateStr());
        DateTime queryStartTime = DateUtil.beginOfDay(yesterday);
        DateTime queryEndTime = DateUtil.endOfDay(yesterday);
        System.out.println(queryStartTime.toString());
    }

}
