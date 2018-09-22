package codeit.template.resource;

import codeit.template.solution.SingleRunwaySolution;
import codeit.template.solution.DistressedFlightsSolution;

import codeit.template.model.Flight;
import codeit.template.model.FlightUpgraded;
import codeit.template.model.Runway;
import codeit.template.model.Landing;
import codeit.template.model.RunwayUpgraded;
import codeit.template.model.StaticFlags;
import codeit.template.model.LandingDowngraded;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.math.BigInteger;

@RestController
public class AirTrafficResource {

    @RequestMapping("/airtrafficcontroller-test")
    public String hello(){
        return  "Testing broadcaster! hooray";
    }

   	// @RequestMapping(value = "/airtrafficcontroller",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    // public HashMap<String, ArrayList<Landing>> solveFirst(@RequestBody Runway body) {
        
    //     Flight[] dataArray = body.getFlights();
    //     Map<String, String> staticStuff = body.getStaticStuff();

    //     SingleRunwaySolution ms = new SingleRunwaySolution(dataArray, Integer.parseInt(staticStuff.get("ReserveTime")));
    //     ArrayList<Landing> res = ms.solve(); 

    //     HashMap<String, ArrayList<Landing>> output = new HashMap<>();
    //     output.put("Flights", res);

    //     return output;
    // }

    @RequestMapping(value = "/airtrafficcontroller",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, ArrayList<Object>> solveSecond(@RequestBody RunwayUpgraded body) {
        
        FlightUpgraded[] dataArray = body.getFlights();
        StaticFlags staticStuff = body.getStaticStuff();

        boolean isFirst = false;

        if (staticStuff.head == null) {            
            staticStuff.head = new String[] {"ZZLATAN"};  
            isFirst = true;
        }

        DistressedFlightsSolution ms = new DistressedFlightsSolution(dataArray, staticStuff.head, Integer.parseInt(staticStuff.tail));
        ArrayList<Landing> res = ms.solve(); 

        HashMap<String, ArrayList<Object>> output = new HashMap<>();


        if (isFirst) {
            ArrayList<Object> downgradedResult = new ArrayList<>();
            for (Landing landing : res) {
                downgradedResult.add(new LandingDowngraded(landing.getPlaneId(), landing.getLandingTime()));
            }

            output.put("Flights", downgradedResult);
        } else {
            output.put("Flights", new ArrayList<>(res));
        }


        return output;
    }   

}



