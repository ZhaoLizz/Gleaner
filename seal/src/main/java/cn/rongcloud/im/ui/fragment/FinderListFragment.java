package cn.rongcloud.im.ui.fragment;

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
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.rongcloud.im.R;
import cn.rongcloud.im.bean.SearchThing;
import cn.rongcloud.im.ui.adapter.SearchListAdapter;

import static com.chad.library.adapter.base.BaseQuickAdapter.SCALEIN;

public class FinderListFragment extends Fragment {
    @BindView(R.id.fragment_rv)
    RecyclerView mFragmentRv;
    @BindView(R.id.refersh_layout)
    SwipeRefreshLayout mRefershLayout;
    Unbinder unbinder;
    private List<SearchThing> mThingArrayList = new ArrayList<>();
    private SearchListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rv, container, false);
        unbinder = ButterKnife.bind(this, view);

        mFragmentRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new SearchListAdapter(mThingArrayList);
        mAdapter.openLoadAnimation(SCALEIN);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Logger.d("here");
//                Intent intent = new Intent(getContext(), ConnectGleanerActivity.class);
//                intent.putExtra("thing", mThingArrayList.get(position));
//                startActivity(intent);
            }
        });
        mFragmentRv.setAdapter(mAdapter);
        mRefershLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                querySearchThing();
            }
        });

        querySearchThing();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        querySearchThing();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void querySearchThing() {
        BmobQuery<SearchThing> query = new BmobQuery<>();
        query.setLimit(50);
        query.findObjects(getActivity(), new FindListener<SearchThing>() {
            @Override
            public void onSuccess(List<SearchThing> list) {
                Logger.d(list.size());
                mThingArrayList.clear();
                mThingArrayList.addAll(list);
//                mAdapter.notifyDataSetChanged();
                mRefershLayout.setRefreshing(false);
            }

            @Override
            public void onError(int i, String s) {
                Logger.e(s);
            }
        });
    }
}
