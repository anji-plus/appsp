package com.anji.sp.service.impl;

import com.anji.sp.enums.IsDeleteEnum;
import com.anji.sp.enums.PlatformEnum;
import com.anji.sp.enums.UserStatus;
import com.anji.sp.mapper.SpVersionMapper;
import com.anji.sp.model.PageQueryRep;
import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.po.SpVersionPO;
import com.anji.sp.model.vo.SpVersionForAPPVO;
import com.anji.sp.model.vo.SpVersionVO;
import com.anji.sp.service.SpVersionService;
import com.anji.sp.util.APPVersionCheckUtil;
import com.anji.sp.util.SecurityUtils;
import com.anji.sp.util.StringUtils;
import com.anji.sp.util.file.FileProfileConfig;
import com.anji.sp.util.file.FileUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author : kean_qi
 * create at:  2020/6/26  11:36 上午
 * @description:
 */
@Service
@Slf4j
public class SpVersionServiceImpl implements SpVersionService {

    @Autowired
    SpVersionMapper spVersionMapper;

    //默认毫秒数 7 天
    @Value("${version.timestamp.default}")
    private int defaultTimeStamp;
    @Value("${file.apk.url:}")
    private String fileApkUrl;

    /**
     * 根据平台名称及应用id查询所有版本信息
     *
     * @param reqData
     * @return
     */
    @Override
    public ResponseModel getAppVersionByPlatformAndAppId(SpVersionVO reqData) {
        if (Objects.isNull(reqData.getAppId()) || reqData.getAppId() <= 0) {
            return ResponseModel.errorMsg("应用id不能为空");
        }
        log.info("reqData : {}", reqData);
        PageQueryRep<SpVersionVO> pageQueryRep = new PageQueryRep<>();

        if (reqData.getPageSize() != 999) {
            PageHelper.startPage(reqData.getPageNo(), reqData.getPageSize());
        }
        List<SpVersionPO> spRolePOS = getAppVersion(reqData);
        PageInfo<SpVersionPO> pageInfo = new PageInfo<>(spRolePOS);
        pageQueryRep.setTotal(pageInfo.getTotal());

        List<SpVersionVO> collects = spRolePOS.stream().map(s -> {
            SpVersionVO sp = new SpVersionVO();
            BeanUtils.copyProperties(s, sp);
            //系统版本号
            if (StringUtils.isNotEmpty(s.getVersionConfig())) {
                String versionConfig = s.getVersionConfig();
                sp.setVersionConfigStrList(Arrays.asList(versionConfig.split(",")));
            }

            //版本配置 APP版本号
            if (StringUtils.isNotEmpty(s.getNeedUpdateVersions())) {
                String needUpdateVersions = s.getNeedUpdateVersions();
                sp.setNeedUpdateVersionList(Arrays.asList(needUpdateVersions.split(",")));
            }

            //发布阶段
            if (StringUtils.isNotEmpty(s.getCanaryReleaseStage())) {
                String canaryReleaseStage = s.getCanaryReleaseStage();
                sp.setCanaryReleaseStageList(Arrays.asList(canaryReleaseStage.split(",")));
            }


            //更新时间 判断1、该数据是否开启 2、灰度发布是否开启 3、开始时间是否存在 4、已用时间是否小于计划灰度发布时间
            if (sp.getEnableFlag() == UserStatus.OK.getIntegerCode()
                    && sp.getCanaryReleaseEnable() == UserStatus.OK.getIntegerCode()
                    && Objects.nonNull(sp.getEnableTime())
                    && sp.getCanaryReleaseUseTime() < defaultTimeStamp) {
                //返回自从GMT 1970-01-01 00:00:00到此date对象上时间的毫秒数
                Long enableTimeStamp = sp.getEnableTime().getTime();
                //当前时间戳 毫秒数
                Long nowTimeStamp = new Date().getTime();
                //相差时间戳
                Long gap = nowTimeStamp - enableTimeStamp;
                //新已用时间
                Long newCanaryReleaseUseTime = sp.getOldCanaryReleaseUseTime() + gap;
                if (newCanaryReleaseUseTime < 0) {
                    newCanaryReleaseUseTime = 0L;
                }
                sp.setCanaryReleaseUseTime(newCanaryReleaseUseTime);
                updateAppVersion(sp);
            }
            return sp;
        }).collect(Collectors.toList());
        if (reqData.getPageNo() > pageInfo.getPageNum()) {
            pageQueryRep.setRows(new ArrayList<>());
        } else {
            pageQueryRep.setRows(collects);
        }
        return ResponseModel.successData(pageQueryRep);
    }

