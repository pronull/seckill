package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2016/5/31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest
{
    @Resource
    private SuccessKilledDao successKilledDao;
    @Test
    public void insertSuccessKilled() throws Exception
    {
        long id = 1001l;
        long phone = 13823133214l;
        int insertCount = successKilledDao.insertSuccessKilled(id, phone);
        System.out.println("insertCount = " + insertCount);
    }

    @Test
    public void queryByIdWithSeckill() throws Exception
    {
        long id = 1001l;
        long phone = 13823133214l;
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(id, phone);
        System.out.println(successKilled);
    }

}