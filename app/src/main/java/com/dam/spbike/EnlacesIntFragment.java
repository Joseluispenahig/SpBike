package com.dam.spbike;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class EnlacesIntFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_enlaceinteres, container, false);

        ImageButton sevici = (ImageButton) view.findViewById(R.id.imageButton3);
        ImageButton bicimad = (ImageButton) view.findViewById(R.id.imageButton4);
        ImageButton bicing = (ImageButton) view.findViewById(R.id.imageButton5);
        ImageButton elpais = (ImageButton) view.findViewById(R.id.imageButton6);
        ImageButton larazon = (ImageButton) view.findViewById(R.id.imageButton7);
        ImageButton elmundo = (ImageButton) view.findViewById(R.id.imageButton8);

        sevici.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.sevici.es/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        bicimad.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.bicimad.com/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        bicing.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.bicing.cat/es/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        elpais.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Uri uri = Uri.parse("https://elpais.com/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        elmundo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.elmundo.es/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        larazon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.larazon.es/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    return view;
    }
}
