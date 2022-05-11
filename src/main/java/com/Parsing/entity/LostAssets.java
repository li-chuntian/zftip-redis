package com.Parsing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName : LostAssets
 * @Description : 失陷资产
 * @Author : Lyn
 * @CopyRight ZFINFO
 * @Date: 2021-04-29 13:47
 */
@TableName("lost_assets_original")
@Data

public class LostAssets {

    @TableId(type = IdType.AUTO)
    private Long id;
    /**局点名称*/
    private String departName;
    /**任务执行时间*/
    private Date taskInvokeDate;
    /**出口IP地址*/
    private String exitIp;
    /**地址*/
    private String address;
    /**网络类型*/
    private String networkType;
    /**用户类型*/
    private String userType;
    /**最近看到时间*/
    private Date latestSeenDate;
    /**回连域名*/
    private String c2Domain;
    /**攻击类型*/
    private String malwareType;
    /**攻击团伙*/
    private String malwareFamily;
    /**是否定向攻击*/
    private String directAttackFlag;
    /**回连次数*/
    private Long connTime;

    private transient String latestSeenDateFrom;
    private transient String latestSeenDateTo;
}
