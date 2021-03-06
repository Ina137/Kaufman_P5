package com.slkaufma3gmail.tictactoe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/*
 * This demos a custom view.  There is almost nothing in this activity
 * except for a call to the customview to reset the grid from a menu item.
 *
 */

public class DrawFragment extends Fragment {

    DrawView dv;

    public DrawFragment() {
        // Required empty public constructor
        //required if the fragment is adding menu items, otherwise it calls the menu methods.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.draw_frag, container, false);
        dv = (DrawView) myView.findViewById(R.id.dv1);

        return myView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reset:
                dv.clearmaze();
                return true;
            case R.id.about:
                dv.about();
        }
        return false;
    }
}

