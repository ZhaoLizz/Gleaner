package cn.rongcloud.im.bean;

import java.util.List;

/**
 * Created by a6100890 on 2017/11/15.
 */

public class OCRCharDataJson {
    public static class Ret {
        private String word;

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }
    }

    private List<Ret> ret;
    private Boolean success;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Ret> getRet() {
        return ret;
    }

    public void setRet(List<Ret> ret) {
        this.ret = ret;
    }
}




