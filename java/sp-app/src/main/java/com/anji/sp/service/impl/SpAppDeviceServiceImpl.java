package com.anji.sp.service.impl;

import com.anji.sp.mapper.SpAppDeviceMapper;
import com.anji.sp.model.po.SpAppDevicePO;
import com.anji.sp.model.po.SpAppLogPO;
import com.anji.sp.model.vo.SpAppLogVO;
import com.anji.sp.service.SpAppDeviceService;
import com.anji.sp.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class SpAppDeviceServiceImpl implements SpAppDeviceService {

    @Resource
    private SpAppDeviceMapper spAppDeviceMapper;

    /**
     * 更新device表
     *
     * @param spAppLogPO
     * @return
     */
    @Override
    public int updateDeviceInfo(SpAppLogPO spAppLogPO) {

        if (StringUtils.isEmpty(spAppLogPO.getAppKey()) || StringUtils.isEmpty(spAppLogPO.getDeviceId()) || StringUtils.isEmpty(spAppLogPO.getPlatform())) {
            return 0;
        }
        QueryWrapper<SpAppDevicePO> queryWrapper = new QueryWrapper<>();
        //只需要查询未删除的
        queryWrapper.eq("app_key", spAppLogPO.getAppKey());
        queryWrapper.eq("device_id", spAppLogPO.getDeviceId());
        queryWrapper.eq("platform", spAppLogPO.getPlatform());
        List<SpAppDevicePO> spAppDevicePOS = spAppDeviceMapper.selectList(queryWrapper);
        if (spAppDevicePOS.size() > 0) {
            return 1;
        }
        SpAppDevicePO sp = new SpAppDevicePO();
        BeanUtils.copyProperties(spAppLogPO, sp);
        sp.setCreateBy(1L);
        sp.setUpdateBy(1L);
        sp.setCreateDate(new Date());
        sp.setUpdateDate(new Date());
        int insert = spAppDeviceMapper.insert(sp);
        return insert;
    }

    /**
     * 查询对应appkey的device表
     *
     * @param spAppLogVO
     * @return
     */
    @Override
    public int selectCount(SpAppLogVO spAppLogVO) {
        if (StringUtils.isEmpty(spAppLogVO.getAppKey())) {
            return 0;
        }
        QueryWrapper<SpAppDevicePO> queryWrapper = new QueryWrapper<>();
        //只需要查询未删除的
        queryWrapper.eq("app_key", spAppLogVO.getAppKey());
        queryWrapper.eq("platform", spAppLogVO.getPlatform());

        Integer count = spAppDeviceMapper.selectCount(queryWrapper);
        return count;
    }


}
