package com.anji.sp.service;

import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.vo.SpUploadVO;

/**
 * 字典表
 *
 * @author Kean
 * @date 2020/06/23
 */
public interface SpUploadService {
    /**
     * 上传文件
     * @param spUploadVO
     * @return
     */
    ResponseModel uploadFile(SpUploadVO spUploadVO);
}