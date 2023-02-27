package com.acg.goodweatherjava.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.acg.goodweatherjava.db.bean.Province;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * @Classname ProvinceDao
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/2/27 18:31
 * @Created by an
 */
@Dao
public interface ProvinceDao {

    //    Flowable<T>与Observable<T>不同的是它提供了Backpressure策略的支持。
    //    所以在性能上低于Observable，因为内部为了完成背压操作添加了许多其他操作。
    /**
     * 查询所有
     */
    @Query("select * from province")
    Flowable<List<Province>> getAll();

    //    Completable 在创建后，不会发射任何数据, 只有 onComplete 与 onError事件，
    //    同时没有Observable中的一些操作符，如 map，flatMap。通常与 andThen 操作符结合使用。
    //    我的理解，即只产生成功或失败，但无法对流操作
    /**
     * 插入所有
     * @param provinces 所有行政区数据
     * @return
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(Province... provinces);

}
