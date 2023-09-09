package com.semen.atiper.utility;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.semen.atiper.model.response.Branch;
import com.semen.atiper.model.response.Repository;
import com.semen.atiper.model.serializeble.Repo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SerializeTemplates {

    public static List<Repository> serializeRepos(ResponseEntity<String> response, RestTemplate restTemplate){
        ObjectMapper mapper = new ObjectMapper();
        List<Repository> repositories = new ArrayList<>();
        try {
            List<Repo> objs = mapper.readValue(response.getBody(), new TypeReference<List<Repo>>() {});
            for (Repo rep : objs) {
                List<Branch> branches = new ArrayList<>();
                String apiUrl = "https://api.github.com/repos/"+rep.getOwner().getLogin()+"/"+rep.getName()+"/branches";
                ResponseEntity<String> branch = restTemplate.getForEntity(apiUrl, String.class);
                List<com.semen.atiper.model.serializeble.Branch> branchesSerial = mapper.readValue(branch.getBody(), new TypeReference<List<com.semen.atiper.model.serializeble.Branch>>() {});
                for (com.semen.atiper.model.serializeble.Branch br :
                        branchesSerial) {
                    branches.add(new Branch(br.getName(),br.getCommit().getSha()));
                }
                repositories.add(new Repository(rep.getName(),rep.getOwner().getLogin(),branches));
            }
        } catch (IOException e) {
            return null;
        }
        return repositories;
    }
}
