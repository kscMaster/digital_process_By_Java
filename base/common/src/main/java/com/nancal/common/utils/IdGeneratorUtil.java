package com.nancal.common.utils;


import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;

@Slf4j
public class IdGeneratorUtil {

    public static String generate() {
        long datacenterId = getDatacenterId();
        long workerId = getMaxWorkerId(datacenterId);
        Snowflake snowflake = IdUtil.getSnowflake(workerId, datacenterId);
        return snowflake.nextIdStr();
    }

    public static Long generateLong() {
        return Long.parseLong(generate());
    }

    /***
     * 通过当前物理网卡地址获取数据标识id部分
     *
     * @author 徐鹏军
     * @date 2022/3/30 9:30
     * @return {@link long}
     */
    protected static long getDatacenterId() {
        long id = 0L;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                id = 1L;
            } else {
                byte[] mac = network.getHardwareAddress();
                if (null != mac) {
                    id = ((0x000000FF & (long) mac[mac.length - 2]) | (0x0000FF00 & (((long) mac[mac.length - 1]) << 8))) >> 6;
                    id = id % ((long) 31 + 1);
                }
            }
        } catch (Exception e) {
            log.warn(" getDatacenterId异常", e);
        }
        return id;
    }

    /***
     * 物理网卡地址+jvm进程pi获取workerId
     *
     * @param datacenterId  数据标识id
     * @author 徐鹏军
     * @date 2022/3/30 9:31
     * @return {@link long}
     */
    protected static long getMaxWorkerId(long datacenterId) {
        StringBuilder mpid = new StringBuilder();
        mpid.append(datacenterId);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (StringUtils.isNotBlank(name)) {
            /*
             * GET jvmPid
             */
            mpid.append(name.split(StrPool.AT)[0]);
        }
        /*
         * MAC + PID 的 hashcode 获取16个低位
         */
        return (mpid.toString().hashCode() & 0xffff) % ((long) 31 + 1);
    }
}