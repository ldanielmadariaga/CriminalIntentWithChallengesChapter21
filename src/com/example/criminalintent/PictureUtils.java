package com.example.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.widget.ImageView;

public class PictureUtils {

	public static BitmapDrawable getScaledDrawable(Activity activity, String filePath) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		@SuppressWarnings("deprecation")
		float destinationWidth = display.getWidth();
		@SuppressWarnings("deprecation")
		float destinationHeight = display.getHeight();

		Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		float sourceWidth = options.outWidth;
		float sourceHeight = options.outHeight;
		int inSampleSize = 1;
		if (sourceHeight > destinationHeight || sourceWidth > destinationWidth) {
			if (sourceWidth > sourceHeight) {
				inSampleSize = Math.round(sourceHeight / destinationHeight);
			} else {
				inSampleSize = Math.round(sourceWidth / destinationWidth);
			}
		}
		options = new BitmapFactory.Options();
		options.inSampleSize = inSampleSize;

		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
		return new BitmapDrawable(activity.getResources(), bitmap);
	}

	public static void cleanImageView(ImageView imageView) {
		// TODO ufff espanto
		if (!(imageView.getDrawable() instanceof BitmapDrawable)) {
			return;
		}
		BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
		bitmapDrawable.getBitmap().recycle();
		imageView.setImageDrawable(null);
	}
}
