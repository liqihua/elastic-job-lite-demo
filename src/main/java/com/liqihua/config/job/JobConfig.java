package com.liqihua.config.job;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.liqihua.job.MyJob;
import com.liqihua.job.MyListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author liqihua
 * @since 2018/9/17
 * elastic任务配置类
 */
@Configuration
public class JobConfig {

    @Value("${zk.server-list}")
    private String zkServerList;
    @Value("${zk.namespace}")
    private String zkNamespace;
    @Value("${job.name}")
    private String jobName;
    @Value("${job.cron}")
    private String cron;
    @Value("${job.shardingTotalCount}")
    private int shardingTotalCount;
    @Value("${job.shardingItemParameters}")
    private String shardingItemParameters;


    @Resource
    private DataSource dataSource;
    @Resource
    private MyListener myListener;


    /**
     * zookeeper注册中心
     * @return
     */
    @Bean
    public CoordinatorRegistryCenter coordinatorRegistryCenter(){
        ZookeeperConfiguration zkConfig = new ZookeeperConfiguration(zkServerList,zkNamespace);
        CoordinatorRegistryCenter zkRegCenter = new ZookeeperRegistryCenter(zkConfig);
        zkRegCenter.init();
        return zkRegCenter;
    }


    /**
     * elastic任务配置类
     * @return
     */
    @Bean
    public LiteJobConfiguration liteJobConfiguration(){
        JobCoreConfiguration jobCoreConfig = JobCoreConfiguration.newBuilder(jobName, cron, shardingTotalCount).shardingItemParameters(shardingItemParameters).build();
        SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(jobCoreConfig, MyJob.class.getCanonicalName());
        LiteJobConfiguration liteJobConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).overwrite(true).build();
        return liteJobConfig;
    }


    /**
     * elastic任务执行类
     * @param dataSource 执行痕迹记录数据源
     * @param myListener 执行监听器
     * @return
     */
    @Bean(initMethod = "init")
    public JobScheduler jobScheduler(DataSource dataSource,MyListener myListener){
        JobEventConfiguration jobEventRdbConfig = new JobEventRdbConfiguration(dataSource);
        return new JobScheduler(coordinatorRegistryCenter(), liteJobConfiguration(),jobEventRdbConfig,myListener);
    }




}
