package com.enclave.enclavemod.entity.ai.courier.state;

public class StateMachine {
    private CourierState courierState = CourierState.IDLE;

    public CourierState getState() {
        return courierState;
    }

    public void setState(CourierState state) {
        courierState = state;
    }
}
