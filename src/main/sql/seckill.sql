--秒杀执行存储过程
DELIMITER $$ --CONSOLE ; 转化为 $$
--定义存储过程
--参数：in 输入参数 out输出参数
--row_count()：返回上一条修改类型sql（delete，update，insert）影响的行数
--row_count：0：未修改数据；>0;表示修改的行数；<0:sql出错
CREATE  PROCEDURE `seckill`.`execute_seckill`
  (in v_seckill_id bigint, in v_phone bigint,
    in v_kill_time TIMESTAMP , out r_result int)
  BEGIN
    DECLARE insert_count int DEFAULT 0;
    START TRANSACTION ;
    INSERT ignore into success_killed
      (seckill_id, user_phone, create_time)
      VALUES (v_seckill_id, v_phone,v_kill_time);
    SELECT ROW_COUNT() into insert_count;
  IF(insert_count = 0) THEN
    ROLLBACK ;
    SET r_result = -1;
  ELSEIF(insert_count<0) THEN
    ROLLBACK;
    SET r_result = -2;
  ELSE
    UPDATE seckill
    SET number = number-1
    WHERE seckill_id = v_seckill_id
      AND end_time>v_kill_time
      AND start_time<v_kill_time
      AND NUMBER > 0;
    SELECT ROW_COUNT() into insert_count;
    IF (insert_count = 0) THEN
      ROLLBACK;
      SET r_result = 0;
    ELSEIF  (insert_count < 0) THEN
      ROLLBACK;
      SET r_result = -2;
    ELSE
      COMMIT;
      SET r_result = 1;
    END IF;
  END IF;
END;
$$

DELIMITER ;

set @r_result=-3;

CALL execute_seckill(1000, 13823133213,now(),@r_result);

SELECT @r_result;


