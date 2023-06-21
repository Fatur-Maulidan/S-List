package com.shopping.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements MyAdapterList.OnItemClickListener {

    private TextView dateTextView;
    private RecyclerView recyclerView;
    private ArrayList<String> varJudul, varTanggal, varId;
    private DBHandler db;
    private MyAdapterList adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView varBtnTambah = findViewById(R.id.btnIconTambah);
        dateTextView = findViewById(R.id.tvTanggal);

        // Instance Object
        db = new DBHandler(MainActivity.this);
        varJudul = new ArrayList<>();
        varTanggal = new ArrayList<>();
        varId = new ArrayList<>();

        // Get Data From Layout
        recyclerView = findViewById(R.id.recyclerViewPendapatan);
        adapter = new MyAdapterList(MainActivity.this, varJudul, varTanggal, this, db, MainActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        displayData();

        // Mendapatkan tanggal hari ini
        Date today = new Date();

        // Membuat format tanggal yang diinginkan
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        // Memformat tanggal menjadi string
        String formattedDate = dateFormat.format(today);

        // Menampilkan tanggal pada TextView
        dateTextView.setText(formattedDate);

        varBtnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, InputDataListActivity.class));
            }
        });
    }

    // Munculkan semua data dari database kedalam tampilan
// Munculkan semua data dari database kedalam tampilan
    void displayData() {
        Cursor cursor = db.getData();
        if (cursor.getCount() == 0) {
            Toast.makeText(MainActivity.this, "Tidak ada Data", Toast.LENGTH_SHORT).show();
            return;
        } else {
            varId.clear(); // Kosongkan data sebelum mengisi dengan data baru
            varJudul.clear();
            varTanggal.clear();

            while (cursor.moveToNext()) {
                varId.add(cursor.getString(0));
                varJudul.add(cursor.getString(2));
                varTanggal.add(cursor.getString(3));
            }

            adapter.notifyDataSetChanged(); // Perbarui data di RecyclerView
        }
    }


    @Override
    public void onItemClick(int position) {
        // Tangani klik pada item di sini
        String id = varId.get(position);

        // Buat Intent untuk memindahkan ke InputDataListActivity
        Intent intent = new Intent(MainActivity.this, InputDataListActivity.class);

        // Sertakan data judul dan tanggal dalam Intent
        intent.putExtra("id", id);

        // Pindahkan ke InputDataListActivity
        startActivity(intent);
    }

}
