package org.seckill.exception;

/**
 * Created by Administrator on 2016/5/31.
 */
public class SeckillException extends RuntimeException
{

    public SeckillException(String message)
    {
        super(message);
    }

    public SeckillException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
