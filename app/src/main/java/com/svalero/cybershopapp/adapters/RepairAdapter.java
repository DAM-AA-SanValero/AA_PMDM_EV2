package com.svalero.cybershopapp.adapters;

import static com.svalero.cybershopapp.database.Constants.DATABASE_PRODUCTS;
import static com.svalero.cybershopapp.database.Constants.DATABASE_REPAIRS;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.svalero.cybershopapp.ProductDetailsActivity;
import com.svalero.cybershopapp.R;
import com.svalero.cybershopapp.RepairDetailsActivity;
import com.svalero.cybershopapp.UpdateProductActivity;
import com.svalero.cybershopapp.UpdateRepairActivity;
import com.svalero.cybershopapp.database.AppDatabase;
import com.svalero.cybershopapp.domain.Product;
import com.svalero.cybershopapp.domain.Repair;

import java.sql.Date;
import java.util.List;
import java.util.Locale;

public class RepairAdapter extends RecyclerView.Adapter<RepairAdapter.RepairHolder> {

    public List<Repair> repairList;
    public Context context;
    RepairAdapter repairAdapter;

    public RepairAdapter(List<Repair> repairList, Context context) {
        this.repairList = repairList;
        this.context = context;
    }

    @Override
    public RepairAdapter.RepairHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repair_item, parent, false);
        return new RepairAdapter.RepairHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepairAdapter.RepairHolder holder, int position) {
        holder.repairComponent.setText(repairList.get(position).getComponent());
        holder.repairAddress.setText(repairList.get(position).getShippingAddress());


        Date repairedDate = repairList.get(position).getRepairedDate();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        if(repairedDate != null) {
            String repairedDateString = format.format(repairedDate);
            if ("01/01/0001".equals(repairedDateString)) {
                holder.repairedDate.setText(context.getString(R.string.not_repaired));
            } else {
                holder.repairedDate.setText(context.getString(R.string.repaired));
            }
        } else {
            holder.repairedDate.setText(context.getString(R.string.not_repaired));
        }
    }

    @Override
    public int getItemCount() {
        return repairList.size();
    }

    public class RepairHolder extends RecyclerView.ViewHolder{
        public TextView repairComponent;
        public TextView repairAddress;
        public TextView repairedDate;
        public View parentView;

        public Button detailsButton;
        public Button updateButton;
        public Button deleteButton;

        public RepairHolder(View view){
            super(view);
            parentView = view;

            repairComponent = view.findViewById(R.id.repairComponent);
            repairAddress = view.findViewById(R.id.repairAddress);
            repairedDate = view.findViewById(R.id.repairDate);

            detailsButton = view.findViewById(R.id.detailsButton);
            updateButton = view.findViewById(R.id.updateButton);
            deleteButton = view.findViewById(R.id.deleteButton);


            detailsButton.setOnClickListener(v -> seeRepair(getAdapterPosition()));
            updateButton.setOnClickListener(v -> updateRepair(getAdapterPosition()));
            deleteButton.setOnClickListener(v -> deleteRepair(getAdapterPosition()));

        }
    }

    public void seeRepair(int position){
        Repair repair = repairList.get(position);
        Intent intent = new Intent(context, RepairDetailsActivity.class);
        intent.putExtra("component", repair.getComponent());
        context.startActivity(intent);


    }
    public void updateRepair(int position){
        Repair repair = repairList.get(position);
        Intent intent = new Intent(context, UpdateRepairActivity.class);
        intent.putExtra("component", repair.getComponent());
        intent.putExtra("position", position);
        context.startActivity(intent);
    }
    public void deleteRepair(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.Are_you_sure_alert_dialog)
                .setTitle(R.string.delete_repair)
                .setPositiveButton(R.string.yes, (dialog, i) -> {
                    final AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, DATABASE_REPAIRS)
                            .allowMainThreadQueries().build();
                    Repair repair = repairList.get(position);
                    db.repairDao().delete(repair);

                    repairList.remove(position);
                    notifyItemRemoved(position);
                })
                .setNegativeButton(R.string.no, (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
