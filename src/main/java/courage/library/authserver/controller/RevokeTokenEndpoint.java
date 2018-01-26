package courage.library.authserver.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@FrameworkEndpoint
public class RevokeTokenEndpoint {

    @Autowired
    private DefaultTokenServices tokenServices;

    @ApiOperation(value = "Logout of application")
    @RequestMapping(
            value = "/oauth/token",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String,String>> logout(HttpServletRequest request,
                                                     @RequestParam(value = "access_token", required = false) String access_token) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.contains("Bearer")) {
            String token = authHeader.substring("Bearer".length()+1);
            tokenServices.revokeToken(token);
        } else if ( access_token != null ){
            tokenServices.revokeToken(access_token);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Map<String, String> response = new HashMap<>();
        response.put("response", "Successful logout");
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

}
