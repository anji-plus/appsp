package com.anji.sp.service;

import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.vo.QueryDictByTypeVO;
import com.anji.sp.model.vo.SpDictPageVO;
import com.anji.sp.model.vo.SpDictVO;

/**
 * 字典表
 *
 * @author Kean
 * @date 2020/06/23
 */
public interface SpDictService {
    /**
     * 查询字典信息 进行分页查询
     *
     * @param spDictVO
     * @return
     */
    ResponseModel queryByPage(SpDictVO spDictVO);

    /**
     * 根据type 查询字典信息 升序排练
     *
     * @param queryDictByTypeVO
     * @return
     */
    ResponseModel queryByType(QueryDictByTypeVO queryDictByTypeVO);

    /**
     * 新增字典内容
     *
     * @param spDictVO
     * @return
     */
    ResponseModel insertDict(SpDictVO spDictVO);

    /**
     * 新增字典内容
     *
     * @param reqData
     * @return
     */
    ResponseModel checkVersion(QueryDictByTypeVO reqData);

    /**
     * 根据id 更新版本信息 (value)
     *
     * @param reqData
     * @return
     */
    ResponseModel updateVersionInfoById(SpDictPageVO reqData);

    /**
     * 逻辑删除 并不是真的删除
     *
     * @param reqData
     * @return
     */
    ResponseModel deleteVersionById(SpDictPageVO reqData);

}