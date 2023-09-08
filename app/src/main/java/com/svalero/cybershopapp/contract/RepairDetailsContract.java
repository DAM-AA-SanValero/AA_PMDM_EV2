package com.svalero.cybershopapp.contract;

import com.svalero.cybershopapp.domain.Repair;

public interface RepairDetailsContract {

    interface Model{

        interface OnLoadRepairListener{
            void onLoadRepairSuccess(Repair repair);
            void onLoadRepairError(String message);
        }
        void loadRepairById(long repairId, OnLoadRepairListener listener);
    }

    interface View {
        void showRepairDetails(Repair repair);
        void showMessage(String message);

    }

    interface Presenter {
        void loadRepairById(long id);

    }
}
