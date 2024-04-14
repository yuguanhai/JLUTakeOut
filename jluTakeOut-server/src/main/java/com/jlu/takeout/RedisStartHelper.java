package com.jlu.takeout;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@Slf4j
class RedisStartHelper {

    private static final String REDIS_SERVER_EXE_PATH = "D:\\cs_tools\\Redis-x64-3.2.100\\redis-server.exe";
    private static final String CONFIG_FILE_PATH = "D:\\cs_tools\\Redis-x64-3.2.100\\redis.windows.conf"; // 指定配置文件路径

    public void ensureRedisRunning() throws Exception {
        log.info("正在检测redis服务是否启动...");
        if (!isRedisRunning()) {
            log.info("redis服务未启动，正在启动...");
            startRedis();
        }
    }

    private boolean isRedisRunning() {
        // 实际上，你应该使用类似Jedis或Lettuce等Redis Java客户端来尝试连接Redis服务器，
        // 并通过ping命令确认服务是否可用。此处仅给出概念性伪代码。
        // 请根据实际情况替换为真实检测Redis服务状态的代码。
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("tasklist").getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("redis-server")) {
                    return true;
                }
            }
        } catch (Exception e) {
            // 忽略异常，只是无法判断是否运行
        }
        return false;
    }

    private void startRedis() throws Exception {
        // 创建批处理命令
        Path batchFile = Paths.get("start_redis.bat");
        Files.write(batchFile,
                Arrays.asList("@echo off", "\"" + REDIS_SERVER_EXE_PATH + "\" \"" + CONFIG_FILE_PATH + "\""),
                StandardCharsets.UTF_8);

        // 执行批处理命令
        ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "start_redis.bat");
        pb.start();

        // 可能还需要等待Redis启动完成，可通过发送ping命令并等待响应的方式来确认启动成功
        Thread.sleep(2000); // 延迟一段时间，确保Redis有足够的时间启动
    }
}