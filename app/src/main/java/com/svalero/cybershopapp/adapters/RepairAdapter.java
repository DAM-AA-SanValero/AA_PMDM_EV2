package com.svalero.cybershopapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.svalero.cybershopapp.R;
import com.svalero.cybershopapp.contract.RepairDeleteContract;
import com.svalero.cybershopapp.presenter.RepairDeletePresenter;
import com.svalero.cybershopapp.view.RepairDetailsView;
import com.svalero.cybershopapp.view.RepairUpdateView;
import com.svalero.cybershopapp.domain.Repair;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class RepairAdapter extends RecyclerView.Adapter<RepairAdapter.RepairHolder>
    implements RepairDeleteContract.View {

    RepairDeletePresenter presenter;

    private View snackBarView;
    public List<Repair> repairList;
    public Context context;
    RepairAdapter repairAdapter;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    public RepairAdapter(List<Repair> repairList, Context context) {
        this.repairList = repairList;
        this.context = context;
        presenter = new RepairDeletePresenter(this);
    }

    @Override
    public RepairHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repair_item, parent, false);
        return new RepairHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepairAdapter.RepairHolder holder, int position) {
        holder.repairComponent.setText(repairList.get(position).getComponent());
        holder.repairAddress.setText(repairList.get(position).getShippingAddress());


        LocalDate repairedDate = repairList.get(position).getRepairedDate();
        if(repairedDate != null) {
            String repairedDateString = repairedDate.format(dateFormatter);
            if ("01/01/0001".equals(repairedDateString)) {
                holder.repairedDate.setText(context.getString(R.string.not_repaired));
            } else {
                holder.repairedDate.setText(context.getString(R.string.repaired) + " " + repairedDateString);
            }
        } else {
            holder.repairedDate.setText(context.getString(R.string.not_repaired));
        }
    }

    @Override
    public int getItemCount() {
        return repairList.size();
    }

    @Override
    public void showError(String errorMessage) {
        Snackbar.make(snackBarView, errorMessage, BaseTransientBottomBar.LENGTH_LONG).show();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(context, R.string.repair_deleted, Toast.LENGTH_LONG).show();
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
            snackBarView = parentView;

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
        Intent intent = new Intent(context, RepairDetailsView.class);
        intent.putExtra("repair_id", repair.getId());
        context.startActivity(intent);


    }
    public void updateRepair(int position){
        Repair repair = repairList.get(position);
        Intent intent = new Intent(context, RepairUpdateView.class);
        intent.putExtra("repair_id", repair.getId());
        //intent.putExtra("position", position);
        context.startActivity(intent);
    }
    public void deleteRepair(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.Are_you_sure_alert_dialog)
                .setTitle(R.string.delete_repair)
                .setPositiveButton(R.string.yes, (dialog, i) -> {
                    Repair repair = repairList.get(position);
                    presenter.deleteRepair(repair.getId());

                    repairList.remove(position);
                    notifyItemRemoved(position);
                })
                .setNegativeButton(R.string.no, (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
