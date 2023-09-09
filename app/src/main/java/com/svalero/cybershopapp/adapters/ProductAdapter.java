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

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.svalero.cybershopapp.contract.ProductDeleteContract;
import com.svalero.cybershopapp.presenter.ProductDeletePresenter;
import com.svalero.cybershopapp.view.ProductDetailsView;
import com.svalero.cybershopapp.R;
import com.svalero.cybershopapp.view.ProductUpdateView;
import com.svalero.cybershopapp.domain.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder>
implements ProductDeleteContract.View {

    public List<Product> productList;
    public Context context;
    private View snackBarView;

    private ProductDeletePresenter presenter;
    ProductAdapter productAdapter;
    public ProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
        presenter = new ProductDeletePresenter(this);
    }

    @Override
    public ProductAdapter.ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);
        return new ProductAdapter.ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductAdapter.ProductHolder holder, int position) {
        holder.productName.setText(productList.get(position).getName());
        holder.productType.setText(productList.get(position).getType());

        if(productList.get(position).isInStock()){
            holder.productStock.setText(R.string.yes_it_is);
        } else {
            holder.productStock.setText(R.string.no_it_isnt);
        }


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public void showError(String errorMessage) {
        Snackbar.make(snackBarView, errorMessage, BaseTransientBottomBar.LENGTH_LONG).show();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(context, R.string.product_deleted, Toast.LENGTH_LONG).show();
    }

    public class ProductHolder extends RecyclerView.ViewHolder{
        public TextView productName;
        public TextView productType;
        public TextView productStock;
        public View parentView;

        public Button detailsButton;
        public Button updateButton;
        public Button deleteButton;

        public ProductHolder(View view){
            super(view);
            parentView = view;
            snackBarView = parentView;

            productName = view.findViewById(R.id.productName);
            productType = view.findViewById(R.id.productType);
            productStock = view.findViewById(R.id.productInStock);

            detailsButton = view.findViewById(R.id.detailsButton);
            updateButton = view.findViewById(R.id.updateButton);
            deleteButton = view.findViewById(R.id.deleteButton);


            detailsButton.setOnClickListener(v -> seeProduct(getAdapterPosition()));
            updateButton.setOnClickListener(v -> updateProduct(getAdapterPosition()));
            deleteButton.setOnClickListener(v -> deleteProduct(getAdapterPosition()));

        }
    }

    public void seeProduct(int position){
        Product product = productList.get(position);
        Intent intent = new Intent(context, ProductDetailsView.class);
        intent.putExtra("product_id", product.getId());
        context.startActivity(intent);


    }
    public void updateProduct(int position){
        Product product = productList.get(position);
        Intent intent = new Intent(context, ProductUpdateView.class);
        intent.putExtra("product_id", product.getId());
       // intent.putExtra("position", position);
        context.startActivity(intent);
    }
    public void deleteProduct(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.Are_you_sure_alert_dialog)
                .setTitle(R.string.delete_client)
                .setPositiveButton(R.string.yes, (dialog, i) -> {
                    Product product = productList.get(position);
                    presenter.deleteProduct(product.getId());
                    productList.remove(position);
                    notifyItemRemoved(position);
                })
                .setNegativeButton(R.string.no, (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
