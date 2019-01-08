package com.example.kamill.modul_zamawiania_application;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class zamowienia_magazynu extends Fragment {


    private DatabaseReference baza;
    private ListView list_view;
    private ArrayList lista_id;
    private ArrayList lista_produktow;

    public zamowienia_magazynu() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_zamowienia_magazynu, container, false);

        baza = FirebaseDatabase.getInstance().getReference();
        list_view = v.findViewById(R.id.list_view);

        wyswietl_zmamowienia();


        return v;
    }

    //wyświetlanie aktywnych zamówień
    public void wyswietl_zmamowienia(){
        baza.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                lista_id = new ArrayList();
                final ArrayList lista_stan = new ArrayList();
                final ArrayList lista_id_wyswietl = new ArrayList();
                for(DataSnapshot id: dataSnapshot.child("Magazyn").child("Zamowienia").getChildren()){
                    lista_id.add(id.getKey());
                    lista_id_wyswietl.add("2019/"+id.getKey()+" ("+id.child("Stan").getValue()+")");
                    lista_stan.add(id.child("Stan").getValue());
                }

                ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, lista_id_wyswietl);
                list_view.setAdapter(adapter);
                list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        final AlertDialog.Builder alert_dialog = new AlertDialog.Builder(getContext());
                        String przedmioty ="";
                        for(DataSnapshot przedmiot: dataSnapshot.child("Magazyn").child("Zamowienia").child((String)lista_id.get(position)).child("Produkty").getChildren()){
                            przedmioty = przedmioty+"\n"+przedmiot.getKey()+"  | ilość: "+przedmiot.getValue();
                        }
                        alert_dialog.setMessage(przedmioty)
                                .setCancelable(true)
                                .setNegativeButton("Zamknij", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        if(lista_stan.get(position).equals("w trakcie")){
                            alert_dialog.setPositiveButton("Anuluj zamówienie", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    baza.child("Magazyn").child("Zamowienia").child((String)lista_id.get(position)).child("Stan").setValue("Anulowano");
                                    wyswietl_zmamowienia();
                                }
                            });
                        }

                        AlertDialog alert = alert_dialog.create();
                        alert.setTitle("Zamówienie '"+lista_id_wyswietl.get(position)+"'");
                        alert.show();



                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
