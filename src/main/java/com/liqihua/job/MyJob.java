package com.liqihua.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

/**
 * @author liqihua
 * @since 2018/9/17
 * 任务执行类
 */
public class MyJob implements SimpleJob {

    /**
     * 任务执行方法
     * @param context
     */
    @Override
    public void execute(ShardingContext context) {
        System.out.println("ShardingItem："+context.getShardingItem()+"，shardingParam："+context.getShardingParameter());
        //填充业务
    }



}
