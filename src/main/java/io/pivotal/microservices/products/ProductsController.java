package io.pivotal.microservices.products;

import io.pivotal.microservices.exceptions.AccountNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by admin
 * on 21.10.2018.
 */
@RestController
public class ProductsController {
    protected Logger LOGGER = Logger.getLogger(io.pivotal.microservices.products.ProductsController.class
            .getName());
    protected ProductRepository productRepository;

    /**
     * Create an instance plugging in the respository of Accounts.
     *
     * @param productRepository An product repository implementation.
     */
    @Autowired
    public ProductsController(ProductRepository productRepository) {
        this.productRepository = productRepository;

        LOGGER.info("ProductRepository says system has "
                + productRepository.countAccounts() + " accounts");
    }

    /**
     * Fetch an account with the specified account number.
     *
     * @param accountNumber A numeric, 9 digit account number.
     * @return The account if found.
     * @throws AccountNotFoundException If the number is not recognised.
     */
    @RequestMapping("/products/{productNumber}")
    public Product byNumber(@PathVariable("productNumber") String productNumber) {
        LOGGER.info("product-service byNumber() invoked: " + productNumber);
        Product product = productRepository.findByNumber(productNumber);
        LOGGER.info("product-service byNumber() found: " + product);

        if (product == null)
            throw new AccountNotFoundException(productNumber);
        else {
            return product;
        }
    }

    /**
     * Fetch accounts with the specified name. A partial case-insensitive match
     * is supported. So <code>http://.../accounts/owner/a</code> will find any
     * accounts with upper or lower case 'a' in their name.
     *
     * @param partialName
     * @return A non-null, non-empty set of accounts.
     * @throws AccountNotFoundException If there are no matches at all.
     */
    @RequestMapping("/products/name/{name}")
    public List<Product> byOwner(@PathVariable("name") String partialName) {
        LOGGER.info("product-service byName() invoked: "
                + productRepository.getClass().getName() + " for "
                + partialName);

        List<Product> products = productRepository
                .findByProductNameContainingIgnoreCase(partialName);
        LOGGER.info("product-service byOwner() found: " + products);

        if (products == null || products.size() == 0)
            throw new AccountNotFoundException(partialName);
        else {
            return products;
        }
    }
}
