package com.shopping.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapterList extends RecyclerView.Adapter<MyAdapterList.MyViewHolder> {
    private Context context;
    private ArrayList<String> judul;
    private ArrayList<String> tanggal;
    private OnItemClickListener listener; // Listener untuk menangani klik item
    private int lastClickedPosition = -1;
    private DBHandler db;
    private MainActivity mainActivity; // Reference to MainActivity instance

    public MyAdapterList(Context context, ArrayList<String> judul, ArrayList<String> tanggal, OnItemClickListener listener, DBHandler db, MainActivity mainActivity) {
        this.context = context;
        this.judul = judul;
        this.tanggal = tanggal;
        this.listener = listener;
        this.db = db; // Assign the DBHandler instance
        this.mainActivity = mainActivity; // Store the reference to MainActivity instance
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_row_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.judul.setText(judul.get(position));
        holder.tanggal.setText(tanggal.get(position));

        final int itemPosition = position; // Declare as final

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(itemPosition); // Use the final variable
            }
        });
    }


    private void showDeleteConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Konfirmasi Hapus Data")
                .setMessage("Apakah Anda yakin ingin menghapus data ini?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int id = getIdByPosition(position);
                        if (id != -1) {
                            deleteDataFromDatabase(id);
                            removeItem(position);
                            mainActivity.displayData();
                        }
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }



    private int getIdByPosition(int position) {
        Cursor cursor = db.getData(); // Retrieve the data from the database
        if (cursor.moveToPosition(position)) { // Move the cursor to the desired position
            return cursor.getInt(cursor.getColumnIndex("id")); // Retrieve the ID from the cursor
        }
        return -1; // Return -1 if the ID is not found
    }


    private void deleteDataFromDatabase(int id) {
        boolean deleted = db.deleteDataById(id);
        if (deleted) {
            Toast.makeText(context, "Data Berhasil Di Delete", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Gagal Delete Data", Toast.LENGTH_SHORT).show();
        }
    }


    private void removeItem(int position) {
        judul.remove(position);
        tanggal.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return judul.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView judul, tanggal;
        ImageView btnMinus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.tvJudul);
            tanggal = itemView.findViewById(R.id.tvTanggal);
            btnMinus = itemView.findViewById(R.id.btnMinus);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                // Panggil listener untuk menangani klik pada item
                listener.onItemClick(position);
            }
        }
    }

    // Interface untuk menangani klik item
    public interface OnItemClickListener {
        void onItemClick(int position);
    }


}
