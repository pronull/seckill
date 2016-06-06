package org.seckill.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2016/6/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml"})
public class SeckillServiceImplTest
{
    private final Logger logger = LoggerFactory.getLogger(SeckillServiceImplTest.class);

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception
    {
        List<Seckill> seckillList = seckillService.getSeckillList();
        logger.info("list={}",seckillList);
    }

    @Test
    public void getById() throws Exception
    {
        Seckill seckill = seckillService.getById(1000);
        logger.info("seckill={}",seckill);
    }

    @Test
    public void testSeckillLogic() throws Exception
    {
        long id = 1000L;
        //23:36:44.673 [main] INFO  o.s.s.impl.SeckillServiceImplTest - exposer = Exposer{exposed=true, md5='8a08c20c8384d527cc1b2434016aa58c', seckillId=1000, now=0, start=0, end=0}
        Exposer exposer = seckillService.exportSeckillUrl(id);

        if(exposer.isExposed())
        {
            logger.info("exposer = {}", exposer);
            String md5 = exposer.getMd5();
            try{
                SeckillExecution seckillExecution = seckillService.executeSeckill(id, 13823133244L, md5);
                logger.info("seckillExecution={}",seckillExecution);
            }
            catch (RepeatKillException e)
            {
                logger.error(e.getMessage());
            }
            catch (SeckillCloseException e)
            {
                logger.error(e.getMessage());
            }
            catch (SeckillException e)
            {
                logger.error(e.getMessage());
            }
        }
        else
        {
            logger.warn("seckill is closed:{}",exposer);
        }
    }

    @Test
    public void executeSeckill() throws Exception
    {
    }

//    @Test
//    public void updateSeckillTime()
//    {
//        Seckill seckill = new Seckill();
//        seckill.setSeckillId(1000);
//        Date now = new Date();
//        long end = now.getTime() + 10000000;
//        Date endTime = new Date(end);
//        seckill.setStartTime(now);
//        seckill.setEndTime(endTime);
//        int updateCount = seckillService.updateSeckillTime(seckill);
//        logger.info("updateCount = {}",updateCount);
//    }

}