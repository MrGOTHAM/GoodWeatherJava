package com.acg.goodweatherjava.db.bean;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname CityConverter
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/2/27 18:24
 * @Created by an
 */
public class CityConverter {

    /**
     * 字符串转 对象
     * @param value
     * @return
     */
    @TypeConverter
    public List<Province.City> stringToObject(String value){
        Type userListType = new TypeToken<ArrayList<Province.City>>(){}.getType();
        return new Gson().fromJson(value,userListType);
    }

    /**
     * 对象转字符串
     * @param list
     * @return
     */
    @TypeConverter
    public String objectToString(List<Province.City> list){
        return new Gson().toJson(list);
    }
}
