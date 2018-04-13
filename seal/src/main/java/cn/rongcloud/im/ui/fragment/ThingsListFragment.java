package cn.rongcloud.im.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.rongcloud.im.R;

public class ThingsListFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.fragment_container)
    FrameLayout mFragmentContainer;
    private List<Fragment> mFragments = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_things_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        mFragments.add(new GleanerListFragment());
        mFragments.add(new FinderListFragment());
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mFragments.get(0))
                .add(R.id.fragment_container, mFragments.get(1))
                .hide(mFragments.get(1))
                .show(mFragments.get(0))
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.thingslist_gleaner, R.id.thingslist_finder})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.thingslist_gleaner:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .hide(mFragments.get(1))
                        .show(mFragments.get(0))
                        .commit();
                break;
            case R.id.thingslist_finder:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .hide(mFragments.get(0))
                        .show(mFragments.get(1))
                        .commit();
                break;
        }
    }
}
