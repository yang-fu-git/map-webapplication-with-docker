package org.programmierprojekt.server;

import org.programmierprojekt.map.Graph;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MapServerApplication {
    static Graph graph;

    public static void main(String[] args) {
        graph = new Graph(String.format("./Maps/%s.fmi", args[0]));
        SpringApplication.run(MapServerApplication.class, args);
    }

}
