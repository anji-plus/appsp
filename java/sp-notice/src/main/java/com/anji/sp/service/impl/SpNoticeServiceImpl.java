package com.anji.sp.service.impl;

import com.anji.sp.enums.IsDeleteEnum;
import com.anji.sp.enums.UserStatus;
import com.anji.sp.mapper.SpNoticeMapper;
import com.anji.sp.model.PageQueryRep;
import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.po.SpNoticePO;
import com.anji.sp.model.vo.SpNoticeForAppVO;
import com.anji.sp.model.vo.SpNoticeVO;
import com.anji.sp.service.SpNoticeService;
import com.anji.sp.util.DateUtils;
import com.anji.sp.util.SecurityUtils;
import com.anji.sp.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 公告管理
 *
 * @author zhudaijie
 * @date 2020/06/24
 */
@Service
@Slf4j
public class SpNoticeServiceImpl implements SpNoticeService {

    @Autowired
    private SpNoticeMapper spNoticeMapper;

    /**
     * 分页根据应用id分页查询公告信息
     *
     * @param reqData
     * @return
     */
    @Override
    public ResponseModel getAppNoticeByAppId(SpNoticeVO reqData) {
        if (Objects.isNull(reqData.getAppId())) {
            return ResponseModel.errorMsg("应用id不能为空");
        }
        taskDisableInvalidNotice();
        PageQueryRep<SpNoticeVO> pageQueryRep = new PageQueryRep<>();
        PageHelper.startPage(reqData.getPageNo(), reqData.getPageSize());
        QueryWrapper<SpNoticePO> queryWrapper = new QueryWrapper<>();
        //只需要查询未删除的
        queryWrapper.eq("delete_flag", IsDeleteEnum.NOT_DELETE.getIntegerCode());
        queryWrapper.eq("app_id", reqData.getAppId());
        queryWrapper.orderByDesc("id");
        List<SpNoticePO> spRolePOS = spNoticeMapper.selectList(queryWrapper);
        PageInfo<SpNoticePO> pageInfo = new PageInfo<>(spRolePOS);
        pageQueryRep.setTotal(pageInfo.getTotal());
        List<SpNoticeVO> collects = spRolePOS.stream().map(s -> {
            SpNoticeVO sp = new SpNoticeVO();
            BeanUtils.copyProperties(s, sp);
            sp.setStartTime(DateUtils.dateDayTime(s.getStartTime()));
            sp.setEndTime(DateUtils.dateDayTime(s.getEndTime()));
            return sp;
        }).collect(Collectors.toList());
        if (reqData.getPageNo() > pageInfo.getPageNum()) {
            pageQueryRep.setRows(new ArrayList<>());
        } else {
            pageQueryRep.setRows(collects);
        }
        return ResponseModel.successData(pageQueryRep);
    }

    /**
     * 判空操作
     *
     * @param reqData
     * @return
     */
    private String checkNoticeValid(SpNoticeVO reqData) {
        if (Objects.isNull(reqData.getAppId()) || reqData.getAppId() <= 0) {
            return "应用Id传参错误";
        } else if (StringUtils.isEmpty(reqData.getName())) {
            return "公告名称为空";
        } else if (StringUtils.isEmpty(reqData.getTitle())) {
            return "公告标题为空";
        } else if (StringUtils.isEmpty(reqData.getDetails())) {
            return "公告内容为空";
        } else if (StringUtils.isEmpty(reqData.getTemplateTypeName())) {
            return "模板类型为空";
        } else if (StringUtils.isEmpty(reqData.getTemplateType())) {
            return "模板类型为空";
        } else if (Objects.isNull(reqData.getStartTime()) && Objects.isNull(reqData.getEndTime())) {
            return "持续时间为空";
        } else if (DateUtils.parseDateVsDate(reqData.getStartTime(), new Date())) {
            return "开始时间不能小于当前时间";
        } else if (DateUtils.parseDateVsDate(reqData.getEndTime(), new Date())) {
            return "结束时间不能小于当前时间";
        }
        return "";
    }

    /**
     * 新增公告信息
     *
     * @param reqData
     * @return
     */
    @Override
    public ResponseModel insertNotice(SpNoticeVO reqData) {
        //校验空值
        String noticeValid = checkNoticeValid(reqData);
        if (StringUtils.isNotEmpty(noticeValid)) {
            return ResponseModel.errorMsg(noticeValid);
        } else if (!DateUtils.parseDate1VsDate2(reqData.getStartTime(), reqData.getEndTime())) {
            return ResponseModel.errorMsg("结束时间不能小于开始时间");
        }
        SpNoticePO sp = new SpNoticePO();
        sp.setStartTime(DateUtils.parseDate(reqData.getStartTime()));
        sp.setEndTime(DateUtils.parseDate(reqData.getEndTime()));
        BeanUtils.copyProperties(reqData, sp);
        //默认停用
        sp.setDeleteFlag(IsDeleteEnum.NOT_DELETE.getIntegerCode());
        sp.setEnableFlag(UserStatus.DISABLE.getIntegerCode());
        sp.setCreateBy(SecurityUtils.getUserId());
        sp.setCreateDate(new Date());
        sp.setUpdateBy(SecurityUtils.getUserId());
        sp.setUpdateDate(new Date());
        int insert = spNoticeMapper.insert(sp);
        if (insert > 0) {
            return ResponseModel.success();
        }
        return ResponseModel.errorMsg("新增失败");
    }

