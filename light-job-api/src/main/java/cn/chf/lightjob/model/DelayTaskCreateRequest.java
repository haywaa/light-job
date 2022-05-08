package cn.chf.lightjob.model;

import java.util.Date;

import lombok.Data;

/**
 * @description
 * @author: davy
 * @create: 2022-01-29 00:31
 */
@Data
public class DelayTaskCreateRequest {

    private String taskHandler;

    private String bizKey;

    private String params;

    /**
     * 可指定下一次重试时间, 默认立即执行
     */
    private Date nextFireTime;

    private static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private DelayTaskCreateRequest req = new DelayTaskCreateRequest();

        public DelayTaskCreateRequest build() {
            return req;
        }

        public Builder setTaskHandler(String taskHandler) {
            this.req.setTaskHandler(taskHandler);
            return this;
        }

        public Builder setBizKey(String bizKey) {
            this.req.setBizKey(bizKey);
            return this;
        }

        public Builder setParams(String params) {
            this.req.setParams(params);
            return this;
        }
    }
}
