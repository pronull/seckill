--数据库初始化脚本

--创建数据库
create database if not exists seckill;

--使用数据库
USE seckill;

--创建秒杀库存表
CREATE TABLE seckill (
`seckill_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品库存ID',
`NAME` VARCHAR (120) NOT NULL COMMENT '商品名称',
`number` INT NOT NULL COMMENT '秒杀库存',
`start_time` timestamp NOT NULL COMMENT '秒杀开启时间',
`end_time` timestamp NOT NULL COMMENT '秒杀结束时间',
`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (seckill_id),
KEY idx_start_time (start_time),
KEY idx_end_time (end_time),
KEY idx_create_time (create_time)
) ENGINE =InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET =utf8 COMMENT = '秒杀库存';

--初始化数据
INSERT INTO
  seckill (name, number, start_time, end_time)
VALUES
  ('1000元秒杀iPhone6',100,'2015-11-01 00:00:00','2015-11-02 00:00:00'),
  ('500元秒杀ipad2',100,'2015-11-01 00:00:00','2015-11-02 00:00:00'),
  ('300元秒杀小米5',100,'2015-11-01 00:00:00','2015-11-02 00:00:00'),
  ('200元秒杀华为P7',100,'2015-11-01 00:00:00','2015-11-02 00:00:00');

--秒杀成功明细表
--用户登陆认证相关的信息
create TABLE success_killed(
  `seckill_id` bigint NOT NULL COMMENT '秒杀商品ID',
  `user_phone` bigint NOT NULL COMMENT '用户手机号',
  `state` tinyint NOT NULL DEFAULT -1 COMMENT '状态显示：-1：无效，0：成功，1：已付款',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  PRIMARY KEY(seckill_id,user_phone),
  key idx_create_time(create_time)
)ENGINE =InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET =utf8 COMMENT = '秒杀成功明细表';

--连接数据控制台
mysql -uroot -p
123456

--show create table seckill\G;