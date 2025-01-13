package com.spliceforms;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import jakarta.inject.Inject;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import org.hibernate.validator.constraints.URL;
import org.jose4j.lang.JoseException;

@Path("/api/goto/readme")
public class GotoReadmeResource {

    @Inject
    AuthTokenService createJwt;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public Response gotoReadMe(
            @NotBlank @FormParam("readme_jwt_secret") String jwtSecret,
            @NotBlank @URL @FormParam("readme_site_url") String siteUrl,
            @NotBlank @FormParam("readme_project") String project,
            @NotBlank @FormParam("name") String name,
            @NotBlank @Email @FormParam("email") String email,
            @FormParam("pagePath") String pagePath,
            @FormParam("redirect") boolean redirect) throws URISyntaxException, JoseException {

        // the final URL to redirect to (this should be the base URL)
        UriBuilder redirectUriBuilder = UriBuilder.fromUri(siteUrl);

        // and to send the user to a specfic project, page we need another path
        String redirect_param = UriBuilder.fromUri(siteUrl)
                .path(project)
                .path(pagePath).build().getPath();

        // One user can have access to multiple projects
        // In the demo, we only have one project
        List<String> projects = List.of(project);
        String auth_token = createJwt.createJwt(jwtSecret, name, email, projects);

        redirectUriBuilder.queryParam("auth_token", auth_token);
        redirectUriBuilder.queryParam("redirect", redirect_param);

        URI redirectUri = redirectUriBuilder.build();

        if (redirect) {
            // Since the HTML used HTMX, we need to set the HX-Redirect header to redirect
            // If the HTML didn't use HTMX, we would do Response.seeOther(finalUrl)
            return Response.ok().header("HX-Redirect", redirectUri.toString()).build();
        } else {
            return Response.ok(redirectUri.toString()).build();
        }
    }
}
