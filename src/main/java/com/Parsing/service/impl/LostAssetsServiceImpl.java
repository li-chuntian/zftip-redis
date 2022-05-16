package com.Parsing.service.impl;

import cn.hutool.core.date.DateUtil;
import com.Parsing.entity.FebsConstant;
import com.Parsing.utils.DownloadUtil;
import com.Parsing.utils.OKHttpClientBuilder;
import com.Parsing.service.LostAssetsService;
import cn.hutool.json.JSONObject;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName : LostAssetsServiceImpl
 * @Description :
 * @Author : Lyn
 * @CopyRight ZFINFO
 * @Date: 2021-04-29 17:19
 */
@Service
public class LostAssetsServiceImpl implements LostAssetsService {

    @Override
    public String fetchLostAssetsResult(String areaName, String onceQueryStartTime, String onceQueryEndTime) {

        // 压缩包目录
        String dirPath = FebsConstant.UNCOMPRESS + areaName;
        String fileName = areaName + ".zip";
        /**计算所需参数*/
        String timeStampStr = String.valueOf(new Date().getTime() / 1000);
        String salt = "1fd8da2ed2f6297098e3104e153726bd";
        String finalStr = timeStampStr + salt;

        OkHttpClient okHttpClient = OKHttpClientBuilder.buildOKHttpClient().build();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        JSONObject jsonObjectTemp = new JSONObject();
        jsonObjectTemp.set("query", getIPsByAreaName(areaName));
        jsonObjectTemp.set("start_time", onceQueryStartTime);
        jsonObjectTemp.set("end_time", onceQueryEndTime);
        System.out.println(jsonObjectTemp.toString());
        RequestBody body = RequestBody.create(mediaType, jsonObjectTemp.toString());
        Request request = new Request.Builder()
                .url(FebsConstant.QIHOO_LOSTASSETS_URL)
                .addHeader("X-Api-Key", FebsConstant.QIHOO_ASSETS_API_KEY)
                .addHeader("timestamp", timeStampStr)
                .addHeader("sign", encode(finalStr))
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            JSONObject jsonObject = new JSONObject(response.body().string());
            String resultJson = jsonObject.get("result").toString();
            JSONObject resultJsonStr = new JSONObject(resultJson);
            String taskId = resultJsonStr.get("task_id").toString();
            /**获取失陷检测结果*/
            OkHttpClient resultHttpClient = OKHttpClientBuilder.buildOKHttpClient().build();
            Request resultRequest = new Request.Builder()
                    .url(FebsConstant.QIHOO_LOSTASSETS_RESULT_URL + taskId)
                    .header("X-Api-Key", FebsConstant.QIHOO_ASSETS_API_KEY)
                    .header("timestamp", timeStampStr)
                    .header("sign", encode(finalStr))
                    .header("Content-Type", "application/json")
                    .get()
                    .build();
            /**查询等待时间*/
            Thread.sleep(30 * 1000);
            Call resultCall = resultHttpClient.newCall(resultRequest);
            Response resultResponse = resultCall.execute();
            JSONObject resultJsonObject = new JSONObject(resultResponse.body().string());
            String downloadURL = resultJsonObject.get("result").toString();
            JSONObject downloadURLStr = new JSONObject(downloadURL);
            String realURL = downloadURLStr.get("download_url").toString();
            if (realURL != null) {
                System.out.println("下载地址为:" + realURL);
                DownloadUtil.get().download(realURL, dirPath, fileName, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dirPath + File.separatorChar + fileName;
    }

    private static String[] getIPsByAreaName(String areaName) {
        switch (areaName) {
            case "changde":
                return FebsConstant.CHANGDE_EXIT_IPS;

            case "chongqing":
                return FebsConstant.CHONGQING_EXIT_IPS;

            case "jiangsu":
                return FebsConstant.JIANGSU_EXIT_IPS;

            case "guizhou":
                return FebsConstant.GUIZHOU_EXIT_IPS;

            case "henan":
                return FebsConstant.HENAN_EXIT_IPS;

            case "jingmen":
                return FebsConstant.JINGMEN_EXIT_IPS;

            case "yunnan":
                return FebsConstant.YUNNAN_EXIT_IPS;

            case "guangdong":
                return FebsConstant.GUANGDONG_EXIT_IPS;
            case "kunming":
                return FebsConstant.KUNMING_EXIT_IPS;
            case "hunan":
                return FebsConstant.HUNAN_EXIT_IPS;
            case "hebei":
                return FebsConstant.HEBEI_EXIT_IPS;
            case "sichuan":
                return FebsConstant.SICHUAN_EXIT_IPS;
            case "guiyang":
                return FebsConstant.GUIYANG_EXIT_IPS;
        }
        return null;
    }

    //Unicode转中文方法
    private static String unicodeToCn(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }

    public static String encode(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] buffer = digest.digest(text.getBytes());
            // byte -128 ---- 127
            StringBuffer sb = new StringBuffer();
            for (byte b : buffer) {
                int a = b & 0xff;
                // Log.d(TAG, "" + a);
                String hex = Integer.toHexString(a);

                if (hex.length() == 1) {
                    hex = 0 + hex;
                }
                sb.append(hex);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String folder = DateUtil.format(new Date(), new SimpleDateFormat("yyyy-MM-dd-HH时mm分ss秒"));
        System.out.println(folder);
    }

}
