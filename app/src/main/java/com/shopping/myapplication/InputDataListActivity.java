package com.shopping.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class InputDataListActivity extends AppCompatActivity {

    private EditText varEtJudul, varEtCatatan;
    private DatePicker varDpTanggal;
    private Button varBtnYakin;
    private DBHandler db;
    private String judul, catatan, tanggal;
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data_list);

        db = new DBHandler(InputDataListActivity.this);

//      Get Data Layout
        varEtJudul = findViewById(R.id.etJudul);
        varEtCatatan = findViewById(R.id.etCatatan);
        varDpTanggal = findViewById(R.id.dpTanggal);
        varBtnYakin = findViewById(R.id.btnYakin);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        if (id != null && !id.isEmpty()) {
            Cursor cursor = db.getDataById(Integer.parseInt(id));
            Log.d("cursor nya adalah : ", cursor.toString());
            if (cursor != null && cursor.moveToFirst()) {
                // Mengambil data dari cursor
                String judul = cursor.getString(cursor.getColumnIndexOrThrow("judul"));
                String catatan = cursor.getString(cursor.getColumnIndexOrThrow("keterangan"));
                String tgl = cursor.getString(cursor.getColumnIndexOrThrow("tanggal"));

                // Mengatur teks ke dalam TextView
                varEtJudul.setText(judul);
                varEtCatatan.setText(catatan);

                // Melakukan parsing tanggal
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date tanggal;

                try {
                    tanggal = inputFormat.parse(tgl);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return;
                }

                // Mendapatkan tahun, bulan, dan hari dari tanggal
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(tanggal);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Mengatur tanggal pada DatePicker
                varDpTanggal.init(year, month, day, null);
            }
        }

//      Ketika Button Yakin di Klik
        varBtnYakin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              Mengambil data dari layout Nominal Catatan Tanggal dan Juga Pilihan (Pendapatan/Pengeluaran)
                judul = varEtJudul.getText().toString();
                catatan = varEtCatatan.getText().toString();

                // Mendapatkan tanggal yang dipilih dari varDpTanggal
                int year = varDpTanggal.getYear();
                int month = varDpTanggal.getMonth();
                int dayOfMonth = varDpTanggal.getDayOfMonth();

                // Membuat objek Calendar
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                // Format tanggal menjadi string
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                tanggal = dateFormat.format(calendar.getTime());

//              Instance Database
                DBHandler db = new DBHandler(InputDataListActivity.this);

//              Ketika ada salah satu form kosong
                if(judul.isEmpty() || catatan.isEmpty() || tanggal.isEmpty()) {
//                  Memberikan short message bahwa data belum terisi
                    Toast.makeText(InputDataListActivity.this, "Isi semua data dengan benar", Toast.LENGTH_SHORT).show();
                }
//              Ketika semua berhasil terisi
                else {
                    if (id != null && !id.isEmpty()) {
                        db.updateDataById(Integer.parseInt(id), judul, catatan, tanggal);
                        id = null;
                        Toast.makeText(InputDataListActivity.this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show();
                    } else {
                        db.addNewData(catatan, judul, tanggal);
                        // Semua data berhasil terisi dan masuk kedalam database
                        Toast.makeText(InputDataListActivity.this, "Data berhasil dimasukkan", Toast.LENGTH_SHORT).show();
                    }

//                  Dipindahkan ke aktivitas Home yaitu bagian tampilan awal
                    startActivity(new Intent(InputDataListActivity.this, MainActivity.class));
                }
            }
        });
    }
}