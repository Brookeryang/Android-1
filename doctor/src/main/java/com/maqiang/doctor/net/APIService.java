package com.maqiang.doctor.net;

import com.maqiang.doctor.bean.InfoDetail;
import com.maqiang.doctor.bean.InfoList;
import com.maqiang.doctor.utils.ConstantUtil;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by maqiang on 2017/3/9.
 */

public interface APIService {
    //获取信息列表
    @GET(ConstantUtil.JUHE_INFO_LIST)
    Observable<InfoList> getInfo(@Query("key") String key,@Query("id") int id,
        @Query("page") int page,@Query("limit") int limit);

    //获取详细信息
    @GET(ConstantUtil.JUHE_INFO_DETAIL)
    Observable<InfoDetail> getInfoDetail(@QueryMap Map<String,String> options);
}
