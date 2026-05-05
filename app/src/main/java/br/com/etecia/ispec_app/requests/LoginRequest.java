package br.com.etecia.ispec_app.requests;

import br.com.etecia.ispec_app.interfaces.SerializedName;

public class LoginRequest {


    private String email;


    private String senha;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
