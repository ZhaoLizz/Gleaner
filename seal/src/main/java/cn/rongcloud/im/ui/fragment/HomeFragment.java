package cn.rongcloud.im.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.rongcloud.im.R;
import cn.rongcloud.im.server.utils.photo.PhotoUtils;
import cn.rongcloud.im.server.widget.BottomMenuDialog;

/**
 * Created by qinba on 2018/4/10.
 */

public class HomeFragment extends Fragment {

    @BindView(R.id.btn_home_camera_publish)
    TextView mBtnHomeCameraPublish;
    @BindView(R.id.btn_home_search)
    TextView mBtnHomeSearch;
    @BindView(R.id.btn_home_recruit_notice)
    TextView mBtnHomeRecruitNotice;
    Unbinder unbinder;

    private PhotoUtils mPhotoUtils;
    private Uri mPhotoUri;
    private BottomMenuDialog dialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        mPhotoUtils = new PhotoUtils(new PhotoUtils.OnPhotoResultListener() {
            @Override
            public void onPhotoResult(Uri uri) {
                Logger.d("onPhotoResult");
                if (uri != null && !TextUtils.isEmpty(uri.getPath())) {
                    Logger.d(uri);
                }
            }

            @Override
            public void onPhotoCancel() {
                Logger.e("onPhotocancel");
            }
        });
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_home_camera_publish, R.id.btn_home_search, R.id.btn_home_recruit_notice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_home_camera_publish:
                //失物招领
                showPhotoDialog();
//                PhotoUtils.matisseSelectPic(getActivity(), PhotoUtils.REQUEST_CODE_CHOOSE);
                break;
            case R.id.btn_home_search:
                //寻物启事
                break;
            case R.id.btn_home_recruit_notice:
                break;
        }
    }



    /**
     * 弹出底部框
     */
    private void showPhotoDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog = new BottomMenuDialog(getContext());
        dialog.setConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                mPhotoUtils.takePicture(getActivity());
            }
        });
        dialog.setMiddleListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                mPhotoUtils.selectPicture(getActivity());
            }
        });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*Logger.d("Home onActivityResult: \n" + data.getData());
        switch (requestCode) {
            case PhotoUtils.INTENT_CROP:
            case PhotoUtils.INTENT_TAKE:
            case PhotoUtils.INTENT_SELECT:
                mPhotoUtils.onActivityResult(getActivity(), requestCode, resultCode, data);
                break;
        }*/
    }
}
