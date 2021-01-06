package com.anji.sp.service.impl;

import com.alibaba.fastjson.JSON;
import com.anji.sp.enums.RepCodeEnum;
import com.anji.sp.enums.UserStatus;
import com.anji.sp.mapper.SpAppLogMapper;
import com.anji.sp.mapper.SpAppReleaseMapper;
import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.po.SpAppLogPO;
import com.anji.sp.model.po.SpAppReleasePO;
import com.anji.sp.model.po.SpApplicationPO;
import com.anji.sp.model.vo.SpAppLogVO;
import com.anji.sp.model.vo.SpAppReqDataVO;
import com.anji.sp.model.vo.SpAppReqVO;
import com.anji.sp.model.vo.SpNoticeForAppVO;
import com.anji.sp.model.vo.SpNoticeVO;
import com.anji.sp.model.vo.SpVersionAppTempVO;
import com.anji.sp.model.vo.SpVersionForAPPVO;
import com.anji.sp.model.vo.SpVersionVO;
import com.anji.sp.service.RedisService;
import com.anji.sp.service.SpAppDeviceService;
import com.anji.sp.service.SpAppReqService;
import com.anji.sp.service.SpApplicationService;
import com.anji.sp.service.SpNoticeService;
import com.anji.sp.service.SpVersionService;
import com.anji.sp.util.APPVersionCheckUtil;
import com.anji.sp.util.Constants;
import com.anji.sp.util.IPUntils;
import com.anji.sp.util.RSAUtil;
import com.anji.sp.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SpAppReqServiceImpl implements SpAppReqService {
    // APP是否开启加密请求
    @Value("${version.need.decrypt}")
    private boolean needDecrypt;

    //默认毫秒数 7 天
    @Value("${version.timestamp.default}")
    private int defaultTimeStamp;

    //默认毫秒数 1 天
    @Value("${version.timestamp.one}")
    private int defaultTimeStampOneDay;

    @Resource
    private SpAppLogMapper spAppLogMapper;

    @Resource
    private SpApplicationService spApplicationService;

    @Resource
    private SpNoticeService spNoticeService;

    @Resource
    private SpVersionService spVersionService;

    @Resource
    private RedisService redisService;

    @Resource
    private SpAppDeviceService spAppDeviceService;
    @Resource
    private RedissonClient redissonClient;

    @Resource
    private SpAppReleaseMapper spAppReleaseMapper;



    /**
     * 初始化保存用户设备信息
     *
     * @param spAppReqDataVO
     * @param request
     * @return
     */
    @Override
    public ResponseModel deviceInit(SpAppReqDataVO spAppReqDataVO, HttpServletRequest request) {
        log.info("deviceInit");
        //校验
        ResponseModel responseModel = checkReq(spAppReqDataVO, request, "1", "初始化");
        //不通过返回
        if (!responseModel.isSuccess()) {
            return responseModel;
        }
        SpAppReqVO reqVO = (SpAppReqVO) responseModel.getRepData();
        int i = spAppLogMapper.insert(reqVO.getSpAppLogPO());
        CompletableFuture.supplyAsync(() -> spAppDeviceService.updateDeviceInfo(reqVO.getSpAppLogPO()));

        if (i > 0) {
            return ResponseModel.success();
        }
        return ResponseModel.errorMsg("初始化失败");
    }


    /**
     * 获取APP 版本 信息
     *
     * @param spAppReqDataVO
     * @param request
     * @return
     */
    @Override
    public ResponseModel getAppVersion(SpAppReqDataVO spAppReqDataVO, HttpServletRequest request) {
        log.info("getAppVersion");

        //校验
        ResponseModel responseModel = checkReq(spAppReqDataVO, request, "2", "版本信息");
        //不通过返回
        if (!responseModel.isSuccess()) {
            return responseModel;
        }
        SpAppReqVO reqVO = (SpAppReqVO) responseModel.getRepData();
        SpAppLogPO spAppLogPO = reqVO.getSpAppLogPO();
        SpApplicationPO spApplicationPO = reqVO.getSpApplicationPO();
        if (StringUtils.isEmpty(spAppLogPO.getPlatform())) {
            return ResponseModel.errorMsg("获取失败");
        }
        if (StringUtils.isEmpty(spAppLogPO.getOsVersion())) {
            responseModel.setRepCodeEnum(RepCodeEnum.OS_VERSION_INVALID);
            return responseModel;
        }

        if ("Android".equals(spAppLogPO.getPlatform())) {
            if (StringUtils.isEmpty(spAppLogPO.getVersionCode())) {
                responseModel.setRepCodeEnum(RepCodeEnum.APP_VERSION_INVALID);
                return responseModel;
            }
        }

        if ("iOS".equals(spAppLogPO.getPlatform())) {
            if (StringUtils.isEmpty(spAppLogPO.getVersionName())) {
                responseModel.setRepCodeEnum(RepCodeEnum.APP_VERSION_INVALID);
                return responseModel;
            }
        }

        //异步保存log
        CompletableFuture.supplyAsync(() -> spAppLogMapper.insert(spAppLogPO));
        CompletableFuture.supplyAsync(() -> spAppDeviceService.updateDeviceInfo(spAppLogPO));
        try {
            SpVersionVO spVersionVO = new SpVersionVO();
            spVersionVO.setPlatform(spAppReqDataVO.getData().getPlatform());
            spVersionVO.setAppId(spApplicationPO.getAppId());
            SpVersionVO vo = spVersionService.getEffectiveVersionVO(spVersionVO);
            //处理灰度发布
            long start = System.currentTimeMillis();
            boolean success = canaryReleaseConfig(spAppReqDataVO.getData(), vo);
            long end = System.currentTimeMillis();
            log.info("canaryReleaseConfig time: {}", end - start);
            if (!success) { //不返回信息
                return ResponseModel.successData(null);
            }
            if (Objects.nonNull(vo)) {
                SpVersionAppTempVO spVersionAppTempVO = new SpVersionAppTempVO();
                BeanUtils.copyProperties(vo, spVersionAppTempVO);
                //os版本号
                String osVersion = APPVersionCheckUtil.getOSVersion(spAppLogPO.getOsVersion()) + "";
                SpVersionForAPPVO spVersionForAPPVO = new SpVersionForAPPVO();
                BeanUtils.copyProperties(spVersionAppTempVO, spVersionForAPPVO);
                //版的数据中的版本号是否大于接口传过来的版本号
                boolean showUpdate = false;
                if ("Android".equals(spAppLogPO.getPlatform())) {
                    //数据中的versionNumber是否大于接口传过来的versionCode
                    showUpdate = Integer.parseInt(vo.getVersionNumber().trim()) > Integer.parseInt(spAppLogPO.getVersionCode().trim());
                }

                if ("iOS".equals(spAppLogPO.getPlatform())) {
                    int v = APPVersionCheckUtil.compareAppVersion(spAppLogPO.getVersionName(), spVersionAppTempVO.getVersionName());
                    log.info("compareAppVersion {}", v);
                    showUpdate = v > 0;
                }

                spVersionForAPPVO.setShowUpdate(showUpdate);
                //如果不需要更新 强制更新也为false 否则进行处理
                if (!showUpdate) {
                    spVersionForAPPVO.setMustUpdate(false);
                } else {
                    //[1.1.1,1.1.2,1.1.3]
                    if (Objects.isNull(vo.getNeedUpdateVersionList())) {
                        vo.setNeedUpdateVersionList(new ArrayList<>());
                    }
                    //[10,11,12]
                    if (Objects.isNull(vo.getVersionConfigStrList())) {
                        vo.setVersionConfigStrList(new ArrayList<>());
                    }
                    spVersionForAPPVO.setMustUpdate(vo.getNeedUpdateVersionList().contains(spAppLogPO.getVersionName())
                            || vo.getVersionConfigStrList().contains(osVersion));
//                        spVersionForAPPVO.setMustUpdate(spVersionAppTempVO.getVersionConfig().contains(osVersion));
                }
                return ResponseModel.successData(spVersionForAPPVO);
            }
        } catch (Exception e) {
            return ResponseModel.errorMsg(e.getMessage());
        }
        return ResponseModel.errorMsg("获取失败");
    }

    /**
     * 获取APP 公告 信息
     *
     * @param spAppReqDataVO
     * @param request
     * @return
     */
    @Override
    public ResponseModel getAppNotice(SpAppReqDataVO spAppReqDataVO, HttpServletRequest request) {
        log.info("getAppNotice");
        //校验
        ResponseModel responseModel = checkReq(spAppReqDataVO, request, "3", "公告信息");
        //不通过返回
        if (!responseModel.isSuccess()) {
            return responseModel;
        }
        SpAppReqVO reqVO = (SpAppReqVO) responseModel.getRepData();
        SpApplicationPO spApplicationPO = reqVO.getSpApplicationPO();
        //异步保存log
        CompletableFuture.supplyAsync(() -> spAppLogMapper.insert(reqVO.getSpAppLogPO()));
        CompletableFuture.supplyAsync(() -> spAppDeviceService.updateDeviceInfo(reqVO.getSpAppLogPO()));
        try {
            SpNoticeVO spNoticeVO = new SpNoticeVO();
            spNoticeVO.setAppId(spApplicationPO.getAppId());
            List<SpNoticeForAppVO> notices = spNoticeService.getNotices(spNoticeVO);
            return ResponseModel.successData(notices);
        } catch (Exception e) {
            return ResponseModel.errorMsg(e.getMessage());
        }
    }

    /**
     * 处理灰度发布策略
     *
     * @param spAppLogVO
     * @param vo
     * @return 是否可以发送数据
     */
    private boolean canaryReleaseConfig(SpAppLogVO spAppLogVO, SpVersionVO vo) {

        //1、接口信息及版本信息 判断 deviceId、appkey、platform是否存在  不存在 不返回信息
        if (Objects.isNull(vo) || Objects.isNull(spAppLogVO)
                || StringUtils.isEmpty(spAppLogVO.getDeviceId())
                || StringUtils.isEmpty(spAppLogVO.getAppKey())
                || StringUtils.isEmpty(spAppLogVO.getPlatform())) {
            return false;
        }
        //2、Android
        if ("Android".equals(spAppLogVO.getPlatform())) {
            //数据中的versionNumber是否大于接口传过来的versionCode
            //app版本是否大于等于数据版本 跳过 返回信息
            if (StringUtils.isNotEmpty(spAppLogVO.getVersionName())
                    && StringUtils.isNotEmpty(vo.getVersionName())
                    && Integer.parseInt(vo.getVersionNumber().trim()) <= Integer.parseInt(spAppLogVO.getVersionCode().trim())) {
                return true;
            }
        }
        // 3、iOS
        if ("iOS".equals(spAppLogVO.getPlatform())) {
            // APP版本>= 数据版本 跳过
            if (StringUtils.isNotEmpty(spAppLogVO.getVersionName())
                    && StringUtils.isNotEmpty(vo.getVersionName())
                    && APPVersionCheckUtil.compareAppVersion(vo.getVersionName(), spAppLogVO.getVersionName()) > -1) {
                return true;
            }
        }

        //4、不开启灰度发布直接跳过 展示数据
        //开启时间没有直接跳过 展示数据
        //灰度发布时间超过7天直接跳过 展示数据
        if (vo.getCanaryReleaseEnable() == UserStatus.DISABLE.getIntegerCode()
                || Objects.isNull(vo.getEnableTime())
                || vo.getCanaryReleaseUseTime() > defaultTimeStamp) {
            return true;
        }

        //5、读取Redis中对应 appKey_versionName  为key是否包含对应deviceID
        String cacheKey = Constants.APP_VERSION_KEYS + spAppLogVO.getAppKey() + "_" + spAppLogVO.getPlatform() + "_" + vo.getVersionName();
        RLock redissionLock = redissonClient.getLock(cacheKey);
        try {
            redissionLock.lock(30, TimeUnit.SECONDS);
            //返回自从GMT 1970-01-01 00:00:00到此date对象上时间的毫秒数
            Long enableTimeStamp = vo.getEnableTime().getTime();
            //当前时间戳 毫秒数
            Long nowTimeStamp = System.currentTimeMillis();
            //相差时间戳
            Long gap = nowTimeStamp - enableTimeStamp;
            //已用时间
            Long canaryReleaseUseTime = vo.getOldCanaryReleaseUseTime();
            //新已用时间
            Long newCanaryReleaseUseTime = canaryReleaseUseTime + gap;
            if (newCanaryReleaseUseTime < 0) {
                newCanaryReleaseUseTime = 0L;
            }

            //未用时间
            Long nowGap = defaultTimeStamp - newCanaryReleaseUseTime;
            //如果小于0 代表灰度发布已结束
            if (nowGap < 0) {
                spVersionService.updateSpVersionTime(vo);
                return true;
            }

            //灰度发布确认
            if (Objects.nonNull(vo.getCanaryReleaseStageList()) && vo.getCanaryReleaseStageList().size() == 7) {
                //已用时间除以每天的时间戳 得到当前是第几天
                int c = (new Long(newCanaryReleaseUseTime).intValue()) / defaultTimeStampOneDay;
                //1、拿到百分比 比如0.4 （数据库）
                double percentage = Integer.parseInt(vo.getCanaryReleaseStageList().get(c)) / 100.0;
                //2、如果大于1 全部发布 运行请求
                if (percentage >= 1) {
                    spVersionService.updateSpVersionTime(vo);
                    return true;
                }
                //3、查询当前appKey所有deviceID(去重) count

                Long deviceIdCount = Long.valueOf(spAppDeviceService.selectCount(spAppLogVO));
                //4、count * 0.4 取整
                int reqCount = new Double((new Long(deviceIdCount).intValue()) * percentage).intValue();
                //如果数值少于1，也代表全部
                if (reqCount < 1) {
                    spVersionService.updateSpVersionTime(vo);
                    return true;
                }
                log.info("sql cacheKey: {}", cacheKey);

                log.info("sql deviceIdCount: {}", deviceIdCount);

                log.info("sql reqCount: {}", reqCount);

                QueryWrapper<SpAppReleasePO> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("app_key", spAppLogVO.getAppKey());
                queryWrapper.eq("version_name", vo.getVersionName());
                //查询灰度发布已经收到版本更新接口的用户设备唯一标识表
                List<SpAppReleasePO> spAppReleasePOS = spAppReleaseMapper.selectList(queryWrapper);
                log.info("sql spAppReleasePOS: {}", spAppReleasePOS);

                //将列表唯一标示转换为 string list
                List<String> cacheList =  spAppReleasePOS.stream().map(s -> s.getDeviceId()).collect(Collectors.toList());
                log.info("sql cacheList: {}", cacheList);

                //为空代表没有数据需要添加
                if (StringUtils.isEmpty(cacheList) || cacheList.size() == 0) {
                    int i = insertReleasePO(spAppLogVO, vo);
                    log.info("insertReleasePO: {}", i);
                    spVersionService.updateSpVersionTime(vo);
                    return true;
                }

                //如果不为空 判断
                //6、如果包含 运行拿到数据 return 1
                if (cacheList.contains(spAppLogVO.getDeviceId())) {
                    return true;
                }

                //7、如果不包含 Redis中数据条数与灰度数目进行比较  r 和 c
                //8、如果 r > c return 0 不允许返回
                if (cacheList.size() >= reqCount) {
                    return false;
                }
                //9、如果 r < c  返回1   并保持到Redis中
                int i = insertReleasePO(spAppLogVO, vo);
                log.info("insertReleasePO: {}", i);

                //10、最后将 newCanaryReleaseUseTime 更新到version数据表中
                spVersionService.updateSpVersionTime(vo);
                return true;
            }
            return false;
        } finally {
            redissionLock.unlock();
        }
    }

    private int insertReleasePO(SpAppLogVO spAppLogVO,SpVersionVO vo){
        SpAppReleasePO s = new SpAppReleasePO();
        s.setAppKey(spAppLogVO.getAppKey());
        s.setDeviceId(spAppLogVO.getDeviceId());
        s.setVersionName(vo.getVersionName());
        return spAppReleaseMapper.insert(s);
    }

    /**
     * request转换ip地址及ip信息
     *
     * @param spAppLogVO
     * @param request
     * @return
     */
    private SpAppLogVO voConvert(SpAppLogVO spAppLogVO, HttpServletRequest request) {
        String ipAddress = IPUntils.getIpAddress(request);
        String regional = IPUntils.getInstance().getCityInfo(ipAddress);
        if (Objects.isNull(regional)) {
            regional = "0|0|0|0|0";
        }
        log.info("----> ipAddress {}, regional {}", ipAddress, regional);
        spAppLogVO.setIp(ipAddress);
        spAppLogVO.setRegional(regional);

        return spAppLogVO;
    }


    /**
     * 基础数据校验 加密解密
     *
     * @param spAppReqDataVO
     * @param request
     * @param faceType
     * @param interfaceTypeName
     * @return
     */
    private ResponseModel checkReq(SpAppReqDataVO spAppReqDataVO, HttpServletRequest request, String faceType, String interfaceTypeName) {
        ResponseModel responseModel = new ResponseModel();
        SpAppLogVO vo = spAppReqDataVO.getData();
        //appkey为空
        if (StringUtils.isEmpty(vo.getAppKey())) {
            responseModel.setRepCodeEnum(RepCodeEnum.LACK_DATA_APP_KEY);
            return responseModel;
        }

        //deviceId为空
        if (StringUtils.isEmpty(vo.getDeviceId())) {
            responseModel.setRepCodeEnum(RepCodeEnum.LACK_DATA_DEVICE_ID);
            return responseModel;
        }

        log.info("spAppReqDataVO -- > {}", spAppReqDataVO);

        try {
            //获取application信息
            SpApplicationPO spApplicationPO = spApplicationService.selectByAppKey(vo.getAppKey());
            //appKey无效
            if (Objects.isNull(spApplicationPO)) {
                responseModel.setRepCodeEnum(RepCodeEnum.INVALID_FORMAT_APP_KEY);
                return responseModel;
            }
            SpAppLogVO spAppLogVO = vo;
            if (needDecrypt) {
                //------------------RSA解密-------------------
                log.info("spApplicationPO -- > {}", spApplicationPO);
                //解密
                String decryptData = RSAUtil.decrypt(spAppReqDataVO.getSign(), RSAUtil.getPrivateKey(spApplicationPO.getPrivateKey()));
                log.info("解密decryptData -- > {}", decryptData);
                spAppLogVO = JSON.parseObject(decryptData, SpAppLogVO.class);
                log.info("vo -- > {}", vo);
                log.info("解密解析 spAppLogVO -- > {}", spAppLogVO);
                //解密时效
                if (Objects.isNull(spAppLogVO)) {
                    responseModel.setRepCodeEnum(RepCodeEnum.DATA_PARSING_INVALID);
                    return responseModel;
                }
                //appKey不一致
                if (!vo.getAppKey().equals(spAppLogVO.getAppKey())) {
                    responseModel.setRepCodeEnum(RepCodeEnum.INVALID_FORMAT_APP_KEY);
                    return responseModel;
                }
                //设备id不一致
                if (!vo.getDeviceId().equals(spAppLogVO.getDeviceId())) {
                    responseModel.setRepCodeEnum(RepCodeEnum.DATA_REQUEST_INVALID);
                    return responseModel;
                }
                //-----------------------------------------------------------
            }

            spAppLogVO = voConvert(spAppLogVO, request);
            spAppLogVO.setInterfaceType(faceType);
            spAppLogVO.setInterfaceTypeName(interfaceTypeName);

            SpAppLogPO spAppLogPO = new SpAppLogPO();
            BeanUtils.copyProperties(spAppLogVO, spAppLogPO);
            spAppLogPO.setCreateDate(new Date());
            spAppLogPO.setCreateBy(1L);
            spAppLogPO.setUpdateDate(new Date());
            spAppLogPO.setUpdateBy(1L);

            //存储数据结构(应用信息、appLog信息)
            SpAppReqVO reqVO = new SpAppReqVO();
            reqVO.setSpAppLogPO(spAppLogPO);
            reqVO.setSpApplicationPO(spApplicationPO);
            responseModel.setRepData(reqVO);
            responseModel.setRepCodeEnum(RepCodeEnum.SUCCESS);
        } catch (Exception e) {
            responseModel.setRepCodeEnum(RepCodeEnum.ERROR);
            responseModel.setRepData(e.getMessage());
        }
        return responseModel;
    }
}
