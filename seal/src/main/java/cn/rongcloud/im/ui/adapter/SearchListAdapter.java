package cn.rongcloud.im.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.rongcloud.im.R;
import cn.rongcloud.im.bean.SearchThing;

/**
 * Created by qinba on 2018/4/14.
 */

public class SearchListAdapter extends BaseQuickAdapter<SearchThing,BaseViewHolder> {
    public SearchListAdapter(int layoutResId,@Nullable List<SearchThing> data) {
        super(layoutResId,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchThing item) {
//        helper.setText(R.id.lost_item_name, item.getItemName());
//        helper.setText(R.id.lost_description, item.getDescription());
//        helper.setText(R.id.lost_location, item.getLocation());
//        helper.setText(R.id.lost_time, item.getTime());
    }
}
