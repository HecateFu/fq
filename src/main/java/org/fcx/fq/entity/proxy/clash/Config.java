package org.fcx.fq.entity.proxy.clash;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.fcx.fq.exception.MyToolException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Setter
public class Config {
    // HTTPS代理 和 SOCKS5代理 使用同一个端口
    @JsonProperty("mixed-port")
    private int mixedPort = 7890;
    // 是否允许来自局域网的连接
    @JsonProperty("allow-lan")
    private boolean allowLan = true;
    // 规则模式 global 全局代理 direct 直连 rule 根据 rules 配置选择连接方式
    private String mode = "rule";
    // 日志级别 info / warning / error / debug / silent
    @JsonProperty("log-level")
    private String logLevel = "info";
    // RESTful API 服务地址，clash可通过RESTful接口查看、修改配置、传输信息、日志
    @JsonProperty("external-controller")
    private String externalController = "127.0.0.1:9090";
//    private Dns dns;
    // 代理
    private List<Proxy> proxies;
    // 代理组
    @JsonProperty("proxy-groups")
    private List<ProxyGroup> proxyGroups;
    // 路由规则
    private List<String> rules;

    public Config(Set<Proxy> proxies){
        // 代理节点
        if(CollectionUtils.isEmpty(proxies)){
            throw new MyToolException("该链接没有可用节点");
        }
        // 过滤掉 Clash 不支持的节点
        this.proxies = proxies.stream().filter(p -> {
            if(p instanceof SSR){
                SSR ssr = (SSR) p;
                if(ssr.getCipher().equals("none") || ssr.getCipher().equals("rc4")){
                    return false;
                } else {
                    return true;
                }
            } else if (p instanceof SS) {
                SS ss = (SS)p;
                if(ss.getCipher().equals("rc4")){
                    return false;
                } else {
                    return true;
                }
            }else {
                return true;
            }
        }).collect(Collectors.toList());

        // 代理组
        List<String> nodeNames = this.proxies.stream().map(Proxy::getName).collect(Collectors.toList());
        List<String> proxyList = new ArrayList<>();
        proxyList.add("fallback-auto");
        proxyList.addAll(nodeNames);
        ProxyGroup fallback = new ProxyGroup("fallback-auto","fallback",nodeNames);
        fallback.setInterval(300);
        fallback.setUrl("http://www.gstatic.com/generate_204");
        ProxyGroup proxy = new ProxyGroup("Proxy","select",proxyList);
        ProxyGroup direct = new ProxyGroup("Direct","select",Arrays.asList("DIRECT","REJECT","Proxy"));
        ProxyGroup ad = new ProxyGroup("Ad","select",Arrays.asList("REJECT","DIRECT","Proxy"));
        ProxyGroup other = new ProxyGroup("Other","select",Arrays.asList("Proxy","DIRECT","REJECT"));
        this.proxyGroups = Arrays.asList(proxy,fallback,direct,ad,other);

        // 路由规则
        InputStream directRulesIn = this.getClass().getResourceAsStream("/route_rules/direct.txt");
        List<String> directRules = this.readRules(directRulesIn);
        InputStream proxyRulesIn = this.getClass().getResourceAsStream("/route_rules/proxy.txt");
        List<String> proxyRules = this.readRules(proxyRulesIn);
        InputStream finalRulesIn = this.getClass().getResourceAsStream("/route_rules/final.txt");
        List<String> finalRules = this.readRules(finalRulesIn);
        this.rules = new ArrayList<>();
        this.rules.addAll(proxyRules);
        this.rules.addAll(directRules);
        this.rules.addAll(finalRules);
    }

    private List<String> readRules(InputStream in){
        List<String> rules = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String temp = null;
            while ( (temp = br.readLine()) != null){
                temp = temp.trim();
                if(!StringUtils.isEmpty(temp) && !temp.startsWith("#")){
                    rules.add(temp);
                }
            }
        } catch (IOException ex) {
            log.error("read rules err:",ex);
        }
        return rules;
    }

}
