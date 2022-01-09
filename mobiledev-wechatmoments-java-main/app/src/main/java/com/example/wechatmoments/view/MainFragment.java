package com.example.wechatmoments.view;

import static com.example.wechatmoments.data.Constants.LABEL;
import static com.example.wechatmoments.data.Constants.ON_LOAD_TWEETS_FAILED;
import static com.example.wechatmoments.data.Constants.ON_LOAD_USER_FAILED;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.wechatmoments.R;
import com.example.wechatmoments.data.entity.Tweet;
import com.example.wechatmoments.data.entity.User;
import com.example.wechatmoments.databinding.FragmentMainBinding;
import com.example.wechatmoments.imageloader.GlideImageLoader;
import com.example.wechatmoments.presenter.Presenter;
import com.example.wechatmoments.presenter.PresenterInterface;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;

import java.util.List;

public class MainFragment extends Fragment implements ViewInterface {

    private RecyclerViewAdapter recyclerViewAdapter;
    private FragmentMainBinding binding;
    private PresenterInterface presenter;
    private GlideImageLoader glideImageLoader;
    private ImageView profile_image;
    private ImageView avator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.refreshLayout.setRefreshHeader(new ClassicsHeader(requireActivity()));
        binding.refreshLayout.setRefreshFooter(new ClassicsFooter(requireActivity()));
        binding.refreshLayout.setEnableLoadMore(true);
        binding.refreshLayout.setEnableRefresh(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewAdapter = new RecyclerViewAdapter();
        binding.recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayout.HORIZONTAL));
        binding.recyclerView.setAdapter(recyclerViewAdapter);
        presenter = Presenter.createPresenter(this);
        glideImageLoader = GlideImageLoader.getGlideImageLoader();
        profile_image = (ImageView) view.findViewById(R.id.profile_image);
        avator = (ImageView) view.findViewById(R.id.avator);

        String username = "jsmith";
        presenter.loadData(username);
        setListener();
        return view;
    }

    @Override
    public void onTweetsLoaded(List<Tweet> tweets) {
        Log.i(LABEL, "loadTweetsData");
        recyclerViewAdapter.setData(tweets);
    }

    @Override
    public void onUserLoaded(User user) {
        glideImageLoader.onDisplayImage(getActivity(),profile_image,user.getProfile_image());
        glideImageLoader.onDisplayImage(getActivity(), avator, user.getAvatar());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void setListener() {
        binding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            presenter.refreshTweets();
            refreshLayout.finishRefresh();
        });

        binding.refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            presenter.loadMoreTweets();
            refreshLayout.finishLoadMore();
        });
    }

    @Override
    public void onTweetsLoadFailed() {
        Toast.makeText(getActivity(), ON_LOAD_TWEETS_FAILED, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUserLoadFailed() {
        Toast.makeText(getActivity(), ON_LOAD_USER_FAILED, Toast.LENGTH_LONG).show();
    }
}
