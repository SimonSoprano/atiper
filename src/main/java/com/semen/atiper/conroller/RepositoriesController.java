package com.semen.atiper.conroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.semen.atiper.model.response.Repository;
import com.semen.atiper.model.service.ErrorResponse;
import com.semen.atiper.model.serializeble.Repo;
import com.semen.atiper.utility.SerializeTemplates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@RestController
public class RepositoriesController {



    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/users/{username}/repositories")
    public Object getList(@PathVariable String username, @RequestHeader("Accept") String accept ) throws JsonProcessingException {
        List<Repository> response = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {

            if (!accept.equals("application/json")) {
                throw new IllegalArgumentException("Unsupported 'Accept' header value");
            }

            String apiUrl = "https://api.github.com/users/"+username+"/repos";
             response = SerializeTemplates.serializeRepos(restTemplate.getForEntity(apiUrl, String.class),restTemplate);

        }catch (HttpClientErrorException exception ){
            if(exception.getStatusCode().value()==404 ){
              //  return new ErrorResponse( errorResponse.getStatusCode().value(),errorResponse.getMessage());
                return  new ResponseEntity<>( objectMapper.writeValueAsString(new ErrorResponse(404,exception.getMessage())), HttpStatus.valueOf(404));
            }
        } catch (IllegalArgumentException exception){
            return new ResponseEntity<>( objectMapper.writeValueAsString(new ErrorResponse(406,exception.getMessage())), HttpStatus.valueOf(406));
           // return new HttpClientErrorException(HttpStatusCode.valueOf(406),exception.getMessage());
        }

        return response;
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return new ResponseEntity<>( objectMapper.writeValueAsString(new ErrorResponse(406,ex.getMessage())), HttpStatus.valueOf(406));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<String> handlerEx(HttpMediaTypeNotAcceptableException ex){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return new ResponseEntity<>( objectMapper.writeValueAsString(new ErrorResponse(406,ex.getMessage())), HttpStatus.valueOf(406));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }




}
