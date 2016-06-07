package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/23.
 */
public interface SeckillDao
{

    /**
     * 减库存
     *
     * @param seckillId
     * @param killTime
     * @return
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * 根据id查询秒杀对象
     *
     * @param seckillId
     * @return 如果影响行数为1，表示更新的记录行数
     */
    Seckill queryById(long seckillId);

    /**
     * 根据偏移量查询秒杀列表
     *
     * @param offset
     * @param limit
     * @return
     */
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);


    void killByProcedure(Map<String, Object> paramMap);

    /**
     * 更新秒杀时间区间
     * @return
     */
//    int updateSekillTime(@Param("seckillId") long seckillId, @Param("startTime") Date startTime, @Param("endTime")
//                         Date endTime);



}
