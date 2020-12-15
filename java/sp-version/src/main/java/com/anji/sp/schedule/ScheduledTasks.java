package com.anji.sp.schedule;

import com.anji.sp.service.SpVersionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务类
 */

@Slf4j
@EnableScheduling
@Component
public class ScheduledTasks {
    @Autowired
    SpVersionService spVersionService;

    /**
     * t_alert_event表归档，每月1号将3个月前的那个月归档到t_alert_event_yyyyMM
     */
//    @Scheduled(cron = "0 30 1 1 * ?")//每月1号凌晨一点半
//    @Scheduled(cron = "0 30 1 1 4,7,10,1 ?")//每个季度1号凌晨一点半
    @Scheduled(cron = "0 30 1 * * *")//每天凌晨1:30触发
//    @Scheduled(cron = "30 * * * * ?")//测试(每30秒执行一次)
    public void deleteInvalidFile() {
        try {
            log.info("删除无效apk，每三个月1号凌晨一点半删除");
            spVersionService.deleteInvalidFile();
        } catch (Exception e) {
            log.error("删除无效apk，每三个月1号凌晨一点半删除异常", e);
        }
    }


}
