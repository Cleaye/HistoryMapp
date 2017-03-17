package com.example.icalvin.historymapp;

public enum Period_Type {
    Paleolithicum("Paleolithicum"), Mesolithicum("Mesolithicum"), Neolithicum("Neolithicum"),
    Bronstijd("Bronstijd"), IJzertijd("IJzertijd"), Romeinse_tijd("Romeinse tijd"),
    Middeleeuwen_vroeg("Middeleeuwen vroeg"), Middeleeuwen_laat("Middeleeuwen laat"), Nieuwe_tijd("Nieuwe tijd");

    private final String text;

    private Period_Type(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
