package com.svalero.cybershopapp.presenter;

import com.svalero.cybershopapp.contract.ClientDetailsContract;
import com.svalero.cybershopapp.contract.ProductDetailsContract;
import com.svalero.cybershopapp.domain.Client;
import com.svalero.cybershopapp.domain.Product;
import com.svalero.cybershopapp.model.ClientDetailsModel;
import com.svalero.cybershopapp.model.ProductDetailsModel;
import com.svalero.cybershopapp.view.ClientDetailsView;
import com.svalero.cybershopapp.view.ProductDetailsView;

public class ProductDetailsPresenter implements ProductDetailsContract.Presenter,
    ProductDetailsContract.Model.OnLoadProductListener{

    private ProductDetailsModel model;
    private ProductDetailsContract.View view;

    public ProductDetailsPresenter(ProductDetailsContract.View view) {
        this.view = view;
        model = new ProductDetailsModel();
    }

    @Override
    public void loadProductById(long productId) {
        model.loadProductById(productId, this);
    }

    @Override
    public void getProductDetails(long productId) {
        loadProductById(productId);
    }

    @Override
    public void onLoadProductSuccess(Product product) {
        view.showProductDetails(product);
    }

    @Override
    public void onLoadProductError(String message) {
        view.showErrorMessage(message);
    }
}
