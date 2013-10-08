package com.mengdd.search;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.mengdd.arapp.R;
import com.mengdd.components.ViewModel;
import com.mengdd.utils.AppConstants;

public class SearchBottomViewModel extends ViewModel
{
	private View mRootView;

	private Button btn1;
	private Button btn2;
	private Button btn3;
	private Button btn4;

	public SearchBottomViewModel(Activity activity)
	{
		super(activity);
	}

	@Override
	public void onCreate(Intent intent)
	{
		super.onCreate(intent);

		mRootView = View.inflate(mActivity, R.layout.bottom_menu_search, null);

		btn1 = (Button) mRootView.findViewById(R.id.search_menu_ui);
		btn2 = (Button) mRootView.findViewById(R.id.search_menu_list);
		btn3 = (Button) mRootView.findViewById(R.id.search_menu_map);
		btn4 = (Button) mRootView.findViewById(R.id.search_menu_real);

		setOnClickListener(mTestListener);

	}

	public Button getButton(int index)
	{
		Button resultButton = null;
		switch (index)
		{
		case 1:

			resultButton = btn1;
			break;

		case 2:
			resultButton = btn2;
			break;

		case 3:
			resultButton = btn3;
			break;

		case 4:
			resultButton = btn4;
			break;

		default:
			break;
		}

		return resultButton;
	}

	public void setOnClickListener(OnClickListener listener)
	{
		if (null == listener)
		{
			throw new IllegalArgumentException("listener is null!");
		}

		btn1.setOnClickListener(listener);
		btn2.setOnClickListener(listener);
		btn3.setOnClickListener(listener);
		btn4.setOnClickListener(listener);
	}

	private OnClickListener mTestListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			Log.i(AppConstants.LOG_TAG,
					"bottom menu: onClickListener: " + v.getId());
		}
	};

	@Override
	public View getView()
	{
		return mRootView;
	}

}
