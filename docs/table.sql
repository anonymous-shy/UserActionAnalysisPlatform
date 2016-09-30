#第一表：session_aggr_rate表，存储第一个功能，session聚合统计的结果
CREATE TABLE `session_aggr_rate` (
  `task_id`                   INT(11) NOT NULL,
  `session_count`             INT(11) DEFAULT NULL,
  `visit_length_1s_3s_rate`   DOUBLE  DEFAULT NULL,
  `visit_length_4s_6s_rate`   DOUBLE  DEFAULT NULL,
  `visit_length_7s_9s_rate`   DOUBLE  DEFAULT NULL,
  `visit_length_10s_30s_rate` DOUBLE  DEFAULT NULL,
  `visit_length_30s_60s_rate` DOUBLE  DEFAULT NULL,
  `visit_length_1m_3m_rate`   DOUBLE  DEFAULT NULL,
  `visit_length_3m_10m_rate`  DOUBLE  DEFAULT NULL,
  `visit_length_10m_30m_rate` DOUBLE  DEFAULT NULL,
  `visit_length_30m_rate`     DOUBLE  DEFAULT NULL,
  `step_length_1_3_rate`      DOUBLE  DEFAULT NULL,
  `step_length_4_6_rate`      DOUBLE  DEFAULT NULL,
  `step_length_7_9_rate`      DOUBLE  DEFAULT NULL,
  `step_length_10_30_rate`    DOUBLE  DEFAULT NULL,
  `step_length_30_60_rate`    DOUBLE  DEFAULT NULL,
  `step_length_60_rate`       DOUBLE  DEFAULT NULL,
  PRIMARY KEY (`task_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

#第二个表：session_random_extract表，存储我们的按时间比例随机抽取功能抽取出来的1000个session
CREATE TABLE `session_random_extract` (
  `task_id`            INT(11) NOT NULL,
  `session_id`         VARCHAR(255) DEFAULT NULL,
  `start_time`         VARCHAR(50)  DEFAULT NULL,
  `end_time`           VARCHAR(50)  DEFAULT NULL,
  `search_keywords`    VARCHAR(255) DEFAULT NULL,
  `click_category_ids` VARCHAR(255) DEFAULT NULL,
  `click_product_ids`  VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`task_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

#第三个表：top10_category表，存储按点击、下单和支付排序出来的top10品类数据
CREATE TABLE `top10_category` (
  `task_id`     INT(11) NOT NULL,
  `category_id` INT(11) DEFAULT NULL,
  `click_count` INT(11) DEFAULT NULL,
  `order_count` INT(11) DEFAULT NULL,
  `pay_count`   INT(11) DEFAULT NULL,
  PRIMARY KEY (`task_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

#第四个表：top10_category_session表，存储top10每个品类的点击top10的session
CREATE TABLE `top10_category_session` (
  `task_id`     INT(11) NOT NULL,
  `category_id` INT(11)      DEFAULT NULL,
  `session_id`  VARCHAR(255) DEFAULT NULL,
  `click_count` INT(11)      DEFAULT NULL,
  PRIMARY KEY (`task_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

#最后一张表：session_detail，用来存储随机抽取出来的session的明细数据、top10品类的session的明细数据
CREATE TABLE `session_detail` (
  `task_id`            INT(11) NOT NULL,
  `user_id`            INT(11)      DEFAULT NULL,
  `session_id`         VARCHAR(255) DEFAULT NULL,
  `page_id`            INT(11)      DEFAULT NULL,
  `action_time`        VARCHAR(255) DEFAULT NULL,
  `search_keyword`     VARCHAR(255) DEFAULT NULL,
  `click_category_id`  INT(11)      DEFAULT NULL,
  `click_product_id`   INT(11)      DEFAULT NULL,
  `order_category_ids` VARCHAR(255) DEFAULT NULL,
  `order_product_ids`  VARCHAR(255) DEFAULT NULL,
  `pay_category_ids`   VARCHAR(255) DEFAULT NULL,
  `pay_product_ids`    VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`task_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

#额外的一张表：task表，用来存储J2EE平台插入其中的任务的信息
CREATE TABLE `task` (
  `task_id`     INT(11) NOT NULL AUTO_INCREMENT,
  `task_name`   VARCHAR(255)     DEFAULT NULL,
  `create_time` VARCHAR(255)     DEFAULT NULL,
  `start_time`  VARCHAR(255)     DEFAULT NULL,
  `finish_time` VARCHAR(255)     DEFAULT NULL,
  `task_type`   VARCHAR(255)     DEFAULT NULL,
  `task_status` VARCHAR(255)     DEFAULT NULL,
  `task_param`  TEXT,
  PRIMARY KEY (`task_id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8;
