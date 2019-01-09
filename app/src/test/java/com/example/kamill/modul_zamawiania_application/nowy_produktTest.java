package com.example.kamill.modul_zamawiania_application;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class nowy_produktTest {

private nowy_produkt service;
private DataBase_Reference mockDataBaseReference;


    @Before
    public void setUp() throws Exception {
        service = new nowy_produkt();
        mockDataBaseReference = mock(DataBase_Reference.class);
        service.setDataBaseReference(mockDataBaseReference);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void onCreateView() {

        DatabaseReference baza = mock(DatabaseReference.class);
        when(mockDataBaseReference.getDatebaseRef()).thenReturn(baza);

        View v1 = mock(View.class);
        LayoutInflater inflater = mock(LayoutInflater.class);
        ViewGroup container = mock(ViewGroup.class);
        Button button = mock(Button.class);
        EditText edittext = mock(EditText.class);

        when(inflater.inflate(R.layout.fragment_nowy_produkt, container, false)).thenReturn(v1);
        when(v1.findViewById(R.id.btn)).thenReturn(button);
        when(v1.findViewById(R.id.editText)).thenReturn(edittext);

        View v2 = service.onCreateView(inflater, container, null);

        assertTrue( v1 == v2);

        verify(button).setOnClickListener(Mockito.any(View.OnClickListener.class));



    }

    @Test
    public void dodaj() {
        DatabaseReference baza = mock(DatabaseReference.class);

        EditText nazwa = mock(EditText.class);
        String jakis_string = "jakis string";
        Editable editable = mock(Editable.class);
        when(nazwa.getText()).thenReturn(editable);
        when(editable.toString()).thenReturn(jakis_string);
        service.setNazwa(nazwa);
        service.setBaza(baza);
        when(baza.child("Magazyn")).thenReturn(baza);
        when(baza.child("Produkty")).thenReturn(baza);
        when(baza.child(jakis_string)).thenReturn(baza);

        service.dodaj();

        verify(baza).addListenerForSingleValueEvent(Mockito.any(ValueEventListener.class));





    }
}