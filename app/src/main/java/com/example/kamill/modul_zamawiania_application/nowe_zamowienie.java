package com.example.kamill.modul_zamawiania_application;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class nowe_zamowienie extends Fragment {

    private Spinner spinner_z_przedmiotami;
    private EditText liczba_przedmiotow;
    private ListView lista_zamowienia;
    private Button dodaj_zamowienie_btn;
    private Button zloz_zamowienie_btn;
    private DatabaseReference baza;
    private ArrayList lista_przedmiotow_do_spinnera;
    private String wybrany_produkt;
    private  ArrayList lista_przedmiotow_do_listy;
    private ArrayList nazwa;
    private ArrayList liczba;
    private String id_zamowienia;
    private DataBase_Reference dataBaseReference;

    public nowe_zamowienie() {
        // Required empty public constructor
        dataBaseReference = new DataBase_Reference();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nowe_zamowienie, container, false);
        spinner_z_przedmiotami = v.findViewById(R.id.spinner_z_produktami);
        liczba_przedmiotow= v.findViewById(R.id.liczba_produktow_EditText);
        lista_zamowienia = v.findViewById(R.id.lista_zamowienie);
        lista_przedmiotow_do_listy = new ArrayList();
        zloz_zamowienie_btn = v.findViewById(R.id.zloz_zamowienie_btn);
        nazwa = new ArrayList();
        liczba = new ArrayList();

        dodaj_zamowienie_btn = v.findViewById(R.id.dodaj_do_zamowienia_btn);
        baza = dataBaseReference.getDatebaseRef();


        //tworzenie spinnera z przedmiotami
        stworz_spinner_z_przedmiotami();

        dodaj_zamowienie_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dodaj_do_zamowienia();
            }
        });

        zloz_zamowienie_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zloz_zamowienie();
            }
        });

        return v;
    }

    //tworzenie spinnera z przedmiotami
    public void stworz_spinner_z_przedmiotami(){
        baza.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista_przedmiotow_do_spinnera = new ArrayList();
                for(DataSnapshot przedmiot: dataSnapshot.child("Magazyn").child("Produkty").getChildren()){
                    lista_przedmiotow_do_spinnera.add(przedmiot.getKey());
                }
                ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, lista_przedmiotow_do_spinnera);
                spinner_z_przedmiotami.setAdapter(adapter);
                spinner_z_przedmiotami.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        wybrany_produkt = (String)lista_przedmiotow_do_spinnera.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //dodawanie produktu do zamówienia
    public void dodaj_do_zamowienia(){
        baza.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(TextUtils.isEmpty(wybrany_produkt)){
                    Toast.makeText(getContext(),"Nie wybrano produktu", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(liczba_przedmiotow.getText().toString())){
                    Toast.makeText(getContext(), "Wpisz liczbę produktów", Toast.LENGTH_SHORT).show();
                }else if(nazwa.contains(wybrany_produkt)){
                    Toast.makeText(getContext(), "Produkt znajduje się w zamówieniu", Toast.LENGTH_SHORT).show();
                }else{
                    lista_przedmiotow_do_listy.add(wybrany_produkt+"     ilość: "+liczba_przedmiotow.getText().toString());
                    nazwa.add(wybrany_produkt);
                    liczba.add(liczba_przedmiotow.getText().toString());
                    ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, lista_przedmiotow_do_listy);
                    lista_zamowienia.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //składanie zamówienia
    public void zloz_zamowienie(){
        if(lista_przedmiotow_do_listy.isEmpty()){
            Toast.makeText(getContext(), "Lista jest pusta", Toast.LENGTH_SHORT).show();
        }else{
            baza.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot id : dataSnapshot.child("Magazyn").child("Zamowienia").getChildren()) {
                        id_zamowienia = id.getKey();

                    }
                    if (TextUtils.isEmpty(id_zamowienia)) {
                        id_zamowienia = "1";
                    } else {
                        id_zamowienia = String.valueOf(Integer.parseInt(id_zamowienia) + 1);
                    }
                    for(int i = 0; i< liczba.size(); i++){
                        baza.child("Magazyn").child("Zamowienia").child(id_zamowienia).child("Produkty").child(nazwa.get(i).toString()).setValue(liczba.get(i).toString());
                        baza.child("Magazyn").child("Zamowienia").child(id_zamowienia).child("Stan").setValue("w trakcie");
                    }
                    liczba_przedmiotow.setText("");
                    ArrayList pusta_lista = new ArrayList();
                    ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1, pusta_lista);
                    lista_zamowienia.setAdapter(adapter);
                    final AlertDialog.Builder alert_dialog = new AlertDialog.Builder(getContext());
                    alert_dialog.setMessage("Zamówienie zostało zatwierdzone")
                            .setCancelable(true)
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = alert_dialog.create();
                    alert.setTitle("Złożono zamówienie");
                    alert.show();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }
    }

    public void setDataBaseReference(DataBase_Reference dataBaseReference) {
        this.dataBaseReference = dataBaseReference;
    }

    public void setBaza(DatabaseReference baza) {
        this.baza = baza;
    }
}
