package com.mengdd.custommarker;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.mengdd.arapp.FrameHeaderViewModel;
import com.mengdd.arapp.R;
import com.mengdd.arapp.FrameHeaderViewModel.OnBackListener;
import com.mengdd.arapp.activities.CustomMapActivity;
import com.mengdd.components.ViewModel;

public class MainCustomMarkerViewModel extends ViewModel
{
	public enum CustomMarkerScene
	{
		Map, List, RealScene
	}

	private View mRootView = null;

	private FrameHeaderViewModel mHeaderViewModel = null;
	private Resources resources = null;

	private List<ViewModel> mViewModels = null;

	private ListCustomMarkerViewModel mListViewModel = null;
	private RealSceneCMViewModel mRealSceneViewModel = null;

	private View mListView = null;
	private View mRealSceneView = null;

	public MainCustomMarkerViewModel(Activity activity)
	{
		super(activity);
	}

	@Override
	public void onCreate(Intent intent)
	{
		super.onCreate(intent);

		mRootView = mInflater.inflate(R.layout.custom_main, null);

		initHeader();

		initBottom();

		// list
		mListViewModel = new ListCustomMarkerViewModel(mActivity);

		// real scene
		mRealSceneViewModel = new RealSceneCMViewModel(mActivity);

		mViewModels = new ArrayList<ViewModel>();

		mViewModels.add(mListViewModel);
		mViewModels.add(mRealSceneViewModel);

		for (ViewModel viewModel : mViewModels)
		{
			viewModel.onCreate(null);
		}

		mListView = mListViewModel.getView();
		mRealSceneView = mRealSceneViewModel.getView();

		RelativeLayout mainLayout = (RelativeLayout) mRootView
				.findViewById(R.id.main_content);

		mainLayout.addView(mListView, 0);
		mainLayout.addView(mRealSceneView, 1);

		switchScene(CustomMarkerScene.Map);

	}

	private void initHeader()
	{
		// Header
		resources = mActivity.getResources();
		mHeaderViewModel = new FrameHeaderViewModel(mActivity);
		mHeaderViewModel.onCreate(null);
		mHeaderViewModel.setSettingVisibility(View.GONE);
		mHeaderViewModel.setTitle(resources
				.getString(R.string.custom_marker_title));
		ViewGroup headerGourp = (ViewGroup) mRootView.findViewById(R.id.title);
		headerGourp.addView(mHeaderViewModel.getView(), 0);
		mHeaderViewModel.setOnBackListener(new OnBackListener()
		{

			@Override
			public void onBack()
			{
				// mActivity.onKeyDown(KeyEvent.KEYCODE_BACK, new KeyEvent(
				// KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));

				mActivity.finish();

			}
		});

	}

	private void initBottom()
	{

		// Bottom Menu
		CustomBottomViewModel bottomViewModel = new CustomBottomViewModel(
				mActivity);
		bottomViewModel.onCreate(null);
		// bottom menu
		FrameLayout bottomFrameLayout = (FrameLayout) mRootView
				.findViewById(R.id.bottom_menu);
		bottomFrameLayout.addView(bottomViewModel.getView());
		bottomViewModel.setOnClickListener(mBottomOnClickListener);
	}

	private OnClickListener mBottomOnClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.custom_map:

				switchScene(CustomMarkerScene.Map);

				break;
			case R.id.custom_list:

				switchScene(CustomMarkerScene.List);

				break;
			case R.id.custom_realscene:

				switchScene(CustomMarkerScene.RealScene);

				break;

			default:
				break;
			}

		}

	};

	private void switchScene(CustomMarkerScene scene)
	{
		switch (scene)
		{
		case Map:

			Intent intent = new Intent();
			intent.setClass(mActivity, CustomMapActivity.class);
			mActivity.startActivity(intent);

			break;
		case List:
			mListView.setVisibility(View.VISIBLE);
			mRealSceneView.setVisibility(View.GONE);

			break;
		case RealScene:
			mListView.setVisibility(View.GONE);
			mRealSceneView.setVisibility(View.VISIBLE);

			break;

		default:
			break;
		}

	}

	@Override
	public View getView()
	{
		return mRootView;
	}

	@Override
	public void onStop()
	{
		super.onStop();

		for (ViewModel viewModel : mViewModels)
		{
			viewModel.onStop();
		}
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

		for (ViewModel viewModel : mViewModels)
		{
			viewModel.onDestroy();
		}
	}

	@Override
	public void onResume(Intent intent)
	{
		super.onResume(intent);

		for (ViewModel viewModel : mViewModels)
		{
			viewModel.onResume(null);
		}

	}

	@Override
	public void onPause()
	{
		super.onPause();

		for (ViewModel viewModel : mViewModels)
		{
			viewModel.onPause();
		}

	}
}