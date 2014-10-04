package com.example.criminalintent.fragments;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.criminalintent.R;

public class CrimeCameraFragment extends Fragment {

	private static final String TAG = "CrimeCameraFragment";
	public static final String EXTRA_PHOTO_FILENAME = "com.example.criminalintent.photo_filename";

	private Camera camera;
	private SurfaceView surfaceView;
	private View progressContainter;

	// TODO Extract from here
	private Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {

		@Override
		public void onShutter() {
			progressContainter.setVisibility(View.VISIBLE);
		}
	};

	private Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			String filename = UUID.randomUUID().toString() + ".jpg";
			FileOutputStream outputStream = null;
			boolean success = true;
			try {
				outputStream = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
				outputStream.write(data);
			} catch (Exception e) {
				Log.e(TAG, "Error writing to file" + filename, e);
				success = false;
			} finally {
				try {
					if (outputStream != null) {
						outputStream.close();
					}
				} catch (Exception e) {
					Log.e(TAG, "Error closing file" + filename, e);
					success = false;
				}
			}
			if (success) {
				Intent intent = new Intent();
				intent.putExtra(EXTRA_PHOTO_FILENAME, filename);
				getActivity().setResult(Activity.RESULT_OK, intent);
			} else {
				getActivity().setResult(Activity.RESULT_CANCELED);
			}
			getActivity().finish();
		}
	};

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_crime_camera, container, false);

		progressContainter = view.findViewById(R.id.crime_camera_progressContainer);
		progressContainter.setVisibility(View.INVISIBLE);

		Button takePictureButton = (Button) view.findViewById(R.id.crime_camera_takePictureButton);
		// ListenerFactory.getPictureButtonListener(getActivity()));
		// TODO Refactor
		takePictureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (camera != null) {
					camera.takePicture(shutterCallback, null, jpegCallback);

				}
			}
		});

		surfaceView = (SurfaceView) view.findViewById(R.id.crime_camera_surfaceView);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		// TODO encapsulate
		surfaceHolder.addCallback(new SurfaceHolder.Callback() {

			@Override
			public void surfaceCreated(SurfaceHolder surfaceHolder) {
				try {
					if (camera != null) {
						camera.setPreviewDisplay(surfaceHolder);
					}
				} catch (IOException e) {
					Log.e(TAG, "Error setting the preview display", e);
				}
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
				if (camera != null) {
					camera.stopPreview();
				}
			}

			@Override
			public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
				// TODO remove flow control
				if (camera == null) {
					return;
				}

				Camera.Parameters parameters = camera.getParameters();
				Size size = getBestSupportSize(parameters.getSupportedPreviewSizes(), width, height);
				parameters.setPreviewSize(size.width, size.height);
				size = getBestSupportSize(parameters.getSupportedPictureSizes(), width, height);
				parameters.setPictureSize(size.width, size.height);
				camera.setParameters(parameters);

				try {
					camera.startPreview();
				} catch (Exception e) {
					// TODO: handle specific exceptions
					Log.e(TAG, "Coul not start preview", e);
					camera.release();
					camera = null;
				}
			}
		});
		return view;
	}

	@TargetApi(9)
	@Override
	public void onResume() {
		super.onResume();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			camera = Camera.open(0);
		} else {
			camera = Camera.open();
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		if (camera != null) {
			camera.release();
			camera = null;
		}
	}

	private Size getBestSupportSize(List<Size> sizes, int width, int height) {
		Size bestSize = sizes.get(0);
		int largestArea = bestSize.width * bestSize.height;
		for (Size size : sizes) {
			int area = size.width * size.height;
			if (area > largestArea) {
				bestSize = size;
				largestArea = area;
			}
		}
		return bestSize;
	}
}
