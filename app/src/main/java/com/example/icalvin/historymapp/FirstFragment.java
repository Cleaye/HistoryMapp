package com.example.icalvin.historymapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;


public class FirstFragment extends Fragment implements IFragment{

    private ListView lvFindings;
    private List<FindingContent.FindingItem> findingsList;
    private FavouriteAdapter findingsAdapter;
    private FavouriteEditor favouriteEditor;

    /**
     * Required empty public constructor
     */
    public FirstFragment() {}

    /**
     * Called when the Fragment is being created.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favouriteEditor = new FavouriteEditor(getContext());
    }

    /**
     * Called when the View inside the Fragment is being created.
     * @param inflater Used to set the view in a ViewGroup.
     * @param container Contains the View that is wanted inside the Fragment.
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.first_fragment, container, false);
        lvFindings = (ListView) inflatedView.findViewById(R.id.listview_findings);
        lvFindings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FindingContent.FindingItem item = (FindingContent.FindingItem) adapterView.getItemAtPosition(i);

                Context context = getContext();
                Intent intent = new Intent(context, ItemDetailActivity.class);
                Gson gson = new Gson();
                intent.putExtra(ItemDetailFragment.ARG_ITEM, gson.toJson(item));

                context.startActivity(intent);
            }
        });

        setLists();

        return inflatedView;
    }

    /**
     * Get the favourite list(s) and bind them to a ListView.
     */
    private void setLists() {
        if (lvFindings != null) {
            findingsList = favouriteEditor.getFavourites("Findings");

            if (findingsList != null) {
                findingsAdapter = new FavouriteAdapter(getContext(), R.layout.item_list_content, findingsList);
                lvFindings.setAdapter(findingsAdapter);
            }

        }
    }

    /**
     * Updates the Fragment, so changes in the favourites will be shown.
     * @return Returns if the Fragment is updated or not.
     */
    @Override
    public boolean update() {
        setLists();
        if (findingsAdapter != null)
            findingsAdapter.notifyDataSetChanged();
        return true;
    }

    class FavouriteAdapter extends ArrayAdapter {

        Context context;
        List data;
        private LayoutInflater inflater = null;

        /**
         * Constructor to make a new FavouriteAdapter.
         * @param context Application context.
         * @param resource Layout of a row in the ListView.
         * @param objects List of objects that have to be shown in the ListView.
         */
        public FavouriteAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
            super(context, resource, objects);
            this.context = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            data = objects;
        }

        /**
         * Get the number of items inside the adapter.
         * @return Returns the number of items inside the adapter.
         */
        @Override
        public int getCount() {
            return data.size();
        }

        /**
         * Gets an item at certain position.
         * @param position Position to get the item from.
         * @return Returns an item at a certain position.
         */
        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        /**
         * Gets the View of a row and prepares it's content.
         * @param position Position in the adapter.
         * @param convertView
         * @param parent
         * @return Returns the prepared View of a row.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null)
                view = inflater.inflate(R.layout.item_list_content, null);
            ImageView image = (ImageView) view.findViewById(R.id.List_icon);
            ((FindingContent.FindingItem) data.get(position)).getImage(context, image);
            TextView text = (TextView) view.findViewById(R.id.id);
            text.setText(((FindingContent.FindingItem) data.get(position)).name);
            return view;
        }
    }
}
