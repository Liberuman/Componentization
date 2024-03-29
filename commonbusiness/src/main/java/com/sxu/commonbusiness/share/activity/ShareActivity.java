package com.sxu.commonbusiness.share.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sxu.commonbusiness.R;
import com.sxu.commonbusiness.share.ShareConstants;
import com.sxu.commonbusiness.share.ShareManager;
import com.sxu.commonlibrary.commonutils.DisplayUtil;
import com.sxu.commonlibrary.commonutils.LogUtil;
import com.sxu.commonlibrary.commonutils.ViewBgUtil;

/**
 * Copyright (c) 2017. zhinanmao Inc. All rights reserved.
 * <p>
 * <p>
 * 类或接口的描述信息
 *
 * @author Freeman
 * @date 17/9/25
 */


public class ShareActivity extends Activity {

	private ImageView closeIcon;
	private GridView shareGrid;
	private LinearLayout rootLayout;

	private ShareManager shareManager;
	private ShareItemBean[] shareData = {
		new ShareItemBean(R.drawable.cb_share_weibo_icon, "微博", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				shareManager.share(ShareConstants.SHARE_BY_WEIBO);
			}
		}),
		new ShareItemBean(R.drawable.cb_share_wechat_icon, "微信", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				shareManager.share(ShareConstants.SHARE_BY_WECAHT);
			}
		}),
		new ShareItemBean(R.drawable.cb_share_wechat_mement_icon, "朋友圈", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				shareManager.share(ShareConstants.SHARE_BY_WECHAT_MOMENT);
			}
		}),
		new ShareItemBean(R.drawable.cb_share_qq_icon, "QQ", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				shareManager.share(ShareConstants.SHARE_BY_QQ);
			}
		}),
		new ShareItemBean(R.drawable.cb_share_qq_zone_icon, "QQ空间", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				shareManager.share(ShareConstants.SHARE_BY_QQ_ZONE);
			}
		}),
		new ShareItemBean(R.drawable.cb_share_other_icon, "其他", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(Intent.ACTION_SEND);
				intent.setType("image/*");
				intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
				intent.putExtra(Intent.EXTRA_TEXT, "I have successfully share my message through my app");
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(Intent.createChooser(intent, getTitle()));
			}
		}),
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(0, 0);
		setContentView(R.layout.cb_activity_share_layout);
		getViews();
		initActivity();

		shareManager = ShareManager.getInstance();
		shareManager.init(this, "title", "desc", "http://images.cnitblog.com/blog/316630/201408/091736497094159.png", "http://www.zhinanmao.com");
	}

	protected void getViews() {
		closeIcon = (ImageView) findViewById(R.id.cb_close_icon);
		shareGrid = (GridView) findViewById(R.id.cb_share_grid);
		rootLayout = (LinearLayout) findViewById(R.id.cb_root_layout);
	}

	protected void initActivity() {
		ViewGroup.LayoutParams params = rootLayout.getLayoutParams();
		params.width = (DisplayUtil.getScreenWidth() - DisplayUtil.dpToPx(60));
		rootLayout.setLayoutParams(params);
		ViewBgUtil.setShapeBg(rootLayout, GradientDrawable.RECTANGLE, Color.WHITE, DisplayUtil.dpToPx(4));
		shareGrid.setNumColumns(shareData.length % 3 == 0 ? 3 : 4);
		shareGrid.setAdapter(new ShareAdapter());

		closeIcon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	public static void enter(Context context, String title, String desc, String url, String iconUrl) {
		Intent intent = new Intent(context, ShareActivity.class);
		if (desc != null && desc.length() > 140) {
			desc = desc.substring(0, 140);
		}
		intent.putExtra("title", title);
		intent.putExtra("desc", desc);
		intent.putExtra("url", url);
		intent.putExtra("iconUrl", iconUrl);
		context.startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtil.i("onActivityResult");
		if (shareManager.getShareInstance() != null) {
			LogUtil.i("handleResult called");
			shareManager.getShareInstance().handleResult(requestCode, resultCode, data);
		} else {
			LogUtil.i("handleResult not called");
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		LogUtil.i("onNetIntent");
		// 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
		// 来接收微博客户端返回的数据；执行成功，返回 true，并调用
		// {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
		if (shareManager.getShareInstance() != null) {
			shareManager.getShareInstance().handleResult(0, 0, intent);
		}
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, 0);
	}

	public class ShareAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return shareData.length;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public ShareItemBean getItem(int position) {
			return shareData[position];
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = getLayoutInflater().inflate(R.layout.cb_item_share_layout, null);
			((ImageView)itemView.findViewById(R.id.cb_share_icon)).setImageResource(getItem(position).shareIcon);
			((TextView)itemView.findViewById(R.id.cb_share_text)).setText(getItem(position).shareText);
			itemView.setOnClickListener(getItem(position).listener);

			return itemView;
		}
	}

	public class ShareItemBean {
		public int shareIcon;
		public String shareText;
		public View.OnClickListener listener;

		public ShareItemBean(int shareIcon, String shareText, View.OnClickListener listener) {
			this.shareIcon = shareIcon;
			this.shareText = shareText;
			this.listener = listener;
		}
	}
}
