package org.fcx.fq.entity.proxy.clash;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.fcx.fq.exception.MyToolException;
import org.fcx.fq.util.MyToolUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString(callSuper = true)
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SS extends Proxy {
    private String cipher;
    private String password;
    private boolean udp = true;
    private String plugin;
    @JsonProperty("plugin-opts")
    private Map<String,String> pluginOpts;

    public SS() {
        super("ss");
    }

    public SS(String link) {
        super("ss");
        String raw = link.substring(5);
        try {
            String urlDecoded = URLDecoder.decode(raw.trim(), StandardCharsets.UTF_8.name());
            log.debug(urlDecoded);
            String p0 = "[a-zA-z0-9+/=]+/#\\S+";
            String p1 = "[a-zA-z0-9+/=]+#.+";
            String p2 = "[a-zA-Z0-9+/=]+@\\S+/?\\?plugin=\\S+";
            String p3 = "[a-zA-Z0-9+/=]+@\\S+#.+";
            if(urlDecoded.matches(p0)){
                String[] fields = urlDecoded.split("/#");
                setName(fields[1]);
                String serverDecoded = MyToolUtil.base64Decode(fields[0]);
                int lastAt = serverDecoded.lastIndexOf("@");
                String encryptStr = serverDecoded.substring(0,lastAt);
                String serverStr = serverDecoded.substring(lastAt+1);
                String[] encryptFields = encryptStr.split(":");
                this.cipher = encryptFields[0];
                this.password = encryptFields[1];
                String[] serverFields = serverStr.split(":");
                setServer(serverFields[0]);
                setPort(Integer.parseInt(serverFields[1]));
            }else if(urlDecoded.matches(p1)){
                String[] fields = urlDecoded.split("#");
                setName(fields[1]);
                String serverDecoded = MyToolUtil.base64Decode(fields[0]);
                int lastAt = serverDecoded.lastIndexOf("@");
                String encryptStr = serverDecoded.substring(0,lastAt);
                String serverStr = serverDecoded.substring(lastAt+1);
                String[] encryptFields = encryptStr.split(":");
                this.cipher = encryptFields[0];
                this.password = encryptFields[1];
                String[] serverFields = serverStr.split(":");
                setServer(serverFields[0]);
                setPort(Integer.parseInt(serverFields[1]));
            }else if(urlDecoded.matches(p2)){
                String[] fields = urlDecoded.split("/?\\?");
                this.proxyInfo(fields[0]);
                String[] otherFields = fields[1].split("#");
                String[] pluginFields = otherFields[0].split(";");
                String[] pluginKV = pluginFields[0].split("=");
                if("obfs-local".equals(pluginKV[1])){
                    this.plugin = "obfs";
                    this.pluginOpts = new HashMap<>();
                    this.pluginOpts.put("mode",pluginFields[1].split("=")[1]);
                    this.pluginOpts.put("host",pluginFields[2].split("=")[1]);
                }
                setName(otherFields[1]);
            } else if (urlDecoded.matches(p3)) {
                // demo YWVzLTI1Ni1nY206ZmFCQW9ENTRrODdVSkc3@172.107.233.230:2376#(已存活1天)西班牙-2.94MB%
                String[] fields = urlDecoded.split("#");
                // String name = URLDecoder.decode(fields[1],"UTF-8");
                setName(fields[1]);
                this.proxyInfo(fields[0]);
            }else {
                String serverDecoded = MyToolUtil.base64Decode(raw);
                String noName = "\\S+:\\S+@\\S+:\\d+";
                if(serverDecoded.matches(noName)){
                    int lastAt = serverDecoded.lastIndexOf("@");
                    String encryptStr = serverDecoded.substring(0,lastAt);
                    String serverStr = serverDecoded.substring(lastAt+1);
                    String[] encryptFields = encryptStr.split(":");
                    this.cipher = encryptFields[0];
                    this.password = encryptFields[1];
                    String[] serverFields = serverStr.split(":");
                    setName(serverFields[0]);
                    setServer(serverFields[0]);
                    setPort(Integer.parseInt(serverFields[1]));
                } else {
                    throw new MyToolException("unsupport ss link format : " + urlDecoded);
                }
            }
        } catch (UnsupportedEncodingException e) {
            MyToolException myex = new MyToolException("ss link url decode failed : "+e.getMessage());
            myex.setStackTrace(e.getStackTrace());
            throw myex;
        }
    }

    private void proxyInfo(String proxyStr) {
        String[] proxyFields = proxyStr.split("@");
        String encryptDecoded = MyToolUtil.base64Decode(proxyFields[0]);
        String[] encryptFields = encryptDecoded.split(":");
        this.cipher = encryptFields[0];
        this.password = encryptFields[1];
        String[] serverFields = proxyFields[1].split(":");
        setServer(serverFields[0]);
        setPort(Integer.parseInt(serverFields[1]));
    }
}
