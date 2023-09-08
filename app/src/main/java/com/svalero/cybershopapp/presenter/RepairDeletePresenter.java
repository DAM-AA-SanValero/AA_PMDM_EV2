package com.svalero.cybershopapp.presenter;

import com.svalero.cybershopapp.adapters.RepairAdapter;
import com.svalero.cybershopapp.contract.RepairDeleteContract;
import com.svalero.cybershopapp.model.RepairDeleteModel;

public class RepairDeletePresenter implements RepairDeleteContract.Presenter,
    RepairDeleteContract.Model.OnDeleteRepairListener{

    private RepairDeleteModel model;
    private RepairAdapter view;

    public RepairDeletePresenter(RepairAdapter view) {
        model = new RepairDeleteModel();
        this.view = view;
    }

    @Override
    public void deleteRepair(long repairId) {
        model.deleteRepair(repairId, this);
    }

    @Override
    public void onDeleteRepairSuccess() {
        view.showMessage("La reparación se ha eliminado correctamente");
    }

    @Override
    public void onDeleteRepairError(String message) {
        view.showError("Error al eliminar la reparación");
    }
}
