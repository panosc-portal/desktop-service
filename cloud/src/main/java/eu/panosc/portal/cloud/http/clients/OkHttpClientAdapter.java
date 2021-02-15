package eu.panosc.portal.cloud.http.clients;

import eu.panosc.portal.cloud.http.HttpClient;
import eu.panosc.portal.cloud.http.HttpException;
import eu.panosc.portal.cloud.http.HttpMethod;
import eu.panosc.portal.cloud.http.HttpResponse;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class OkHttpClientAdapter implements HttpClient {

    private static final MediaType JSON_CONTENT_TYPE = MediaType.parse("application/json");

    private final OkHttpClient client = new OkHttpClient();

    private Headers buildHeaders(Map<String, String> headers) {
        Headers.Builder headersBuilder = new Headers.Builder();
        if (headers == null) {
            return headersBuilder.build();
        }
        final Iterator<Entry<String, String>> entries = headers.entrySet().iterator();
        while (entries.hasNext()) {
            Entry<String, String>  header = entries.next();
            String key    = header.getKey();
            String value  = header.getValue();
            headersBuilder.add(key, value);
        }
        return headersBuilder.build();
    }

    private HttpResponse buildResponse(final Response response) throws HttpException {
        try {
            final Map<String, String> retHeaders = new HashMap<>();
            final Headers             headers    = response.headers();

            for (final String name : headers.names()) {
                retHeaders.put(name, headers.get(name));
            }

            return new HttpResponse(response.body().string(),
                response.code(),
                retHeaders
            );
        } catch (IOException exception) {
            throw new HttpException("Error building response from HTTP request", exception);
        } finally {
            response.body().close();
        }
    }

    private HttpResponse doRequest(final Request request) throws HttpException {
        try {
            final Response response = client.newCall(request).execute();
            return buildResponse(response);
        } catch (IOException exception) {
            throw new HttpException("Error sending HTTP request", exception);
        }
    }

    private HttpResponse doPost(final String url, final Headers headers, final String data) throws HttpException {
        final RequestBody body = RequestBody.create(JSON_CONTENT_TYPE, data);
        final Request request = new Request.Builder()
            .url(url)
            .headers(headers)
            .post(body)
            .build();

        return doRequest(request);
    }

    private HttpResponse doGet(final String url, final Headers headers) throws HttpException {
        final Request request = new Request.Builder()
            .url(url)
            .headers(headers)
            .build();
        return doRequest(request);
    }

    private HttpResponse doDelete(final String url, final Headers headers) throws HttpException {
        final Request request = new Request.Builder()
            .url(url)
            .headers(headers)
            .delete()
            .build();
        return doRequest(request);
    }


    @Override
    public HttpResponse sendRequest(final String url,
                                    final HttpMethod method) throws HttpException {
        return sendRequest(url, method, new HashMap<>(), null);
    }

    @Override
    public HttpResponse sendRequest(final String url,
                                    final HttpMethod method,
                                    final Map<String, String> headers) throws HttpException {
        return sendRequest(url, method, headers, null);
    }

    @Override
    public HttpResponse sendRequest(final String url,
                                    final HttpMethod method,
                                    final Map<String, String> headers,
                                    final String data) throws HttpException {
        switch (method) {
            case GET:
                return doGet(url, buildHeaders(headers));
            case POST:
                return doPost(url, buildHeaders(headers), data);
            case DELETE:
                return doDelete(url, buildHeaders(headers));
            default:
                throw new HttpException("HTTP method not supported: " + method);
        }
    }
}
