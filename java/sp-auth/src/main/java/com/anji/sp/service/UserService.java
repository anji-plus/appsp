package com.anji.sp.service;

import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.vo.SpApplicationVO;
import com.anji.sp.model.vo.SpUserVO;

/**
 * Created by raodeming on 2020/6/22.
 */
public interface UserService {

    /**
     * 根据用户名称查询用户信息
     * @param username
     * @return
     */
    SpUserVO selectUserByUserName(String username);

    /**
     * 添加用户
     * @param spUserVO
     * @return
     */
    ResponseModel addUser(SpUserVO spUserVO);

    /**
     * 分页查询用户信息
     * @param spUserVO
     * @return
     */
    ResponseModel queryByPage(SpUserVO spUserVO);

    /**
     * 删除用户 逻辑删除
     *
     * @param spUserVO
     * @return
     */
    ResponseModel deleteUserById(SpUserVO spUserVO);


    /**
     * 根据用户id 更新用户信息
     *
     * @param spUserVO
     * @return
     */
    ResponseModel updateUserById(SpUserVO spUserVO);

    /**
     * 查询该项目未添加的用户
     *
     * @param spApplicationVO
     * @return
     */
    ResponseModel selectNoJoinApplicationByAppId(SpApplicationVO spApplicationVO);


}
