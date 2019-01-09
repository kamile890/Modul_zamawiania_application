package com.example.kamill.modul_zamawiania_application;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class nowe_zamowienieTest {

    private nowe_zamowienie service;
    private DataBase_Reference mockDataBaseReference;

    @Before
    public void setUp() throws Exception {
        service = new nowe_zamowienie();
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
        when(inflater.inflate(R.layout.fragment_nowe_zamowienie, container, false)).thenReturn(v1);

        Spinner spinner = mock(Spinner.class);
        EditText editText = mock(EditText.class);
        ListView ListView = mock(ListView.class);
        Button btn1 = mock(Button.class);
        Button btn2 = mock(Button.class);


        when(v1.findViewById(R.id.spinner_z_produktami)).thenReturn(spinner);
        when(v1.findViewById(R.id.liczba_produktow_EditText)).thenReturn(editText);
        when(v1.findViewById(R.id.lista_zamowienie)).thenReturn(ListView);
        when(v1.findViewById(R.id.zloz_zamowienie_btn)).thenReturn(btn1);
        when(v1.findViewById(R.id.dodaj_do_zamowienia_btn)).thenReturn(btn2);
        when(mockDataBaseReference.getDatebaseRef()).thenReturn(baza);
        View v2 = service.onCreateView(inflater, container, null);

        assertTrue(v1 == v2);
        verify(btn1).setOnClickListener(Mockito.any(View.OnClickListener.class));
        verify(btn2).setOnClickListener(Mockito.any(View.OnClickListener.class));





    }

    @Test
    public void stworz_spinner_z_przedmiotami() {
        DatabaseReference baza = mock(DatabaseReference.class);
        service.setBaza(baza);
        service.stworz_spinner_z_przedmiotami();
        verify(baza).addListenerForSingleValueEvent(Mockito.any(ValueEventListener.class));

    }

    @Test
    public void dodaj_do_zamowienia() {
        DatabaseReference baza = mock(DatabaseReference.class);
        service.setBaza(baza);
        service.dodaj_do_zamowienia();
        verify(baza).addListenerForSingleValueEvent(Mockito.any(ValueEventListener.class));

    }

    @Test
    public void zloz_zamowienie() {
        DatabaseReference baza = mock(DatabaseReference.class);
        service.setBaza(baza);
        service.dodaj_do_zamowienia();
        verify(baza).addListenerForSingleValueEvent(Mockito.any(ValueEventListener.class));
    }
}