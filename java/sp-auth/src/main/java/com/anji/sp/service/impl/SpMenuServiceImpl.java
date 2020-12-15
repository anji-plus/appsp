package com.anji.sp.service.impl;

import com.anji.sp.mapper.SpMenuMapper;
import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.po.SpMenuPO;
import com.anji.sp.model.vo.SpMenuVO;
import com.anji.sp.service.SpMenuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author : kean_qi
 * create at:  2020/6/27  9:46 上午
 * @description:
 */
@Service
public class SpMenuServiceImpl implements SpMenuService {

    @Autowired
    SpMenuMapper spMenuMapper;

    /**
     * 查询菜单列表
     *
     * @return
     */
    @Override
    public ResponseModel selectMenuList() {
        SpMenuVO spMenuVO = new SpMenuVO();
        return selectMenuListByParentId(spMenuVO);
    }

    /**
     * 根据父级id查询菜单
     *
     * @param spMenuVO
     * @return
     */
    @Override
    public ResponseModel selectMenuListByParentId(SpMenuVO spMenuVO) {
        QueryWrapper<SpMenuPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("enable_flag", 1);
        queryWrapper.eq("delete_flag", 0);
        queryWrapper.eq("parent_id", Objects.isNull(spMenuVO.getParentId()) ? 1 : spMenuVO.getParentId());
        List<SpMenuPO> spRolePOS = spMenuMapper.selectList(queryWrapper);
        List<SpMenuVO> collects = spRolePOS.stream().map(s -> {
            SpMenuVO sp = new SpMenuVO();
            BeanUtils.copyProperties(s, sp);
            return sp;
        }).collect(Collectors.toList());
        return ResponseModel.successData(collects);
    }
}
