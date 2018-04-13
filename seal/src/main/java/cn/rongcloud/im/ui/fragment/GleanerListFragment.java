package cn.rongcloud.im.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.rongcloud.im.R;
import cn.rongcloud.im.bean.Thing;
import cn.rongcloud.im.ui.activity.ConnectGleanerActivity;
import cn.rongcloud.im.ui.adapter.MultipleItemQuickAdapter;

import static com.chad.library.adapter.base.BaseQuickAdapter.SCALEIN;

public class GleanerListFragment extends Fragment {
    @BindView(R.id.fragment_rv)
    RecyclerView mFragmentRv;
    Unbinder unbinder;
    @BindView(R.id.refersh_layout)
    SwipeRefreshLayout mRefershLayout;
    private MultipleItemQuickAdapter mAdapter;
    private List<Thing> things;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rv, container, false);
        unbinder = ButterKnife.bind(this, view);

        things = new ArrayList<>();
        mAdapter = new MultipleItemQuickAdapter(things, getContext());
        mAdapter.openLoadAnimation(SCALEIN);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getContext(), ConnectGleanerActivity.class);
                intent.putExtra("thing", things.get(position));
                startActivity(intent);
            }
        });

        mFragmentRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mFragmentRv.setAdapter(mAdapter);
        mRefershLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryThing();
            }
        });
        queryThing();
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        queryThing();
    }

    private void queryThing() {
        BmobQuery<Thing> query = new BmobQuery<>();
        query.setLimit(50);
        query.findObjects(getActivity(), new FindListener<Thing>() {
            @Override
            public void onSuccess(List<Thing> list) {
                things.clear();
                things.addAll(list);
                mAdapter.notifyDataSetChanged();
                mRefershLayout.setRefreshing(false);
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }


}
