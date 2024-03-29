package com.sxu.testmodule;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.qiniu.android.http.ResponseInfo;
import com.sxu.smartpicture.choosePicture.ChoosePhotoDialog;
import com.sxu.smartpicture.choosePicture.ChoosePhotoManager;
import com.sxu.smartpicture.choosePicture.OnChoosePhotoListener;
import com.sxu.smartpicture.utils.UploadUtils;

import org.json.JSONObject;

/**
 * Copyright (c) 2017. zhinanmao Inc. All rights reserved.
 * <p>
 * <p>
 * 类或接口的描述信息
 *
 * @author Freeman
 * @date 2017/12/21
 */


public class ChooseAndCropImageActivity extends AppCompatActivity {

	private String filePath;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_and_crop_image_layout);
		Button chooseButton = (Button)findViewById(R.id.choose_button);
		Button uploadToServerButton = (Button)findViewById(R.id.upload_to_server_button);
		Button uploadToQNButton = (Button)findViewById(R.id.upload_to_qn_button);
		final ImageView imageView = (ImageView) findViewById(R.id.image);

		chooseButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ChoosePhotoDialog dialog = new ChoosePhotoDialog(ChooseAndCropImageActivity.this,
						new String[] {"拍照", "从相册选择"});
				dialog.show();
				dialog.setOnItemListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
						ChoosePhotoManager.getInstance().setAutoCrop(true);
						if (i == 0) {
							ChoosePhotoManager.getInstance().choosePhotoFromCamera(ChooseAndCropImageActivity.this);
						} else {
							ChoosePhotoManager.getInstance().choosePhotoFromAlbum(ChooseAndCropImageActivity.this);
						}
						ChoosePhotoManager.getInstance().setChoosePhotoListener(new OnChoosePhotoListener() {
							@Override
							public void choosePhotoFromAlbum(Uri uri, String errMsg) {
								filePath = uri.getPath();
								imageView.setImageURI(uri);
							}

							@Override
							public void choosePhotoFromCamera(Uri uri, String errMsg) {
								filePath = uri.getPath();
								imageView.setImageURI(uri);
							}

							@Override
							public void cropPhoto(Uri uri, String errMsg) {
								filePath = uri.getPath();
								imageView.setImageURI(uri);
							}
						});
					}
				});
			}
		});

		uploadToServerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(filePath)) {
					UploadUtils.uploadToInnerServer(ChooseAndCropImageActivity.this, "http://m.baidu.com",
							filePath, new UploadUtils.OnUploadListener() {
						@Override
						public void onSuccess(String result) {
							Toast.makeText(ChooseAndCropImageActivity.this, "图片上传成功", Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onError(Exception e) {
							Toast.makeText(ChooseAndCropImageActivity.this, "图片上传失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
						}
					});
				} else {
					Toast.makeText(ChooseAndCropImageActivity.this, "请先选择图片", Toast.LENGTH_SHORT).show();
				}
			}
		});

		uploadToQNButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(filePath)) {
					UploadUtils.uploadToQiNiu(ChooseAndCropImageActivity.this, "", filePath,
							new UploadUtils.OnUploadToQiNiuListener() {
						@Override
						public void onCompleted(String key, ResponseInfo info, JSONObject res) {
							Toast.makeText(ChooseAndCropImageActivity.this, info.error, Toast.LENGTH_SHORT).show();
						}
					});
				} else {
					Toast.makeText(ChooseAndCropImageActivity.this, "请先选择图片", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ChoosePhotoManager.getInstance().onActivityResult(this, requestCode, data);
	}
}
