package codeit.template.resource;

import codeit.template.solution.CustomerHotelMinDistSolution;
import codeit.template.solution.CustomerHotelMinCampSolution;
import codeit.template.model.Pair;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class CustomersHotelResource {

    @RequestMapping("/customerhotel-test")
    public String hello(){
        return  "Testing customer hotel! yippie";
    }

   	@RequestMapping(value = "/customers-and-hotel/minimum-distance",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, Long> solveFirst(@RequestBody ArrayList<Long> body) {
        
        CustomerHotelMinDistSolution ms = new CustomerHotelMinDistSolution(body);
        long res = ms.solve();

        HashMap<String, Long> output = new HashMap<>();
        output.put("answer", res);

        return output;
    }

    @RequestMapping(value = "/customers-and-hotel/minimum-camps",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, Integer> solveSecond(@RequestBody ArrayList<Pair> body) {
        CustomerHotelMinCampSolution ms = new CustomerHotelMinCampSolution(body);
        int res = ms.solve();

        HashMap<String, Integer> output = new HashMap<>();
        output.put("answer", res);

        return output;
    }

}



