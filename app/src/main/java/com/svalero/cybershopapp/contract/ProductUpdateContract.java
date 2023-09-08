package com.svalero.cybershopapp.contract;

import com.svalero.cybershopapp.domain.Product;

public interface ProductUpdateContract {

    interface Model {
        interface OnUpdateProductListener {
            void onUpdateProductSuccess(Product product);

            void onUpdateProductError(String errorMessage);
        }

        void updateProduct(long productId, Product product , OnUpdateProductListener listener);
    }

    interface View {
        void showError(String errorMessage);

        void showSuccessMessage(String message);

        void showProductUpdatedMessage(Product product);
    }

    interface Presenter {
        void updateProduct(long id, Product product);
    }
}
