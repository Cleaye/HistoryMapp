package com.example.icalvin.historymapp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity  {

    private static ViewPager mPager;
    private TabLayout tabLayout;
    private MaterialSearchView searchView;
    private Toolbar toolbar;

    /**
     * Creates the main View of the app.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("HistoryMapp");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(mPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mPager);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);
        searchView.setCursorDrawable(R.drawable.color_cursor_white);
        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {

            /**
             * Searches for objects based on user input.
             * @param query User input.
             * @return
             */
            @Override
            public boolean onQueryTextSubmit(String query) {
                //TODO: Search for objects in database
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                searchView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSearchViewClosed() {}
        });
    }

    /**
     * Sets up the ViewPager for sliding through tabs.
     * @param viewPager ViewPager to setup.
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FirstFragment(), "Me");
        adapter.addFragment(new SecondFragment(), "Home");
        adapter.addFragment(new ThirdFragment(getApplicationContext()), "Map");
        viewPager.setAdapter(adapter);
    }

    /**
     * Updates the Fragment incase it's content has changed.
     */
    public static void updateFragment() {
        mPager.getAdapter().notifyDataSetChanged();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        /**
         * Constructor to create a new ViewPagerAdapter.
         * @param manager
         */
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        /**
         * Gets an item at a given position.
         * @param position Position of the wanted item.
         * @return Returns an item at position.
         */
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        /**
         * Gets the number of items in the adapter.
         * @return Return the number of items in the adapter.
         */
        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        /**
         * Gets the title of a page at a given position.
         * @param position Position of the page in the adapter.
         * @return Return the name of a page at position.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        /**
         * Gets the position of a Fragment
         * @param object Fragment to get position from.
         * @return Returns the position of the given Fragment.
         */
        @Override
        public int getItemPosition(Object object) {
            IFragment fragment = (IFragment ) object;
            if (fragment != null) {
                fragment.update();
            }
            return super.getItemPosition(object);
        }
    }

    /**
     * Creates the TabToolbar.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    /**
     * Handles clicks on the search button.
     */
    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

}


