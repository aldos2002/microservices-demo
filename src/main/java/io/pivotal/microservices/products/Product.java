package io.pivotal.microservices.products;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by admin
 * on 21.10.2018.
 */
@Entity
@Table(name = "T_PRODUCT")
public class Product implements Serializable {
    private static final long serialVersionUID = 2L;
    public static Long nextId = 0L;

    protected Long id;
    protected String number;
    protected String productName;
    protected BigDecimal price;

    /**
     * This is a very simple, and non-scalable solution to generating unique
     * ids. Not recommended for a real application. Consider using the
     * <tt>@GeneratedValue</tt> annotation and a sequence to generate ids.
     *
     * @return The next available id.
     */
    protected static Long getNextId() {
        synchronized (nextId) {
            return nextId++;
        }
    }

    /**
     * Default constructor for JPA only.
     */
    protected Product() {
        price = BigDecimal.ZERO;
    }

    @Id
    public long getId() {
        return id;
    }

    /**
     * Set JPA id - for testing and JPA only. Not intended for normal use.
     *
     * @param id The new id.
     */
    protected void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Column(name = "PRODUCT_NAME")
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    public void setPrice(BigDecimal price) {
        this.price = price.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    @Override
    public String toString() {
        return number + " [" + productName + "]: $" + price;
    }
}


