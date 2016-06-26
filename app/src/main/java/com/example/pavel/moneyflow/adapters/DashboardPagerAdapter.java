package com.example.pavel.moneyflow.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pavel.moneyflow.R;
import com.example.pavel.moneyflow.fragments.ExpensesFragment;
import com.example.pavel.moneyflow.fragments.IncomesFragment;

public class DashboardPagerAdapter extends FragmentPagerAdapter {

    public static final int FRAGMENT_CASH_FLOW = 0;
    public static final int FRAGMENT_EXPENSES = 1;
    public static final int FRAGMENT_INCOMES = 2;

    private Context context;

    public DashboardPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case FRAGMENT_CASH_FLOW:
                return context.getResources().getString(R.string.title_tab_cash_flow);
            case FRAGMENT_EXPENSES:
                return context.getResources().getString(R.string.title_tab_expense);
            case FRAGMENT_INCOMES:
                return context.getResources().getString(R.string.title_tab_incomes);
            default:
                throw new IllegalArgumentException("Not supported argument - \'" + position + "\'");
        }
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                DefaultFragment defaultFragmentFirst = new DefaultFragment();
                Bundle argBundleFirst = new Bundle();
                argBundleFirst.putString(DefaultFragment.KEY_NAME, context.getResources().getString(R.string.title_tab_cash_flow));
                defaultFragmentFirst.setArguments(argBundleFirst);
                return defaultFragmentFirst;
            case 1:
                return new ExpensesFragment();
            case 2:
                return new IncomesFragment();
            default:
                throw new IllegalArgumentException("Not supported argument - \'" + position + "\'");
        }
    }



    @Override
    public int getCount() {
        return 3;
    }

    public static class DefaultFragment extends Fragment {

        private static final String KEY_NAME = "name";

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(android.R.layout.simple_list_item_1, container, false);
            String name = getArguments().getString(KEY_NAME);
            ((TextView)view.findViewById(android.R.id.text1)).setText(name);
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }
}
