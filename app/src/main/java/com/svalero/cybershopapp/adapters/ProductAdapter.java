package com.svalero.cybershopapp.adapters;

import static com.svalero.cybershopapp.database.Constants.DATABASE_PRODUCTS;
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

import com.svalero.cybershopapp.ProductDetailsActivity;
import com.svalero.cybershopapp.R;
import com.svalero.cybershopapp.UpdateProductActivity;
import com.svalero.cybershopapp.database.AppDatabase;
import com.svalero.cybershopapp.domain.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {

    public List<Product> productList;
    public Context context;
    ProductAdapter productAdapter;
    public ProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
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
        Intent intent = new Intent(context, ProductDetailsActivity.class);
        intent.putExtra("name", product.getName());
        context.startActivity(intent);


    }
    public void updateProduct(int position){
        Product product = productList.get(position);
        Intent intent = new Intent(context, UpdateProductActivity.class);
        intent.putExtra("name", product.getName());
        intent.putExtra("position", position);
        context.startActivity(intent);
    }
    public void deleteProduct(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.Are_you_sure_alert_dialog)
                .setTitle(R.string.delete_client)
                .setPositiveButton(R.string.yes, (dialog, i) -> {
                    final AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, DATABASE_PRODUCTS)
                            .allowMainThreadQueries().build();
                    Product product = productList.get(position);
                    db.productDao().delete(product);

                    productList.remove(position);
                    notifyItemRemoved(position);
                })
                .setNegativeButton(R.string.no, (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();

    }

}
