package com.example.projectfirebase_2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.common.collect.Table;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class TelaPrincipal extends AppCompatActivity {
    private TextView nomeUsuario,emailUsuario;
    private Button bt_deslogar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;

    private ArrayList<String> data = new ArrayList<String>();
    private ArrayList<String> data1 = new ArrayList<String>();
    private ArrayList<String> data2 = new ArrayList<String>();
    private ArrayList<String> data3 = new ArrayList<String>();

    private TableLayout table;

    EditText ed1,ed2,ed3;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        IniciarComponentes();

        ed1 = findViewById(R.id.editTextProduto);
        ed2 = findViewById(R.id.editTextPreco);
        ed3 = findViewById(R.id.editTextQuantidade);

        b1 = findViewById(R.id.addButton);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });


        bt_deslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(TelaPrincipal.this,FormLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }


        public void add() {

            int tot;

            String prodname = ed1.getText().toString();
            int price = Integer.parseInt(ed2.getText().toString());
            int qty = Integer.parseInt(ed3.getText().toString());
            tot = price * qty;

            data.add(prodname);
            data1.add(String.valueOf(price));
            data2.add(String.valueOf(qty));
            data3.add(String.valueOf(tot));

            TableLayout table = (TableLayout) findViewById(R.id.table1);

            TableRow row = new TableRow(this);
            TextView t1 = new TextView(this);
            TextView t2 = new TextView(this);
            TextView t3 = new TextView(this);
            TextView t4 = new TextView(this);

            String total;

            int sum = 0;

            for(int i = 0; i<data.size(); i++) {
                String pname = data.get(i);
                String prc = data1.get(i);
                String qtyy = data2.get(i);
                total = data3.get(i);

                t1.setText(pname);
                t2.setText(prc);
                t3.setText(qtyy);
                t4.setText(total);

                sum = sum + Integer.parseInt(data3.get(i).toString());
            }

            row.addView(t1);
            row.addView(t2);
            row.addView(t3);
            row.addView(t4);
            table.addView(row);

            ed3.setText("");
            ed2.setText("");
            ed1.setText("");
            ed1.requestFocus();

        }

    @Override
    protected void onStart() {
        super.onStart();

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuários").document(usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null){
                    nomeUsuario.setText(documentSnapshot.getString("nome"));
                    emailUsuario.setText(email);
                }
            }
        });
    }

    private void IniciarComponentes(){
        nomeUsuario = findViewById(R.id.textNomeUsuario);
        emailUsuario = findViewById(R.id.textEmailUsuario);
        bt_deslogar = findViewById(R.id.bt_deslogar);
    }
}