package com.example.icalvin.historymapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class ItemDetailActivity extends AppCompatActivity {

    private String title;

    /**
     * Creates the View and setups it's contents.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        title = getIntent().getStringExtra("Title");

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            String argument = getIntent().getStringExtra(ItemDetailFragment.ARG_ITEM_ID);
            if (argument == null) {
                argument = getIntent().getStringExtra(ItemDetailFragment.ARG_ITEM);
                createFragment(argument, ItemDetailFragment.ARG_ITEM);
            } else
                createFragment(argument, ItemDetailFragment.ARG_ITEM_ID);
        }
    }

    /**
     * Creates a Fragment where the information about the item is shown in.
     * @param argument The whole item or it's position in FindingContent.ITEM_MAP.
     * @param argumentType Indicates if it's the whole item or it's position.
     */
    private void createFragment(String argument, String argumentType) {
        Bundle arguments = new Bundle();
        arguments.putString(argumentType, argument);
        ItemDetailFragment fragment = new ItemDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.item_detail_container, fragment)
                .commit();
    }

    /**
     * Handles input in the menubar.
     * @param item Item in the menubar that has been pressed.
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(this, ItemListActivity.class);
            intent.putExtra("Title", title);
            navigateUpTo(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
