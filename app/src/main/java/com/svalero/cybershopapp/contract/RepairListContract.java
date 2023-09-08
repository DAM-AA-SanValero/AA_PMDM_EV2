package com.svalero.cybershopapp.contract;

import com.svalero.cybershopapp.domain.Repair;

import java.util.List;

public interface RepairListContract {

    interface Model{
        interface OnLoadRepairsListener{
            void onLoadRepairsSuccess(List<Repair> repairs);
            void onLoadRepairsError(String message);
        }
        void loadAllRepairs(OnLoadRepairsListener listener);
    }

    interface View {
        void showRepairs(List<Repair> repairs);
        void showMessage(String message);
    }

    interface Presenter {
        void loadAllRepairs();
    }

}
