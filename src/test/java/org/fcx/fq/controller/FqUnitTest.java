package org.fcx.fq.controller;

import org.fcx.fq.entity.proxy.clash.Proxy;
import org.junit.Test;

import java.time.LocalTime;

public class FqUnitTest {
    @Test
    public void trojanTest() {
        String l0 = "ss://cmM0LW1kNTpwYXNzZncyeHM0ZSE@117.28.243.187:8333#@ShareCentre%20%F0%9F%87%A8%F0%9F%87%B3%20CN-%F0%9F%87%B9%F0%9F%87%BCTW_2776%20%EF%BC%88%E5%89%AF%E6%9C%AC%EF%BC%89";
        Proxy proxy0 = Proxy.factory(l0);
        System.out.println(proxy0);
    }
    @Test
    public void localTimeTest() {
        System.out.println(LocalTime.now().getMinute());
        System.out.println(LocalTime.now().getMinute()>5&&LocalTime.now().getMinute()<55);
    }
}
