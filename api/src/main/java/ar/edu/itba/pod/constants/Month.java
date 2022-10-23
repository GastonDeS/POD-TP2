package ar.edu.itba.pod.constants;

import java.rmi.RemoteException;

public enum Month {

    JAN("01", "January", 31),
    FEB("02", "February", 28),
    MAR("03", "March", 31),
    APR("04", "April", 30),
    MAY("05", "May", 31),
    JUN("06", "June", 30),
    JUL("07", "July", 31),
    AUG("08", "August", 31),
    SEP("09", "September", 30),
    OCT("10", "October", 31),
    NOV("11", "November", 30),
    DEC("12", "December", 31);

    public String monthNum;

    public String monthName;

    public int daysQty;

    Month(String monthNum, String monthName, int daysQty) {
        this.monthNum = monthNum;
        this.monthName = monthName;
        this.daysQty = daysQty;
    }

    public static Month getMonthByName(String monthName) throws RemoteException {
        switch (monthName) {
            case "January":
                return Month.JAN;
            case "February":
                return Month.FEB;
            case "March":
                return Month.MAR;
            case "April":
                return Month.APR;
            case "May":
                return Month.MAY;
            case "June":
                return Month.JUN;
            case "July":
                return Month.JUL;
            case "August":
                return Month.AUG;
            case "September":
                return Month.SEP;
            case "October":
                return Month.OCT;
            case "November":
                return Month.NOV;
            case "December":
                return Month.DEC;
            default:
                throw new RemoteException("Invalid month");
        }

    }
}
