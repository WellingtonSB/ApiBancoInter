package br.com.fwtj.smartnfe.util.inter;

import br.com.fwtj.smartnfe.deltaspike.startup.IdiomaStartup;
import com.google.gson.*;
import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.gson.GsonEncoder;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InterCustonEncoder implements Encoder {

    @Override
    public void encode(Object o, Type type, RequestTemplate requestTemplate) throws EncodeException {

        class LocalDateSerializer implements JsonSerializer<LocalDate> {
            @Override
            public JsonElement serialize(LocalDate localDate, Type srcType, JsonSerializationContext context) {
                return new JsonPrimitive(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(localDate));
            }
        }
        class LocalDateDeserializer implements JsonDeserializer<LocalDate> {
            @Override
            public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return LocalDate.parse(json.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(IdiomaStartup.ptBr));
            }
        }
        class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {
            @Override
            public JsonElement serialize(LocalDateTime localDateTime, Type srcType, JsonSerializationContext context) {
                return new JsonPrimitive(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime));
            }
        }
        class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {
            @Override
            public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withLocale(IdiomaStartup.ptBr));
            }
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        gsonBuilder.setDateFormat("dd/MM/yyyy");
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        //JsonElement jsonElement = gson.toJsonTree(o);
        //System.out.println("encode : " + jsonElement);
        GsonEncoder gsonEncoder = new GsonEncoder(gson);
        gsonEncoder.encode(o, type, requestTemplate);
    }
}
