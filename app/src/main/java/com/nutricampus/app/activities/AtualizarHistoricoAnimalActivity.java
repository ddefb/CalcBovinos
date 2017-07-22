package com.nutricampus.app.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nutricampus.app.R;
import com.nutricampus.app.database.RepositorioDadosComplAnimal;
import com.nutricampus.app.entities.Animal;
import com.nutricampus.app.entities.DadosComplAnimal;
import com.nutricampus.app.entities.Propriedade;
import com.nutricampus.app.utils.Conversor;
import com.nutricampus.app.utils.ValidaFormulario;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Felipe on 19/07/2017.
 * For project NutriCampus.
 * Contact: <felipeguimaraes540@gmail.com>
 */

public class AtualizarHistoricoAnimalActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener{

    @BindView(R.id.input_identificador)
    EditText inputIdentificador;
    @BindView(R.id.input_peso_vivo)
    EditText inputPesoVivo;
    @BindView(R.id.input_data_complementar)
    EditText inputData;
    @BindView(R.id.input_caminhada_vertical)
    EditText inputCaminhadaVertical;
    @BindView(R.id.input_caminhada_horizontal)
    EditText inputCaminhadaHorizontal;
    @BindView(R.id.input_semana_lactacao)
    EditText inputSemanaLactacao;
    @BindView(R.id.ckb_pastando)
    CheckBox ckbPastando;
    @BindView(R.id.ckb_lactacao)
    CheckBox ckbLactacao;
    @BindView(R.id.ckb_cio)
    CheckBox ckbCio;
    @BindView(R.id.ckb_gestante)
    CheckBox ckbGestante;
    @BindView(R.id.rgEec)
    RadioGroup radioGroup;
    @BindView(R.id.btn_salvar)
    Button btnSalvar;
    @BindView(R.id.input_id_propriedade)
    EditText inputIdPropriedade;

    private Calendar data;
    private int eec;
    private Animal animal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar_historico_animal);

        ButterKnife.bind(AtualizarHistoricoAnimalActivity.this);

        Intent intent = getIntent();
        animal = (Animal) intent.getSerializableExtra("animal");
        inicializaCampoData();
        preencherCampos();
    }

    private void preencherCampos() {
        RepositorioDadosComplAnimal repositorioDadosComplAnimal = new RepositorioDadosComplAnimal(AtualizarHistoricoAnimalActivity.this);
        DadosComplAnimal dadosComplAnimal = repositorioDadosComplAnimal.buscarDadosComplAnimal(animal.getId());

        inputIdentificador.setText(animal.getIndentificador());
        inputIdentificador.setFocusable(false);

        inputPesoVivo.setText(String.valueOf(dadosComplAnimal.getPesoVivo()));
        inputData.setText(Conversor.dataFormatada(dadosComplAnimal.getData()));
        inputCaminhadaHorizontal.setText(String.valueOf(dadosComplAnimal.getCaminadaHorizontal()));
        inputCaminhadaVertical.setText(String.valueOf(dadosComplAnimal.getCaminhadaVertical()));
        inputSemanaLactacao.setText(String.valueOf(dadosComplAnimal.getSemanaLactacao()));
        ckbPastando.setChecked(dadosComplAnimal.isPastando());
        ckbLactacao.setChecked(dadosComplAnimal.isLactacao());
        ckbGestante.setChecked(dadosComplAnimal.isGestante());
        ckbCio.setChecked(dadosComplAnimal.isCio());
        radioGroup.check(dadosComplAnimal.getEEC());

    }

    protected void inicializaCampoData() {
        Calendar calendario = Calendar.getInstance();
        // O valor do mês pelo Calendar varia entre 0 e 11, por isso soma +1
        if (inputData.getText().toString().equals("")) {
            data = Calendar.getInstance();
            data.set(calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), calendario.get(Calendar.DATE));
            inputData.setText(Conversor.dataFormatada(data));
        } else {
            this.data.setTime(Conversor.StringToDate(inputData.getText().toString()));
        }
    }

    public void showDatePickerDialog(View v) {
        Calendar cDefault = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AtualizarHistoricoAnimalActivity.this,
                this,
                cDefault.get(Calendar.YEAR),
                cDefault.get(Calendar.MONTH),
                cDefault.get(Calendar.DAY_OF_MONTH));

        Calendar cMax = Calendar.getInstance();
        cMax.set(cMax.get(Calendar.YEAR), cMax.get(Calendar.MONTH), cMax.get(Calendar.DATE));
        datePickerDialog.getDatePicker().setMaxDate(cMax.getTime().getTime());

        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
        data.set(ano, mes, dia);

        inputData.setText(Conversor.dataFormatada(data));
        inputData.setError(null);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {

        if ((parent != null) && (parent.getItemAtPosition(position) instanceof Propriedade)) {
            Propriedade propriedade = (Propriedade) parent.getItemAtPosition(position);
            inputIdPropriedade.setText(String.valueOf(propriedade.getId()));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void inserirData(View v) {
        showDatePickerDialog(v);
    }


    public void salvarHistoricoAnimal(View v) {
        if (!validaDados()) {
            Toast.makeText(AtualizarHistoricoAnimalActivity.this, R.string.msg_erro_cadastro_geral, Toast.LENGTH_LONG).show();
            return;
        }

        int idRadioButton = radioGroup.getCheckedRadioButtonId();
        RadioButton radio = (RadioButton) findViewById(idRadioButton);

        if(!(radio == null))
            eec =  Integer.parseInt(radio.getText().toString());

        DadosComplAnimal dadosComplAnimal = new DadosComplAnimal(
                data,
                Float.parseFloat(inputPesoVivo.getText().toString()),
                eec,
                Float.parseFloat(inputCaminhadaHorizontal.getText().toString()),
                Float.parseFloat(inputCaminhadaVertical.getText().toString()),
                Integer.parseInt(inputSemanaLactacao.getText().toString()),
                ckbPastando.isChecked(),
                ckbLactacao.isChecked(),
                ckbGestante.isChecked(),
                ckbCio.isChecked()
        );;

        RepositorioDadosComplAnimal repositorioDadosComplAnimal = new RepositorioDadosComplAnimal(AtualizarHistoricoAnimalActivity.this);
        int idDadosCompl = repositorioDadosComplAnimal.inserirDadosComplAnimal(dadosComplAnimal);

        if(idDadosCompl > 0) {
            Toast.makeText(AtualizarHistoricoAnimalActivity.this, R.string.msg_cadastro_salvo, Toast.LENGTH_LONG).show();
            dadosComplAnimal.setId(idDadosCompl);
            Intent it = new Intent(AtualizarHistoricoAnimalActivity.this, ListaAnimaisActivity.class);
            startActivity(it);
        } else {
            Toast.makeText(AtualizarHistoricoAnimalActivity.this, R.string.msg_erro_cadastro, Toast.LENGTH_LONG).show();
        }

    }

    protected boolean validaDados() {
        boolean valido = true;

        List<TextView> camposTexto = new ArrayList<>();
        camposTexto.add(inputIdentificador);
        camposTexto.add(inputData);
        camposTexto.add(inputPesoVivo);
        camposTexto.add(inputCaminhadaHorizontal);
        camposTexto.add(inputCaminhadaVertical);
        camposTexto.add(inputSemanaLactacao);

        for (TextView view : camposTexto)
            view.setError(null);

        List<TextView> camposVazios = ValidaFormulario.camposTextosVazios(camposTexto);

        if (!camposVazios.isEmpty()) {
            for (TextView view : camposVazios)
                view.setError(getString(R.string.msg_erro_campo));

            valido = false;
        }

        return valido;
    }



}