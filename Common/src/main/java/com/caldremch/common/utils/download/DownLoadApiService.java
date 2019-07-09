package com.caldremch.common.utils.download;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author Caldremch
 * @date 2019-07-09 09:45
 * @email caldremch@163.com
 * @describe
 **/
public interface DownLoadApiService {
    @Streaming
    @GET
    Call<ResponseBody> downLoad(@Url String url, @Header("Range") String range);

    @Streaming
    @GET
    Observable<ResponseBody> downLoad(@Url String url);
}
