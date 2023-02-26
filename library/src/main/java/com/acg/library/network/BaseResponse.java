package com.acg.library.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @Classname BaseResponse
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/2/25 23:09
 * @Created by an
 */
public class BaseResponse {

    /**
     * 结果码
     */
    @SerializedName("res_code")
    @Expose
    public Integer responseCode;

    /**
     * 返回的错误信息
     */
    @SerializedName("res_error")
    @Expose
    public String responseError;

}
