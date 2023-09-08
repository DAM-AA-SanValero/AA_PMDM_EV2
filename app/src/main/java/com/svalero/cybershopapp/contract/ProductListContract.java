package com.svalero.cybershopapp.contract;

import com.svalero.cybershopapp.domain.Product;

import java.util.List;

public interface ProductListContract {

    interface Model{
        interface OnLoadProductsListener {
            void onLoadProductsSuccess(List<Product> products);
            void onLoadProductsError(String message);
        }
        void loadAllProducts(OnLoadProductsListener listener);
    }

    interface View {
        void showProducts(List<Product> products);
        void showMessage(String message);
    }

    interface Presenter {
        void loadAllProducts();
    }

}
