package cn.rongcloud.im.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.rongcloud.im.R;
import cn.rongcloud.im.bean.SearchThing;
import cn.rongcloud.im.db.UserInfoBean;
import cn.rongcloud.im.server.utils.photo.PhotoUtils;
import cn.rongcloud.im.server.widget.BottomMenuDialog;
import id.zelory.compressor.Compressor;

public class SearchThingActivity extends BaseActivity {

    @BindView(R.id.search_image)
    ImageView mSearchImage;
    @BindView(R.id.search_item_name)
    EditText mSearchItemName;
    @BindView(R.id.search_location)
    EditText mSearchLocation;
    @BindView(R.id.search_time)
    TextView mSearchTime;
    @BindView(R.id.publish_location_layout)
    LinearLayout mPublishLocationLayout;
    @BindView(R.id.search_say)
    EditText mSearchSay;
    @BindView(R.id.search_publish)
    Button mSearchPublish;
    @BindView(R.id.search_progress)
    ProgressBar mSearchProgress;

    private BottomMenuDialog dialog;
    private PhotoUtils mPhotoUtils;
    private File compressedFile;
    private String time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_thing);
        ButterKnife.bind(this);

        initPhotoUtils();
    }


    @OnClick({R.id.search_image, R.id.search_time, R.id.search_publish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_image:
                showPhotoDialog();
                break;
            case R.id.search_time:
                new TimePickerDialog.Builder()
                        .setType(Type.YEAR_MONTH_DAY)
                        .setCallBack(new OnDateSetListener() {
                            @Override
                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                time = getDateToString(millseconds);
                                mSearchTime.setText(time);
                            }
                        })
                        .build()
                        .show(getSupportFragmentManager(), "year_month_day");
                break;
            case R.id.search_publish:
                mSearchProgress.setVisibility(View.VISIBLE);

                final SearchThing searchThing = new SearchThing();
                searchThing.setItemName(mSearchItemName.getText().toString());
                searchThing.setTime(time);
                searchThing.setUserInfoBean(BmobUser.getCurrentUser(SearchThingActivity.this, UserInfoBean.class));
                searchThing.setUserPhone(BmobUser.getCurrentUser(SearchThingActivity.this, UserInfoBean.class).getUsername());
                searchThing.setDescription(mSearchSay.getText().toString());
                searchThing.setLocation(mSearchLocation.getText().toString());
                if (compressedFile != null) {
                    BmobFile.uploadBatch(SearchThingActivity.this, new String[]{compressedFile.getPath()}, new UploadBatchListener() {
                        @Override
                        public void onSuccess(List<BmobFile> list, List<String> urls) {
                            searchThing.setPhotoUrl(urls.get(0));
                            searchThing.save(SearchThingActivity.this, new SaveListener() {
                                @Override
                                public void onSuccess() {
                                    mSearchProgress.setVisibility(View.GONE);
                                    Toast.makeText(mContext, "上传成功！", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Logger.e(s);
                                }
                            });
                        }

                        @Override
                        public void onProgress(int i, int i1, int i2, int i3) {

                        }

                        @Override
                        public void onError(int i, String s) {
                            Logger.e(s);
                        }
                    });
                } else {
                    searchThing.save(SearchThingActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            mSearchProgress.setVisibility(View.GONE);
                            Toast.makeText(mContext, "上传成功！", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Logger.e(s);
                        }
                    });
                }
                break;
        }
    }

    private void initPhotoUtils() {
        mPhotoUtils = new PhotoUtils();
        mPhotoUtils.setOnPhotoResultListener(new PhotoUtils.OnPhotoResultListener() {
            @Override
            public void onPhotoResult(Uri uri) {
                Logger.d(uri.getPath());
                if (uri != null && !TextUtils.isEmpty(uri.getPath())) {
                    compressedFile = compressFile(new File(uri.getPath()));
                    Glide.with(SearchThingActivity.this).load(compressedFile)
                            .skipMemoryCache(true) // 不使用内存缓存
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(mSearchImage);
                }
            }

            @Override
            public void onPhotoCancel() {
                Logger.d("here");
            }
        });

    }

    static public final int REQUEST_CODE_ASK_PERMISSIONS = 101;
    /**
     * 弹出底部框
     */
    @TargetApi(23)
    private void showPhotoDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog = new BottomMenuDialog(mContext);
        dialog.setConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkPermission = checkSelfPermission(Manifest.permission.CAMERA);
                    if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS);
                        } else {
                            new AlertDialog.Builder(mContext)
                                    .setMessage("您需要在设置里打开相机权限。")
                                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS);
                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .create().show();
                        }
                        return;
                    }
                }
                mPhotoUtils.takePicture(SearchThingActivity.this);
            }
        });
        dialog.setMiddleListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                mPhotoUtils.selectPicture(SearchThingActivity.this);
            }
        });
        dialog.show();
    }

    private File compressFile(File file) {
        File compressedFile = null;
        try {
            compressedFile = new Compressor(this)
                    .setQuality(20)
                    .compressToFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return compressedFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PhotoUtils.INTENT_CROP:
            case PhotoUtils.INTENT_TAKE:
            case PhotoUtils.INTENT_SELECT:
                mPhotoUtils.onActivityResult(SearchThingActivity.this, requestCode, resultCode, data);
                break;
        }
    }

    public String getDateToString(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date(time);
        return sf.format(d);
    }
}
