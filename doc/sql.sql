-- 数据库隔离级别要求RC（读已提交）
CREATE database if NOT EXISTS `light_job` default character set utf8mb4 collate utf8mb4_unicode_ci;
use `light_job`;

SET NAMES utf8mb4;

-- 周期性任务配置表
CREATE TABLE `periodic_job` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `job_group` varchar(20) NOT NULL COMMENT '执行器主键ID',
    `job_desc` varchar(255) NOT NULL,
    `add_time` datetime DEFAULT NULL,
    `update_time` datetime DEFAULT NULL,
    `author` varchar(64) DEFAULT NULL COMMENT '作者',
    `alarm_email` varchar(255) DEFAULT NULL COMMENT '报警邮件',
    `schedule_type` varchar(50) NOT NULL DEFAULT 'NONE' COMMENT '调度类型',
    `schedule_conf` varchar(128) DEFAULT NULL COMMENT '调度配置，值含义取决于调度类型',
    `misfire_strategy` varchar(50) NOT NULL DEFAULT 'DO_NOTHING' COMMENT '调度过期策略',
    `block_strategy` varchar(50) DEFAULT NULL COMMENT '阻塞处理策略',
    `executor_route_strategy` varchar(50) DEFAULT NULL COMMENT '执行器路由策略',
    `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
    `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
    `executor_timeout` int(11) NOT NULL DEFAULT '0' COMMENT '任务执行超时时间，单位秒',
    `executor_fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败重试次数',
    `child_jobid` varchar(255) DEFAULT NULL COMMENT '子任务ID，多个逗号分隔',
    `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态：0-停止，1-运行',
    `trigger_last_time` bigint(13) NOT NULL DEFAULT '0' COMMENT '上次调度时间',
    `trigger_next_time` bigint(13) NOT NULL DEFAULT '0' COMMENT '下次调度时间',
    `schedule_fail_times` int(11) NOT NULL DEFAULT '0' COMMENT '分配失败次数',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='周期性作业配置表';

-- 排队中的延时任务表
CREATE TABLE `pending_job` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `job_group` varchar(20) NOT NULL COMMENT '执行器主键ID',
    `job_desc` varchar(255) NOT NULL,
    `add_time` datetime NOT NULL,
    `update_time` datetime NOT NULL,
    `plan_trigger_time` datetime NOT NULL COMMENT '计划触发时间',
    `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
    `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
    `executor_timeout` int(11) NOT NULL DEFAULT '0' COMMENT '任务执行超时时间，单位秒',
    `executor_fail_retry_count` int(11) NOT NULL DEFAULT '5' COMMENT '失败重试次数',
    `retry_duration` varchar(100) NULL COMMENT '重试时间间隔, 单位秒, 逗号分隔',
    `trigger_mark` tinyint(1) COMMENT '0-待触发， 1-已触发',
    PRIMARY KEY (`id`),
    KEY idx_plan (`plan_trigger_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='延时任务队列表';

-- 任务执行记录表
CREATE TABLE task (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `status` tinyint(4) NOT NULL COMMENT '状态：0-新记录（调度中），1-已调度(执行中), 2-已完成, 4-已丢弃',
    `job_group` varchar(20) NOT NULL COMMENt '任务分组，等同于appName',
    `job_type` varchar(15) NOT NULL COMMENT '任务类型：PERIODIC_JOB, DELAY_TASK',
    `biz_key` varchar(64) NOT NULL DEFAULT '' COMMENT '业务键，方便查询',
    `plan_trigger_time` datetime NOT NULL COMMENT '期望触发时间',
    `expire_time` datetime NOT NULL COMMENT '过期时间',
    `block_strategy` varchar(50) DEFAULT NULL COMMENT '阻塞处理策略',
    `executor_address` varchar(255) DEFAULT NULL COMMENT '执行器地址，本次执行的地址',
    `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
    `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
    `executor_timeout` int(11) NOT NULL DEFAULT '0' COMMENT '任务执行超时时间，单位秒',
    `executor_fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败重试次数',
    `retry_duration` varchar(100) NULL COMMENT '重试时间间隔, 单位秒, 逗号分隔',
    `from_job_id` bigint(20) DEFAULT NULL COMMENT 'periodic_job_id、pending_job_id等',
    `trigger_time` datetime NULL COMMENT '实际触发时间',
    `trigger_index` int(11) NULL COMMENT '实际触发次数',
    `failure_times` int(11) NULL COMMENT '执行失败次数，执行器回调执行失败才算1次',
    `finish_time` datetime NULL COMMENT '实际完成时间',
    `trigger_log` text COMMENT '调度-日志',
    PRIMARY KEY (`id`),
    KEY idx_expire_trigger(`status`, `expire_time`),
    KEY idx_from(`from_job_id`),
    KEY idx_biz(`biz_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

# CREATE TABLE task_trigger_log (
#   `id` bigint(20) NOT NULL AUTO_INCREMENT,
#   task_id datetime NOT NULL COMMENT '任务ID',
#   trigger_time datetime NULL COMMENT '实际触发时间',
#   expire_trigger_time datetime NOT NULL COMMENT '调度过期时间',
#   expire_execute_time datetime NOT NULL COMMENT '调度过期时间',
#   param text DEFAULT NULL COMMENT '参数',
#   periodic_job_id bigint(20) DEFAULT NULL COMMENT '周期性任务ID',
#   unique_key varchar(64) NOT NULL COMMENT '唯一健',
#   trigger_time datetime NULL COMMENT '实际触发时间',
#   finish_time datetime NULL COMMENT '实际完成时间',
#   `trigger_log` text COMMENT '调度-日志',
#   PRIMARY KEY (`id`),
#   KEY idx_expire_trigger(`status`, `expire_trigger_time`),
#   KEY idx_expire_execute(`status`, `expire_execute_time`),
#   UNIQUE KEY uk(unique_key)
# ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `light_job_lock` (
    `lock_name` varchar(50) NOT NULL COMMENT '锁名称',
    PRIMARY KEY (`lock_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `light_job_mark` (
  `mark_name` varchar(50) NOT NULL COMMENT '标记名称',
  `mark_value` varchar(50) NULL DEFAULT NULL COMMENT '标记值',
  PRIMARY KEY (`mark_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `light_job_lock` ( `lock_name`) VALUES ( 'periodic_job_schedule'), ('pending_job_schedule');
INSERT INTO `light_job_mark` ( `mark_name`) VALUES ( 'periodic_job_scheduler'), ('pending_job_scheduler');