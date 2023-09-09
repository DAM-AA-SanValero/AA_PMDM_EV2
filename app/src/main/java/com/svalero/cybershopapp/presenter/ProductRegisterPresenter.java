package com.svalero.cybershopapp.presenter;

import com.svalero.cybershopapp.R;
import com.svalero.cybershopapp.contract.ProductRegisterContract;
import com.svalero.cybershopapp.domain.Product;
import com.svalero.cybershopapp.model.ProductRegisterModel;
import com.svalero.cybershopapp.view.ProductRegisterView;

public class ProductRegisterPresenter implements ProductRegisterContract.Presenter,
    ProductRegisterContract.Model.OnRegisterProductListener{

    private ProductRegisterModel model;
    private ProductRegisterView view;

    public ProductRegisterPresenter(ProductRegisterView view) {
        model = new ProductRegisterModel();
        this.view = view;
    }

    @Override
    public void registerProduct(Product product) {
        model.registerProduct(product, this);
    }

    @Override
    public void onRegisterProductSuccess(Product product) {
        view.showMessage(product.getId() + "registered");
        view.resetForm();
    }

    @Override
    public void onRegisterProductError(String message) {
        view.showError(String.valueOf(R.string.error_registering));
    }
}
