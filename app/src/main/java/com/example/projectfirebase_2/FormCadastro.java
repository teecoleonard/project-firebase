package com.example.projectfirebase_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FormCadastro extends AppCompatActivity {

    private EditText edit_nome,edit_email,edit_pass;
    private Button bt_cadastrar;
    String[] mensagens = {"Preencha todos os campos.", "Cadastro realizado com sucesso."};
    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);

        IniciarComponentes();

        bt_cadastrar.setOnClickListener(view -> {

            String nome = edit_nome.getText().toString();
            String email = edit_email.getText().toString();
            String pass = edit_pass.getText().toString();

            if (nome.isEmpty() || email.isEmpty() || pass.isEmpty()){
                Snackbar snackbar = Snackbar.make(view,mensagens[0],Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }else{
                CadastrarUsuario(view);
            }
        });
    }

    private void CadastrarUsuario(View view){

        String email = edit_email.getText().toString();
        String pass = edit_pass.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,pass).addOnCompleteListener(task -> {

            if (task.isSuccessful()){

                SalvarDadosUsuario();

                Snackbar snackbar = Snackbar.make(view,mensagens[1],Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }else{
                String erro;
                try {
                    throw task.getException();

                }catch (FirebaseAuthWeakPasswordException e) {
                    erro = "Digite um senha com no mínimo 6 caracteres.";
                }catch (FirebaseAuthUserCollisionException e) {
                    erro = "Este e-mail está foi cadastrado";
                }catch (FirebaseAuthInvalidCredentialsException e) {
                    erro = "E-mail inválido";
                }catch (Exception e){
                    erro = "Erro ao cadastrar usuário";
                }

                Snackbar snackbar = Snackbar.make(view,erro,Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();

            }
        });
    }

    private void SalvarDadosUsuario(){
        String nome = edit_nome.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String,Object> usuarios = new HashMap<>();
        usuarios.put("nome",nome);

        usuarioID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        DocumentReference documentReference = db.collection("Usuários").document(usuarioID);
        documentReference.set(usuarios).addOnSuccessListener(unused -> Log.d("db","Sucesso ao salvar os dados."))
                .addOnFailureListener(e -> Log.d("db_error","Erro ao salvar os dados." + e.toString()));
    }
    private void IniciarComponentes(){
        edit_nome = findViewById(R.id.edit_nome);
        edit_email = findViewById(R.id.edit_email);
        edit_pass = findViewById(R.id.edit_pass);
        bt_cadastrar = findViewById(R.id.bt_cadastrar);
    }

}