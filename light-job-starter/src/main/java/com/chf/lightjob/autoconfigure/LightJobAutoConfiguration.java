package com.chf.lightjob.autoconfigure;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @description
 * @author: wenbai
 * @create: 2019-07-14 18:24
 */
@Configuration
@ComponentScan("com.chf.lightjob")
//@EnableConfigurationProperties({HealthEndpointProperties.class, HealthIndicatorProperties.class})
public class LightJobAutoConfiguration implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        //RedisStore.createRedisStore(
        //        "huibo001.redis.rds.aliyuncs.com",
        //        6379,
        //        "u5EX7sYg",
        //        //                sessionConfig.getSessionServerHost(),
        //        //                sessionConfig.getSessionServerPort(),
        //        //                sessionConfig.getSessionServerPassword(),
        //        3);
    }
}
