package com.anji.sp.service.impl;

import com.anji.sp.model.ResponseModel;
import com.anji.sp.model.po.SpApplicationPO;
import com.anji.sp.model.vo.SpUploadVO;
import com.anji.sp.model.vo.SpVersionVO;
import com.anji.sp.service.SpApplicationService;
import com.anji.sp.service.SpUploadService;
import com.anji.sp.util.file.FileUploadUtils;
import com.anji.sp.util.file.exception.FileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author : kean_qi
 * create at:  2020/7/1  3:37 下午
 * @description:
 */
@Service
@Slf4j
public class SpUploadServiceImpl implements SpUploadService {
    @Autowired
    SpApplicationService spApplicationService;

    /**
     * 上传文件
     * @param spUploadVO
     * @return
     */
    @Override
    public ResponseModel uploadFile(SpUploadVO spUploadVO) {
        SpApplicationPO spApplicationPO = spApplicationService.selectByAppId(spUploadVO.getAppId());
        if (Objects.isNull(spApplicationPO)) {
            return ResponseModel.errorMsg("没有该应用,无法上传");
        }
        spUploadVO.setAppKey(spApplicationPO.getAppKey());
        spUploadVO.setAppId(spApplicationPO.getAppId());
        try {
            SpVersionVO spVersionVO = FileUploadUtils.uploadFile(spUploadVO.getFile(), spUploadVO.getAppKey());
            return ResponseModel.successData(spVersionVO);
        } catch (FileException e) {
            log.error("错误 ： {}", e);
            return ResponseModel.errorMsg(e.getArgs()[0] + "");
        } catch (Exception e) {
            return ResponseModel.errorMsg(e.getMessage());
        }
    }
}
