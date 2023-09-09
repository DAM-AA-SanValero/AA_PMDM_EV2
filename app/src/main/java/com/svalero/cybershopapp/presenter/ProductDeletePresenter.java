package com.svalero.cybershopapp.presenter;

import android.content.Context;

import com.svalero.cybershopapp.R;
import com.svalero.cybershopapp.adapters.ClientAdapter;
import com.svalero.cybershopapp.adapters.ProductAdapter;
import com.svalero.cybershopapp.contract.ClientDeleteContract;
import com.svalero.cybershopapp.contract.ProductDeleteContract;
import com.svalero.cybershopapp.model.ClientDeleteModel;
import com.svalero.cybershopapp.model.ProductDeleteModel;

public class ProductDeletePresenter implements ProductDeleteContract.Presenter,
    ProductDeleteContract.Model.OnDeleteProductListener{

    Context context;
    private ProductDeleteModel model;
    private ProductAdapter view;

    public ProductDeletePresenter(ProductAdapter view) {
        model = new ProductDeleteModel();
        this.view = view;
        this.context = view.context;
    }

    @Override
    public void deleteProduct(long productId) {
        model.deleteProduct(productId, this);
    }

    @Override
    public void onDeleteProductSuccess() {
        view.showMessage(context.getString(R.string.product_deleted));
    }

    @Override
    public void onDeleteProductError(String message) {
        view.showError(context.getString(R.string.error_deleting_product));
    }
}
