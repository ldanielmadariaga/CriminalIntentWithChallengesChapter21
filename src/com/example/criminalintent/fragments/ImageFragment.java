package com.example.criminalintent.fragments;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.criminalintent.PictureUtils;

public class ImageFragment extends DialogFragment {

	public static final String EXTRA_IMAGE_PATH = "com.example.criminalintent.image_path";

	private ImageView imageView;

	public static ImageFragment getInstance(String imagePath) {
		Bundle arguments = new Bundle();
		arguments.putSerializable(EXTRA_IMAGE_PATH, imagePath);

		ImageFragment imageFragment = new ImageFragment();
		imageFragment.setArguments(arguments);
		imageFragment.setStyle(DialogFragment.STYLE_NO_FRAME, 0);

		return imageFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		imageView = new ImageView(getActivity());
		String filePath = (String) getArguments().getSerializable(EXTRA_IMAGE_PATH);
		BitmapDrawable bitmapDrawable = PictureUtils.getScaledDrawable(getActivity(), filePath);
		imageView.setImageDrawable(bitmapDrawable);
		// TODO move
		imageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				onDestroyView();
			}
		});
		return imageView;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		PictureUtils.cleanImageView(imageView);
	}
}
