package cn.rongcloud.im.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.rongcloud.im.R;


public class PublishActivity extends BaseActivity {
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


    private static final String TAG = "PublishActivity";
    public static final String EXTRA_BITMAP_STRING = "extra_bitmap_string";
    public static final String EXTRA_BITMAP = "extra_bitmap";
    private static int mPhotoCategory = -1;
    private static final int CATEGORY_ITEM = 1;
    private static final int CATEGORY_SCHOOL = 2;
    private static final int CATEGORY_ID = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String photoPath = intent.getStringExtra("photoUri");
        
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

    @OnClick(R.id.publish_good_publish_btn)
    public void onPublishClick() {
        Toast.makeText(this, "正在上传至服务器...", Toast.LENGTH_SHORT).show();
    }


}
