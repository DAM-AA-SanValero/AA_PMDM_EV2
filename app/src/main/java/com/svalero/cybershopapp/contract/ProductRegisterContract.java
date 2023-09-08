package com.svalero.cybershopapp.contract;

import com.svalero.cybershopapp.domain.Product;

public interface ProductRegisterContract {

    interface Model {
        interface OnRegisterProductListener {
            void onRegisterProductSuccess(Product product);

            void onRegisterProductError(String message);
        }

        void registerProduct(Product product, OnRegisterProductListener listener);
    }

    interface View {
        void showError(String errorMessage);

        void showMessage(String message);

        void resetForm();

    }

    interface Presenter {
        void registerProduct(Product product);
    }
}
