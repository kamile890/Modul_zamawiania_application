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

public class zamowienia_hurtownia extends Fragment {

    private ListView list_view;
    private DatabaseReference baza;
    private DataBase_Reference dataBaseReference;

    public zamowienia_hurtownia() {
        // Required empty public constructor
        dataBaseReference = new DataBase_Reference();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_zamowienia_hurtownia, container, false);

        baza = dataBaseReference.getDatebaseRef();
        list_view = v.findViewById(R.id.lista_listView);

        wyswietl_zamowienia();

        return v;
    }

    //zamówienia dla hurtowni
    public void wyswietl_zamowienia(){
        baza.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                final ArrayList lista_zamowien = new ArrayList();
                for(DataSnapshot zamowienie: dataSnapshot.child("Magazyn").child("Zamowienia").getChildren()){
                    if(zamowienie.child("Stan").getValue().equals("w trakcie")) {
                        lista_zamowien.add(zamowienie.getKey());
                    }
                }
                ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, lista_zamowien);
                list_view.setAdapter(adapter);
                list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        final AlertDialog.Builder alert_dialog = new AlertDialog.Builder(getContext());
                        //pobieranie listy produktów określonego zamówienia
                        final ArrayList lista_produktow_zamowienia = new ArrayList();
                        final ArrayList lista_liczba_produktow_zamowienia = new ArrayList();
                        String wypisz_produkty = "";
                        for(DataSnapshot produkt: dataSnapshot.child("Magazyn").child("Zamowienia").child((String)lista_zamowien.get(position)).child("Produkty").getChildren()){
                            lista_produktow_zamowienia.add(produkt.getKey());
                            lista_liczba_produktow_zamowienia.add(produkt.getValue());
                            wypisz_produkty = wypisz_produkty + produkt.getKey()+"  | Ilość: "+produkt.getValue()+"\n";
                        }
                        alert_dialog.setMessage(wypisz_produkty)
                                .setCancelable(true)
                                .setNegativeButton("Anuluj zamówienie", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        final AlertDialog.Builder alert_dialog2 = new AlertDialog.Builder(getContext());
                                        alert_dialog2.setMessage("Czy na pewno chcesz anulować to zamówienie ?")
                                                .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {


                                                    }
                                                })
                                                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //anulowanie zamówienia
                                                        baza.child("Magazyn").child("Zamowienia").child((String)lista_zamowien.get(position)).child("Stan").setValue("Anulowano");
                                                        wyswietl_zamowienia();
                                                    }
                                                });
                                        AlertDialog alert2 = alert_dialog2.create();
                                        alert2.setTitle("");
                                        alert2.show();
                                    }
                                });

                        alert_dialog.setPositiveButton("Realizuj zamówienie", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                baza.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        // pobieranie listy produktów w hurtowni
                                        ArrayList lista_produktow_hurtownia = new ArrayList();
                                        ArrayList liczba_produktow_hurtownia = new ArrayList();
                                        for(DataSnapshot przedmit: dataSnapshot.child("Hurtownia").child("Produkty").getChildren()){
                                            liczba_produktow_hurtownia.add(przedmit.getValue());
                                            lista_produktow_hurtownia.add(przedmit.getKey());

                                        }
                                        Boolean czy_mozna_realizowac = false;







                                        for(int i = 0; i<lista_produktow_zamowienia.size(); i++){
                                            int pozycja = lista_produktow_hurtownia.indexOf(lista_produktow_zamowienia.get(i));

                                            if(Integer.parseInt((String)liczba_produktow_hurtownia.get(pozycja))>=Integer.parseInt((String)lista_liczba_produktow_zamowienia.get(i))){
                                                czy_mozna_realizowac = true;
                                            }else{
                                                czy_mozna_realizowac = false;
                                                break;

                                            }
                                        }
                                        if(czy_mozna_realizowac == false){
                                            final AlertDialog.Builder alert_dialog3 = new AlertDialog.Builder(getContext());
                                            alert_dialog3.setMessage("Nie można zrealizować zamówienia. Brakuje produktów")
                                                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {


                                                        }
                                                    });
                                            AlertDialog alert3 = alert_dialog3.create();
                                            alert3.setTitle("Nie udało się zrealizować zamówienia");
                                            alert3.show();
                                        }else {
                                            // pobranie liczby produktów z magazynu
                                            baza.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    ArrayList lista_liczba_produktow_w_magazynie = new ArrayList();
                                                    ArrayList lista_produktow_w_magazynie = new ArrayList();
                                                    for(DataSnapshot przedmiot: dataSnapshot.child("Magazyn").child("Produkty").getChildren()){
                                                        lista_liczba_produktow_w_magazynie.add(przedmiot.child("ilosc").getValue());
                                                        lista_produktow_w_magazynie.add(przedmiot.getKey());
                                                    }
                                                    for(int i = 0; i<lista_produktow_zamowienia.size(); i++){
                                                        int poozycja = lista_produktow_w_magazynie.indexOf(lista_produktow_zamowienia.get(i));
                                                        int ilosc_poczatkowa = Integer.parseInt((String)lista_liczba_produktow_w_magazynie.get(poozycja));
                                                        int ilosc_zamowiona = Integer.parseInt((String)lista_liczba_produktow_zamowienia.get(i));
                                                        int wartosc = ilosc_zamowiona+ilosc_poczatkowa;
                                                        baza.child("Magazyn").child("Produkty").child((String)lista_produktow_zamowienia.get(i)).child("ilosc").setValue(String.valueOf(wartosc));
                                                    }


                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                            for (int i = 0; i < lista_produktow_zamowienia.size(); i++) {
                                                //aktualizacja stanu hurtowni
                                                int pozycja = lista_produktow_hurtownia.indexOf(lista_produktow_zamowienia.get(i));
                                                String ilosc = (String) liczba_produktow_hurtownia.get(pozycja);
                                                int iloscc = Integer.valueOf(ilosc);
                                                int nowa_ilosc = iloscc - Integer.parseInt((String) lista_liczba_produktow_zamowienia.get(i));
                                                baza.child("Hurtownia").child("Produkty").child((String) lista_produktow_zamowienia.get(i)).setValue(String.valueOf(nowa_ilosc));

                                                // aktualizacja stanu zamówienia
                                                baza.child("Magazyn").child("Zamowienia").child((String)lista_zamowien.get(position)).child("Stan").setValue("Zrealizowano");




                                            }



                                            final AlertDialog.Builder alert_dialog3 = new AlertDialog.Builder(getContext());
                                            alert_dialog3.setMessage("Pomyślnie zrealizowano zamówienie")
                                                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {


                                                        }
                                                    });
                                            AlertDialog alert3 = alert_dialog3.create();
                                            alert3.setTitle("Zrealizowano zamówienie");
                                            alert3.show();
                                            wyswietl_zamowienia();



                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }

                        });


                        AlertDialog alert = alert_dialog.create();
                        alert.setTitle("Zamówienie "+lista_zamowien.get(position));
                        alert.show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public void setDataBaseReference(DataBase_Reference dataBaseReference) {
        this.dataBaseReference = dataBaseReference;
    }

    public void setList_view(ListView list_view) {
        this.list_view = list_view;
    }

    public void setBaza(DatabaseReference baza) {
        this.baza = baza;
    }
}
