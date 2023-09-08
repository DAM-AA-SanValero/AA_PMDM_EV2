package com.svalero.cybershopapp.presenter;


import com.svalero.cybershopapp.contract.ProductUpdateContract;
import com.svalero.cybershopapp.domain.Product;
import com.svalero.cybershopapp.model.ProductUpdateModel;
import com.svalero.cybershopapp.view.ProductUpdateView;

public class ProductUpdatePresenter implements ProductUpdateContract.Presenter,
    ProductUpdateContract.Model.OnUpdateProductListener{

    private ProductUpdateModel model;
    private ProductUpdateContract.View view;

    public ProductUpdatePresenter(ProductUpdateContract.View view) {
        model = new ProductUpdateModel();
        this.view = view;
    }
    @Override
    public void updateProduct(long id, Product product) {
        model.updateProduct(id, product, this);
    }

    @Override
    public void onUpdateProductSuccess(Product product) {
        view.showProductUpdatedMessage(product);
    }

    @Override
    public void onUpdateProductError(String errorMessage) {
        view.showError(errorMessage);
    }
}