    //查询所有数据
    private List<SpVersionPO> getAppVersion(SpVersionVO reqData) {
        QueryWrapper<SpVersionPO> wrapper = new QueryWrapper<>();
        //只需要查询未删除的
        wrapper.eq("delete_flag", IsDeleteEnum.NOT_DELETE.getIntegerCode());
        if (StringUtils.isNotEmpty(reqData.getPlatform()) && ("iOS".contains(reqData.getPlatform()) || "Android".contains(reqData.getPlatform()))) {
            wrapper.eq("platform", reqData.getPlatform());
        }
        wrapper.eq("app_id", reqData.getAppId());
        wrapper.orderByDesc("id");
        List<SpVersionPO> spRolePOS = spVersionMapper.selectList(wrapper);
        return spRolePOS;
    }


    /**
     * 版本新增
     *
     * @param reqData
     * @return
     */
    @Override
    public ResponseModel insertVersion(SpVersionVO reqData) {

        String valid = checkVersionValid(reqData);
        if (StringUtils.isNotEmpty(valid)) {
            return ResponseModel.errorMsg(valid);
        }


        //新增对版本名版本号处理
        String checkValid = checkUpdateVersion(reqData, 0);
        if (StringUtils.isNotEmpty(checkValid)) {
            return ResponseModel.errorMsg(checkValid);
        }
        if (Objects.isNull(reqData.getCanaryReleaseEnable())
                || reqData.getCanaryReleaseEnable() != 1) {
            reqData.setCanaryReleaseEnable(UserStatus.DISABLE.getIntegerCode());
            reqData.setCanaryReleaseStageList(null);
        }

        SpVersionPO sp = new SpVersionPO();
        BeanUtils.copyProperties(reqData, sp);
        //需要更新的系统版本
        sp.setVersionConfig(StringUtils.join(reqData.getVersionConfigStrList(), ","));
        //需要更新的版本号
        if (Objects.isNull(reqData.getNeedUpdateVersionList())) {
            reqData.setNeedUpdateVersionList(new ArrayList<String>());
        }
        sp.setNeedUpdateVersions(StringUtils.join(reqData.getNeedUpdateVersionList(), ","));
        //灰度发布阶段
        if (Objects.nonNull(reqData.getCanaryReleaseStageList()) && reqData.getCanaryReleaseStageList().size() == 7) {
            sp.setCanaryReleaseStage(StringUtils.join(reqData.getCanaryReleaseStageList(), ","));
        }

        //默认停用
        sp.setEnableFlag(UserStatus.DISABLE.getIntegerCode());
        sp.setDeleteFlag(IsDeleteEnum.NOT_DELETE.getIntegerCode());
        sp.setUpdateBy(SecurityUtils.getUserId());
        sp.setCreateBy(SecurityUtils.getUserId());
        sp.setCreateDate(new Date());
        sp.setUpdateDate(new Date());
        sp.setOldCanaryReleaseUseTime(0L);

        int insert = spVersionMapper.insert(sp);
        if (insert > 0) {
            return ResponseModel.success();
        }
        return ResponseModel.errorMsg("新增失败");
    }

