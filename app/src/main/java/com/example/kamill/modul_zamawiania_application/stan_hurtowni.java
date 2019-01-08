package com.example.kamill.modul_zamawiania_application;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class stan_hurtowni extends Fragment {

    private ListView lista_przedmiotow;
    private DatabaseReference baza;

    public stan_hurtowni() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stan_hurtowni, container, false);

        lista_przedmiotow = v.findViewById(R.id.lista_przedmiotow);
        baza = FirebaseDatabase.getInstance().getReference();
        pobierz_dane_z_hurtowni_i_pokaz_w_liście();


        return v;
    }

    public void pobierz_dane_z_hurtowni_i_pokaz_w_liście(){
        baza.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList lista_h = new ArrayList();
                for(DataSnapshot przedmiott: dataSnapshot.child("Hurtownia").child("Produkty").getChildren()){
                    lista_h.add(przedmiott.getKey()+" ilość: "+przedmiott.getValue());
                }
                wyświetl_dane_w_ListView(lista_h, lista_przedmiotow);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void wyświetl_dane_w_ListView(ArrayList lista, ListView listView ){
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, lista);
        listView.setAdapter(adapter);
    }


}
