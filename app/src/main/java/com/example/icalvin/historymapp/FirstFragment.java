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

    public FirstFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favouriteEditor = new FavouriteEditor(getContext());
    }

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

        // Inflate the layout for this fragment
        return inflatedView;
    }

    private void setLists() {
        if (lvFindings != null) {
            findingsList = favouriteEditor.getFavourites("Findings");

            if (findingsList != null) {
                findingsAdapter = new FavouriteAdapter(getContext(), R.layout.item_list_content, findingsList);
                lvFindings.setAdapter(findingsAdapter);
            }

        }
    }

    //@Override
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

        public FavouriteAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
            super(context, resource, objects);
            this.context = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            data = objects;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if (vi == null)
                vi = inflater.inflate(R.layout.item_list_content, null);
            ImageView image = (ImageView) vi.findViewById(R.id.List_icon);
            ((FindingContent.FindingItem) data.get(position)).getImage(context, image);
            TextView text = (TextView) vi.findViewById(R.id.id);
            text.setText(((FindingContent.FindingItem) data.get(position)).name);
            return vi;
        }
    }
}
