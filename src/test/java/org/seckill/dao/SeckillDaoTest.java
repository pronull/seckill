package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * 配置spring和junit整合,junit启动时加载springIOC配置
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest
{
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void reduceNumber() throws Exception
    {
        long seckillId = 1000;
        Date killDate = new Date();
        int number = seckillDao.reduceNumber(seckillId, killDate);
        System.out.println(number);
    }

    @Test
    public void queryById() throws Exception
    {
        long id = 1000;
        Seckill seckill = seckillDao.queryById(1000);
        System.out.println(seckill.getName());
        System.out.println(seckill);
    }

    @Test
    public void queryAll() throws Exception
    {

        // org.mybatis.spring.MyBatisSystemException:
        // nested exception is org.apache.ibatis.executor.ExecutorException:
        // A query was run and no Result Maps were found for the Mapped Statement 'org.seckill.dao.SeckillDao.queryAll'.
        // It's likely that neither a Result Type nor a Result Map was specified.
//Caused by: org.apache.ibatis.executor.ExecutorException:
        // A query was run and no Result Maps were found for the Mapped Statement 'org.seckill.dao.SeckillDao.queryAll'.  It's likely that neither a Result Type nor a Result Map was specified.

        List<Seckill> seckills = seckillDao.queryAll(1, 1000);
        for (Seckill seckill : seckills)
        {
            System.out.println(seckill);
        }
    }

}