package com.bangstagram.user.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/***
 * author: Hyo-Jin Kim
 * Date: 2020.05.08
 */

public class HttpUtils {

    public static String getMethod(String apiUrl, Map<String, String> requestHeaders, String contentType) {
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", contentType);
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) // 에러 발생
                return readBody(con.getErrorStream());
            return readBody(con.getInputStream()); // 정상 호출
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    public static String postMethod(String apiUrl, String requestBody, String contentType) {
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", contentType);
            con.setDoOutput(true);
            con.setDoInput(true);

            DataOutputStream os = new DataOutputStream(con.getOutputStream());
            // byte[] inputBytes = requestBody.getBytes("utf-8");
            os.writeBytes(requestBody);
            os.flush();
            os.close();

            int responseCode = con.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) // 에러 발생
                return readBody(con.getErrorStream());
            return readBody(con.getInputStream()); // 정상 호출
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }
}
