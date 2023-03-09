package com.example.mygames;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CadastroActivity extends AppCompatActivity {
    EditText txtTitle, txtStudio;
    Button btnCadastrar;
    SQLiteDatabase bancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        txtTitle = (EditText) findViewById(R.id.txtTitle);
        txtStudio = (EditText) findViewById(R.id.txtStudio);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrar();
            }
        });
    }

    public void cadastrar(){
        if(!TextUtils.isEmpty(txtTitle.getText().toString())){
            try{
                bancoDados = openOrCreateDatabase("mygames", MODE_PRIVATE, null);
                String sql = "INSERT INTO game (title) VALUES (?)";
                SQLiteStatement stmt = bancoDados.compileStatement(sql);
                stmt.bindString(1,txtTitle.getText().toString());
                stmt.executeInsert();
                bancoDados.close();
                finish();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}