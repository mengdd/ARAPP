package com.mengdd.search.keywords;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CategoryData {

    private String mCategoryName;
    private Map<String, Integer> mItemMap;

    public CategoryData(String name) {
        mCategoryName = name;
        mItemMap = new HashMap<String, Integer>();
    }

    public void setName(String name) {
        mCategoryName = name;
    }

    public String getName() {
        return mCategoryName;
    }

    public void addItem(String item) {

        if (mItemMap.containsKey(item)) {
            Integer count = mItemMap.get(item);
            mItemMap.put(item, count + 1);
        }
        else {
            mItemMap.put(item, 1);
        }
    }

    public void addItems(String[] items) {
        if (null != items) {
            for (String item : items) {
                addItem(item);
            }
        }
    }

    public void putItem(String item, int count) {
        mItemMap.put(item, count);
    }

    public void removeItem(String item) {
        if (mItemMap.containsKey(item)) {
            mItemMap.remove(item);
        }
    }

    public ArrayList<String> getItems() {
        ArrayList<String> sortedItems = null;

        if (null != mItemMap) {
            sortedItems = new ArrayList<String>();

            List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(
                    mItemMap.entrySet());
            // 根据count值降序返回item
            Collections.sort(list,
                    new Comparator<Map.Entry<String, Integer>>() {

                        @Override
                        public int compare(Entry<String, Integer> lhs,
                                Entry<String, Integer> rhs) {
                            return rhs.getValue() - lhs.getValue();
                        }
                    });

            for (int i = 0; i < list.size(); ++i) {

                sortedItems.add(list.get(i).getKey());
            }
        }

        return sortedItems;

    }

    public int getItemCount(String item) {
        int count = 0;
        if (mItemMap.containsKey(item)) {
            count = mItemMap.get(item);
        }

        return count;
    }

    @Override
    public String toString() {
        return "CategoryData [mCategoryName=" + mCategoryName + ", mItemMap="
                + mItemMap + "]";
    }

}
