package com.svalero.cybershopapp.presenter;

import com.svalero.cybershopapp.contract.RepairListContract;
import com.svalero.cybershopapp.domain.Repair;
import com.svalero.cybershopapp.model.RepairListModel;

import java.util.List;

public class RepairListPresenter implements RepairListContract.Presenter
        , RepairListContract.Model.OnLoadRepairsListener  {

    private RepairListModel model;
    private RepairListContract.View view;

    public RepairListPresenter(RepairListContract.View view) {
        this.view = view;
        this.model = new RepairListModel();

    }

    @Override
    public void loadAllRepairs() {
        model.loadAllRepairs(this);
    }

    @Override
    public void onLoadRepairsSuccess(List<Repair> repairs) {
        view.showRepairs(repairs);
    }

    @Override
    public void onLoadRepairsError(String message) {
        view.showMessage(message);
    }
}
