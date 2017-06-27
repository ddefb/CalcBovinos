package com.nutricampus.app;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.AppCompatButton;
import android.widget.Button;
import android.widget.EditText;

import com.nutricampus.app.activities.CadastrarUsuarioActivity;
import com.nutricampus.app.activities.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Mateus on 27/06/2017.
 * For project NutriCampus.
 * Contact: <paulomatew@gmail.com>
 */

@RunWith(AndroidJUnit4.class)
public class InstrumentedCadastrarActivityActivity {
    @Rule
    public ActivityTestRule<CadastrarUsuarioActivity> rule = new ActivityTestRule<>(CadastrarUsuarioActivity.class);

    @Test
    public void ensureViewIsShowing() throws Exception {
        CadastrarUsuarioActivity activity = rule.getActivity();
        EditText cpf = (EditText)activity.findViewById(R.id.edtCpf);
        assertNotNull(cpf);

        EditText crm = (EditText)activity.findViewById(R.id.edtRegistro);
        assertNotNull(crm);

        Button btn_savar = (Button)activity.findViewById(R.id.btn_salvar_cadastro);
        assertNotNull(btn_savar);
    }
}
