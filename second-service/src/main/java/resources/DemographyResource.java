package resources;

import dto.ResultDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import service.PeopleService;


@Path("/demography")
@Produces(MediaType.APPLICATION_JSON)
public class DemographyResource {

    private final PeopleService peopleService;

    public DemographyResource() {
        peopleService = new PeopleService();
    }

    @GET
    @Path("/hair-color/{hair-color}/percentage")
    public Response getPercentageByHairColor(@PathParam("hair-color") String color) {
        return Response.ok(
                new ResultDTO(peopleService.getPercentage(color))
        ).build();
    }

    @GET
    @Path("/nationality/{nationality}/hair-color")
    public Response getCountByNationalityAndHairColor(
            @PathParam("nationality") String nationality,
            @QueryParam("hair-color") String color
    ) {
        return Response.ok(
                new ResultDTO(String.valueOf(peopleService.getCount(nationality, color)))
        ).build();
    }
}
