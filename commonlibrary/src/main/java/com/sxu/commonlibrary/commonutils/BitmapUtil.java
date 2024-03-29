package com.sxu.commonlibrary.commonutils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;

/**
 * Created by Freeman on 17/4/21.
 */

public class BitmapUtil {

	public static Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable != null) {
			if (drawable instanceof BitmapDrawable) {
				return ((BitmapDrawable) drawable).getBitmap();
			}

			try {
				if (!(drawable instanceof ColorDrawable)) {
					Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
					Canvas canvas = new Canvas(bitmap);
					drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
					drawable.draw(canvas);
					return bitmap;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	public static Drawable bitmapToDrawable(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled()) {
			return new BitmapDrawable(Resources.getSystem(), bitmap);
		}

		return null;
	}

	public static Bitmap zoomBitmap(Bitmap sourceBitmap, int maxSize) {
		if (sourceBitmap != null && !sourceBitmap.isRecycled()) {
			int bitmapSize = sourceBitmap.getByteCount();
			if (bitmapSize > maxSize) {
				float scale = maxSize * 1.0f / bitmapSize;
				Matrix matrix = new Matrix();
				matrix.postScale(scale, scale);
				return Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(),
						sourceBitmap.getHeight(), matrix, true);
			} else {
				return sourceBitmap;
			}
		}

		return null;
	}

	public static Bitmap zoomBitmap(Bitmap sourceBitmap, float newWidth, float newHeight) {
		if (sourceBitmap != null && !sourceBitmap.isRecycled()) {
			float width = sourceBitmap.getWidth();
			float height = sourceBitmap.getHeight();
			Matrix matrix = new Matrix();
			// 计算宽高缩放率
			float scaleWidth = newWidth / width;
			float scaleHeight = newHeight / height;
			// 对图片进行缩放
			matrix.postScale(scaleWidth, scaleHeight);
			return Bitmap.createBitmap(sourceBitmap, 0, 0, (int) width, (int) height, matrix, true);
		}

		return null;
	}

	public static Drawable zoomDrawable(Drawable drawable, int newWidth, int newHeight) {
		Bitmap bitmap = drawableToBitmap(drawable);
		return bitmapToDrawable(zoomBitmap(bitmap, newWidth, newHeight));
	}

	public static byte[] bitmapToByteArray(final Bitmap bitmap, final boolean needRecycle) {
		byte[] result = null;
		if (bitmap != null && !bitmap.isRecycled()) {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
			if (needRecycle) {
				bitmap.recycle();
			}

			result = output.toByteArray();
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}
}
