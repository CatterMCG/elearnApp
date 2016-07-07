package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import fragment.SingleChooseFragment;

/**
 * Created by 成刚 on 2016/4/2.
 */
public class SingleChooseFragmentPagerAdapter extends FragmentPagerAdapter {
    ArrayList<SingleChooseFragment> list;

    public SingleChooseFragmentPagerAdapter(FragmentManager fm, ArrayList<SingleChooseFragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment getItem(int position) {
       /* boolean isLastPic = false;
        if (position == list.size() - 1)
            isLastPic = true;*/
        return SingleChooseFragment.newInstance(list.get(position),list.size(),position);
    }

}
