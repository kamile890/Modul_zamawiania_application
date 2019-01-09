package com.example.kamill.modul_zamawiania_application;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataBase_Reference {

    public DatabaseReference getDatebaseRef() {
        return FirebaseDatabase.getInstance().getReference();
    }
}
