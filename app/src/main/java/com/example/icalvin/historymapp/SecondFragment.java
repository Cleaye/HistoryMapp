package com.example.icalvin.historymapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class SecondFragment extends Fragment{

    private ImageButton ib1;
    private ImageButton ib2;
    private ImageButton ib3;
    private ImageButton ib4;
    private ImageButton ib5;
    private ImageButton ib6;
    private ImageButton ib7;
    private ImageButton ib8;
    private ImageButton ib9;


    public SecondFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView =  inflater.inflate(R.layout.second_fragment, container, false);

        ImageButton ib1 = (ImageButton) inflatedView.findViewById(R.id.bPaleolithicum);
        ImageButton ib2 = (ImageButton) inflatedView.findViewById(R.id.bMesolithicum);
        ImageButton ib3 = (ImageButton) inflatedView.findViewById(R.id.bNeolithicum);
        ImageButton ib4 = (ImageButton) inflatedView.findViewById(R.id.bBronstijd);
        ImageButton ib5 = (ImageButton) inflatedView.findViewById(R.id.bIJzertijd);
        ImageButton ib6 = (ImageButton) inflatedView.findViewById(R.id.bRomeinse_tijd);
        ImageButton ib7 = (ImageButton) inflatedView.findViewById(R.id.bMiddeleeuwen_vroeg);
        ImageButton ib8 = (ImageButton) inflatedView.findViewById(R.id.bMiddeleeuwen_laat);
        ImageButton ib9 = (ImageButton) inflatedView.findViewById(R.id.bNieuwe_tijd);

        if(ib1 != null) {
            ib1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createListView(Period_Type.Paleolithicum);
                }
            });
        }
        if (ib2 != null) {
            ib2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createListView(Period_Type.Mesolithicum);
                }
            });
        }
        if (ib3 != null) {
            ib3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createListView(Period_Type.Neolithicum);
                }
            });
        }
        if (ib4 != null) {
            ib4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createListView(Period_Type.Bronstijd);
                }
            });
        }
        if (ib5 != null) {
            ib5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createListView(Period_Type.IJzertijd);
                }
            });
        }
        if (ib6 != null) {
            ib6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createListView(Period_Type.Romeinse_tijd);
                }
            });
        }
        if (ib7 != null) {
            ib7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createListView(Period_Type.Middeleeuwen_vroeg);
                }
            });
        }
        if (ib8 != null) {
            ib8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createListView(Period_Type.Middeleeuwen_laat);
                }
            });
        }
        if (ib9 != null) {
            ib9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createListView(Period_Type.Nieuwe_tijd);
                }
            });
        }

        return inflatedView;
    }

    private void createListView(Period_Type period) {
        Intent intent = new Intent(getContext(), ItemListActivity.class);
        intent.putExtra("Type", "Period");
        intent.putExtra("Title", period.toString());
        startActivityForResult(intent, 1);
    }

}