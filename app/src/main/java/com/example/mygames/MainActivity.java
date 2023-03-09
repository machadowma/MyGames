package com.example.mygames;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public SQLiteDatabase bancoDados;
    ListView listView;
    FloatingActionButton btnCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        btnCadastro = (FloatingActionButton) findViewById(R.id.btnCadastro);

        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaCadastro();
            }
        });

        criarBancoDados();
        // inserirDadosTemp();
        listarDados();
    }

    public void criarBancoDados(){
        try {
            bancoDados = openOrCreateDatabase("mygames", MODE_PRIVATE, null);
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS game(" +
                    "   id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    " , title VARCHAR" +
                    " , studio VARCHAR)");
            //bancoDados.execSQL("DELETE FROM animal");
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void inserirDadosTemp(){
        try{
            bancoDados = openOrCreateDatabase("mygames", MODE_PRIVATE, null);
            String sql = "INSERT INTO game (title,studio) VALUES (?,?)";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);

            stmt.bindString(1,"Battlefield 1");
            stmt.bindString(2,"Frostbite");
            stmt.executeInsert();

            stmt.bindString(1,"The Last of Us");
            stmt.bindString(2,"Naughtydog");
            stmt.executeInsert();

            bancoDados.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void listarDados(){
        try {

            bancoDados = openOrCreateDatabase("mygames", MODE_PRIVATE, null);
            Cursor meuCursor = bancoDados.rawQuery("SELECT id, title FROM game", null);
            ArrayList<String> linhas = new ArrayList<String>();
            ArrayAdapter meuAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    linhas
            );
            listView.setAdapter(meuAdapter);
            //arrayIds = new ArrayList<>();
            meuCursor.moveToFirst();
            do {
                linhas.add(meuCursor.getString(0) + " - " + meuCursor.getString(1));
                //arrayIds.add(meuCursor.getInt(0));
            } while(meuCursor.moveToNext());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void abrirTelaCadastro(){
        Intent intent = new Intent(this,CadastroActivity.class);
        startActivity(intent);
    }

}