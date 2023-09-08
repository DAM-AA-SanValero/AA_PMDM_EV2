package com.svalero.cybershopapp.contract;

import com.svalero.cybershopapp.domain.Repair;

public interface RepairRegisterContract {

    interface Model {
        interface OnRegisterRepairListener {
            void onRegisterRepairSuccess(Repair repair);

            void onRegisterRepairError(String message);
        }

        void registerRepair(Repair repair, OnRegisterRepairListener listener);
    }

    interface View {
        void showError(String errorMessage);

        void showMessage(String message);

        void resetForm();

    }

    interface Presenter {
        void registerRepair(Repair repair);
    }
}
