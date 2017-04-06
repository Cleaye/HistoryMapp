package com.example.icalvin.historymapp;

/**
 * Enumerator for the time periods.
 */
public enum Period_Type {
    Paleolithicum("Paleolithicum"), Mesolithicum("Mesolithicum"), Neolithicum("Neolithicum"),
    Bronstijd("Bronstijd"), IJzertijd("IJzertijd"), Romeinse_tijd("Romeinse tijd"),
    Middeleeuwen_vroeg("Middeleeuwen vroeg"), Middeleeuwen_laat("Middeleeuwen laat"), Nieuwe_tijd("Nieuwe tijd");

    private final String text;

    /**
     * Constructor to make a new Period_Type.
     * @param text Name of the period in String format.
     */
    Period_Type(String text) {
        this.text = text;
    }

    /**
     * Gives the name of a period in String format.
     * @return Returns the name of a period.
     */
    @Override
    public String toString() {
        return text;
    }
}
