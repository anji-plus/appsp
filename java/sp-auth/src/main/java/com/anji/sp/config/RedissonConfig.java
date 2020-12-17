package com.anji.sp.config;

import com.anji.sp.util.APPVersionCheckUtil;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class RedissonConfig {
    private static final String REDIS_PROTOCOL_PREFIX = "redis://";
    private static final String REDISS_PROTOCOL_PREFIX = "rediss://";
    @Value("${spring.redis.host:}")
    private String host;

    @Value("${spring.redis.port:}")
    private String port;

    @Value("${spring.redis.password:}")
    private String password;

    @Value("${spring.redis.cluster.nodes:}")
    private String redisClustersNodes;

    @Value("${spring.redis.sentinel.nodes:}")
    private String redisSentinelNodes;

    @Value("${spring.redis.sentinel.master:}")
    private String redisSentinelMaster;

    @Value("${spring.redis.timeout:}")
    private String timeout;
    @Value("${spring.redis.database:}")
    private String database;


    @Bean
    public RedissonClient redissonClient() {
        //单机模式
        if (!StringUtils.isBlank(host) && !StringUtils.isBlank(port)) {
            return singleClient();
        }
        //集群模式
        if (!StringUtils.isBlank(redisClustersNodes)) {
            return clusterClient();
        }
        //哨兵模式
        if (!StringUtils.isBlank(redisSentinelNodes)) {
            return sentinelClient();
        }

        return null;
    }

    /**
     * 单机模式
     *
     * @return
     */
    public RedissonClient singleClient() {
        //单机模式 根据具体情况分析
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer()
                .setAddress("redis://" + host + ":" + port);
        //哨兵模式
            if (!StringUtils.isBlank(password)) {
                singleServerConfig.setPassword(password);
            }
        return Redisson.create(config);
    }

    /**
     * 集群模式
     *
     * @return
     */
    public RedissonClient clusterClient() {
        Config config = new Config();
        String[] strings = convert(Arrays.asList(redisClustersNodes.split(",")));
        //useClusterServers 集群模式
        ClusterServersConfig clusterServersConfig = config.useClusterServers()
                .addNodeAddress(strings);
        clusterServersConfig.setPassword(password);//设置密码
        clusterServersConfig.setTimeout(APPVersionCheckUtil.strToInt(timeout));//设置密码
        return Redisson.create(config);
    }


    /**
     * 哨兵模式
     *
     * @return
     */
    public RedissonClient sentinelClient() {
        Config config = new Config();
        String[] strings = convert(Arrays.asList(redisSentinelNodes.split(",")));
        SentinelServersConfig serverConfig = config.useSentinelServers()
                .addSentinelAddress(strings)
                .setReadMode(ReadMode.MASTER_SLAVE)
                .setTimeout(APPVersionCheckUtil.strToInt(timeout));
        // 设置密码
        if(StringUtils.isNotBlank(redisSentinelMaster)){
            serverConfig.setMasterName(redisSentinelMaster);
        }
        // 设置密码
        if(StringUtils.isNotBlank(password)){
            serverConfig.setPassword(password);
        }
        // 设置database
        if (APPVersionCheckUtil.strToInt(database) !=0){
            serverConfig.setDatabase(APPVersionCheckUtil.strToInt(database));
        }

        return Redisson.create(config);
    }
    private String[] convert(List<String> nodesObject) {
        List<String> nodes = new ArrayList<String>(nodesObject.size());
        for (String node : nodesObject) {
            if (!node.startsWith(REDIS_PROTOCOL_PREFIX) && !node.startsWith(REDISS_PROTOCOL_PREFIX)) {
                nodes.add(REDIS_PROTOCOL_PREFIX + node);
            } else {
                nodes.add(node);
            }
        }
        return nodes.toArray(new String[nodes.size()]);
    }

}


