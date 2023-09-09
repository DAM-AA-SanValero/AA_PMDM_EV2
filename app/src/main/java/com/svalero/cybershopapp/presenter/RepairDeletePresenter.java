package com.svalero.cybershopapp.presenter;

import android.content.Context;

import com.svalero.cybershopapp.R;
import com.svalero.cybershopapp.adapters.RepairAdapter;
import com.svalero.cybershopapp.contract.RepairDeleteContract;
import com.svalero.cybershopapp.model.RepairDeleteModel;

public class RepairDeletePresenter implements RepairDeleteContract.Presenter,
    RepairDeleteContract.Model.OnDeleteRepairListener{

    Context context;
    private RepairDeleteModel model;
    private RepairAdapter view;

    public RepairDeletePresenter(RepairAdapter view) {
        model = new RepairDeleteModel();
        this.view = view;
        this.context = view.context;
    }

    @Override
    public void deleteRepair(long repairId) {
        model.deleteRepair(repairId, this);
    }

    @Override
    public void onDeleteRepairSuccess() {
        view.showMessage(context.getString(R.string.repair_deleted));
    }

    @Override
    public void onDeleteRepairError(String message) {
        view.showError(context.getString(R.string.error_deleting_repair));
    }
}
