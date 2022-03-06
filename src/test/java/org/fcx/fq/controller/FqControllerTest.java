package org.fcx.fq.controller;

import org.fcx.fq.entity.proxy.clash.Proxy;
import org.fcx.fq.util.MyToolUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "mytool.init-proxies=false")
@ActiveProfiles("dev")
@AutoConfigureMockMvc
public class FqControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    FqController fq;
    @Test
    public void getProxyServersRawTest() throws Exception {
        mvc.perform(get("/fq/config/ml.yaml")).andExpect(status().isOk());
    }
    @Test
    public void decodeServerInfoTest() {
        String link = "https://raw.githubusercontent.com/ssrsub/ssr/master/ssrsub";
        String raw = fq.downloadProxyServersRaw(link);
        Set<Proxy> proxies = fq.parseProxyList(raw);
        proxies.forEach(System.out::println);
    }

    @Test
    public void createProxyTest(){
        Proxy proxy = Proxy.factory("ssr://MTAxLjY3LjguMTkyOjI1MTc5OmF1dGhfYWVzMTI4X21kNTphZXMtMjU2LWNmYjp0bHMxLjJfdGlja2V0X2F1dGg6YUVkclVUWTVNVFYwUkEvP29iZnNwYXJhbT1ZV3BoZUM1dGFXTnliM052Wm5RdVkyOXQmcHJvdG9wYXJhbT1Nekl6T0RFNlF6SndTSGxwVVdabmJRJnJlbWFya3M9UTA1ZjVMcU01NGkzNTcrNzVhS1o1NzJSSUdoMGRIQnpPaTh2TVRnd09DNW5ZVjh4TkRNPSZncm91cD02YnVZNks2azVZaUc1N3VF");
        System.out.println(proxy);
    }

    @Test
    public void base64DecodeTest(){
        String result = MyToolUtil.base64Decode("Q05f5LqM54i357+75aKZ572RIGh0dHBzOi8vMTgwOC5nYV8xNDM=");
        System.out.println(result);
    }

    @Test
    public void updateLinksTest () throws  Exception{
        String resp = mvc.perform(MockMvcRequestBuilders

                .get("/fq/updatelink")
                .param("key","ss")
                .param("link","https%3A%2F%2Fwww.111003.ml%2F%2Flink%2FXp7XUEOqe3dYQVNu%3Fsub%3D1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(resp);
    }

    @Test
    public void loadProxiesTest () {
        Instant s = Instant.now();
        fq.preLoadProxies();
        Instant e = Instant.now();
        System.out.println(Duration.between(s,e).getSeconds());
    }
}