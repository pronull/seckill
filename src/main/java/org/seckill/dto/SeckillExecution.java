package org.seckill.dto;

import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;

/**
 * 封装秒杀执行结果
 * Created by Administrator on 2016/5/31.
 */
public class SeckillExecution
{
    private long seckillId;

    //秒杀执行结束状态
    private int state;

    //状态标识
    private String stateInfo;

    //秒杀成功对象
    private SuccessKilled successKilled;

    public SeckillExecution(long seckillId, SeckillStateEnum seckillStateEnum, SuccessKilled successKilled)
    {
        this.seckillId = seckillId;
        this.state = seckillStateEnum.getState();
        this.stateInfo = seckillStateEnum.getStateInfo();
        this.successKilled = successKilled;
    }

    public SeckillExecution(long seckillId, int state, String stateInfo)
    {
        this.seckillId = seckillId;
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public long getSeckillId()
    {
        return seckillId;
    }

    public void setSeckillId(long seckillId)
    {
        this.seckillId = seckillId;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    public String getStateInfo()
    {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo)
    {
        this.stateInfo = stateInfo;
    }

    public SuccessKilled getSuccessKilled()
    {
        return successKilled;
    }

    public void setSuccessKilled(SuccessKilled successKilled)
    {
        this.successKilled = successKilled;
    }
}