package com.yutain;

import org.apache.commons.lang3.StringUtils;
import com.jcraft.jsch.Session;
import cn.hutool.extra.ssh.JschUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class})
@ComponentScans({
        @ComponentScan("com.yutian.common"),
        @ComponentScan("com.yutian.controller"),
        @ComponentScan("com.yutian.service")
})
public class checkOrderApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(checkOrderApplication.class);
    private final static String MAC_OS_FLAG = "MAC";
    private final static String WINDOWS_OS_FLAG = "WINDOWS";

    public static void main(String[] args) {
        // 为本地环境启动 acm 代理
//        acmProxy();
        SpringApplication.run(checkOrderApplication.class, args);
    }

    public static void acmProxy() {
        // 获得系统名称
        String osName = StringUtils.upperCase(System.getProperty("os.name"));
        // 如果是 mac 或 windows 系统（说明是本地环境）就开启代理
        if (StringUtils.containsAny(osName, MAC_OS_FLAG, WINDOWS_OS_FLAG)) {
            LOGGER.info("启动 ACM 访问代理,系统名称: {}", osName);
            //新建会话，此会话用于ssh连接到跳板机（堡垒机）
            Session session = JschUtil.getSession("114.55.5.52", 1105, "fshows", "9l#0gk7k");
            // 将堡垒机保护的内网8080端口映射到localhost，我们就可以通过访问http://localhost:8080/访问内网服务了
            JschUtil.bindPort(session, "acm.aliyun.com", 8080, 8080);
        }
    }


}
