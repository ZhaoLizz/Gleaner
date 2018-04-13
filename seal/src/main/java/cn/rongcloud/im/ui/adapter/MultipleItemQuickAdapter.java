package cn.rongcloud.im.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;

import java.util.List;

import cn.rongcloud.im.R;
import cn.rongcloud.im.bean.Thing;
import cn.rongcloud.im.ui.activity.ConnectGleanerActivity;

public class MultipleItemQuickAdapter extends BaseMultiItemQuickAdapter<Thing, BaseViewHolder> {
    private Context mContext;

    public MultipleItemQuickAdapter(List<Thing> data,Context context) {
        super(data);
        mContext = context;
        addItemType(Thing.ITEM, R.layout.item_thing);
        addItemType(Thing.SCHOOLCARD, R.layout.item_school_card);
        addItemType(Thing.IDCARD, R.layout.item_id_card);
    }

    @Override
    protected void convert(BaseViewHolder helper, final Thing item) {
        switch (item.getItemType()) {
            case Thing.ITEM:
                helper.setText(R.id.item_name_tv, item.getItemName());
                helper.setText(R.id.item_time_tv, item.getTime());
                helper.setText(R.id.item_location_tv, item.getLocation());
                if (item.getPhotoUrl() != null) {
                    Logger.d(item.getPhotoUrl());
                    Glide.with(mContext).load(item.getPhotoUrl()).into((ImageView) helper.getView(R.id.lost_pro_img));
                }
                helper.getView(R.id.item_thing).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, ConnectGleanerActivity.class);
                        intent.putExtra("thing", item);
                        mContext.startActivity(intent);
                    }
                });
                break;
            case Thing.SCHOOLCARD:
                if (item.getPhotoUrl() != null) {
                    Glide.with(mContext).load(item.getPhotoUrl()).into((ImageView) helper.getView(R.id.lost_pro_img));
                }
                helper.setText(R.id.school_name_tv, item.getName());
                helper.setText(R.id.school_id_tv, item.getNumber());
                helper.setText(R.id.school_content_tv, item.getCollege());
                helper.setText(R.id.school_card_time_tv, item.getTime());
                helper.setText(R.id.school_card_location_tv, item.getLocation());
                break;
            case Thing.IDCARD:
//                ((TextView) helper.getView(R.id.id_card_name_tv)).setText(item.getName());
                helper.setText(R.id.id_card_name_tv, item.getName());
                helper.setText(R.id.id_card_gender_tv, item.getSex());
                helper.setText(R.id.id_card_ethnic_tv, item.getNation());
                helper.setText(R.id.id_card_id_tv, item.getNumber());
                break;
        }
    }
}
