package com.sxu.testmodule.refreshlayout.test;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sxu.testmodule.R;
import com.sxu.testmodule.refreshlayout.RefreshLayout;

/**
 * @author Freeman
 * @date 2017/12/28
 */


public class ScrollViewFragment extends Fragment {

	private View mContentView;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mContentView = inflater.inflate(R.layout.fragment_scrollview_layout, null);
		return mContentView;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		final RefreshLayout refreshLayout = mContentView.findViewById(R.id.refresh_layout);

		refreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh(View headerView) {
				final TextView refreshText = headerView.findViewById(R.id.refresh_text);
				refreshText.setText("刷新中...");
				refreshLayout.postDelayed(new Runnable() {
					@Override
					public void run() {
						refreshText.setText("刷新完成");
						refreshLayout.postDelayed(new Runnable() {
							@Override
							public void run() {
								refreshLayout.refreshComplete();
							}
						}, 250);
					}
				}, 2000);
			}

			@Override
			public void onLoad(View footerView) {
				final TextView refreshText = footerView.findViewById(R.id.refresh_text);
				refreshText.setText("底部在刷新...");
				refreshLayout.postDelayed(new Runnable() {
					@Override
					public void run() {
						refreshText.setText("底部刷新完成");
						refreshLayout.postDelayed(new Runnable() {
							@Override
							public void run() {
								refreshLayout.refreshComplete();
							}
						}, 250);
					}
				}, 2000);
			}
		});
	}
}
