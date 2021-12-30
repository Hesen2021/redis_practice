package com.atcsc.sendvalid;


import redis.clients.jedis.Jedis;

import java.util.Random;

public class CodeValidate {
    public static void main(String[] args) {
        //模拟验证码发送
        verifyCode("15572936626");

//        getRedisCode("15572936626", "593431");

    }



    //3 验证码校验
    public static void getRedisCode(String phone, String code) {
        //从redis获取验证码
        Jedis jedis = new Jedis("101.133.146.68", 6379);
        //验证码key
        String codeKey = "VerifyCode" + phone + ":code";
        String redisCode = jedis.get(codeKey);
        //判断
        if (redisCode.equals(code)) {
            System.out.println("成功");
        } else {
            System.out.println("失败");
        }
    }

    //每个手机每天只能发送三次，验证码放到redis中，设置过期时间120s
    private static void verifyCode(String phone) {
        //连接redis
        Jedis jedis = new Jedis("101.133.146.68", 6379);
        //拼接key
        //手机发送次数
        String countKey = "VerifyCode" + phone + ":count";
        //验证码
        String codeKey = "VerifyCode" + phone + ":code";

        //每个手机每天只能发送三次
        String count = jedis.get(countKey);
        if(count == null) {
            //没有发送次数，第一次发送
            //设置发送次数是1
            jedis.setex(countKey, 24*60*60, "1");
        } else if(Integer.parseInt(count) <= 2) {
            //发送次数 +1
            jedis.incr(countKey);
        } else if(Integer.parseInt(count) > 2) {
            System.out.println("今天发送次数已经超过三次");
            jedis.close();
            return;
        }
        //发送验证码放到redis里面
        String vcode = getCode();
        jedis.setex(codeKey, 120, vcode);
        jedis.close();

    }

    //生成6位验证码
    public static String getCode() {
        Random random = new Random();
        String code = "";
        for(int i = 0; i < 6; i++) {
            int num = random.nextInt(10);
            code += num;
        }
        return code;
    }
}
