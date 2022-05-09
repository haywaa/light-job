-- 数据库隔离级别要求RC（读已提交）
CREATE database if NOT EXISTS `light_job` default character set utf8mb4 collate utf8mb4_unicode_ci;
use `light_job`;

SET NAMES utf8mb4;

CREATE TABLE `light_job_node` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ip` varchar(15) NOT NULL COMMENT '节点IP',
  `status` varchar(10) NOT NULL COMMENT '状态',
  `gmt_heatbeat` datetime NOT NULL COMMENT '心跳时间',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='节点状态表';

CREATE TABLE `light_job_group` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `group_code` varchar(20) NOT NULL COMMENT '执行器Code',
    `group_name` varchar(30) NOT NULL COMMENT '执行器名称',
    `gmt_create` datetime NOT NULL COMMENT '创建时间',
    `gmt_modified` datetime NOT NULL COMMENT '最近更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='执行器';

-- 周期性任务配置表
CREATE TABLE `periodic_job` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `job_group` varchar(20) NOT NULL COMMENT '执行器code',
    `job_desc` varchar(255) NOT NULL,
    `gmt_create` datetime DEFAULT NULL,
    `gmt_modified` datetime DEFAULT NULL,
    `author` varchar(64) DEFAULT NULL COMMENT '作者',
    `alarm_email` varchar(255) DEFAULT NULL COMMENT '报警邮件',
    `schedule_type` varchar(50) NOT NULL DEFAULT 'NONE' COMMENT '调度类型',
    `schedule_conf` varchar(128) DEFAULT NULL COMMENT '调度配置，值含义取决于调度类型',
    `misfire_strategy` varchar(50) NOT NULL DEFAULT 'DO_NOTHING' COMMENT '调度过期策略',
    `block_strategy` varchar(50) DEFAULT NULL COMMENT '阻塞处理策略',
    `max_retry_times` int(11) DEFAULT NULL COMMENT '最大重试次数',
    `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
    `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
    `executor_timeout` int(11) NOT NULL DEFAULT '0' COMMENT '任务执行预估时长，单位秒',
    `child_jobid` varchar(255) DEFAULT NULL COMMENT '子任务ID，多个逗号分隔',
    `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态：0-停止，1-运行',
    `trigger_last_time` bigint(13) NOT NULL DEFAULT '0' COMMENT '上次调度时间',
    `trigger_next_time` bigint(13) NOT NULL DEFAULT '0' COMMENT '下次调度时间',
    `schedule_fail_times` int(11) NOT NULL DEFAULT '0' COMMENT '分配失败次数',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='周期性作业配置表';

