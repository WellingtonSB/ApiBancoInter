package br.com.fwtj.smartnfe.util.inter;

import br.com.fwtj.smartnfe.deltaspike.startup.IdiomaStartup;
import com.google.gson.*;
import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import feign.gson.GsonDecoder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InterCustonDecoder implements Decoder {

    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {

        class LocalDateSerializer implements JsonSerializer<LocalDate> {
            @Override
            public JsonElement serialize(LocalDate localDate, Type srcType, JsonSerializationContext context) {
                return new JsonPrimitive(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(localDate));
            }
        }
        class LocalDateDeserializer implements JsonDeserializer<LocalDate> {
            @Override
            public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return LocalDate.parse(json.getAsString(), DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(IdiomaStartup.ptBr));
            }
        }
        class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {
            @Override
            public JsonElement serialize(LocalDateTime localDateTime, Type srcType, JsonSerializationContext context) {
                return new JsonPrimitive(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(localDateTime));
            }
        }
        class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {
            @Override
            public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withLocale(IdiomaStartup.ptBr));
            }
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        gsonBuilder.setDateFormat("dd/MM/yyyy");
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        GsonDecoder gsonDecoder = new GsonDecoder(gson);
        Object decode = gsonDecoder.decode(response, type);
        //System.out.println("decode : " + decode);
        return decode;
    }

}
