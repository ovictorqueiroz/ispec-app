package br.com.etecia.ispec_app;

public class MenuPrincipalModel {

    String tituloMenu;
    int iconeMenu;

    public MenuPrincipalModel(String tituloMenu, int iconeMenu) {
        this.tituloMenu = tituloMenu;
        this.iconeMenu = iconeMenu;
    }

    public String getTituloMenu() {
        return tituloMenu;
    }

    public int getIconeMenu() {
        return iconeMenu;
    }
}