-- 排队中的延时任务表
CREATE TABLE `delay_task` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `job_group` varchar(20) NOT NULL COMMENT '执行器主键ID',
    `job_desc` varchar(255) NOT NULL,
    `gmt_create` datetime NOT NULL,
    `gmt_modified` datetime NOT NULL,
    `plan_trigger_time` datetime NOT NULL COMMENT '计划触发时间',
    `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
    `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
    `executor_timeout` int(11) NOT NULL DEFAULT '0' COMMENT '任务执行预估时长，单位秒',
    `executor_fail_retry_count` int(11) NOT NULL DEFAULT '5' COMMENT '失败重试次数',
    `max_retry_times` varchar(50) DEFAULT NULL COMMENT '最大重试次数',
    `status` varchar(10) COMMENT 'WAIT，FINISHED, FAILURE',
    PRIMARY KEY (`id`),
    KEY idx_plan (`plan_trigger_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='延时任务队列表';

-- 任务执行记录表
CREATE TABLE async_task (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `job_group` varchar(20) NOT NULL COMMENt '任务分组，等同于appName',
    `biz_key` varchar(64) NOT NULL DEFAULT '' COMMENT '业务键，方便查询',
    `plan_trigger_time` datetime NOT NULL COMMENT '计划触发时间',
    `executor_address` varchar(255) DEFAULT NULL COMMENT '执行器地址，本次执行的地址',
    `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
    `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
    `executor_timeout` int(11) NOT NULL DEFAULT '0' COMMENT '任务执行预估时长，单位秒',
    `executor_fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败重试次数',
    `max_retry_times` varchar(50) DEFAULT NULL COMMENT '最大重试次数',
    `periodic_job_id` bigint(20) DEFAULT NULL COMMENT 'periodic_job_id',
    `block_strategy` varchar(50) DEFAULT NULL COMMENT '阻塞处理策略',
    `gmt_create` datetime NOT NULL COMMENT '创建时间',
    `gmt_modified` datetime NOT NULL COMMENT '最近执行时间',
    PRIMARY KEY (`id`),
    KEY idx_biz(`biz_key`),
    KEY idx_pjid(`periodic_job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------- 日志相关 ------------------------
CREATE TABLE deadline_task (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `job_group` varchar(20) NOT NULL COMMENt '任务分组，等同于appName',
    `biz_key` varchar(64) NOT NULL DEFAULT '' COMMENT '业务键，方便查询',
    `plan_trigger_time` datetime NOT NULL COMMENT '计划触发时间',
    `executor_address` varchar(255) DEFAULT NULL COMMENT '执行器地址，本次执行的地址',
    `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
    `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
    `executor_timeout` int(11) NOT NULL DEFAULT '0' COMMENT '任务执行预估时长，单位秒',
    `executor_fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败重试次数',
    `max_retry_times` varchar(50) DEFAULT NULL COMMENT '最大重试次数',
    `periodic_job_id` bigint(20) DEFAULT NULL COMMENT 'periodic_job_id',
    `block_strategy` varchar(50) DEFAULT NULL COMMENT '阻塞处理策略',
    `gmt_create` datetime NOT NULL COMMENT '创建时间',
    `gmt_modified` datetime NOT NULL COMMENT '最近执行时间',
    PRIMARY KEY (`id`),
    KEY idx_biz(`biz_key`),
    KEY idx_pjid(`periodic_job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE history_task (
   `id` bigint(20) NOT NULL AUTO_INCREMENT,
   `job_group` varchar(20) NOT NULL COMMENt '任务分组，等同于appName',
   `biz_key` varchar(64) NOT NULL DEFAULT '' COMMENT '业务键，方便查询',
   `plan_trigger_time` datetime NOT NULL COMMENT '计划触发时间',
   `executor_address` varchar(255) DEFAULT NULL COMMENT '执行器地址，本次执行的地址',
   `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
   `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
   `executor_timeout` int(11) NOT NULL DEFAULT '0' COMMENT '任务执行预估时长，单位秒',
   `executor_fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败重试次数',
   `max_retry_times` varchar(50) DEFAULT NULL COMMENT '最大重试次数',
   `periodic_job_id` bigint(20) DEFAULT NULL COMMENT 'periodic_job_id',
   `block_strategy` varchar(50) DEFAULT NULL COMMENT '阻塞处理策略',
   `gmt_create` datetime NOT NULL COMMENT '创建时间',
   `gmt_modified` datetime NOT NULL COMMENT '最近执行时间',
   PRIMARY KEY (`id`),
   KEY idx_biz(`biz_key`),
   KEY idx_pjid(`periodic_job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------- 框架并发控制支持 ----------------------
CREATE TABLE `light_job_lock` (
    `lock_name` varchar(50) NOT NULL COMMENT '锁名称',
    PRIMARY KEY (`lock_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `light_job_mark` (
  `mark_name` varchar(50) NOT NULL COMMENT '标记名称',
  `mark_value` varchar(50) NULL DEFAULT NULL COMMENT '标记值',
  PRIMARY KEY (`mark_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `light_job_lock` ( `lock_name`) VALUES ( 'periodic_job_schedule'), ('node_check');
INSERT INTO `light_job_mark` ( `mark_name`) VALUES ( 'periodic_job_scheduler');

-- ----------------- 用户/登录相关 -----------------------
CREATE TABLE `light_job_user` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `usercode` varchar(20) NOT NULL COMMENt '用户code',
    `password` varchar(32) NOT NULL DEFAULT '' COMMENT '密码',
    `user_name` varchar(50) DEFAULT NULL COMMENT '用户名称',
    `gmt_create` datetime NOT NULL COMMENT '创建时间',
    `gmt_modified` datetime NOT NULL COMMENT '最近更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY uk(`usercode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `light_user_token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENt '用户code',
  `token` varchar(32) NOT NULL DEFAULT '' COMMENT '密码',
  `gmt_expire` datetime NOT NULL COMMENT '过期时间',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY uk(`token`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- admin / admin
INSERT INTO `light_job_user` ( `usercode`, `password`, `user_name`, `gmt_create`, `gmt_modified`) VALUES ('admin', '239c0db342733f700f973a8eccd480ac', 'admin', now(), now());
