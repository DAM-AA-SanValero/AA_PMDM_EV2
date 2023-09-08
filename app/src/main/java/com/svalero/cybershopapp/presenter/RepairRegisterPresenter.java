package com.svalero.cybershopapp.presenter;

import com.svalero.cybershopapp.R;
import com.svalero.cybershopapp.contract.RepairRegisterContract;
import com.svalero.cybershopapp.domain.Repair;
import com.svalero.cybershopapp.model.RepairRegisterModel;
import com.svalero.cybershopapp.view.RepairRegisterView;

public class RepairRegisterPresenter implements RepairRegisterContract.Presenter,
    RepairRegisterContract.Model.OnRegisterRepairListener{

    private RepairRegisterModel model;
    private RepairRegisterView view;

    public RepairRegisterPresenter(RepairRegisterView view) {
        model = new RepairRegisterModel();
        this.view = view;
    }

    @Override
    public void registerRepair(Repair repair) {
        model.registerRepair(repair, this);
    }

    @Override
    public void onRegisterRepairSuccess(Repair repair) {
        view.showMessage(repair.getId() + "se ha registrado correctamente");
        view.resetForm();
    }

    @Override
    public void onRegisterRepairError(String message) {
        view.showError(String.valueOf(R.string.error_registering));
    }
}
