package io.pivotal.microservices.services.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by admin
 * on 21.10.2018.
 */
@Controller
public class WebProductsController {
    @Autowired
    protected WebProductService productService;

    protected Logger LOGGER = Logger.getLogger(WebProductsController.class
            .getName());

    public WebProductsController(WebProductService productService) {
        this.productService = productService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAllowedFields("accountNumber", "searchText");
    }

    @RequestMapping("/products")
    public String goHome() {
        return "index";
    }

    @RequestMapping("/products/{productNumber}")
    public String byNumber(Model model,
                           @PathVariable("productNumber") String productNumber) {

        LOGGER.info("web-service byNumber() invoked: " + productNumber);

        Product product = productService.findByNumber(productNumber);
        LOGGER.info("web-service byNumber() found: " + product);
        model.addAttribute("account", product);
        return "account";
    }

    @RequestMapping("/products/name/{text}")
    public String ownerSearch(Model model, @PathVariable("text") String name) {
        LOGGER.info("web-service byOwner() invoked: " + name);

        List<Product> products = productService.byNameContains(name);
        LOGGER.info("web-service byOwner() found: " + products);
        model.addAttribute("search", name);
        if (products != null)
            model.addAttribute("accounts", products);
        return "accounts";
    }

    @RequestMapping(value = "/accounts/search", method = RequestMethod.GET)
    public String searchForm(Model model) {
        model.addAttribute("searchCriteria", new SearchCriteria());
        return "accountSearch";
    }

    @RequestMapping(value = "/accounts/dosearch")
    public String doSearch(Model model, SearchCriteria criteria,
                           BindingResult result) {
        LOGGER.info("web-service search() invoked: " + criteria);

        criteria.validate(result);

        if (result.hasErrors())
            return "accountSearch";

        String accountNumber = criteria.getAccountNumber();
        if (StringUtils.hasText(accountNumber)) {
            return byNumber(model, accountNumber);
        } else {
            String searchText = criteria.getSearchText();
            return ownerSearch(model, searchText);
        }
    }
}
