package com.nutricampus.app.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nutricampus.app.R;
import com.nutricampus.app.adapters.ListaAnimaisAdapter;
import com.nutricampus.app.entities.Animal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListaAnimaisActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener{

    @BindView(R.id.spinnerPropriedade) Spinner spinnerPropriedade;
    @BindView(R.id.listaAnimais) ListView listAnimais;
    @BindView(R.id.text_quantidades_encontrados) TextView registrosEncontrados;
    @BindView(R.id.linha) View linha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_animais);

        ButterKnife.bind(this);

        listAnimais.setEmptyView(findViewById(android.R.id.empty));
        listAnimais.setOnItemClickListener(this);
        carregarListView("");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btn_add_proprietario);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaAnimaisActivity.this, CadastrarAnimalActivity.class);
                startActivity(intent);
                ListaAnimaisActivity.this.finish();

            }
        });

    }

    private void carregarListView(String s) {
        List<Animal> animais = new ArrayList<>();//this.buscarAnimal(nome);

        ListaAnimaisAdapter adapter =
                new ListaAnimaisAdapter(this, animais);

        listAnimais.setAdapter(adapter);

        if (animais.isEmpty()) {
            linha.setVisibility(View.GONE);
        } else {
            spinnerPropriedade.setVisibility(View.GONE);
            linha.setVisibility(View.VISIBLE);
            registrosEncontrados.setText(getResources().getQuantityString(
                    R.plurals.quatidade_registros,
                    adapter.getCount(),
                    adapter.getCount()));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}