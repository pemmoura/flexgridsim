package flexgridsim.util;

/*
 * Copyright 2007 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author ycoppel@google.com (Yohann Coppel)
 * 
 * @param <T>
 *            Object's type in the tree.
 */
public class Tree<T> {

	private T head;

	private ArrayList<Tree<T>> leafs = new ArrayList<Tree<T>>();

	private Tree<T> parent = null;

	private HashMap<T, Tree<T>> locate = new HashMap<T, Tree<T>>();

	/**
	 * Instantiates a new tree.
	 *
	 * @param head the head node
	 */
	public Tree(T head) {
		this.head = head;
		locate.put(head, this);
	}

	/**
	 * Adds a leaf to a root of a subtree.
	 *
	 * @param root the root
	 * @param leaf the leaf
	 */
	public void addLeaf(T root, T leaf) {
		if (locate.containsKey(root)) {
			Tree<T> tree = locate.get(root);
			ArrayList<T> sucessors = tree.getSuccessors(root);
			for (T s : sucessors) {
				if (s.equals(leaf)){
					return;
				}
			}
			locate.get(root).addLeaf(leaf);
		} else {
			Tree<T> tree = locate.get(root);
			ArrayList<T> sucessors = tree.getSuccessors(root);
			for (T s : sucessors) {
				if (s.equals(leaf)){
					return;
				}
			}
			addLeaf(root).addLeaf(leaf);
		}
	}

	/**
	 * Adds a leaf.
	 *
	 * @param leaf the leaf
	 * @return the tree
	 */
	public Tree<T> addLeaf(T leaf) {
		Tree<T> t = new Tree<T>(leaf);
		leafs.add(t);
		t.parent = this;
		t.locate = this.locate;
		locate.put(leaf, t);
		return t;
	}

	/**

	/**
	 * Gets the head of the tree.
	 *
	 * @return the head
	 */
	public T getHead() {
		return head;
	}

	/**
	 * Gets a subtree of the tree by the value of its root.
	 *
	 * @param element the element
	 * @return the tree
	 */
	public Tree<T> getTree(T element) {
		return locate.get(element);
	}

	/**
	 * Gets the parent of .
	 *
	 * @return the parent
	 */
	public Tree<T> getParent() {
		return parent;
	}

	/**
	 * Gets the successors of one element
	 *
	 * @param element the value of the element
	 * @return the successors
	 */
	public ArrayList<T> getSuccessors(T element) {
		ArrayList<T> successors = new ArrayList<T>();
		Tree<T> tree = getTree(element);
		if (null != tree) {
			for (Tree<T> leaf : tree.leafs) {
				successors.add(leaf.head);
			}
		}
		return successors;
	}

	/**
	 *
	 * @return the height of the tree
	 */
	public int height() {
		if (this.leafs.size() == 0) {
			return 0;
		} else {
			Tree<T> max = this.leafs.get(0);
			for (int i = 0; i < this.leafs.size() - 1; i++) {
				if (this.leafs.get(i).height() < this.leafs.get(i + 1).height()) {
					max = this.leafs.get(i+1);
				}
			}
			return max.height() + 1;
		}
	}

	/**
	 * Gets the sub trees.
	 *
	 * @return the sub trees
	 */
	public ArrayList<Tree<T>> getSubTrees() {
		return leafs;
	}

	/**
	 * returns all leaves on a level a the tree
	 *
	 * @param level the level
	 * @return the leaves on the level
	 */
	public ArrayList<T> getLeavesOnLevel(int level){
		ArrayList<T> result = new ArrayList<T>();
		getLeavesHeight(level, 0, result);
		return result;
	}
	
	private ArrayList<T> getLeavesHeight(int level, int depth, ArrayList<T> result) {
		if (depth == level){
			result.add(this.getHead());
		}
		for (int i = 0; i < this.leafs.size(); i++) {
			this.leafs.get(i).getLeavesHeight(level, depth+1, result);
		}
		return result;
	}

	@Override
	public String toString() {
		return printTree(0);
	}

	private static final int indent = 2;

	private String printTree(int increment) {
		String s = "";
		String inc = "";
		for (int i = 0; i < increment; ++i) {
			inc = inc + " ";
		}
		s = inc + head;
		for (Tree<T> child : leafs) {
			s += "\n" + child.printTree(increment + indent);
		}
		return s;
	}
	
	
}