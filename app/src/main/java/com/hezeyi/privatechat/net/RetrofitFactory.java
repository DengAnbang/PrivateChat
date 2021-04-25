package com.hezeyi.privatechat.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.hezeyi.privatechat.Const;
import com.xhab.utils.utils.LogUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by dab on 2018/4/11 17:38
 */

public class RetrofitFactory {
    private static Retrofit sRetrofit;
    private static final String TAG = "RetrofitFactory";


    public static <T> T getService(Class<T> serviceClass) {

        if (sRetrofit == null) {
            sRetrofit = setRetrofit(Const.Api.API_HOST);
        }
        return sRetrofit.create(serviceClass);
    }


    public static Gson getGson() {
        Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(List.class, new JsonDeserializer<List<?>>() {
            @Override
            public List<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                if (json.isJsonArray()) {
                    List list = new ArrayList<>();
                    try {
                        JsonArray array = json.getAsJsonArray();
                        Type itemType = ((ParameterizedType) typeOfT).getActualTypeArguments()[0];
                        for (int i = 0; i < array.size(); i++) {
                            JsonElement element = array.get(i);
                            try {
                                Object item = context.deserialize(element, itemType);
                                list.add(item);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return list;
                } else {
                    //和接口类型不符，返回空List
                    return Collections.EMPTY_LIST;
                }
            }
        }).create();
        return gson;
    }


    public static Retrofit setRetrofit(String baseUrl) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)//注意此处,设置服务器的地址
                .addConverterFactory(GsonConverterFactory.create(getGson()))//用于Json数据的转换,非必须
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());//用于返回Rxjava调用,非必须
        OkHttpClient okHttpClient = getOkHttpClient();
        builder.client(okHttpClient);
        sRetrofit = builder.build();
        return sRetrofit;
    }

    public static OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.addInterceptor(chain -> {
            Request request = chain.request();
            HttpUrl url = request.url();

            Response proceed = chain.proceed(request);

            if (!url.toString().contains("/public/file") && !url.toString().contains("public/app/updates/download")) {
//            if (BuildConfig.DEBUG) {
                LogUtils.e("request: " + url.toString());
//                if (Objects.equals(BuildConfig.FLAVOR, "nb")) {
                BufferedSource source = proceed.body().source();
                source.request(Long.MAX_VALUE);
                String s1 = source.buffer().clone().readString(Charset.forName("UTF-8"));
                LogUtils.e(s1);
//                LogUtils.e("Response: " + proceed.peekBody(1204 * 1204).string());
            }


            return proceed;
        });
        return builder.build();
    }
}
