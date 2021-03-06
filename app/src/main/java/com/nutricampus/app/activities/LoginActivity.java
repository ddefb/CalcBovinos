package com.nutricampus.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nutricampus.app.R;
import com.nutricampus.app.database.RepositorioGrupo;
import com.nutricampus.app.database.RepositorioUsuario;
import com.nutricampus.app.database.SharedPreferencesManager;
import com.nutricampus.app.entities.Grupo;
import com.nutricampus.app.entities.Usuario;


/*
Explicação para a supressão de warnings:
 - "squid:MaximumInheritanceDepth" = herança extendida em muitos niveis (mais que 5), permitido aqui já
 que refere-se a herança das classes das activities Android
 - "squid:S1172" = erro do sonarqube para os parametros "view" não utilizados
*/
@java.lang.SuppressWarnings({"squid:S1172", "squid:MaximumInheritanceDepth"})
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private SharedPreferencesManager session;

    private EditText editTextUsuario;
    private EditText editTextSenha;
    private Button buttonEntrar;

    /**
     * Método para criar usuário automaticamente sem necessidade de cadastrar todas as vezes
     */
    private void criarUsuarioDefault() {
        String admin = "admin";

        RepositorioUsuario repo = new RepositorioUsuario(this);
        if (repo.buscarUsuario(admin, admin) == null) {
            repo.inserirUsuario(new Usuario(1, admin, "", admin, "admin@mail.com", admin));
        }
    }

    private void init() {
        editTextSenha = (EditText) findViewById(R.id.input_senha);
        editTextUsuario = (EditText) findViewById(R.id.input_usuario);
        buttonEntrar = (Button) findViewById(R.id.btn_login);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        criarUsuarioDefault();

        session = new SharedPreferencesManager(this);

        if (session.isLoggedIn()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(LoginActivity.this, getString(R.string.msg_bem_vindo), Toast.LENGTH_LONG).show();
            this.finish();
        }

        setContentView(R.layout.activity_login);
        init();
    }

    public void cadastroOnClick(View view) {
        Intent intent = new Intent(this, CadastrarUsuarioActivity.class);

        startActivity(intent);
    }

    public void recuperarSenhaOnClick(View view) {
        Intent intent = new Intent(this, RecuperarSenhaActivity.class);
        startActivity(intent);
    }

    public void entrarOnClick(View view) {
        Log.d(TAG, "Login");

        if (!validaDados()) {
            falhaLogin("");
            return;
        }

        this.buttonEntrar.setEnabled(false);

        String usuario = this.editTextUsuario.getText().toString();
        String senha = this.editTextSenha.getText().toString();
        Usuario usuarioLogado = buscarUsuario(usuario, senha);

        if (usuarioLogado != null) {
            session.createLoginSession(
                    usuarioLogado.getId(), usuarioLogado.getNome(), usuarioLogado.getEmail(), usuarioLogado.getSenha(), usuarioLogado.getCrmv());
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(LoginActivity.this, getString(R.string.msg_bem_vindo), Toast.LENGTH_LONG).show();
            this.finish();
        } else {
            falhaLogin(getString(R.string.msg_dados_login_invalido));
            buttonEntrar.setEnabled(true);
        }

    }

    private Usuario buscarUsuario(String usuarioValor, String senhaValor) {
        RepositorioUsuario repositorioUsuario = new RepositorioUsuario(getBaseContext());
        return repositorioUsuario.buscarUsuario(usuarioValor, senhaValor);

    }

    private void falhaLogin(String mensagem) {
        String msg = mensagem.isEmpty() ? "" : (", " + mensagem);
        Toast.makeText(LoginActivity.this, getString(R.string.msg_falha_login) + msg, Toast.LENGTH_LONG).show();

        buttonEntrar.setEnabled(true);
    }

    private boolean validaDados() {
        boolean valido = true;

        String usuario = this.editTextUsuario.getText().toString();
        String password = this.editTextSenha.getText().toString();

        if (usuario.isEmpty()) {
            this.editTextUsuario.setError(getString(R.string.msg_erro_campo));
            valido = false;
        } else if (usuario.length() < 4) {
            this.editTextUsuario.setError(getString(R.string.msg_erro_crz));
            valido = false;
        } else {
            this.editTextUsuario.setError(null);
        }

        if (password.isEmpty()) {
            this.editTextSenha.setError(getString(R.string.msg_erro_campo));
            valido = false;
        } else if (password.length() < 5) {
            this.editTextSenha.setError(getString(R.string.msg_erro_senha));
            valido = false;
        } else {
            this.editTextSenha.setError(null);
        }

        return valido;
    }


}