    /**
     * 更新版本
     *
     * @param reqData
     * @return
     */
    @Override
    public ResponseModel updateVersion(SpVersionVO reqData) {
        String valid = checkVersionValid(reqData);
        if (StringUtils.isNotEmpty(valid)) {
            return ResponseModel.errorMsg(valid);
        }
        if (Objects.isNull(reqData.getCanaryReleaseEnable())) {
            reqData.setCanaryReleaseEnable(UserStatus.DISABLE.getIntegerCode());
        }
        SpVersionPO po = spVersionMapper.selectById(reqData.getId());
        //发布状态为true  下载地址、版本名、版本号、灰度发布状态 不能修改
        if (po.getPublished().intValue() == UserStatus.OK.getIntegerCode()) {
            if (!StringUtils.equals(po.getDownloadUrl(), reqData.getDownloadUrl())) {
                return ResponseModel.errorMsg("下载地址不能修改");
            } else if (!StringUtils.equals(po.getVersionName(), reqData.getVersionName())) {
                return ResponseModel.errorMsg("版本名不能修改");
            } else if (!StringUtils.equals(po.getVersionNumber(), reqData.getVersionNumber())) {
                return ResponseModel.errorMsg("版本号不能修改");
            } else if ((po.getCanaryReleaseEnable() != reqData.getCanaryReleaseEnable())) {
                return ResponseModel.errorMsg("灰度发布状态不能修改");
            }
        } else { //发布状态为false 需进行参数校验  设置灰度发布信息
            String checkValid = checkUpdateVersion(reqData, 1);
            if (StringUtils.isNotEmpty(checkValid)) {
                return ResponseModel.errorMsg(checkValid);
            }
            if (Objects.isNull(reqData.getCanaryReleaseEnable())
                    || reqData.getCanaryReleaseEnable() != 1) {
                reqData.setCanaryReleaseEnable(UserStatus.DISABLE.getIntegerCode());
                reqData.setCanaryReleaseStageList(null);
            }
        }

        if (updateAppVersion(reqData) > 0) {
            return ResponseModel.success();
        }
        return ResponseModel.errorMsg("更新失败");
    }

    /**
     *  情况下校验更新信息
     * @param vo
     * @param checkIndex 新增 校验第1条  更新校验第二条
     * @return
     */
    private String checkUpdateVersion(SpVersionVO vo, int checkIndex) {
        //新增对版本名版本号处理
        List<SpVersionPO> spRolePOS = getAppVersion(vo);
        if (spRolePOS.size() > checkIndex) {
            SpVersionPO versionInfo = spRolePOS.get(checkIndex);
            if (APPVersionCheckUtil.compareAppVersion(versionInfo.getVersionName(), vo.getVersionName()) < 1) {
                return "版本名不能低于" + versionInfo.getVersionName();
            }

            if ("Android".contains(vo.getPlatform())) {
                if (APPVersionCheckUtil.strToInt(versionInfo.getVersionNumber()) >= APPVersionCheckUtil.strToInt(vo.getVersionNumber())) {
                    return "版本号不能低于" + versionInfo.getVersionNumber();
                }
            }
        }
        //灰度发布  默认canaryReleaseEnable == 0
        if (Objects.nonNull(vo.getCanaryReleaseEnable()) && vo.getCanaryReleaseEnable() == 1) {
            if (Objects.isNull(vo.getCanaryReleaseStageList())) {
                return "灰度发布选择项数据有误";
            }
            if (vo.getCanaryReleaseStageList().size() != 7) {
                return "灰度发布选择项数据有误";
            }
            if (vo.getCanaryReleaseStageList().size() == 7) {
                for (int i = 0; i < 7; i++) {
                    if (Integer.parseInt(vo.getCanaryReleaseStageList().get(i)) == 0) {
                        return "灰度发布每个阶段不能为0";
                    }
                    //如果前者大于后者
                    if (i > 0 && Integer.parseInt(vo.getCanaryReleaseStageList().get(i - 1)) > (Integer.parseInt(vo.getCanaryReleaseStageList().get(i)))) {
                        return "灰度发布后一天的值不能小于前一天的";
                    }
                }
            }
        }

        return "";
    }

