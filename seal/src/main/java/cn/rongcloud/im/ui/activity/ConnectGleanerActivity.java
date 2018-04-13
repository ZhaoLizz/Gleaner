package cn.rongcloud.im.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.rongcloud.im.R;
import cn.rongcloud.im.SealAppContext;
import cn.rongcloud.im.SealConst;
import cn.rongcloud.im.SealUserInfoManager;
import cn.rongcloud.im.bean.SearchThing;
import cn.rongcloud.im.bean.Thing;
import cn.rongcloud.im.db.Friend;
import cn.rongcloud.im.db.UserInfoBean;
import cn.rongcloud.im.server.network.async.AsyncTaskManager;
import cn.rongcloud.im.server.network.http.HttpException;
import cn.rongcloud.im.server.response.FriendInvitationResponse;
import cn.rongcloud.im.server.response.GetUserInfoByPhoneResponse;
import cn.rongcloud.im.server.utils.CommonUtils;
import cn.rongcloud.im.server.utils.NToast;
import cn.rongcloud.im.server.widget.DialogWithYesOrNoUtils;
import cn.rongcloud.im.server.widget.LoadDialog;
import io.rong.imlib.model.UserInfo;

public class ConnectGleanerActivity extends BaseActivity {
    @BindView(R.id.publish_4st_r_tv)
    TextView tv_time;
    @BindView(R.id.publish_time_layout)
    LinearLayout mPublishTimeLayout;
    @BindView(R.id.publish_sex_nation_layout)
    LinearLayout mPublishSexNationLayout;
    @BindView(R.id.publish_5st_r_tv)
    TextView tv_location;
    @BindView(R.id.publish_location_layout)
    LinearLayout mPublishLocationLayout;
    @BindView(R.id.publish_good_img)
    ImageView img_publish_good;
    @BindView(R.id.publish_good_name_tv)
    TextView tv_1st_r;
    @BindView(R.id.publish_good_time_tv)
    TextView tv_2st_r;
    @BindView(R.id.publish_good_location_tv)
    TextView tv_3st_r;
    @BindView(R.id.publish_good_publish_btn)
    Button btn_publish_good_publish;
    @BindView(R.id.publish_1st_tv)
    TextView tv_1st_l;
    @BindView(R.id.publish_2st_tv)
    TextView tv_2st_l;
    @BindView(R.id.publish_3st_tv)
    TextView tv_3st_l;
    @BindView(R.id.publish_sex_tv)
    TextView mPublishSexTv;
    @BindView(R.id.publish_nation_tv)
    TextView mPublishNationTv;
    @BindView(R.id.publish_progress)
    ProgressBar mPublishProgress;
    @BindView(R.id.publish_4st_l_tv)
    TextView mPublish4stLTv;
    @BindView(R.id.publish_5st_l_tv)
    TextView mPublish5stLTv;

    private static final int SEARCH_PHONE = 10;
    private static final int ADD_FRIEND = 11;
    private static final int CLICK_CONVERSATION_USER_PORTRAIT = 1;
    private String addFriendMessage = "我是失主,感谢您发布的招领启事";
    private String mFriendId = "";
    private String mPhone = "";
    private UserInfoBean mUser;
    private Friend mFriend;
    private Thing thing;
    private SearchThing searchThing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        thing = (Thing) intent.getSerializableExtra("thing");
        searchThing = (SearchThing) intent.getSerializableExtra("searching");


