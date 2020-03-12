package eu.panosc.portal.cloud.http;


import java.util.Map;

public interface HttpClient {
    HttpResponse sendRequest(final String url,
                             final HttpMethod method,
                             final Map<String, String> headers,
                             final String data) throws HttpException;

    HttpResponse sendRequest(final String url,
                             final HttpMethod method,
                             final Map<String, String> headers) throws HttpException;

    HttpResponse sendRequest(final String url,
                             final HttpMethod method) throws HttpException;
}
