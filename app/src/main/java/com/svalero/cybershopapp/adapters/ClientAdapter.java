package com.svalero.cybershopapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.svalero.cybershopapp.api.CybershopApi;
import com.svalero.cybershopapp.api.CybershopApiInterface;
import com.svalero.cybershopapp.contract.ClientDeleteContract;
import com.svalero.cybershopapp.contract.ClientUpdateContract;
import com.svalero.cybershopapp.presenter.ClientDeletePresenter;
import com.svalero.cybershopapp.presenter.ClientUpdatePresenter;
import com.svalero.cybershopapp.view.ClientDetailsView;
import com.svalero.cybershopapp.R;
import com.svalero.cybershopapp.view.ClientUpdateView;
import com.svalero.cybershopapp.domain.Client;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        Client currentClient = clientList.get(position);
        holder.clientName.setText(currentClient.getName());
        holder.clientSurname.setText(currentClient.getSurname());
        holder.clientNumber.setText(String.valueOf(currentClient.getNumber()));

        boolean isFavorite = currentClient.getFavourite() != null && currentClient.getFavourite();
        holder.favoriteImageView.setImageResource(isFavorite ? R.drawable.star : R.drawable.staroff);
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
        Toast.makeText(context, R.string.clientDeleted, Toast.LENGTH_LONG).show();

    }

    public class ClientHolder extends RecyclerView.ViewHolder{
        public TextView clientName;
        public TextView clientSurname;
        public TextView clientNumber;
        public View parentView;
        public ImageView favoriteImageView;
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
            favoriteImageView = view.findViewById(R.id.isFavourite);

            detailsButton.setOnClickListener(v -> seeClient(getAdapterPosition()));
            updateButton.setOnClickListener(v -> updateClient(getAdapterPosition()));
            deleteButton.setOnClickListener(v -> deleteClient(getAdapterPosition()));
            favoriteImageView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                Client client = clientList.get(position);
                Boolean currentFavoriteStatus = client.getFavourite();
                if (currentFavoriteStatus == null) {
                    currentFavoriteStatus = false;
                }
                client.setFavourite(!currentFavoriteStatus);
                notifyDataSetChanged();

                CybershopApiInterface cybershopApi = new CybershopApi().buildInstance();
                Call<Client> call = cybershopApi.updateClient(client.getId(), client);
                call.enqueue(new Callback<Client>() {
                    @Override
                    public void onResponse(Call<Client> call, Response<Client> response) {
                        if (response.isSuccessful()) {
                        } else {
                            client.setFavourite(!client.getFavourite());
                            notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onFailure(Call<Client> call, Throwable t) {
                        client.setFavourite(!client.getFavourite());
                        Snackbar.make(view, R.string.fail_trying_to_mark_favourite, Snackbar.LENGTH_SHORT).show();

                        notifyDataSetChanged();

                    }
                });
            });

        }
    }

    public void seeClient(int position){
        Client client = clientList.get(position);
        Intent intent = new Intent(context, ClientDetailsView.class);
        intent.putExtra("client_id", client.getId());
        context.startActivity(intent);


    }
    public void updateClient(int position){
        Client client = clientList.get(position);
        Intent intent = new Intent(context, ClientUpdateView.class);
        intent.putExtra("client_id", client.getId());
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
                }).setNegativeButton(R.string.no, (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
