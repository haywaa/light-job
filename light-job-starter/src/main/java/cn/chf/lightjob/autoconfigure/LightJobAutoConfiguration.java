package cn.chf.lightjob.autoconfigure;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @description
 * @author: wenbai
 * @create: 2019-07-14 18:24
 */
@Configuration
@ComponentScan("cn.chf.lightjob")
//@EnableConfigurationProperties({HealthEndpointProperties.class, HealthIndicatorProperties.class})
public class LightJobAutoConfiguration implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        //RedisStore.createRedisStore(
        //        "host",
        //        6379,
        //        "password",
        //        //                sessionConfig.getSessionServerHost(),
        //        //                sessionConfig.getSessionServerPort(),
        //        //                sessionConfig.getSessionServerPassword(),
        //        3);
    }
}
