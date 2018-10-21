package io.pivotal.microservices.products;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by admin
 * on 21.10.2018.
 */
@Configuration
@ComponentScan
@EntityScan("io.pivotal.microservices.products")
@EnableJpaRepositories("io.pivotal.microservices.products")
@PropertySource("classpath:db-config.properties")
public class ProductsConfiguration {
    protected Logger logger;

    public ProductsConfiguration() {
        logger = Logger.getLogger(getClass().getName());
    }

    /**
     * Creates an in-memory "rewards" database populated with test data for fast
     * testing
     */
    @Bean
    public DataSource dataSource() {
        logger.info("dataSource() invoked");

        // Create an in-memory H2 relational database containing some demo
        // accounts.
        DataSource dataSource = (new EmbeddedDatabaseBuilder()).addScript("classpath:testdb/products_schema.sql")
                .addScript("classpath:testdb/product_data.sql").build();

        logger.info("dataSource = " + dataSource);

        // Sanity check
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Map<String, Object>> products = jdbcTemplate.queryForList("SELECT number FROM T_PRODUCT");
        logger.info("System has " + products.size() + " products");

        // Populate with random balances
        Random rand = new Random();

        for (Map<String, Object> item : products) {
            String number = (String) item.get("number");
            BigDecimal balance = new BigDecimal(rand.nextInt(10000000) / 100.0).setScale(2, BigDecimal.ROUND_HALF_UP);
            jdbcTemplate.update("UPDATE T_PRODUCT SET price = ? WHERE number = ?", balance, number);
        }

        return dataSource;
    }
}
