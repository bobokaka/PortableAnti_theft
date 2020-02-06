package com.example.portableanti_theft.Util;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @ProjectName: PortableAnti_theft
 * @Package: com.example.portableanti_theft.Activity
 * @ClassName: HttpUtil
 * @Description: 发送http请求工具类（OKHttp开源框架）
 * @Author: ED_Peng
 * @CreateDate: 2019-3-26 0026 下午 10:27
 * @UpdateUser: 更新者
 * @UpdateDate: 2019-3-26 0026 下午 10:27
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 **/
public class HttpUtil {
    public static void sendOKHttpRequest(String address,RequestBody requestBody,
                                         okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
