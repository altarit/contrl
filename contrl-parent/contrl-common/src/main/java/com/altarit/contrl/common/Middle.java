package com.altarit.contrl.common;

public class Middle extends Parent {
    private int a = 2;
    protected int b = 12;
    public int c = 12;

    @Override
    public void printParent() {
        super.printParent();
        System.out.println("a=" + a + " b=" + b + " c=" + c);
    }
}
