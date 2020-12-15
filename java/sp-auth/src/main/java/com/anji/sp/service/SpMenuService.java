package com.anji.sp.service;

import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.vo.SpMenuVO;

/**
 * 字典表
 *
 * @author Kean
 * @date 2020/06/23
 */
public interface SpMenuService {
    /**
     * 查询菜单列表
     *
     * @return
     */
    ResponseModel selectMenuList();

    /**
     * 根据父级id查询菜单
     *
     * @param spMenuVO
     * @return
     */
    ResponseModel selectMenuListByParentId(SpMenuVO spMenuVO);


}