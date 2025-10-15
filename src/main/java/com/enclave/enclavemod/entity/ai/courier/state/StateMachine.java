package com.enclave.enclavemod.entity.ai.courier.state;

public class StateMachine {
    private CourierState courierState;

    public StateMachine() {
        onInit();
    }

    public CourierState getCourierState() {
        return courierState;
    }

    private void onInit() {
        courierState = CourierState.IDLE;
    }

    public void finishIdleState() {
        courierState = CourierState.MOVE_TO_ROW;
    }

    public void finishMoveState() {
        courierState = CourierState.HARVEST;
    }

    public void finishHarvestState() {
        courierState = CourierState.DELIVER;
    }

    public void finishDeliverState() {
        courierState = CourierState.IDLE;
    }
}
