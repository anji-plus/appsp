package com.anji.sp.model.vo;

import com.anji.sp.model.po.SpAppLogPO;
import com.anji.sp.model.po.SpApplicationPO;
import lombok.Data;

import java.io.Serializable;

/**
 * APP接口json配置类
 *
 * @author kean 2020-06-26
 */
@Data
public class SpAppReqVO implements Serializable {
    private SpAppLogPO spAppLogPO;
    private SpApplicationPO spApplicationPO;
}