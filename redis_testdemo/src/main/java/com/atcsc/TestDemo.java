package com.atcsc;

import redis.clients.jedis.Jedis;

public class TestDemo {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("101.133.146.68", 6379);
        jedis.auth("atcscmd");
        String pong = jedis.ping();
        System.out.println("连接成功" + pong);
        jedis.close();
    }
}
