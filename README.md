# programmierprojekt2022
# Programmierprojekt:Routeplanner phase2

<html>
  <body>
  <p style="text-align:right">Sichun Zheng 3202876</p>
  <p style="text-align:right">Yang Fu 3595130</p>
</body>
</html>

## Introduction
With this part of project we build a website to interative find the nearest points and shortestest path of two selected points on the map, and show the cordinates of themself and their nearest points on the right side.

## Requirements

- Java 17
- Gradle 7.3

the file to be read in can be found here <br/>
https://fmi.uni-stuttgart.de/alg/research/stuff/

## Usage

0. Checkout the repo to local.
1. copy the maps into directory _Maps_
2. build and run the application:

    ```bash ./run.sh germany```



**Note that germany map is huge, we therefore need 6 GB maximal heap size for JVM by passing _-Xmx6g_**

As for convenience, we set maximal heap size as 6 GB for all the cases in _run.sh_