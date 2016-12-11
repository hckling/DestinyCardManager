package com.cardmanager.apps.kling.destinycardmanager;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.cardmanager.apps.kling.destinycardmanager.activities.BuildDeckActivity;
import com.cardmanager.apps.kling.destinycardmanager.activities.SelectCardsActivity;
import com.cardmanager.apps.kling.destinycardmanager.model.CardSet;
import com.cardmanager.apps.kling.destinycardmanager.model.CardSetBuilder;

import org.xmlpull.v1.XmlPullParser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<CardSet> cardSets = new ArrayList<CardSet>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.btnManageCollection);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SelectCardsActivity.class);
                startActivity(intent);
            }
        });

        Button newDeckButton = (Button) findViewById(R.id.btnNewDeck);
        newDeckButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BuildDeckActivity.class);
                startActivity(intent);
            }
        });
    }
}
