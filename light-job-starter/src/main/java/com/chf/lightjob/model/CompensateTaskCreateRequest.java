package com.chf.lightjob.model;

import lombok.Data;

/**
 * @description
 * @author: davy
 * @create: 2022-02-09 23:29
 */
@Data
public class CompensateTaskCreateRequest {

    private String taskHandler;

    private String bizKey;

    private String params;

    private static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private CompensateTaskCreateRequest req = new CompensateTaskCreateRequest();

        public CompensateTaskCreateRequest build() {
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
