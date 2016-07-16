package com.example.joao.baba1;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyListFragment extends ListFragment implements OnItemClickListener {

    private String[] vet = {"Oi 1", "Oi 2", "Oi 3"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Planets, android.R.layout.simple_list_item_1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, vet);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
    }
}
