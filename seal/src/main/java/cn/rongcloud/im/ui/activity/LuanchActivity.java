package cn.rongcloud.im.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.rongcloud.im.R;
import cn.rongcloud.im.bean.BitmapBodyJson;
import cn.rongcloud.im.bean.Thing;
import cn.rongcloud.im.db.UserInfoBean;
import cn.rongcloud.im.utils.DateUtils;
import cn.rongcloud.im.utils.RecognizeUtil;
import id.zelory.compressor.Compressor;

public class LuanchActivity extends Activity {
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

    private File photoFile;
    private File compressedFile;
    private UserInfoBean user;
    private String location = "山西省太原市尖草坪区上兰村中北大学主楼";
    private Thing thing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        String filePath = intent.getStringExtra("photoUri");
        Logger.d(filePath);


        photoFile = new File(filePath);
        compressedFile = compressFile(photoFile);
        Glide.with(this).load(compressedFile).skipMemoryCache(true) // 不使用内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)// 不使用磁盘缓存 .into(img_publish_good);
                .into(img_publish_good);

        final String curTime = DateUtils.getStringToday("yyyy-MM-dd HH:mm:ss");
        user = BmobUser.getCurrentUser(this, UserInfoBean.class);
        final String base64Str = RecognizeUtil.file2Str(compressedFile);
        String jsonBody = new Gson().toJson(new BitmapBodyJson(base64Str));
        try {
            RecognizeUtil.sendPost(jsonBody, new RecognizeUtil.OnRecognizeListener() {
                @Override
                public void onRecognize(String jsonResult) {
                    String itemName = RecognizeUtil.parseItemJson(jsonResult);
                    Logger.d(itemName);
                    if (itemName.equals("校园卡") || itemName.equals("身份证")) {
                        RecognizeUtil.readTextImgByBaidu(compressedFile, new RecognizeUtil.OnRecognizeListener() {
                            @Override
                            public void onRecognize(String jsonResult) {
                                Map<String, String> schoolcardMessage = RecognizeUtil.parseSchoolJson(jsonResult);
                                //idcard
                                if (schoolcardMessage.get("card").equals("身份证")) {
                                    thing = new Thing();
                                    String name = schoolcardMessage.get("name");
                                    String number = schoolcardMessage.get("number");
                                    String nation = schoolcardMessage.get("nation");
                                    String sex = schoolcardMessage.get("sex");
                                    String birth = schoolcardMessage.get("birth");
                                    String home = schoolcardMessage.get("home");
                                    updataIdCardUI(name, sex, nation, home, number, location, curTime);
                                    thing.setItemtype(Thing.IDCARD);
                                    thing.setName(name);
                                    thing.setNumber(number);
                                    thing.setNation(nation);
                                    thing.setSex(sex);
                                    thing.setHomeLocation(home);
                                    thing.setUser(user);
                                    thing.setTime(curTime);
                                    thing.setLocation(location);
                                } else {
                                    //school card
                                    updataSchoolCardUI(schoolcardMessage.get("name"), schoolcardMessage.get("number"), schoolcardMessage.get("college"), curTime, location);
                                    thing = new Thing();
                                    thing.setItemtype(Thing.SCHOOLCARD);
                                    thing.setUser(user);
                                    thing.setItemName("校园卡");
                                    thing.setName(schoolcardMessage.get("name"));
                                    thing.setNumber(schoolcardMessage.get("number"));
                                    thing.setCollege(schoolcardMessage.get("college"));
                                    thing.setTime(curTime);
                                    thing.setLocation(location);
                                }
                            }
                        });
                    } else {
                        updataItemUI(itemName, location, curTime);
                        thing = new Thing();
                        thing.setItemtype(Thing.ITEM);
                        thing.setUser(user);
                        thing.setItemName(itemName);
                        thing.setLocation(location);
                        thing.setTime(curTime);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private File compressFile(File file) {
        Logger.d("压缩before大小" + file.getPath() + "\n" + file.length());
        File compressedFile = null;
        try {
            compressedFile = new Compressor(this)
                    .setQuality(20)
                    .compressToFile(file);
            Logger.d("压缩后大小" + compressedFile.getPath() + "\n" + compressedFile.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return compressedFile;
    }

    @OnClick(R.id.publish_good_publish_btn)
    public void onViewClicked() {
        mPublishProgress.setVisibility(View.VISIBLE);
        Toast.makeText(this, "正在上传至服务器...", Toast.LENGTH_SHORT).show();
        BmobFile.uploadBatch(LuanchActivity.this, new String[]{compressedFile.getPath()}, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> urls) {
                thing.setPhotoUrl(urls.get(0));
                thing.save(LuanchActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Logger.d("succeed");
                        mPublishProgress.setVisibility(View.GONE);
                        Toast.makeText(LuanchActivity.this, "成功上传数据!", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Logger.d(s);
                    }
                });
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void updataItemUI(final String name, final String location, final String time) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPublishProgress.setVisibility(View.GONE);
                mPublishLocationLayout.setVisibility(View.GONE);
                mPublishTimeLayout.setVisibility(View.GONE);
                tv_1st_r.setText(name);
                tv_3st_r.setText(location);
                tv_2st_r.setText(time);
            }
        });
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
}
