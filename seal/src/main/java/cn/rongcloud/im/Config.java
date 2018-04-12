package cn.rongcloud.im;

/**
 * @author :smile
 * @project:Config
 * @date :2016-01-15-18:23
 */
public class Config {
    //是否是debug模式
    public static final boolean DEBUG = true;
    //好友请求：未读-未添加->接收到别人发给我的好友添加请求，初始状态
    public static final int STATUS_VERIFY_NONE = 0;
    //好友请求：已读-未添加->点击查看了新朋友，则都变成已读状态
    public static final int STATUS_VERIFY_READED = 2;
    //好友请求：已添加
    public static final int STATUS_VERIFIED = 1;
    //好友请求：拒绝
    public static final int STATUS_VERIFY_REFUSE = 3;
    //好友请求：我发出的好友请求-暂未存储到本地数据库中
    public static final int STATUS_VERIFY_ME_SEND = 4;

    /**
     *  Api绑定的的AppKey，可以在“阿里云官网”->"API网关"->"应用管理"->"应用详情"查看
     */
    public static final String APP_KEY = "24690123";

    /**
     *  Api绑定的的AppSecret，用来做传输数据签名使用，可以在“阿里云官网”->"API网关"->"应用管理"->"应用详情"查看
     */
    public static final String APP_SECRET = "71005179cbd1f9c2e8c4f2d1ecea7ed0";

    /**
     * 是否以HTTPS方式提交请求
     * 本SDK采取忽略证书的模式,目的是方便大家的调试
     * 为了安全起见,建议采取证书校验方式
     *
     * 这里改为true后，银行卡识别无法使用
     */
    public static final boolean IS_HTTPS = false;

    /**
     *  Api绑定的域名
     */
    public static final String ID_CARD_BASE_URL = "dm-51.data.aliyun.com";//身份证识别baseurl
    public static final String BANK_CARD_BASE_URL = "yhk.market.alicloudapi.com";//银行卡识别baseUrl
    public static final String DRIVING_LICENSE_BASE_URL = "dm-53.data.aliyun.com";//行驶证识别baseurl
    public static final String ChAR_BASE_URL = "tysbgpu.market.alicloudapi.com";//通用文字识别
    public static final String APP_HOST = "dm-53.data.aliyun.com";


    /**
     *  超时时间，单位为秒
     */
    public static final int APP_CONNECTION_TIMEOUT = 5;
}
