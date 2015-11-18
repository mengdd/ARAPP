package com.mengdd.search.keywords;

import java.util.List;

import com.mengdd.arapp.R;
import com.mengdd.utils.AppConstants;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class CategoryView extends LinearLayout {

    private Context mContext = null;
    private LayoutInflater mInflater = null;
    private TextView mTitle;
    private TableLayout mContentTable;
    private TableRow mCurrentRow = null;
    private static final int MAX_COLUMN_ITEMS = 4;

    public CategoryView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mInflater.inflate(R.layout.category_view, this);
        this.setOrientation(LinearLayout.VERTICAL);
        this.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        mTitle = (TextView) findViewById(R.id.name);
        mTitle.setClickable(true);
        mTitle.setOnClickListener(mOnClickListener);
        mContentTable = (TableLayout) findViewById(R.id.item_table);

    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    /**
     * Convenience method to expand or hide the dialogue
     */
    public void setExpanded(boolean expanded) {
        mContentTable.setVisibility(expanded ? VISIBLE : GONE);
    }

    public void addItemView(String item) {
        if (null == mCurrentRow
                || mCurrentRow.getChildCount() >= MAX_COLUMN_ITEMS) {

            mCurrentRow = new TableRow(mContext);
            mContentTable.addView(mCurrentRow, new TableLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        }

        TextView textView = (TextView) mInflater.inflate(
                R.layout.category_keyword_item, null);
        textView.setText(item);

        textView.setClickable(true);
        textView.setOnClickListener(mOnClickListener);

        mCurrentRow.addView(textView);

        requestLayout();

    }

    public void addItemViews(List<String> items) {
        if (null != items) {
            for (String item : items) {
                Log.i(AppConstants.LOG_TAG, "item: " + item);
                addItemView(item);
            }
        }
    }

    public interface KeywordListener {
        public void onKeywordSelected(String keyword);
    }

    private KeywordListener mKeywordListener = null;

    public void setKeywordListener(KeywordListener listener) {
        mKeywordListener = listener;
    }

    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (null != mKeywordListener) {
                if (v instanceof TextView) {
                    String text = ((TextView) v).getText().toString();
                    mKeywordListener.onKeywordSelected(text);
                    Log.i(AppConstants.LOG_TAG, "onClick: " + text);
                }
            }
        }
    };

}
