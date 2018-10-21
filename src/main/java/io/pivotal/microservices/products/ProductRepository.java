package io.pivotal.microservices.products;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by admin
 * on 21.10.2018.
 */
public interface ProductRepository extends Repository<Product, Long> {
    /**
     * Find an account with the specified account number.
     *
     * @param productNumber
     * @return The account if found, null otherwise.
     */
    public Product findByNumber(String productNumber);

    /**
     * Find accounts whose owner name contains the specified string
     *
     * @param productName
     *            Any alphabetic string.
     * @return The list of matching accounts - always non-null, but may be
     *         empty.
     */
    public List<Product> findByProductNameContainingIgnoreCase(String productName);

    @Query("SELECT count(*) from Product ")
    public int countAccounts();
}