    /**
     * 编辑公告信息
     *
     * @param reqData
     * @return
     */
    @Override
    public ResponseModel updateNotice(SpNoticeVO reqData) {
        //校验空值
        String noticeValid = checkNoticeValid(reqData);
        if (StringUtils.isNotEmpty(noticeValid)) {
            return ResponseModel.errorMsg(noticeValid);
        } else if (Objects.isNull(reqData.getId())) {
            return ResponseModel.errorMsg("id不能为空");
        } else if (!DateUtils.parseDate1VsDate2(reqData.getStartTime(), reqData.getEndTime())) {
            return ResponseModel.errorMsg("结束时间不能小于开始时间");
        }
        SpNoticePO sp = new SpNoticePO();
        BeanUtils.copyProperties(reqData, sp);
        sp.setStartTime(DateUtils.parseDate(reqData.getStartTime()));
        sp.setEndTime(DateUtils.parseDate(reqData.getEndTime()));
        sp.setUpdateBy(SecurityUtils.getUserId());
        sp.setUpdateDate(new Date());
        int update = spNoticeMapper.updateById(sp);
        if (update > 0) {
            return ResponseModel.success();
        }
        return ResponseModel.errorMsg("更新失败");
    }

    /**
     * 删除公告信息
     *
     * @param reqData
     * @return
     */
    @Override
    public ResponseModel deleteNotice(SpNoticeVO reqData) {
        if (Objects.isNull(reqData.getId())) {
            return ResponseModel.errorMsg("id不能为空");
        } else if (Objects.isNull(reqData.getAppId())) {
            return ResponseModel.errorMsg("appId不能为空");
        }
        SpNoticePO spNoticePO = spNoticeMapper.selectById(reqData.getId());
        if (Objects.isNull(spNoticePO)) {
            return ResponseModel.errorMsg("未查到该公告");
        }
        spNoticePO.setDeleteFlag(IsDeleteEnum.DELETE.getIntegerCode());
        spNoticePO.setUpdateBy(SecurityUtils.getUserId());
        spNoticePO.setUpdateDate(new Date());
        int update = spNoticeMapper.updateById(spNoticePO);
        if (update > 0) {
            //写入json文件
            return ResponseModel.success();
        }
        return ResponseModel.errorMsg("删除失败");
    }

    /**
     * 根据id启用/禁用公告信息
     *
     * @param reqData
     * @return
     */
    @Override
    public ResponseModel enableNotice(SpNoticeVO reqData) {
        if (Objects.isNull(reqData.getId())) {
            return ResponseModel.errorMsg("id不能为空");
        } else if (Objects.isNull(reqData.getAppId())) {
            return ResponseModel.errorMsg("AppId不能为空");
        } else if (Objects.isNull(reqData.getEnableFlag()) || reqData.getEnableFlag() < 0 || reqData.getEnableFlag() > 1) {
            return ResponseModel.errorMsg("启用状态有误");
        } else if (Objects.isNull(reqData.getEndTime())) {
            return ResponseModel.errorMsg("结束时间不能为空");
        } else if (DateUtils.parseDateVsDate(reqData.getEndTime(), new Date()) && reqData.getEnableFlag() == UserStatus.OK.getIntegerCode()) {
            return ResponseModel.errorMsg("结束时间小于当前时间，无法启用");
        }
        SpNoticePO spNoticePO = spNoticeMapper.selectById(reqData.getId());
        if (Objects.isNull(spNoticePO)) {
            return ResponseModel.errorMsg("未查到该公告");
        }
        spNoticePO.setEnableFlag(reqData.getEnableFlag());
        spNoticePO.setUpdateBy(SecurityUtils.getUserId());
        spNoticePO.setUpdateDate(new Date());
        int update = spNoticeMapper.updateById(spNoticePO);
        if (update > 0) {
            return ResponseModel.success();
        }

        return ResponseModel.errorMsg("更新失败");
    }


    /**
     * 处理无效公告
     */
    @Override
    public void taskDisableInvalidNotice() {
        int i = spNoticeMapper.setNoticeEnableInvalid();
        log.info("setNoticeEnableInvalid : {}", i);
    }

    /**
     * 获取公告信息
     * @param spNoticeVO
     * @return
     */
    @Override
    public List<SpNoticeForAppVO> getNotices(SpNoticeVO spNoticeVO) {
        taskDisableInvalidNotice();
        QueryWrapper<SpNoticePO> noticePOQueryWrapper = new QueryWrapper<>();
        noticePOQueryWrapper.eq("enable_flag", UserStatus.OK.getIntegerCode());
        noticePOQueryWrapper.eq("delete_flag", IsDeleteEnum.NOT_DELETE.getIntegerCode());
        noticePOQueryWrapper.eq("app_id", spNoticeVO.getAppId());
        noticePOQueryWrapper.orderByAsc("update_date");
        List<SpNoticePO> spNoticePOs = spNoticeMapper.selectList(noticePOQueryWrapper);
        List<SpNoticeForAppVO> spNoticeForAppVOS = spNoticePOs.stream().map(s -> {
            SpNoticeForAppVO sp = new SpNoticeForAppVO();
            BeanUtils.copyProperties(s, sp);
            sp.setStartTime(s.getStartTime().getTime());
            sp.setEndTime(s.getEndTime().getTime());
            return sp;
        }).collect(Collectors.toList());
        return spNoticeForAppVOS;
    }
}

