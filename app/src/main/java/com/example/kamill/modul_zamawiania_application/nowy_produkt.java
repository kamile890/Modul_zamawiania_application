package com.example.kamill.modul_zamawiania_application;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class nowy_produkt extends Fragment {

    private DatabaseReference baza;
    // Required empty public constructor
    private DataBase_Reference dataBaseReference;
    private Button btn;
    private EditText nazwa;

    public nowy_produkt() {
        dataBaseReference = new DataBase_Reference();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nowy_produkt, container, false);

        baza = dataBaseReference.getDatebaseRef();
        btn = v.findViewById(R.id.btn);
        nazwa = v.findViewById(R.id.editText);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dodaj();
            }
        });

        return v;
    }

    //dodaj produkt
    public void dodaj(){
        final String nazwa_produktu = nazwa.getText().toString();
        baza.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList lista = new ArrayList();
                for(DataSnapshot przedmit: dataSnapshot.child("Magazyn").child("Produkty").getChildren()){
                    lista.add(przedmit.getKey().toLowerCase());
                }
                if(lista.contains(nazwa_produktu.toLowerCase())){
                    Toast.makeText(getContext(),"Produkt już znajduje się w bazie", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(nazwa_produktu)){
                    Toast.makeText(getContext(),"Wpisz nazwę produktu", Toast.LENGTH_SHORT).show();
                }else {
                    baza.child("Magazyn").child("Produkty").child(nazwa_produktu).child("ilosc").setValue("0");
                    baza.child("Magazyn").child("Produkty").child(nazwa_produktu).child("stan").setValue("aktywny");
                    Toast.makeText(getContext(), "Dodano produkt do bazy", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void setDataBaseReference(DataBase_Reference dataBaseReference) {
        this.dataBaseReference = dataBaseReference;
    }

    public void setBaza(DatabaseReference baza) {
        this.baza = baza;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    public void setNazwa(EditText nazwa) {
        this.nazwa = nazwa;
    }
}
