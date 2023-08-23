package com.svalero.cybershopapp.adapters;

import static com.svalero.cybershopapp.database.Constants.DATABASE_CLIENTS;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.svalero.cybershopapp.contract.ClientDeleteContract;
import com.svalero.cybershopapp.presenter.ClientDeletePresenter;
import com.svalero.cybershopapp.view.ClientDetailsView;
import com.svalero.cybershopapp.R;
import com.svalero.cybershopapp.UpdateClientActivity;
import com.svalero.cybershopapp.database.AppDatabase;
import com.svalero.cybershopapp.domain.Client;

import java.util.List;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientHolder>
    implements ClientDeleteContract.View {
    public List<Client> clientList;
    public Context context;
    private View snackBarView;
    private ClientDeletePresenter presenter;

    ClientAdapter clientAdapter;
    public ClientAdapter(List<Client> clientList, Context context) {
        this.clientList = clientList;
        this.context = context;
        presenter = new ClientDeletePresenter(this);
    }

    public Context getContext() {
        return context;
    }

    @Override
    public ClientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.client_item, parent, false);
        return new ClientHolder(view);
    }

    @Override
    public void onBindViewHolder(ClientHolder holder, int position) {
        holder.clientName.setText(clientList.get(position).getName());
        holder.clientSurname.setText(clientList.get(position).getSurname());
        holder.clientNumber.setText(String.valueOf(clientList.get(position).getNumber()));
    }

    @Override
    public int getItemCount() {
        return clientList.size();
    }

    @Override
    public void showError(String errorMessage) {
        Snackbar.make(snackBarView, errorMessage, BaseTransientBottomBar.LENGTH_LONG).show();
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(snackBarView, message, BaseTransientBottomBar.LENGTH_LONG).show();
    }

    public class ClientHolder extends RecyclerView.ViewHolder{
        public TextView clientName;
        public TextView clientSurname;
        public TextView clientNumber;
        public View parentView;

        public Button detailsButton;
        public Button updateButton;
        public Button deleteButton;

        public ClientHolder(View view){
            super(view);
            parentView = view;
            snackBarView = parentView;

            clientName = view.findViewById(R.id.clientName);
            clientSurname = view.findViewById(R.id.clientSurname);
            clientNumber = view.findViewById(R.id.clientNumber);

            detailsButton = view.findViewById(R.id.detailsButton);
            updateButton = view.findViewById(R.id.updateButton);
            deleteButton = view.findViewById(R.id.deleteButton);


            detailsButton.setOnClickListener(v -> seeClient(getAdapterPosition()));
            updateButton.setOnClickListener(v -> updateClient(getAdapterPosition()));
            deleteButton.setOnClickListener(v -> deleteClient(getAdapterPosition()));

        }
    }

    public void seeClient(int position){
        Client client = clientList.get(position);
        Intent intent = new Intent(context, ClientDetailsView.class);
        intent.putExtra("name", client.getName());
        context.startActivity(intent);


    }
    public void updateClient(int position){
        Client client = clientList.get(position);
        Intent intent = new Intent(context, UpdateClientActivity.class);
        intent.putExtra("name", client.getName());
        intent.putExtra("position", position);
        context.startActivity(intent);
    }
    public void deleteClient(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.Are_you_sure_alert_dialog)
                .setTitle(R.string.delete_client)
                .setPositiveButton(R.string.yes, (dialog, i) -> {
                  Client client = clientList.get(position);
                  presenter.deleteClient(client.getId());
                  clientList.remove(position);
                  notifyItemRemoved(position);
                })
                        .setNegativeButton(R.string.no, (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();

    }



}
