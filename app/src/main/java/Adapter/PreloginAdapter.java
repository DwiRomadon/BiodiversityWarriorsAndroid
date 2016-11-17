package Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import pptik.project.biodiversitywarriors.pre1;
import pptik.project.biodiversitywarriors.pre2;
import pptik.project.biodiversitywarriors.pre3;


public class PreloginAdapter extends FragmentStatePagerAdapter {

    public PreloginAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:

                return new pre1();

            case 1:

                return new pre2();
            case 2:

                return new pre3();

        }
        return null;

    }

    @Override
    public int getCount() {
        return 3;
    }

}
