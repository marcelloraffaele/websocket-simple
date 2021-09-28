package it.rmarcello.ws;

public class TelemetryData {
    
    private int type;
    private String descr;
    private double value;
    
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getDescr() {
        return descr;
    }
    public void setDescr(String descr) {
        this.descr = descr;
    }
    public double getValue() {
        return value;
    }
    public void setValue(double value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return "TelemetryData [descr=" + descr + ", type=" + type + ", value=" + value + "]";
    }

    

}
