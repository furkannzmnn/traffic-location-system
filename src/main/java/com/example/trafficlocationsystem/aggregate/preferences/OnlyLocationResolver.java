package com.example.trafficlocationsystem.aggregate.preferences;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

public class OnlyLocationResolver<T> extends AbstractAggregatePreferences<T> {

    private static final String JSON_PREFIX = "\n";
    private static final String GEO_URL = "http://ip-api.com/line";

    @Override
    public String onlyPreferences(T request, Map<String, String> totalData) {
        HttpClient httpClient = HttpClient.newHttpClient();
        try {
            final HttpRequest httpRequest1 = HttpRequest.newBuilder().uri(URI.create(GEO_URL)).build();
            final CompletableFuture<HttpResponse<String>> async = httpClient
                    .sendAsync(httpRequest1, HttpResponse.BodyHandlers.ofString());

            final String body = async.join().body();

            final Converter<String, String> typeConverter = (convert) -> {
                ObjectMapper objectMapper = new ObjectMapper();
                return convertJson(convert, objectMapper);
            };

            final String toJson = typeConverter.convert(body);
            if (toJson != null) {
                final String[] divideData = toJson.split(JSON_PREFIX);

                merge(totalData, divideData);
            }

            return toJson;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return EMPTY_DEVICE_NOT_FOUND;
    }

    private void merge(Map<String, String> totalData, String[] divideData) {
        totalData.put("STATUS", divideData[0]);
        totalData.put("COUNTRY", divideData[1]);
        totalData.put("COUNTRY_PREFIX", divideData[2]);
        totalData.put("CITY_CODE", divideData[3]);
        totalData.put("CITY_NAME", divideData[4]);
        totalData.put("CITY_NAME_RETRY", divideData[5]);
        totalData.put("ZIP_CODE", divideData[6]);
        totalData.put("LATITUDE", divideData[7]);
        totalData.put("LONGITUDE", divideData[8]);
        totalData.put("REGION", divideData[9]);
        totalData.put("SERVICE_HOST", divideData[10]);
    }

    private String convertJson(String convert, ObjectMapper objectMapper) {
        try {
            return objectMapper.convertValue(convert, String.class);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void logger(String info) {
        LOGGER.info("user info :" + info);
    }

}

@FunctionalInterface
interface Converter<INPUT, OUT_PUT> {
    OUT_PUT convert(INPUT input);
}

