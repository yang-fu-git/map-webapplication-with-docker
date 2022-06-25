package org.programmierprojekt.map;

import java.util.Arrays;
import java.util.Comparator;

public class TwoDimensionalTree {
	TwoDimensionalTreeNode closestNode = null;
	private TwoDimensionalTreeNode root;
	private double minDist = Double.POSITIVE_INFINITY;

	public TwoDimensionalTree(TwoDimensionalTreeNode[] nodes) {
		root = buildTree(0, nodes, 0, nodes.length);
		Arrays.sort(nodes, Comparator.comparing(TwoDimensionalTreeNode::getIndex));
	}

	private TwoDimensionalTreeNode buildTree(int axis, TwoDimensionalTreeNode[] coordinates, int l, int r) {
		if (coordinates == null || l >= r) {
			return null;
		}
		Arrays.sort(coordinates, l, r, Comparator.comparingDouble(a -> a.getCoordinate(axis)));
		int mid = (r - l) / 2 + l;
		TwoDimensionalTreeNode node = coordinates[mid];
		int nextAxis = (axis + 1) % 2;
		node.left = buildTree(nextAxis, coordinates, l, mid);
		if (mid + 1 < coordinates.length) {
			node.right = buildTree(nextAxis, coordinates, mid + 1, r);
		}
		return node;
	}

	public TwoDimensionalTreeNode findNearestNeighbour(double x, double y) {
		minDist = Double.POSITIVE_INFINITY;
		closestNode = null;
		findNearestNeighbour(root, x, y, 0);
		return closestNode;
	}

	private void findNearestNeighbour(TwoDimensionalTreeNode fromNode, double x, double y, int axis) {
		if (fromNode == null)
			return;
		double dist = euclideanDistSquare(fromNode, x, y);
		if (dist < minDist) {
			minDist = dist;
			closestNode = fromNode;
		}
		double distToAxis = (axis == 0 ? x : y) - fromNode.getCoordinate(axis);
		int nextAxis = (axis + 1) % 2;
		TwoDimensionalTreeNode childMust = distToAxis < 0 ? fromNode.left : fromNode.right;
		TwoDimensionalTreeNode childOption = distToAxis < 0 ? fromNode.right : fromNode.left;
		findNearestNeighbour(childMust, x, y, nextAxis);
		// if axis is already too far, we do pruning
		if (distToAxis * distToAxis < minDist) {
			findNearestNeighbour(childOption, x, y, nextAxis);
		}
	}

	private double euclideanDistSquare(TwoDimensionalTreeNode node, double x, double y) {
		return (node.getCoordinate(0) - x) * (node.getCoordinate(0) - x)
				+ (node.getCoordinate(1) - y) * (node.getCoordinate(1) - y);
	}

	public TwoDimensionalTreeNode getRoot() {
		return this.root;
	}
}