package org.software.state;

/**
 * 已取消状态
 */
public class CancelledState extends PaymentOrderState {

    public CancelledState(PaymentOrder order) {
        super(order);
    }

    @Override
    public boolean pay(String method) {
        System.out.println("订单已取消，不能支付。");
        return false;
    }

    @Override
    public boolean cancel(String reason) {
        System.out.println("订单已取消，无需重复操作。");
        return false;
    }

    @Override
    public boolean refund(String reason) {
        System.out.println("订单已取消，未支付，无需退款。");
        return false;
    }

    @Override
    public boolean expire() {
        System.out.println("订单已取消，不能再过期。");
        return false;
    }
}
