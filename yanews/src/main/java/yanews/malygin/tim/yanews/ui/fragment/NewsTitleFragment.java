package yanews.malygin.tim.yanews.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import yanews.malygin.tim.yanews.R;
import yanews.malygin.tim.yanews.api.Api;
import yanews.malygin.tim.yanews.api.ApiKeys;
import yanews.malygin.tim.yanews.api.methods.GetNewsMethod;
import yanews.malygin.tim.yanews.data.News;
import yanews.malygin.tim.yanews.ui.adapter.NewsAdapter;

import static yanews.malygin.tim.yanews.util.ViewUtils.findById;

/**
 * @author timofey.malygin
 */
public class NewsTitleFragment extends Fragment implements GetNewsMethod.GetNewsResult {

    private NewsAdapter adapter;
    private GetNewsMethod method;
    private View progressView;
    private RecyclerView newsView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        method = Api.createMethod(ApiKeys.GET_NEWS);
        method.setCallback(this).send();

        newsView = findById(view, R.id.news);
        progressView = findById(view, R.id.loading);
        newsView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsAdapter((NewsAdapter.OnNewsSelectedListener) getActivity());
        newsView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        method.cancel();
    }

    @Override
    public void showLoading() {
        if (progressView == null) return;
        progressView.setVisibility(View.VISIBLE);
        newsView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onNewsLoaded(@NonNull List<News> news) {
        progressView.setVisibility(View.INVISIBLE);
        newsView.setVisibility(View.VISIBLE);
        adapter.setNews(news);
    }

    @Override
    public void onError(@Nullable String msg) {
        progressView.setVisibility(View.INVISIBLE);
        newsView.setVisibility(View.VISIBLE);
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }
}
