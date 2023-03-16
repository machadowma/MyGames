package com.example.mygames;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditeActivity extends AppCompatActivity {
    SQLiteDatabase bancoDados;
    EditText txtTitle, txtStudio;
    Button btnEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edite);

        txtTitle = (EditText) findViewById(R.id.txtTitle);
        txtStudio = (EditText) findViewById(R.id.txtStudio);
        btnEditar = (Button) findViewById(R.id.btnEditar);

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterar();
            }
        });

        carregarDados();
    }

    public void carregarDados(){
        Intent intent = getIntent();
        Integer id = intent.getIntExtra("id",0);
        try {
            bancoDados = openOrCreateDatabase("mygames", MODE_PRIVATE, null);
            Cursor cursor = bancoDados.rawQuery("SELECT id, title, studio FROM game WHERE id = " + id.toString(), null);
            cursor.moveToFirst();
            txtTitle.setText(cursor.getString(1));
            txtStudio.setText(cursor.getString(2));

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void alterar(){
        Intent intent = getIntent();
        Integer id = intent.getIntExtra("id",0);

        String strTitle = txtTitle.getText().toString();
        String strStudio = txtStudio.getText().toString();

        try{
            bancoDados = openOrCreateDatabase("mygames", MODE_PRIVATE, null);
            String sql = "UPDATE game SET title=?, studio=? WHERE id=?";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindString(1,strTitle);
            stmt.bindString(2,strStudio);
            stmt.bindLong(3,id);
            stmt.executeUpdateDelete();
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }

}