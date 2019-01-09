package com.example.kamill.modul_zamawiania_application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

public class stan_magazynuTest {

    private stan_magazynu service;
    private DataBase_Reference mockDataBaseReference;

    @Before
    public void setUp() throws Exception {
        service = new stan_magazynu();
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
        ListView lista = mock(ListView.class);

        when(inflater.inflate(R.layout.fragment_stan_magazynu, container, false)).thenReturn(v1);
        when(v1.findViewById(R.id.lista_przedmiotow)).thenReturn(lista);

        View v2 = service.onCreateView(inflater, container, null);

        assertTrue( v1 == v2);

        verify(baza).addValueEventListener(Mockito.any(ValueEventListener.class));



    }
}