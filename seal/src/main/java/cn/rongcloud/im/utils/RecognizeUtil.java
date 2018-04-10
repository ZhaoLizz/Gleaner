package cn.rongcloud.im.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import Decoder.BASE64Encoder;
import cn.rongcloud.im.Config;
import cn.rongcloud.im.constant.Constants;


/**
 * Created by a6100890 on 2018/2/25.
 */

public class RecognizeUtil {
    public static String toGMTString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.UK);
        df.setTimeZone(new java.util.SimpleTimeZone(0, "GMT"));
        return df.format(date);
    }

    /*
       计算MD5+BASE64
     */
    public static String MD5Base64(String s) {
        if (s == null)
            return null;
        String encodeStr = "";
        byte[] utfBytes = s.getBytes();
        MessageDigest mdTemp;
        try {
            mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(utfBytes);
            byte[] md5Bytes = mdTemp.digest();
            BASE64Encoder b64Encoder = new BASE64Encoder();
            encodeStr = b64Encoder.encode(md5Bytes);
        } catch (Exception e) {
            throw new Error("Failed to generate MD5 : " + e.getMessage());
        }
        return encodeStr;
    }


    /*
    计算 HMAC-SHA1
     */
    public static String HMACSha1(String data, String key) {
        String result;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = (new BASE64Encoder()).encode(rawHmac);
        } catch (Exception e) {
            throw new Error("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }


    /**
     * 把bitmap文件压缩后base64编码
     *
     * @param bitmap
     * @param compressRate 压缩率
     * @return
     */
    public static String bitmap2Str(Bitmap bitmap, int compressRate) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressRate, baos);
        byte[] bitmapByte = baos.toByteArray();
        String content = Base64.encodeToString(bitmapByte, Base64.DEFAULT);
        return content;
    }

    /**
     * 压缩bitmap
     */
    public static byte[] bitmap2bytes(Bitmap image, int compressRate) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, compressRate, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        byte[] bytes = baos.toByteArray();
        return bytes;
    }


    /***
     *
     * @param body 发送的json
     * @return json
     * @throws Exception
     */
    public static String sendPost(String body) throws Exception {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        int statusCode = 200;
        try {
            final String url = "https://dtplus-cn-shanghai.data.aliyuncs.com/image/tag";
            URL realUrl = new URL(url);

            /*
             * http header 参数
             */
            String ak_id = "LTAI4CrS1MlxoaAL";
            String ak_secret = "uZ7xb6YE3kn9QKzBmXPUURklgKv0Ax";
            String method = "POST";
            String accept = "application/json";
            String content_type = "application/json";
            String path = realUrl.getFile();
            String date = toGMTString(new Date());

            // 1.对body做MD5+BASE64加密
            String bodyMd5 = MD5Base64(body);
            String stringToSign = method + "\n" + accept + "\n" + bodyMd5 + "\n" + content_type + "\n" + date + "\n"
                    + path;
            // 2.计算 HMAC-SHA1
            String signature = HMACSha1(stringToSign, ak_secret);
            // 3.得到 authorization header
            String authHeader = "Dataplus " + ak_id + ":" + signature;
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", accept);
            conn.setRequestProperty("content-type", content_type);
            conn.setRequestProperty("date", date);
            conn.setRequestProperty("Authorization", authHeader);

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(body);
            // flush输出流的缓冲
            out.flush();

            // 定义BufferedReader输入流来读取URL的响应
            statusCode = ((HttpURLConnection) conn).getResponseCode();
            if (statusCode != 200) {
                in = new BufferedReader(new InputStreamReader(((HttpURLConnection) conn).getErrorStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (statusCode != 200) {
            throw new IOException("\nHttp StatusCode: " + statusCode + "\nErrorMessage: " + result);
        }
        return result;
    }

    /**
     * 文字识别
     *
     * @param jsonBody
     * @return
     */
    public static String readTextImg(String jsonBody) {
        String getPath = "/api/predict/ocr_general";
        final StringBuilder result = new StringBuilder();
        HttpUtil.getInstance().httpPostBytes(Config.ChAR_BASE_URL, getPath, null, null, jsonBody.getBytes(Constants.CLOUDAPI_ENCODING), null, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() != 200) {
                    Logger.d(result.append("错误原因：").append(response.header("X-Ca-Error-Message")).append(Constants.CLOUDAPI_LF).append(Constants.CLOUDAPI_LF).toString());
                    return;
                }
                result.append(new String(response.body().bytes(), Constants.CLOUDAPI_ENCODING));
            }
        });
        return result.toString();
    }

    public static String readIdCardImg(String base64Img) {
        String body = "{\"inputs\":[{\"image\":{\"dataType\":50,\"dataValue\":\"" + base64Img + "\"},\"configure\":{\"dataType\":50,\"dataValue\":\"{\\\"side\\\":\\\"" + "face" + "\\\"}\"}}]}";
        String getPath = "/rest/160601/ocr/ocr_idcard.json";
        final StringBuilder result = new StringBuilder();
        HttpUtil.getInstance().httpPostBytes(Config.ID_CARD_BASE_URL, getPath, null, null, body.getBytes(Constants.CLOUDAPI_ENCODING), null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.e(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() != 200) {
                    Logger.d("OCR", result.append("错误原因：").append(response.header("X-Ca-Error-Message")).append(Constants.CLOUDAPI_LF).append(Constants.CLOUDAPI_LF).toString());
                    return;
                }
                result.append(new String(response.body().bytes(), Constants.CLOUDAPI_ENCODING));
            }
        });
        return result.toString();
    }

    /**
     * 解析通识物品的Json
     */
    public static String parseItemJson(String json) {
        Gson gson = new Gson();
        String result = "";
        List<Tags> imageTags;
        if (json != null) {
            ImageBody imageBody = gson.fromJson(json, ImageBody.class);
            if (imageBody != null) {
                imageTags = imageBody.getTags();
                if (imageTags.size() != 0) {
                    //调参权重
                    for (Tags tag : imageTags) {
                        String value = tag.getValue();  //识别结果
                        if (value.equals("名片") || value.equals("香烟") || value.equals("菜单") || value.equals("创可贴")) {
                            result = "校园卡";
                            return result;
                        } else if (value.equals("钱")) {
                            result = "身份证";
                            return result;
                        }
                    }
                    result = imageTags.get(0).getValue();
                }
            }
        } else {
            Logger.d("jsonResult is null!");
        }
        return result;
    }

    /**
     * 解析校园卡识别返回的json
     * @param json
     * @return
     */
    public static Map<String, String> parseSchoolJson(String json) {
        Map<String, String> cardData = new HashMap<>();
        cardData.put("name", "未成功识别");
        cardData.put("number", "未成功识别");
        cardData.put("college", "未成功识别");
        OCRCharDataJson data = new Gson().fromJson(json, OCRCharDataJson.class);
        if (data != null) {
            for (OCRCharDataJson.Ret ret : data.getRet()) {
                if (ret != null) {
                    String message = ret.getWord();
                    Log.d("card", message);
                    if (message.contains("名") || message.contains("姓")) {
                        if (message.contains(":")) {
                            cardData.put("name", message.substring(message.lastIndexOf(":") + 1));
                        } else {
                            cardData.put("name", message);
                        }
                    } else if (message.contains("号")) {
                        if (message.contains(":")) {
                            cardData.put("number", message.substring(message.lastIndexOf(":") + 1));
                        } else {
                            cardData.put("number", message);
                        }
                    } else if (message.contains("院")) {
                        if (message.contains(":")) {
                            cardData.put("college", message.substring(message.lastIndexOf(":") + 1));
                        } else {
                            cardData.put("college", message);
                        }
                    }
                } else {
                    Logger.d("ret == null");
                }
            }
        }
        return cardData;
    }

    public static IdCardData parseIdCardJson(String json) {
        OCRIdCardResultJson resultJson = new Gson().fromJson(json, OCRIdCardResultJson.class);
        OCRIdCardFaceDataJson faceDataJson= new Gson().fromJson(resultJson.getOutputs().get(0).getOutputValue().getDataValue(), OCRIdCardFaceDataJson.class);
        IdCardData idCardData = IdCardData.getInstance();
        idCardData.reset();
        idCardData.setName(faceDataJson.getName());
        idCardData.setSex(faceDataJson.getSex());
        idCardData.setNation(faceDataJson.getNationality());
        idCardData.setAddress(faceDataJson.getAddress());
        idCardData.setNumber(faceDataJson.getNum());
        return idCardData;
    }


    public interface RecognizeListener {
        void onRecognize(Bitmap bitmap);
    }




}
