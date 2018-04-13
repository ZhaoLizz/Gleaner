package cn.rongcloud.im.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.File;
import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.rongcloud.im.db.UserInfoBean;

public class Thing extends BmobObject implements MultiItemEntity,Serializable{
    public static final int ITEM = 0;
    public static final int SCHOOLCARD = 1;
    public static final int IDCARD = 2;

    private String itemName;
    private String time;
    private String location;
    private UserInfoBean user;
    private String photoUrl;


    private String name;    //idcard or school card person name
    private String number;  //idcard or school card number

    private String college; //school card college

    private String sex;
    private String nation;
    private String birth;
    private String homeLocation;

    private int itemtype; // 0:item  1:school  2:idcard

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setItemtype(int itemtype) {
        this.itemtype = itemtype;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public UserInfoBean getUser() {
        return user;
    }

    public void setUser(UserInfoBean user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getHomeLocation() {
        return homeLocation;
    }

    public void setHomeLocation(String homeLocation) {
        this.homeLocation = homeLocation;
    }

    @Override
    public int getItemType() {
        return itemtype;
    }

    /*public class Item implements MultiItemEntity{
        private String itemName;
        private String time;
        private String location;
        private UserInfoBean user;

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public UserInfoBean getUser() {
            return user;
        }

        public void setUser(UserInfoBean user) {
            this.user = user;
        }

        @Override
        public int getItemType() {
            return 0;
        }
    }

    public class SchoolCard extends Item {
        private String name;    //idcard or school card person name
        private String number;  //idcard or school card number

        private String college; //school card college

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getCollege() {
            return college;
        }

        public void setCollege(String college) {
            this.college = college;
        }

        @Override
        public int getItemType() {
            return 1;
        }
    }

    public class IDCard extends Item {
        private String name;    //idcard or school card person name
        private String number;  //idcard or school card number
        private String sex;
        private String nation;
        private String birth;
        private String homeLocation;

        @Override
        public int getItemType() {
            return 2;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getNation() {
            return nation;
        }

        public void setNation(String nation) {
            this.nation = nation;
        }

        public String getBirth() {
            return birth;
        }

        public void setBirth(String birth) {
            this.birth = birth;
        }

        public String getHomeLocation() {
            return homeLocation;
        }

        public void setHomeLocation(String homeLocation) {
            this.homeLocation = homeLocation;
        }
    }
*/


}
