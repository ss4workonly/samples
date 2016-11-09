package samples.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
class XController {

    @Autowired
    private XService service;

    @RequestMapping(value = "/companies", method = GET)
    List<Integer> companies(
            @RequestParam("created") @DateTimeFormat(pattern = "yyyy-MM-dd") Date created,
            @RequestParam("keywords") List<String> keywords) {
        return service.getCompanies(created, keywords);
    }
}
