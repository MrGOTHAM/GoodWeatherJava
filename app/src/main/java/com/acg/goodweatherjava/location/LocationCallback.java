package com.acg.goodweatherjava.location;

import com.baidu.location.BDLocation;

/**
 * @Classname LocationCallback
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/2/25 21:50
 * @Created by an
 */
public interface LocationCallback {
    /**
     * 接收定位数据
     * @param bdLocation
     */
    void onReceiveLocation(BDLocation bdLocation);
}
