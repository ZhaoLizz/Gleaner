package cn.rongcloud.im.bean;

/**
 * Created by a6100890 on 2018/2/25.
 */

public class BitmapBodyJson {
    private int type;
    private String content;

    public BitmapBodyJson(String content) {
        this.type = 1;
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
