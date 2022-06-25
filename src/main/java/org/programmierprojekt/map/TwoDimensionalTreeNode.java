package org.programmierprojekt.map;

import java.util.Arrays;

public class TwoDimensionalTreeNode {
    private final int index;
    TwoDimensionalTreeNode left;
    TwoDimensionalTreeNode right;
    private double[] coordinate;

    public TwoDimensionalTreeNode(int idx, double[] coordinate) {
        this.coordinate = coordinate;
        index = idx;
    }

    public double[] getCoordinate() {
        return coordinate;
    }

    public int getIndex() {
        return index;
    }

    public double getCoordinate(int axis) {
        return coordinate[axis];
    }

    @Override
    public String toString() {
        return "TwoDimensionalTreeNode{" +
                "index=" + index +
                ", coordinate=" + Arrays.toString(coordinate) +
                '}';
    }
}