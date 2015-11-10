package com.slkaufma3gmail.tictactoe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


/*
 *  this does nothing except get the fragment
 *  see fragment code for what this example does.
 * 
 */


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DrawFragment()).commit();
        }
    }
}
