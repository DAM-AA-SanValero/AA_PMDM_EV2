package com.svalero.cybershopapp.presenter;

import com.svalero.cybershopapp.adapters.ClientAdapter;
import com.svalero.cybershopapp.adapters.ProductAdapter;
import com.svalero.cybershopapp.contract.ClientDeleteContract;
import com.svalero.cybershopapp.contract.ProductDeleteContract;
import com.svalero.cybershopapp.model.ClientDeleteModel;
import com.svalero.cybershopapp.model.ProductDeleteModel;

public class ProductDeletePresenter implements ProductDeleteContract.Presenter,
    ProductDeleteContract.Model.OnDeleteProductListener{

    private ProductDeleteModel model;
    private ProductAdapter view;

    public ProductDeletePresenter(ProductAdapter view) {
        model = new ProductDeleteModel();
        this.view = view;
    }

    @Override
    public void deleteProduct(long productId) {
        model.deleteProduct(productId, this);
    }

    @Override
    public void onDeleteProductSuccess() {
        view.showMessage("El producto se ha eliminado correctamente");
    }

    @Override
    public void onDeleteProductError(String message) {
        view.showError("Error al eliminar el producto");
    }
}
