package ar.edu.itba.pod.constants;

public enum Months {

    JAN(1, "January", 31),
    FEB(2, "February", 28),
    MAR(3, "March", 31),
    APR(4, "April", 30),
    MAY(5, "May", 31),
    JUN(6, "June", 30),
    JUL(7, "July", 31),
    AUG(8, "August", 31),
    SEP(9, "September", 30),
    OCT(10, "October", 31),
    NOV(11, "November", 30),
    DEC(12, "December", 31);

    public int monthNum;

    public String monthName;

    public int daysQty;

    Months(int monthNum, String monthName, int daysQty) {
        this.monthNum = monthNum;
        this.monthName = monthName;
        this.daysQty = daysQty;
    }
}
