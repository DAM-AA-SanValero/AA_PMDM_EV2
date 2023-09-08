package com.svalero.cybershopapp.contract;
public interface RepairDeleteContract {

    interface Model {
        interface OnDeleteRepairListener {
            void onDeleteRepairSuccess();

            void onDeleteRepairError(String message);
        }

        void deleteRepair(long repairId, OnDeleteRepairListener listener);
    }

    interface View {
        void showError(String errorMessage);

        void showMessage(String message);
    }

    interface Presenter {
        void deleteRepair(long repairId);
    }
}
