package org.fcx.fq.controller;

import org.fcx.fq.entity.proxy.clash.Proxy;
import org.junit.Test;

import java.time.LocalTime;

public class FqUnitTest {
    @Test
    public void trojanTest() {
        String l0 = "ss://YWVzLTI1Ni1nY206ZmFCQW9ENTRrODdVSkc3@172.107.233.230:2376#(%E5%B7%B2%E5%AD%98%E6%B4%BB1%E5%A4%A9)%E8%A5%BF%E7%8F%AD%E7%89%99-2.94MB%25";
        Proxy proxy0 = Proxy.factory(l0);
        System.out.println(proxy0);
    }
    @Test
    public void localTimeTest() {
        System.out.println(LocalTime.now().getMinute());
        System.out.println(LocalTime.now().getMinute()>5&&LocalTime.now().getMinute()<55);
    }
}
