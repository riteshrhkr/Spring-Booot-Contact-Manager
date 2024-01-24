package com.contect.manager.config;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

public class ForKnowledge {

    // Create the request factory. How to send multipart requests with Spring RestTemplate
    public void sendRequest(MultipartFile file) {
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            
            // Prepare the request parameters
            MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            map.add("file", new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            }); // Send the file content directly
            map.add("id", 5);
            map.add("name", "Ritesh");

            // Create the request entity
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);

            // Create RestTemplate instance with timeout configuration (optional)
            RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());

            // Make the POST request and store the response. This response entity is same as we send ResponseEntity in a rest controller method
            ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8083/saveImage", requestEntity, String.class);
            String response = responseEntity.getBody();
            System.out.println(response);
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to set a timeout for the RestTemplate
    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        int timeout = 5000; // Timeout in milliseconds
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(timeout);
        clientHttpRequestFactory.setReadTimeout(timeout);
        return clientHttpRequestFactory;
    }
}
