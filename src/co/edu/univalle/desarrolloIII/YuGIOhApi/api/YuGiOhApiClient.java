package co.edu.univalle.desarrolloIII.YuGIOhApi.api;

import co.edu.univalle.desarrolloIII.YuGIOhApi.model.Card;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class YuGiOhApiClient {

    private static final String API_URL = "https://db.ygoprodeck.com/api/v7/cardinfo.php";

    public List<Card> getRandomCards(int n) throws Exception {
        try {
            List<Card> cards = new ArrayList<>();
            Random random = new Random();

            while (cards.size() < n) {
                //Obtener una carta aleatoria
                String urlStr = API_URL + "?num=1&offset=" + random.nextInt(12000);
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                JSONObject root = new JSONObject(response.toString());
                JSONArray data = root.getJSONArray("data");
                JSONObject obj = data.getJSONObject(0);

                String type = obj.optString("type", "");

                // Filtramos solo Monster
                if (!type.toLowerCase().contains("monster")) {
                    continue; // Volver a pedir si no es Monster
                }

                String name = obj.optString("name", "Desconocida");
                int atk = obj.optInt("atk", 0);
                int def = obj.optInt("def", 0);

                String imageUrl = "";
                if (obj.has("card_images")) {
                    JSONArray imgs = obj.getJSONArray("card_images");
                    if (imgs.length() > 0) {
                        imageUrl = imgs.getJSONObject(0).optString("image_url", "");
                    }
                }

                cards.add(new Card(name, atk, def, imageUrl));
            }

            return cards;
        } catch (IOException e) {
            throw new IOException("No se pudo conectar a la API de Yu-Gi-Oh!. Verifica tu conexión a Internet.");
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al obtener las cartas: " + e.getMessage());
        }
    }
}
