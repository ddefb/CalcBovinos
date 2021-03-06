package com.nutricampus.app.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nutricampus.app.R;
import com.nutricampus.app.database.RepositorioUsuario;
import com.nutricampus.app.entities.Usuario;
import com.nutricampus.app.utils.services.SendMailTask;

import java.util.Arrays;

/*
Explicação para a supressão de warnings:
 - "squid:MaximumInheritanceDepth" = herança extendida em muitos niveis (mais que 5), permitido aqui já
 que refere-se a herança das classes das activities Android
 - "squid:S1172" = erro do sonarqube para os parametros "view" não utilizados
*/
@java.lang.SuppressWarnings({"squid:S1172", "squid:MaximumInheritanceDepth"})
public class RecuperarSenhaActivity extends AppCompatActivity {

    private EditText textEditUsuario;
    private Button buttonRecuperarSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        textEditUsuario = (EditText) findViewById(R.id.input_usuario_recupera);
        buttonRecuperarSenha = (Button) findViewById(R.id.btn_recuperar);

    }

    public void recuperaSenha(View view) {

        if (!validaDados()) {
            return;
        }

        if (isOnline()) {
            buttonRecuperarSenha.setEnabled(false);

            String usuarioValor = this.textEditUsuario.getText().toString();
            Usuario usuarioDados = buscaUsuario(usuarioValor);

            if (usuarioDados != null) {
                enviaEmail(usuarioDados);
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                Toast.makeText(RecuperarSenhaActivity.this,
                                        getString(R.string.msg_senha_enviada), Toast.LENGTH_LONG).show();

                            }
                        }, 3000);
            } else {
                Toast.makeText(RecuperarSenhaActivity.this, getString(R.string.msg_crz_nao_encontrado),
                        Toast.LENGTH_LONG).show();
            }
            buttonRecuperarSenha.setEnabled(true);
        } else {
            Toast toast = Toast.makeText(RecuperarSenhaActivity.this,
                    getString(R.string.msg_sem_internet), Toast.LENGTH_LONG);
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);

            if (v != null) v.setGravity(Gravity.CENTER);
            toast.show();
        }
    }


    public boolean validaDados() {
        boolean valido = true;

        String usuario = this.textEditUsuario.getText().toString();

        if (usuario.isEmpty()) {
            this.textEditUsuario.setError(getString(R.string.msg_erro_campo));
            valido = false;
        } else if (usuario.length() < 4) {
            this.textEditUsuario.setError(getString(R.string.msg_erro_crz));
            valido = false;
        } else {
            this.textEditUsuario.setError(null);
        }

        return valido;
    }

    public Usuario buscaUsuario(String usuarioValor) {

        RepositorioUsuario repositorioUsuario = new RepositorioUsuario(getBaseContext());
        return repositorioUsuario.buscarUsuario(usuarioValor);

    }

    public void enviaEmail(Usuario usuario) {
        String corpoEmail = "Olá, " + usuario.getNome() + ".<br><br>" +
                "Você está recebendo este e-mail por que solicitou sua senha " +
                "através do nosso aplicativo.<br>" +
                "Sua senha é: " + usuario.getSenha() + "<br><br>" +
                "Atenciosamente,<br>NutriCampus App.";

        String assuntoEmail = "NutriCampusApp - Recuperação de Senha";

        new SendMailTask(RecuperarSenhaActivity.this).execute("nutricampusapp@gmail.com",
                "nutcam0511",
                Arrays.asList(usuario.getEmail().split("\\s*,\\s*")),
                assuntoEmail, corpoEmail);

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

}
