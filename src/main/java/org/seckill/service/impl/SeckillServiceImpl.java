package org.seckill.service.impl;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/5/31.
 */
public class SeckillServiceImpl implements SeckillService
{

    private Logger logger = LoggerFactory.getLogger(SeckillServiceImpl.class);

    private SeckillDao seckillDao;

    private SuccessKilledDao successKilledDao;

    //盐值，混淆MD5
    private final String SLAT = "xiehaifan";

    public List<Seckill> getSeckillList()
    {
        return seckillDao.queryAll(0, 4);
    }

    public Seckill getById(long seckillId)
    {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId)
    {
        Seckill seckill = seckillDao.queryById(seckillId);
        if (seckill == null)
        {
            return new Exposer(seckillId, false);
        }

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime())
        {
            return new Exposer(seckillId, false, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        String md5 = getMd5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    private String getMd5(long seckillId)
    {
        String base = seckillId + "/" + SLAT;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException
    {
        if (md5 == null || !md5.equals(getMd5(seckillId)))
        {
            throw new SeckillException("seckill data rewrite.");
        }
        //执行秒杀逻辑  减库存-记录购买行为
        try
        {
            Date nowTime = new Date();
            int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
            if (updateCount <= 0)
            {
                //没有更新到记录
                throw new SeckillCloseException("seckill is closed");
            } else
            {
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                if (insertCount <= 0)
                {
                    throw new RepeatKillException("seckill repeated");
                } else
                {
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }
        }
        catch (SeckillCloseException e)
        {
            throw e;
        }
        catch (RepeatKillException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }
    }
}