package com.svalero.cybershopapp.presenter;

import com.svalero.cybershopapp.contract.RepairDetailsContract;
import com.svalero.cybershopapp.domain.Repair;
import com.svalero.cybershopapp.model.RepairDetailsModel;
import com.svalero.cybershopapp.view.RepairDetailsView;

public class RepairDetailsPresenter implements RepairDetailsContract.Presenter,
    RepairDetailsContract.Model.OnLoadRepairListener{

    private RepairDetailsModel model;
    private RepairDetailsContract.View view;

    public RepairDetailsPresenter(RepairDetailsContract.View view) {
        this.view = view;
        model = new RepairDetailsModel();
    }

    @Override
    public void loadRepairById(long repairId) {
        model.loadRepairById(repairId, this);
    }

    @Override
    public void getRepairDetails(long repairId) {
        loadRepairById(repairId);
    }

    @Override
    public void onLoadRepairSuccess(Repair repair) {
        view.showRepairDetails(repair);
    }

    @Override
    public void onLoadRepairError(String message) {
        view.showMessage(message);
    }
}
