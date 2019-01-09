package com.example.kamill.modul_zamawiania_application;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

public class stan_hurtowniTest {

    private stan_hurtowni service;
    private DataBase_Reference mockDataBaseReference;

    @Before
    public void setUp() {
        service = new stan_hurtowni();
        mockDataBaseReference = mock(DataBase_Reference.class);
        service.setDataBaseReference(mockDataBaseReference);

    }

    @After
    public void tearDown() {
    }

    @Test
    public void onCreateView() {

        DatabaseReference baza = mock(DatabaseReference.class);
        when(mockDataBaseReference.getDatebaseRef()).thenReturn(baza);
        View v1 = mock(View.class);
        ListView lista = mock(ListView.class);
        LayoutInflater inflater = mock(LayoutInflater.class);
        ViewGroup container = mock(ViewGroup.class);
        when(v1.findViewById(R.id.lista_przedmiotow)).thenReturn(lista);
        when(inflater.inflate(R.layout.fragment_stan_hurtowni, container, false)).thenReturn(v1);
        View v2 = service.onCreateView(inflater, container, null);
        assertTrue(v1 == v2);



    }

    @Test
    public void pobierz_dane_z_hurtowni_i_pokaz_w_liście() {
        DatabaseReference baza = mock(DatabaseReference.class);
        service.setBaza(baza);
        service.pobierz_dane_z_hurtowni_i_pokaz_w_liście();
        verify(baza).addListenerForSingleValueEvent(Mockito.any(ValueEventListener.class));


    }

}