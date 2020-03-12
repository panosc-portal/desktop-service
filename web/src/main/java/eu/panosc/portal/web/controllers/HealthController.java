package eu.panosc.portal.web.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.annotation.security.PermitAll;

@Path("/health")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(tags = {"Health"}, description = "The health controller")
@PermitAll
public class HealthController extends AbstractController {

    HealthController() {
    }

    @GET
    @ApiOperation(value = "Return the health of the system")
    public Response get() {
        return createResponse();
    }
}
