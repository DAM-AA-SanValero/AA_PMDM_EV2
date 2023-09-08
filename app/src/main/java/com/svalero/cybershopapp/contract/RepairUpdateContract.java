package com.svalero.cybershopapp.contract;

import com.svalero.cybershopapp.domain.Repair;

public interface RepairUpdateContract {

    interface Model {
        interface OnUpdateRepairListener {
            void onUpdateRepairSuccess(Repair repair);

            void onUpdateRepairError(String errorMessage);
        }

        void updateRepair(long repairId, Repair repair , OnUpdateRepairListener listener);
    }

    interface View {
        void showError(String errorMessage);

        void showSuccessMessage(String message);

        void showRepairUpdatedMessage(Repair repair);
    }

    interface Presenter {
        void updateRepair(long id, Repair repair);
    }
}