    /**
     * 更新版本信息 不做校验
     *
     * @param reqData
     * @return
     */
    @Override
    public int updateAppVersion(SpVersionVO reqData) {
        SpVersionPO sp = new SpVersionPO();
        BeanUtils.copyProperties(reqData, sp);
        //版本配置 系统版本
        sp.setVersionConfig(StringUtils.join(reqData.getVersionConfigStrList(), ","));
        if (Objects.isNull(reqData.getNeedUpdateVersionList())) {
            reqData.setNeedUpdateVersionList(new ArrayList<String>());
        }
        //版本配置 APP版本
        sp.setNeedUpdateVersions(StringUtils.join(reqData.getNeedUpdateVersionList(), ","));
        //灰度发布阶段
        sp.setCanaryReleaseStage(StringUtils.join(reqData.getCanaryReleaseStageList(), ","));
        sp.setUpdateBy(SecurityUtils.getUserId());
        sp.setUpdateDate(new Date());
        int update = spVersionMapper.updateById(sp);
        if (update > 0) {
            //查询最新一条enable为true的数据 并保持到json文件
//            boolean b = appJsonService.writeVersionInfo(sp.getAppId(), sp.getPlatform());
            return 1;
        }
        return 0;
    }


    /**
     * 判空操作
     *
     * @param reqData
     * @return
     */
    private String checkVersionValid(SpVersionVO reqData) {
        if (Objects.isNull(reqData.getAppId()) || reqData.getAppId() <= 0) {
            return "应用Id传参错误";
        } else if (StringUtils.isEmpty(reqData.getPlatform())) {
            return "平台类型不能为空";
        } else if (StringUtils.isEmpty(reqData.getVersionName())) {
            return "版本名不能为空";
        } else if (StringUtils.isEmpty(reqData.getVersionNumber())) {
            return "版本号不能为空";
        } else if (StringUtils.isEmpty(reqData.getUpdateLog())) {
            return "更新日志不能为空";
        } else if (StringUtils.isEmpty(reqData.getDownloadUrl())) {
            return "下载地址不能为空";
        } else if (!APPVersionCheckUtil.isVersion(reqData.getVersionName())) {
            return "版本名输入有误";
        } else if ("Android".contains(reqData.getPlatform()) && APPVersionCheckUtil.strToInt(reqData.getVersionNumber()) == 0) {
            return "版本号输入有误";
        }
        return "";
    }


    /**
     * 删除版本
     *
     * @param reqData
     * @return
     */
    @Override
    public ResponseModel deleteVersion(SpVersionVO reqData) {
        if (Objects.isNull(reqData.getId()) || reqData.getId() <= 0) {
            return ResponseModel.errorMsg("Id传参错误");
        } else if (Objects.isNull(reqData.getAppId())) {
            return ResponseModel.errorMsg("appId不能为空");
        }
        SpVersionPO spVersionPO = spVersionMapper.selectById(reqData.getId());
        spVersionPO.setDeleteFlag(IsDeleteEnum.DELETE.getIntegerCode());
        int update = spVersionMapper.updateById(spVersionPO);
        if (update <= 0) {
            return ResponseModel.errorMsg("删除失败");
        }
        //查询最新一条enable为true的数据 并保持到json文件
//        boolean b = appJsonService.writeVersionInfo(spVersionPO.getAppId(), spVersionPO.getPlatform());
//        if (!b) System.out.println("更新json失败");
        return ResponseModel.success();
    }

