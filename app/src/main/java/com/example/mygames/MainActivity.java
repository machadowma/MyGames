package com.example.mygames;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public SQLiteDatabase bancoDados;
    ListView listView;
    FloatingActionButton btnCadastro;
    public ArrayList<Integer> arrayIds;
    Integer idSelecionado;

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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                idSelecionado = arrayIds.get(i);
                confirmaExcluir();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                abrirTelaEditar(i);
            }
        });

        criarBancoDados();
        // inserirDadosTemp();
        listarDados();
    }

    @Override
    protected void onResume(){
        super.onResume();
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
            arrayIds = new ArrayList<>();
            meuCursor.moveToFirst();
            do {
                linhas.add(meuCursor.getString(0) + " - " + meuCursor.getString(1));
                arrayIds.add(meuCursor.getInt(0));
            } while(meuCursor.moveToNext());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void abrirTelaCadastro(){
        Intent intent = new Intent(this,CadastroActivity.class);
        startActivity(intent);
    }

    public void abrirTelaEditar(Integer posicao){
        Intent intent = new Intent(this,EditeActivity.class);
        Integer id = arrayIds.get(posicao);
        intent.putExtra("id",id);
        startActivity(intent);
    }


    public void confirmaExcluir() {
        AlertDialog.Builder msgBox = new AlertDialog.Builder(MainActivity.this);
        msgBox.setTitle("Excluir");
        msgBox.setIcon(android.R.drawable.ic_menu_delete);
        msgBox.setMessage("Você realmente deseja excluir esse registro?");
        msgBox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deletar();
                listarDados();
            }
        });
        msgBox.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        msgBox.show();
    }

    public void deletar(){
        try{
            bancoDados = openOrCreateDatabase("mygames", MODE_PRIVATE, null);
            String sql = "DELETE FROM game WHERE id =?";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindLong(1, idSelecionado);
            stmt.executeUpdateDelete();
            listarDados();
            bancoDados.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}