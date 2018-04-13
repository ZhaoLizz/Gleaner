package cn.rongcloud.im.bean;

import cn.bmob.v3.BmobObject;
import cn.rongcloud.im.db.UserInfoBean;

/**
 * Created by qinba on 2018/4/14.
 */

public class SearchThing extends BmobObject{
    private String itemName;
    private String location;
    private String time;
    private String photoUrl;
    private String description;
    private UserInfoBean mUserInfoBean;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserInfoBean getUserInfoBean() {
        return mUserInfoBean;
    }

    public void setUserInfoBean(UserInfoBean userInfoBean) {
        mUserInfoBean = userInfoBean;
    }
}
