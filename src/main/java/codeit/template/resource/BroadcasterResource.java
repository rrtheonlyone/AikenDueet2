package codeit.template.resource;

import codeit.template.solution.MessageBroadcastSolution;
import codeit.template.solution.MostConnectedNodeSolution;
import codeit.template.solution.FastestPathSolution;

import codeit.template.model.FastestPath;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.math.BigInteger;

@RestController
public class BroadcasterResource {

    @RequestMapping("/broadcaster-test")
    public String hello(){
        return  "Testing broadcaster! hooray";
    }

   	@RequestMapping(value = "/broadcaster/message-broadcast",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, ArrayList<String>> solveFirst(@RequestBody Map<String, String[]> body) {
        
        String[] dataArray = body.get("data");

        MessageBroadcastSolution ms = new MessageBroadcastSolution(dataArray);
        ArrayList<String> res = ms.solve();

        HashMap<String, ArrayList<String>> output = new HashMap<>();
        output.put("result", res);

        return output;
    }

    @RequestMapping(value = "/broadcaster/most-connected-node",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, String> solveSecond(@RequestBody Map<String, String[]> body) {
        
        String[] dataArray = body.get("data");

        MostConnectedNodeSolution ms = new MostConnectedNodeSolution(dataArray);
        String res = ms.solve();

        HashMap<String, String> output = new HashMap<>();
        output.put("result", res);

        return output;
    }

    @RequestMapping(value = "/broadcaster/fastest-path",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, ArrayList<String>> solveThird(@RequestBody FastestPath body) {
        
        String[] dataArray = body.getData();
        String sender = body.getSrc();
        String receipient = body.getDest();

        FastestPathSolution ms = new FastestPathSolution(dataArray, sender, receipient);
        ArrayList<String> res = ms.solve();

        HashMap<String, ArrayList<String>> output = new HashMap<>();
        output.put("result", res);

        return output;
    }
}



