package com.svalero.cybershopapp.presenter;

import com.svalero.cybershopapp.contract.ClientUpdateContract;
import com.svalero.cybershopapp.contract.RepairUpdateContract;
import com.svalero.cybershopapp.domain.Client;
import com.svalero.cybershopapp.domain.Repair;
import com.svalero.cybershopapp.model.ClientUpdateModel;
import com.svalero.cybershopapp.model.RepairUpdateModel;
import com.svalero.cybershopapp.view.ClientUpdateView;
import com.svalero.cybershopapp.view.RepairUpdateView;

public class RepairUpdatePresenter implements RepairUpdateContract.Presenter,
    RepairUpdateContract.Model.OnUpdateRepairListener{

    private RepairUpdateModel model;
    private RepairUpdateView view;

    public RepairUpdatePresenter(RepairUpdateView view) {
        model = new RepairUpdateModel();
        this.view = view;
    }
    @Override
    public void updateRepair(long id, Repair repair) {
        model.updateRepair(id, repair, this);
    }

    @Override
    public void onUpdateRepairSuccess(Repair repair) {
        view.showRepairUpdatedMessage(repair);
    }

    @Override
    public void onUpdateRepairError(String errorMessage) {
        view.showError(errorMessage);
    }
}

