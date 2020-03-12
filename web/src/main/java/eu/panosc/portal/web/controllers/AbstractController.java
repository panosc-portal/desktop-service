package eu.panosc.portal.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

abstract class AbstractController {

    Response createResponse(final Object entity) {
        return createResponse(entity, Status.OK, null);
    }

    Response createResponse() {
        return createResponse(null, Status.OK, null);
    }

    Response createResponse(final Object entity, final Status status) {
        return createResponse(entity, status, null);
    }

    Response createResponse(final Object entity, final Status status, final ImmutableMap metadata) {
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode json = mapper.createObjectNode();
        if (metadata != null) {
            json.putPOJO("_metadata", metadata);
        }
        json.putPOJO("data", entity);
        final ResponseBuilder response = Response.status(status).entity(json);
        return response.build();
    }


    ObjectNode createObjectNode() {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.createObjectNode();
    }

    void checkNotNull(Object object, String message) {
        if (object == null) {
            throw new BadRequestException(message);
        }
    }


}
