package com.anji.sp.service;

import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.vo.SpUserVO;

/**
 * Created by raodeming on 2020/6/22.
 */
public interface LoginService {

    /**
     * 用户登录
     * @param spUserVO
     * @return
     */
    ResponseModel login(SpUserVO spUserVO);

    ResponseModel test(SpUserVO spUserVO);
}
