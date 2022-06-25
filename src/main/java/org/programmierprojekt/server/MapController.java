package org.programmierprojekt.server;

import com.fasterxml.jackson.jr.ob.JSON;
import org.programmierprojekt.map.Graph;
import org.programmierprojekt.map.TwoDimensionalTreeNode;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.programmierprojekt.server.MapServerApplication.graph;

@RestController
public class MapController {

    @RequestMapping("/")
    public String index() {
        return "resources/static/map.html";
    }

    @PostMapping(value = "/minDist")
    public String minDistance(@RequestBody String jsonBody) {
        try {
            Map<String, Object> queryPoints = JSON.std.mapFrom(jsonBody);
            double[] startPoint = serializePoint(queryPoints.get("start"));
            double[] endPoint = serializePoint(queryPoints.get("end"));
            TwoDimensionalTreeNode start = graph.findNearestNeighbour(startPoint[0], startPoint[1]);
            TwoDimensionalTreeNode end = graph.findNearestNeighbour(endPoint[0], endPoint[1]);
            System.out.println(Arrays.toString(start.getCoordinate()));
            System.out.println(Arrays.toString(end.getCoordinate()));
            ArrayList<double[]> shortestPath = new ArrayList<>();
            Integer minDist = graph.minDist(null, shortestPath, start.getIndex(), end.getIndex());
            Map<String, Object> geoJSON = new HashMap<>();
            geoJSON.put("type", "LineString");
            geoJSON.put("coordinates", Arrays.deepToString(shortestPath.toArray()));
            return JSON.std.asString(geoJSON);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Error";
    }

    private double[] serializePoint(Object pointString) throws IOException {
        double[] point = new double[2];
        Map<String, Object> latLng = JSON.std.mapFrom(pointString);
        point[0] = (Double) latLng.get("lat");
        point[1] = (Double) latLng.get("lng");
        return point;
    }

}
