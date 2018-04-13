package cn.rongcloud.im.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.rongcloud.im.R;
import cn.rongcloud.im.bean.SearchThing;
import cn.rongcloud.im.ui.activity.ConnectGleanerActivity;

/**
 * Created by qinba on 2018/4/14.
 */

public class FindAdapter extends RecyclerView.Adapter<FindAdapter.SearchItemHolder> {
    private Context mContext;
    private List<SearchThing> mSearchThingList;

    public FindAdapter(Context context, List<SearchThing> searchThingList) {
        mContext = context;
        mSearchThingList = searchThingList;
    }

    @Override
    public SearchItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchItemHolder(LayoutInflater.from(mContext).inflate(R.layout.item_search, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchItemHolder holder, final int position) {
        holder.lostItemName.setText(mSearchThingList.get(position).getItemName());
        holder.location.setText(mSearchThingList.get(position).getLocation());
        holder.time.setText(mSearchThingList.get(position).getTime());
        holder.descrip.setText(mSearchThingList.get(position).getDescription());

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ConnectGleanerActivity.class);
                intent.putExtra("searching", mSearchThingList.get(position));
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mSearchThingList.size();
    }


    class SearchItemHolder extends RecyclerView.ViewHolder {
        TextView lostItemName;
        TextView location;
        TextView time;
        TextView descrip;
        CardView mCardView;

        public SearchItemHolder(View itemView) {
            super(itemView);
            lostItemName = (TextView) itemView.findViewById(R.id.lost_item_name);
            location = (TextView) itemView.findViewById(R.id.lost_location);
            time = (TextView) itemView.findViewById(R.id.lost_time);
            descrip = (TextView) itemView.findViewById(R.id.lost_description);
            mCardView = (CardView) itemView.findViewById(R.id.item_search);
        }


    }
}
