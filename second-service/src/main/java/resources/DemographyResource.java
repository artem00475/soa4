package resources;

import dto.TotalDTO;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.*;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


@Path("/")
public class DemographyResource {

    private Client getClient() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                }
            }, new SecureRandom());
            return ClientBuilder.newBuilder()
                    .hostnameVerifier((s, session) -> true)
                    .sslContext(sslContext)
                    .build();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    @GET
    @Path("/hair-color/{hair-color}/percentage")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPercentage(@PathParam("hair-color") String color) {
        Client client = getClient();
        Response responseTotal = client.target("https://localhost:8081")
                .path("people").queryParam("limit", 1).request()
                .get();
        int total = responseTotal.readEntity(TotalDTO.class).getTotal();
        Response responseTotalByColor = client.target("https://localhost:8081")
                .path("people").queryParam("hairColor", color).request()
                .get();
        int totalByColor = responseTotalByColor.readEntity(TotalDTO.class).getTotal();
        return Response.ok(totalByColor/total*100 + "%").build();
//        return Response.ok(response).build();
    }
}
