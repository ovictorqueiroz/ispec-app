package br.com.etecia.ispec_app;

public enum TipoEquipamento {
    EXTINTOR("Extintor"),
    ALARME("Alarme"),
    HIDRANTE("Hidrante");

    private final String descEquip;

    TipoEquipamento(String descEquip) {
        this.descEquip = descEquip;
    }

    public String getDescricao() {
        return descEquip;
    }

    @Override
    public String toString() {
        return descEquip;
    }
}

