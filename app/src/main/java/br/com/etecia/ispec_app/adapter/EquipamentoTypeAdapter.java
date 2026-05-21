package br.com.etecia.ispec_app.adapter;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import br.com.etecia.ispec_app.model.AlarmeModel;
import br.com.etecia.ispec_app.model.EquipamentoModel;
import br.com.etecia.ispec_app.model.ExtintorModel;
import br.com.etecia.ispec_app.model.HidranteModel;

public class EquipamentoTypeAdapter implements JsonDeserializer<EquipamentoModel> {

    private final Gson gson = new Gson();

    @Override
    public EquipamentoModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        JsonElement tipoElement = jsonObject.get("tipo");
        if (tipoElement == null) {
            throw new JsonParseException("Campo 'tipo' não encontrado no JSON do equipamento");
        }

        String tipo = tipoElement.getAsString().toLowerCase().trim();

        switch (tipo) {
            case "extintor":
                return gson.fromJson(jsonObject, ExtintorModel.class);
            case "alarme":
                return gson.fromJson(jsonObject, AlarmeModel.class);
            case "hidrante":
                return gson.fromJson(jsonObject, HidranteModel.class);
            default:
                throw new JsonParseException("Tipo de equipamento desconhecido: " + tipoElement.getAsString());
        }
    }
}
