package com.example.android.miwok;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CategoryAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 4;

    private Context mContext;

    /**
     * 我们需要一个 Context 对象，以便让该字符串资源 ID 变成实际的字符串
     * 因此需要修改构造函数，使其又一个Context输入参数，才能获得恰当的文本字符串
     */
    public CategoryAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new NumbersFragment();
        } else if (position == 1) {
            return new ColorsFragment();
        } else if (position == 2) {
            return new FamilyFragment();
        } else {
            return new PhrasesFragment();
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.category_numbers);
            case 1:
                return mContext.getString(R.string.category_colors);
            case 2:
                return mContext.getString(R.string.category_family);
            case 3:
                return mContext.getString(R.string.category_phrases);
        }
        return super.getPageTitle(position);
    }
}