    /**
     * 启用或禁用版本
     *
     * @param reqData
     * @return
     */
    @Override
    public ResponseModel enableVersion(SpVersionVO reqData) {
        System.out.println("---------");
        System.out.println(reqData);
        if (Objects.isNull(reqData.getId()) || reqData.getId() <= 0) {
            return ResponseModel.errorMsg("Id传参错误");
        } else if (Objects.isNull(reqData.getEnableFlag()) || reqData.getEnableFlag() < 0 || reqData.getEnableFlag() > 1) {
            return ResponseModel.errorMsg("启用状态有误");
        } else if (Objects.isNull(reqData.getAppId())) {
            return ResponseModel.errorMsg("appId不能为空");
        }
        SpVersionPO spVersionPO = spVersionMapper.selectById(reqData.getId());
        if (Objects.isNull(spVersionPO)) {
            return ResponseModel.errorMsg("未查询带该条数据，更新失败");
        }
        spVersionPO.setEnableFlag(reqData.getEnableFlag());
        //如果是启用状态 更新启用时间
        if (spVersionPO.getEnableFlag() == UserStatus.OK.getIntegerCode()) {
            spVersionPO.setEnableTime(new Date());
        }
        //启用时将已用时间赋值给老已用时间 用于计算
        spVersionPO.setOldCanaryReleaseUseTime(spVersionPO.getCanaryReleaseUseTime());
        //先禁用该appId下所有对应平台的数据
        UpdateWrapper<SpVersionPO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("app_id", reqData.getAppId());
        updateWrapper.eq("platform", reqData.getPlatform());
        updateWrapper.eq("enable_flag", UserStatus.OK.getIntegerCode());
        SpVersionPO po = new SpVersionPO();
        po.setEnableFlag(UserStatus.DISABLE.getIntegerCode());
        //不管是否启用 都设置发布状态为true
        spVersionPO.setPublished(UserStatus.OK.getIntegerCode());

        //更新所有状态为禁用状态
        spVersionMapper.update(po, updateWrapper);
        //更新启用单条数据
        int update = spVersionMapper.updateById(spVersionPO);
        if (update <= 0) {
            return ResponseModel.errorMsg("更新失败");
        }
        //查询最新一条enable为true的数据 并保持到json文件
//        boolean b = appJsonService.writeVersionInfo(spVersionPO.getAppId(), spVersionPO.getPlatform());
//        if (!b) System.out.println("更新json失败");
        return ResponseModel.success();
    }

    /**
     * 查询有效的APP version
     *
     * @param spVersionVO
     * @return
     */
    @Override
    public SpVersionForAPPVO getEffectiveVersion(SpVersionVO spVersionVO) {
        SpVersionPO versionPO = getVersionPO(spVersionVO);
        if (Objects.nonNull(versionPO)) {
            SpVersionForAPPVO vappp = new SpVersionForAPPVO();
            BeanUtils.copyProperties(versionPO, vappp);
            log.info("getEffectiveVersion SpVersionForAPPVO to : {}", vappp);
            return vappp;
        }
        return null;
    }

    /**
     * 获取有效版本VO
     *
     * @param spVersionVO
     * @return
     */
    @Override
    public SpVersionVO getEffectiveVersionVO(SpVersionVO spVersionVO) {
        SpVersionPO s = getVersionPO(spVersionVO);
        if (Objects.nonNull(s)) {
            SpVersionVO sp = new SpVersionVO();
            BeanUtils.copyProperties(s, sp);
            //系统版本号
            if (StringUtils.isNotEmpty(s.getVersionConfig())) {
                String versionConfig = s.getVersionConfig();
                sp.setVersionConfigStrList(Arrays.asList(versionConfig.split(",")));
            }

            //版本配置 APP版本号
            if (StringUtils.isNotEmpty(s.getNeedUpdateVersions())) {
                String needUpdateVersions = s.getNeedUpdateVersions();
                sp.setNeedUpdateVersionList(Arrays.asList(needUpdateVersions.split(",")));
            }
            //发布阶段
            if (StringUtils.isNotEmpty(s.getCanaryReleaseStage())) {
                String canaryReleaseStage = s.getCanaryReleaseStage();
                sp.setCanaryReleaseStageList(Arrays.asList(canaryReleaseStage.split(",")));
            }
            return sp;
        }
        return null;
    }

