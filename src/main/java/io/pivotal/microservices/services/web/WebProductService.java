package io.pivotal.microservices.services.web;

import io.pivotal.microservices.exceptions.AccountNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by admin
 * on 21.10.2018.
 */
@Service
public class WebProductService {
    @Autowired
    @LoadBalanced
    protected RestTemplate restTemplate;

    protected String serviceUrl;

    protected Logger LOGGER = Logger.getLogger(WebProductService.class.getName());

    public WebProductService(String serviceUrl) {
        this.serviceUrl = serviceUrl.startsWith("http") ? serviceUrl
                : "http://" + serviceUrl;
    }

    /**
     * The RestTemplate works because it uses a custom request-factory that uses
     * Ribbon to look-up the service to use. This method simply exists to show
     * this.
     */
    @PostConstruct
    public void demoOnly() {
        // Can't do this in the constructor because the RestTemplate injection
        // happens afterwards.
        LOGGER.warning("The RestTemplate request factory is "
                + restTemplate.getRequestFactory().getClass());
    }

    public Product findByNumber(String productNumber) {

        LOGGER.info("findByNumber() invoked: for " + productNumber);
        return restTemplate.getForObject(serviceUrl + "/products/{productNumber}",
                Product.class, productNumber);
    }

    public List<Product> byNameContains(String name) {
        LOGGER.info("byNameContains() invoked:  for " + name);
        Product[] products = null;

        try {
            products = restTemplate.getForObject(serviceUrl
                    + "/accounts/name/{name}", Product[].class, name);
        } catch (HttpClientErrorException e) { // 404
            // Nothing found
        }

        if (products == null || products.length == 0)
            return null;
        else
            return Arrays.asList(products);
    }

    public Product getByNumber(String productNumber) {
        Product product = restTemplate.getForObject(serviceUrl
                + "/products/{productNumber}", Product.class, productNumber);

        if (product == null)
            throw new AccountNotFoundException(productNumber);
        else
            return product;
    }
}
