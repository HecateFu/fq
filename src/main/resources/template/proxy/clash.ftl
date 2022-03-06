mixed-port: 7890
allow-lan: true
mode: rule
log-level: info
external-controller: '127.0.0.1:9090'
secret: ''
dns:
  enable: true
  ipv6: false
  listen: '0.0.0.0:53'
  default-nameserver:
    - 114.114.114.114
    - 8.8.8.8
  enhanced-mode: fake-ip
  fake-ip-range: 198.18.0.1/16
  nameserver:
    - 114.114.114.114 # default value
    - 8.8.8.8 # default value
    - tls://dns.rubyfish.cn:853 # DNS over TLS
    - https://1.1.1.1/dns-query # DNS over HTTPS
    - dhcp://en0 # dns from dhcp
    - 119.29.29.29
    - 1.1.1.1
    - 'tcp://223.5.5.5'
  fallback:
    - tcp://1.1.1.1
    - 1.0.0.1
    - 8.8.8.8
    - 'tls://223.5.5.5:853'
    - 'https://223.5.5.5/dns-query'
  fallback-filter:
    geoip: true
    ipcidr:
      - 240.0.0.0/4
    domain:
      - '+.google.com'
      - '+.facebook.com'
      - '+.youtube.com'

proxies:
<#list proxies as proxy>
  <#if proxy.type == "vmess">
  - name: '${proxy?counter} ${proxy.name}'
    server: ${proxy.server}
    port: ${proxy.port?c}
    type: ${proxy.type}
    udp: true
    uuid: ${proxy.uuid}
    alterId: ${proxy.alterId?c}
    cipher: auto
    tls: ${proxy.tls?c}
    <#if proxy.network??>
    network: ${proxy.network}
    skip-cert-verify: true
    </#if>
    <#if proxy.network == "ws">
    ws-opts:
      path: ${proxy.wsPath}
      headers: 
      <#list proxy.wsHeaders as k,v>
        ${k}: ${v}
      </#list>
    </#if>
  <#elseif proxy.type == "ss" && proxy.cipher != "rc4">
  - name: '${proxy?counter} ${proxy.name}'
    server: ${proxy.server}
    port: ${proxy.port?c}
    type: ${proxy.type}
    udp: true
    cipher: ${proxy.cipher}
    password: ${proxy.password}
    <#if proxy.plugin??>
    plugin: ${proxy.plugin}
    plugin-opts: 
    <#list proxy.pluginOpts as k,v>
      ${k}: ${v}
    </#list>
    </#if>
  <#elseif proxy.type == "ssr" && proxy.cipher != "none" && proxy.cipher != "rc4" && proxy.cipher != "chacha20">
  - name: '${proxy?counter} ${proxy.name}'
    server: ${proxy.server}
    port: ${proxy.port?c}
    type: ${proxy.type}
    udp: true
    cipher: ${proxy.cipher}
    password: ${proxy.password}
    protocol: ${proxy.protocol}
    <#if proxy.protocolParam??>
    protocol-param: '${proxy.protocolParam}'
    </#if>
    obfs: ${proxy.obfs}
    <#if proxy.obfsParam??>
    obfs-param: '${proxy.obfsParam}'
    </#if>
  <#elseif proxy.type == "trojan">
  - name: '${proxy?counter} ${proxy.name}'
    server: ${proxy.server}
    port: ${proxy.port?c}
    type: ${proxy.type}
    udp: true
    password: ${proxy.password}
  </#if>
</#list>
proxy-groups:
  - name: Proxy
    type: select
    proxies:
      - url-test
      <#list proxies as proxy>
      <#if (proxy.type == "ssr" && proxy.cipher != "none" && proxy.cipher != "rc4" && proxy.cipher != "chacha20") || (proxy.type == "ss" && proxy.cipher != "rc4") || (proxy.type != "ssr" && proxy.type != "ss")>
      - '${proxy?counter} ${proxy.name}'
      </#if>
      </#list>
  - name: url-test
    type: url-test
    url: http://www.gstatic.com/generate_204
    interval: 300
    proxies:
      <#list proxies as proxy>
      <#if (proxy.type == "ssr" && proxy.cipher != "none" && proxy.cipher != "rc4" && proxy.cipher != "chacha20") || (proxy.type == "ss" && proxy.cipher != "rc4") || (proxy.type != "ssr" && proxy.type != "ss")>
      - '${proxy?counter} ${proxy.name}'
      </#if>
      </#list>
  - name: Direct
    type: select
    proxies:
      - DIRECT
      - Proxy
      - REJECT
  - name: Ad
    type: select
    proxies:
      - REJECT
      - DIRECT
      - Proxy
  - name: Other
    type: select
    proxies:
      - Proxy
      - DIRECT
      - REJECT
rules:
<#import "proxy_rules.ftl" as pr>
<#import "direct_rules.ftl" as dr>
<#import "final_rules.ftl" as fr>
<#list pr.rules as r>
  - ${r}
</#list>
<#list dr.rules as r>
  - ${r}
</#list>
<#list fr.rules as r>
  - ${r}
</#list>
  - MATCH,Other