import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;


public class Main {

    public static void main(String[] args) {

        try {
            JSONObject j = getPBH("HOSPITAIS");

            JSONArray ja = j.getJSONArray("features");

            JSONArray hosp = new JSONArray();

            for (int i = 0; i < ja.length(); i++) {

                JSONObject jo = ja.getJSONObject(i);
                JSONObject properties = jo.getJSONObject("properties");
                JSONObject geometry = jo.getJSONObject("geometry");
                JSONArray coordinates = geometry.getJSONArray("coordinates");
                double latitude = coordinates.getDouble(0);
                double longitude = coordinates.getDouble(1);

                JSONObject h = new JSONObject();

                h.put("category", properties.getString("CATEGORIA"));
                h.put("sigla", properties.getString("SIGLA_CATEGORIA"));
                h.put("name", properties.getString("NOME"));
                h.put("id", properties.getInt("ID_EQ_SAUDE"));
                h.put("typeAddress", properties.getString("TIPO_LOGRADOURO"));
                h.put("address", properties.getString("LOGRADOURO"));
                h.put("number", properties.getInt("NUMERO_IMOVEL"));
//                h.put("phone", properties.getString("TELEFONE"));
                h.put("latitude", latitude);
                h.put("longitude", longitude);

                hosp.put(h);

            }

            System.out.println(hosp);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    public static JSONObject getPBH(String id) throws Exception {

        String url = "https://geoservicos.pbh.gov.br/geoserver/wfs?service=WFS&version=1.0.0&request=GetFeature&typeName=ide_bhgeo:" + id +
                "&srsName=EPSG:4326&outputFormat=application%2Fjson";

        JSONObject j = requestGet(url);

        return j;

    }

    public static JSONObject requestGet(String endPoint) throws Exception {

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(endPoint);
        get.setHeader("Content-Type", "application/json");

        HttpResponse response = httpClient.execute(get);

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuilder result = new StringBuilder();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        JSONObject j = new JSONObject(result.toString());
        return j;

    }

}
