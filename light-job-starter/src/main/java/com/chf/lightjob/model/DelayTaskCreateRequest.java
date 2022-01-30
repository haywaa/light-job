package com.chf.lightjob.model;

import com.sun.org.apache.xpath.internal.operations.Bool;

import lombok.Data;

/**
 * @description
 * @author: davy
 * @create: 2022-01-29 00:31
 */
@Data
public class DelayTaskCreateRequest {

    private String taskHandler;

    private String uniqueKey;

    private String params;

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

        public Builder setUniqueKey(String uniqueKey) {
            this.req.setUniqueKey(uniqueKey);
            return this;
        }

        public Builder setParams(String params) {
            this.req.setParams(params);
            return this;
        }
    }
}
