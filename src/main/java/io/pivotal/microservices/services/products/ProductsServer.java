package io.pivotal.microservices.services.products;

import io.pivotal.microservices.products.ProductRepository;
import io.pivotal.microservices.products.ProductsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

import java.util.logging.Logger;

/**
 * Created by admin
 * on 21.10.2018.
 */
@EnableAutoConfiguration
@EnableDiscoveryClient
@Import(ProductsConfiguration.class)
public class ProductsServer {


    @Autowired
    protected ProductRepository productRepository;

    protected Logger LOGGER = Logger.getLogger(io.pivotal.microservices.services.accounts.AccountsServer.class.getName());

    /**
     * Run the application using Spring Boot and an embedded servlet engine.
     *
     * @param args Program arguments - ignored.
     */
    public static void main(String[] args) {
        // Tell server to look for accounts-server.properties or
        // accounts-server.yml
        System.setProperty("spring.config.name", "products-server");

        SpringApplication.run(io.pivotal.microservices.services.products.ProductsServer.class, args);
    }
}
