package com.liqihua.job;

import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import org.springframework.stereotype.Component;

/**
 * @author liqihua
 * @since 2018/9/17
 * 任务执行监听器
 */
@Component
public class MyListener implements ElasticJobListener {

    @Override
    public void beforeJobExecuted(ShardingContexts shardingContexts) {
        System.out.println("--- beforeJobExecuted:"+shardingContexts.toString());

    }

    @Override
    public void afterJobExecuted(ShardingContexts shardingContexts) {
        System.out.println("--- afterJobExecuted:"+shardingContexts.toString());
    }
}
