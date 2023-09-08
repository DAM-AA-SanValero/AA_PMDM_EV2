package com.svalero.cybershopapp.contract;

import com.svalero.cybershopapp.domain.Product;

public interface ProductDetailsContract {

    interface Model{

        interface OnLoadProductListener{
            void onLoadProductSuccess(Product product);
            void onLoadProductError(String message);
        }
        void loadProductById(long productId, OnLoadProductListener listener);
    }

    interface View {
        void showProductDetails(Product product);
        void showErrorMessage(String message);

    }

    interface Presenter {
        void loadProductById(long id);

        void getProductDetails(long productId);

    }
}
