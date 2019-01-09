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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class stan_magazynu extends Fragment {

    private ListView lista_przedmiotow_magazyn;
    private DataBase_Reference dataBaseReference;
    private DatabaseReference baza;
    private ArrayAdapter arrayAdapter;

    ArrayAdapter adapter;

    public stan_magazynu() {
        // Required empty public constructor
        dataBaseReference = new DataBase_Reference();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stan_magazynu, container, false);
        lista_przedmiotow_magazyn = v.findViewById(R.id.lista_przedmiotow);
        baza = dataBaseReference.getDatebaseRef();
        final ArrayList lista_test_wys = new ArrayList();
        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, lista_test_wys);

        baza.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista_test_wys.clear();
                for (DataSnapshot przedmiot : dataSnapshot.child("Magazyn").child("Produkty").getChildren()) {
                    if (przedmiot.child("stan").getValue().equals("Usunięto")) {
                        lista_test_wys.add(przedmiot.getKey() + "  | Ilość: " + przedmiot.child("ilosc").getValue() + "     Usunięto");

                    } else {
                        lista_test_wys.add(przedmiot.getKey() + "  | Ilość: " + przedmiot.child("ilosc").getValue());

                    }


                }

                lista_przedmiotow_magazyn.setAdapter(arrayAdapter);
                lista_przedmiotow_magazyn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        final AlertDialog.Builder alert_dialog = new AlertDialog.Builder(getContext());
                        alert_dialog.setMessage("Czy na pewno chcesz usunąć '"+lista_test_wys.get(position)+"' ? Spowoduje to anulowanie wszystkich zamówień, które zawierają ten przedmiot")
                                .setCancelable(false)
                                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        baza.child("Magazyn").child("Produkty").child((String)lista_test_wys.get(position)).child("stan").setValue("Usunięto");
                                        // aktualizacja listy
                                        baza.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                ArrayList lista_przedmiotow = new ArrayList();
                                                final ArrayList lista = new ArrayList();
                                                for (DataSnapshot przedmiot : dataSnapshot.child("Magazyn").child("Produkty").getChildren()) {
                                                    if (przedmiot.child("stan").getValue().equals("Usunięto")) {
                                                        lista_przedmiotow.add(przedmiot.getKey() + "  | Ilość: " + przedmiot.child("ilosc").getValue() + "     Usunięto");
                                                        lista.add(przedmiot.getKey());
                                                    } else {
                                                        lista_przedmiotow.add(przedmiot.getKey() + "  | Ilość: " + przedmiot.child("ilosc").getValue());
                                                        lista.add(przedmiot.getKey());
                                                    }


                                                }
                                                ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, lista_przedmiotow);
                                                lista_przedmiotow_magazyn.setAdapter(adapter);

                                                for(DataSnapshot zamowienie: dataSnapshot.child("Magazyn").child("Zamowienia").getChildren()){
                                                    for(DataSnapshot produkt: zamowienie.child("Produkty").getChildren()){
                                                        if(produkt.getKey().equals(lista.get(position))){
                                                            baza.child("Magazyn").child("Zamowienia").child(zamowienie.getKey()).child("Stan").setValue("Anulowano");
                                                        }
                                                    }
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                })
                                .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = alert_dialog.create();
                        alert.setTitle("Usuń");
                        alert.show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return v;
    }

    public void setDataBaseReference(DataBase_Reference dataBaseReference) {
        this.dataBaseReference = dataBaseReference;
    }

    public void setLista_przedmiotow_magazyn(ListView lista_przedmiotow_magazyn) {
        this.lista_przedmiotow_magazyn = lista_przedmiotow_magazyn;
    }

    public void setBaza(DatabaseReference baza) {
        this.baza = baza;
    }



}