    SpVersionPO getVersionPO(SpVersionVO spVersionVO) {
        QueryWrapper<SpVersionPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("enable_flag", UserStatus.OK.getIntegerCode());
        queryWrapper.eq("delete_flag", IsDeleteEnum.NOT_DELETE.getIntegerCode());
        queryWrapper.eq("platform", spVersionVO.getPlatform());
        queryWrapper.eq("app_id", spVersionVO.getAppId());
        queryWrapper.orderByDesc("id");
        Page<SpVersionPO> page = new Page<>(1, 1);
        Page<SpVersionPO> spVersionPOPage = spVersionMapper.selectPage(page, queryWrapper);
        List<SpVersionPO> records = spVersionPOPage.getRecords();
        if (records.size() > 0) {
            log.info("records.get(0) {}", records.get(0));

            return records.get(0);
        }
        return null;
    }


    /**
     * 更新版本时间
     *
     * @param vo
     * @return
     */
    @Override
    public int updateSpVersionTime(SpVersionVO vo) {
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
        vo.setCanaryReleaseUseTime(newCanaryReleaseUseTime);
        return updateAppVersion(vo);
    }

    /**
     * 获取版本状态是否可以新增
     *
     * @param vo
     * @return
     */
    public ResponseModel queryVersionState(SpVersionVO vo) {
        if (Objects.isNull(vo.getAppId()) || vo.getAppId() <= 0) {
            return ResponseModel.errorMsg("应用id不能为空");
        } else if (StringUtils.isEmpty(vo.getPlatform())) {
            return ResponseModel.errorMsg("平台名称不能为空");
        } else if (!(PlatformEnum.iOS.getCode().contains(vo.getPlatform())
                || PlatformEnum.Android.getCode().contains(vo.getPlatform()))) {
            return ResponseModel.errorMsg("平F台名称不能为空");
        }

        QueryWrapper<SpVersionPO> wrapper = new QueryWrapper<>();
//        //只需要查询未删除的
        wrapper.eq("delete_flag", IsDeleteEnum.NOT_DELETE.getIntegerCode());
        wrapper.eq("platform", vo.getPlatform());
        wrapper.eq("app_id", vo.getAppId());
        wrapper.eq("published", UserStatus.DISABLE.getIntegerCode());
        Integer selectCount = spVersionMapper.selectCount(wrapper);
        if (selectCount == 0) {
            return ResponseModel.success();
        }
        return ResponseModel.errorMsg("有未发布版本，无法新增");
    }


    /**
     * 删除无效apk文件
     */
    @Override
    public void deleteInvalidFile() {
        QueryWrapper<SpVersionPO> wrapper = new QueryWrapper<>();
        ////数据库版本对应apk
        List<SpVersionPO> spVersionPOS = spVersionMapper.selectList(wrapper);
        if (Objects.isNull(spVersionPOS)) {
            spVersionPOS = new ArrayList<SpVersionPO>();
        }
        List<String> databaseApks = spVersionPOS.stream().map(s -> s.getDownloadUrl()).collect(Collectors.toList());
        if (Objects.isNull(databaseApks)) {
            databaseApks = new ArrayList<String>();
        }
//        log.info("databaseApks: {}", databaseApks);
        String androidAPKPath = FileProfileConfig.getAndroidAPKPath();
        if (FileProfileConfig.getAndroidAPKPath().contains("null")) {
            return;
        }
        //获取文件列表
        List<String> fileStrList = FileUtils.getFiles(androidAPKPath);
//        log.info("fileStrList: {}", fileStrList);
        for (String f : fileStrList) {
            System.out.println(f);
//            log.info("f: {}", f);
            if (f.contains(".apk")) {
//                log.info("f1: {}", f);
                String s = fileApkUrl + f.split("/apk/")[1];
//                log.info("s: {}", f);
                if (!databaseApks.contains(s)) {
//                    log.info("删除文件 f2: {}", f);
                    //删除文件
                    boolean b = FileUtils.deleteFile(f);
                    if (b) {
                        log.info("删除成功 f2: {}", f);
                    }

                }
            }
        }
    }
}
