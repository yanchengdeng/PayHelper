package com.xq.payhelper.net;

import com.xq.payhelper.entity.Result;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserService {


    /**
     * 开启监听
     * @param
     * @return
     */
    @POST("testapi/index.php")
    Observable<Result> startListener(@Body RequestBody body);



    /**
     * 上传监听
     * @param
     * @return
     */
    @POST("testapi/index.php")
    Observable<Result> uploadListener(@Body RequestBody body);

}
