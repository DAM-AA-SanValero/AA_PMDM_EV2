package com.svalero.cybershopapp.contract;
public interface ProductDeleteContract {

    interface Model {
        interface OnDeleteProductListener {
            void onDeleteProductSuccess();

            void onDeleteProductError(String message);
        }

        void deleteProduct(long productId, OnDeleteProductListener listener);
    }

    interface View {
        void showError(String errorMessage);

        void showMessage(String message);
    }

    interface Presenter {
        void deleteProduct(long productId);
    }
}
