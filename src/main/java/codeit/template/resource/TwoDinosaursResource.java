package codeit.template.resource;

import codeit.template.solution.TwoDinosaursSolution;

import codeit.template.model.TwoDinosaurs;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.math.BigInteger;

@RestController
public class TwoDinosaursResource {

    @RequestMapping("/test")
    public String hello(){
        return  "Testing two dinosaurs damnnn...";
    }

   	@RequestMapping(value = "two-dinosaurs",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, BigInteger> solve(@RequestBody TwoDinosaurs body){
       
        int numFood = body.getNumFood();
        int[] raphael = body.getRaphael();
        int[] leonardo = body.getLeonardo();
        int maxDiff = body.getMaxDiff();

    	TwoDinosaursSolution dinosaurs = new TwoDinosaursSolution(numFood, raphael, leonardo, maxDiff);
        BigInteger res = dinosaurs.solve();


        HashMap<String, BigInteger> output = new HashMap<>();
        output.put("result", res);

        return output;

    }
}