        if (thing != null) {


            Glide.with(ConnectGleanerActivity.this).load(thing.getPhotoUrl())
                    .skipMemoryCache(true) // 不使用内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(img_publish_good);
            switch (thing.getItemType()) {
                case Thing.ITEM:
                    Logger.d(thing.getItemName() + thing.getLocation() + thing.getTime());
                    updataItemUI(thing.getItemName(), thing.getLocation(), thing.getTime());
                    break;
                case Thing.SCHOOLCARD:
                    Logger.d(thing.getName(), thing.getNumber(), thing.getCollege(), thing.getTime(), thing.getLocation());
                    updataSchoolCardUI(thing.getName(), thing.getNumber(), thing.getCollege(), thing.getTime(), thing.getLocation());
                    break;
                case Thing.IDCARD:
                    Logger.d(thing.getName()+thing.getSex()+ thing.getNation()+ thing.getHomeLocation()+ thing.getNumber()+thing.getLocation()+ thing.getTime());
                    updataIdCardUI(thing.getName(), thing.getSex(), thing.getNation(), thing.getHomeLocation(), thing.getNumber(), thing.getLocation(), thing.getTime());
                    break;
            }
        } else if (searchThing != null) {
            Logger.d("searchThing not null" );
            updateFindItemUI(searchThing.getItemName(), searchThing.getLocation(), "", searchThing.getDescription());
        }

    }

    private void updataItemUI(final String name, final String location, final String time) {
        mPublishProgress.setVisibility(View.GONE);
        mPublishLocationLayout.setVisibility(View.GONE);
        mPublishTimeLayout.setVisibility(View.GONE);
        tv_1st_l.setText("物品名称");
        tv_1st_r.setText(name);
        tv_3st_r.setText(location);
        tv_2st_l.setText("获取时间");
        tv_2st_r.setText(time);
    }

    private void updateFindItemUI(String name, String location, String time, String descrip) {
        mPublishProgress.setVisibility(View.GONE);
        mPublishLocationLayout.setVisibility(View.GONE);
        mPublishTimeLayout.setVisibility(View.GONE);
        tv_1st_l.setText("物品名称");
        tv_1st_r.setText(name);
        tv_3st_r.setText(location);
        tv_2st_l.setText("物品描述");
        tv_2st_r.setText(descrip);


    }

    private void updataSchoolCardUI(final String name, final String number, final String college, final String time, final String location) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPublishProgress.setVisibility(View.GONE);
                tv_1st_r.setText(name);
                tv_1st_l.setText("姓名");

                tv_2st_r.setText(college);
                tv_2st_l.setText("院系");

                tv_3st_r.setText(number);
                tv_3st_l.setText("学号");

                mPublishLocationLayout.setVisibility(View.VISIBLE);
                mPublishTimeLayout.setVisibility(View.VISIBLE);
                tv_time.setText(time);
                tv_location.setText(location);
            }
        });
    }

    private void updataIdCardUI(final String name, final String sex, final String nation, final String address, final String num, final String location, final String time) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPublishProgress.setVisibility(View.GONE);

                tv_1st_r.setText(name);
                tv_1st_l.setText("姓名");

                mPublishSexNationLayout.setVisibility(View.VISIBLE);
                mPublishSexTv.setText(sex);
                mPublishNationTv.setText(nation);

                tv_2st_r.setText(num);
                tv_2st_l.setText("证件号码");

                tv_3st_r.setText(address);
                tv_3st_l.setText("住址");

                mPublishLocationLayout.setVisibility(View.VISIBLE);
                mPublishTimeLayout.setVisibility(View.VISIBLE);
                tv_time.setText(time);
                tv_location.setText(location);
            }
        });
    }


    @OnClick(R.id.publish_good_publish_btn)
    public void onViewClicked() {
        if (thing != null) {
            
        } else if (searchThing != null) {

        }
        addFirend();
    }

    private void addFirend() {
        LoadDialog.show(mContext);
        request(SEARCH_PHONE, true);
    }

    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case SEARCH_PHONE:
                return action.getUserInfoFromPhone("86", mPhone);
            case ADD_FRIEND:
                return action.sendFriendInvitation(mFriendId, addFriendMessage);
        }
        return super.doInBackground(requestCode, id);
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        if (result != null) {
            switch (requestCode) {
                case SEARCH_PHONE:
                    final GetUserInfoByPhoneResponse userInfoByPhoneResponse = (GetUserInfoByPhoneResponse) result;
                    if (userInfoByPhoneResponse.getCode() == 200) {
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, "连接成功！");
                        mFriendId = userInfoByPhoneResponse.getResult().getId();
                        String portraitUri = null;
                        if (userInfoByPhoneResponse.getResult() != null) {
                            GetUserInfoByPhoneResponse.ResultEntity userInfoByPhoneResponseResult = userInfoByPhoneResponse.getResult();
                            UserInfo userInfo = new UserInfo(userInfoByPhoneResponseResult.getId(),
                                    userInfoByPhoneResponseResult.getNickname(),
                                    Uri.parse(userInfoByPhoneResponseResult.getPortraitUri()));
                            portraitUri = SealUserInfoManager.getInstance().getPortraitUri(userInfo);
                        }

                        if (isFriendOrSelf(mFriendId)) {
                            Logger.d("isSelf");
                            Intent intent = new Intent(ConnectGleanerActivity.this, UserDetailActivity.class);
                            intent.putExtra("friend", mFriend);
                            intent.putExtra("type", CLICK_CONVERSATION_USER_PORTRAIT);
                            startActivity(intent);
                            SealAppContext.getInstance().pushActivity(ConnectGleanerActivity.this);
                            return;
                        }
                        DialogWithYesOrNoUtils.getInstance().showEditDialog(mContext, getString(R.string.add_text), getString(R.string.add_friend), new DialogWithYesOrNoUtils.DialogCallBack() {
                            @Override
                            public void executeEvent() {

                            }

                            @Override
                            public void updatePassword(String oldPassword, String newPassword) {

                            }

                            @Override
                            public void executeEditEvent(String editText) {
                                if (!CommonUtils.isNetworkConnected(mContext)) {
                                    NToast.shortToast(mContext, R.string.network_not_available);
                                    return;
                                }
                                addFriendMessage = editText;
                                if (TextUtils.isEmpty(editText)) {
                                    addFriendMessage = "我是" + getSharedPreferences("config", MODE_PRIVATE).getString(SealConst.SEALTALK_LOGIN_NAME, "");
                                }
                                if (!TextUtils.isEmpty(mFriendId)) {
                                    LoadDialog.show(mContext);
                                    request(ADD_FRIEND);
                                } else {
                                    NToast.shortToast(mContext, "id is null");
                                }
                            }
                        });
                    }
                    break;
                case ADD_FRIEND:
                    FriendInvitationResponse fres = (FriendInvitationResponse) result;
                    if (fres.getCode() == 200) {
                        NToast.shortToast(mContext, getString(R.string.request_success));
                        LoadDialog.dismiss(mContext);
                    } else {
                        NToast.shortToast(mContext, "请求失败 错误码:" + fres.getCode());
                        LoadDialog.dismiss(mContext);
                    }
                    break;
            }
        }

    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        switch (requestCode) {
            case ADD_FRIEND:
                NToast.shortToast(mContext, "你们已经是好友");
                LoadDialog.dismiss(mContext);
                break;
            case SEARCH_PHONE:
                if (state == AsyncTaskManager.HTTP_ERROR_CODE || state == AsyncTaskManager.HTTP_NULL_CODE) {
                    super.onFailure(requestCode, state, result);
                } else {
                    NToast.shortToast(mContext, "用户不存在");
                }
                LoadDialog.dismiss(mContext);
                break;
        }
    }

    private boolean isFriendOrSelf(String id) {
        String inputPhoneNumber = mPhone;
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        String selfPhoneNumber = sp.getString(SealConst.SEALTALK_LOGING_PHONE, "");
        if (inputPhoneNumber != null) {
            if (inputPhoneNumber.equals(selfPhoneNumber)) {
                mFriend = new Friend(sp.getString(SealConst.SEALTALK_LOGIN_ID, ""),
                        sp.getString(SealConst.SEALTALK_LOGIN_NAME, ""),
                        Uri.parse(sp.getString(SealConst.SEALTALK_LOGING_PORTRAIT, "")));
                return true;
            } else {
                mFriend = SealUserInfoManager.getInstance().getFriendByID(id);
                if (mFriend != null) {
                    return true;
                }
            }
        }
        return false;
    }
}
