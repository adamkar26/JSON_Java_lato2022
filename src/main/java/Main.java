import javax.json.*;
import javax.json.bind.JsonbBuilder;
import javax.json.stream.JsonGenerator;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import javax.json.bind.Jsonb;

public class Main {

    private static Jsonb jsonb = JsonbBuilder.create();

    public static void main(String[] args) {

        JsonObject object = buildObject();
        // konfiguracja do ładnego zapisu
        Map<String, ?> config = Collections.singletonMap(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(config);
        writerFactory.createWriter(System.out).write(object);
        readJsonObject(object.toString());

        String newspapperJson = objectWriting(new Newspaper("Gazetka szkolna", 200, LocalDate.now()));
        System.out.println(newspapperJson);
        objectReading(newspapperJson);


    }

    private static String objectWriting(Newspaper newspaper){
        String newspaperJson = jsonb.toJson(newspaper);
        return  newspaperJson;
    }

    private static void objectReading(String json){
        Newspaper n = jsonb.fromJson(json, Newspaper.class);
        System.out.println(n.toString());
    }

    private static void readJsonObject(String jsonDocument){
        JsonReaderFactory jsonReaderFactory = Json.createReaderFactory(Collections.emptyMap());
        try(JsonReader jsonReader = jsonReaderFactory.createReader(new ByteArrayInputStream(jsonDocument.getBytes()))){
            JsonStructure jsonStructure = jsonReader.read();
            // chce wydrukowac pierwszy artykul
            System.out.println(jsonStructure.getValue("/strona/artykuly/0"));
        }

        // druga metoda
        try(JsonReader jsonReader = jsonReaderFactory.createReader(new ByteArrayInputStream(jsonDocument.getBytes()))){
            JsonObject jsonObject = jsonReader.readObject();
            System.out.println(
                    jsonObject.getJsonObject("strona")
                            .getJsonArray("artykuly")
                            .get(0)
            );
        }

    }

    private static JsonObject buildObject(){
        JsonBuilderFactory builderFactory = Json.createBuilderFactory(Collections.emptyMap());

        JsonObject date1 = builderFactory.createObjectBuilder()
                .add("rok", 2020)
                .add("miesiac", 4)
                .add("dzien", 23).build();

        JsonObject article1 = builderFactory.createObjectBuilder()
                .add("tytul", "Instrukcja obsługi kota")
                .add("data", date1).build();

        JsonObject date2 = builderFactory.createObjectBuilder()
                .add("rok", 2020)
                .add("miesiac", 5)
                .add("dzien", 23).build();

        JsonObject article2 = builderFactory.createObjectBuilder()
                .add("tytul", "Instrukcja obsługi psa")
                .add("data", date2).build();

        JsonArray articleArray = builderFactory.createArrayBuilder()
                .add(article1).add(article2).build();

        JsonObject page = builderFactory.createObjectBuilder()
                .add("adres", "example.com")
                .add("artykuly", articleArray)
                .build();

        return builderFactory.createObjectBuilder()
                .add("imie", "Jan")
                .add("nazwisko", "Kowalski")
                .add("strona", page).build();
    }
}

