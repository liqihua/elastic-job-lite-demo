package com.liqihua.config;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.liqihua.job.MyJob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author liqihua
 * @since 2018/9/17
 */
@Configuration
public class JobConfig {


    @Value("${zk.server-list}")
    private String zkServerList;
    @Value("${zk.namespace}")
    private String zkNamespace;


    @Bean
    public CoordinatorRegistryCenter coordinatorRegistryCenter(){
        System.out.println("--- zkServerList : " + zkServerList);
        System.out.println("--- zkNamespace : " + zkNamespace);
        ZookeeperConfiguration zkConfig = new ZookeeperConfiguration(zkServerList,zkNamespace);
        CoordinatorRegistryCenter zkRegCenter = new ZookeeperRegistryCenter(zkConfig);
        zkRegCenter.init();
        return zkRegCenter;
    }

    @Bean
    public LiteJobConfiguration liteJobConfiguration(){
        // 定义作业核心配置
        JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration.newBuilder("job0917", "0/5 * * * * ?", 3).shardingItemParameters("0=广州,1=成都,2=上海").build();
        // 定义SIMPLE类型配置
        SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig, MyJob.class.getCanonicalName());
        // 定义Lite作业根配置
        LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).overwrite(true).build();
        return simpleJobRootConfig;
    }


    @Bean(initMethod = "init")
    public JobScheduler jobScheduler(){
        return new JobScheduler(coordinatorRegistryCenter(), liteJobConfiguration());
    }




}
