/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see:
 * <http://www.opensourcephysics.org/>
 */

package org.dreambot.decomplier.gui.component.util;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.io.File;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * A tree model to display files and jar/zip contents.
 *
 * @author Doug Brown
 * @version 1.0
 */
public class JarTreeModel implements TreeModel {

	// instance fields
  protected File root; // root must be a directory
  protected Map topLevelNodeArrays = new HashMap(); // maps jar to JarNode[]
  protected Map pathMaps = new HashMap(); // maps jar to Map of relative path to JarNode
  
  /**
   * Constructor.
   *
   * @param root a directory file
   */
  public JarTreeModel(File root) { 
  	this.root = root; 
  }

  /**
   * Gets the root of this tree model.
   *
   * @return the root file
   */
  public Object getRoot() { 
  	return root; 
  }

  /**
   * Returns true if the specified node is a leaf.
   *
   * @param node the tree node
   * @return true if node is a leaf
   */
  public boolean isLeaf(Object node) {
  	if (node instanceof File)	{
  		File file = (File)node;
  		if (file.getName().endsWith(".jar")) { //$NON-NLS-1$
  			return false;
  		}
  		return ((File)node).isFile();
  	}
  	else if (node instanceof JarNode) {
  		JarNode treeNode = (JarNode)node;
  		return treeNode.isLeaf();
  	}
  	return true;
  }

  /**
   * Determines the number of child nodes for the specified node.
   *
   * @param parent the parent node
   * @return the number of child nodes
   */
  public int getChildCount(Object parent) {
  	if (parent instanceof File)	{
  		File parentFile = (File)parent;
  		if (parentFile.getName().endsWith(".jar")) { //$NON-NLS-1$
        JarNode[] nodes = getJarNodes(parentFile);
  			return (nodes == null)? 0: nodes.length;
  		}
  		String[] children = ((File)parent).list();
			return (children == null)? 0: children.length;
  	}
  	else if (parent instanceof JarNode) {
  		JarNode treeNode = (JarNode)parent;
  		return treeNode.getChildCount();
  	}
  	return 0;
  }

  /**
   * Gets the child node at a specified index. Parent and child may be a
   * File or JarNode.
   *
   * @param parent the parent node
   * @param index the index
   * @return the child node
   */
  public Object getChild(Object parent, int index) {
  	if (parent instanceof File)	{
  		File parentFile = (File)parent;
  		// if parent is the launch jar, return a JarNode
  		if (parentFile.getName().endsWith(".jar")) { //$NON-NLS-1$
        JarNode[] nodes = getJarNodes(parentFile);
  			if (nodes != null && nodes.length > index)
        	return nodes[index];
  			return "no child found"; //$NON-NLS-1$
  		}
      String[] children = parentFile.list();
      if ((children == null) || (index >= children.length)) return null;
      return new File(parentFile, children[index]) {
      	public String toString() {
      		return getName();
      	}
      };
  	} 
  	else if (parent instanceof JarNode) {
  		JarNode treeNode = (JarNode)parent;
  		return treeNode.getChildAt(index);
  	}
  	return null;
  }

  /**
   * Gets the index of the specified child node.
   *
   * @param parent the parent node
   * @param child the child node
   * @return the index of the child
   */
  public int getIndexOfChild(Object parent, Object child) {
  	if (parent instanceof File)	{
  		File parentFile = (File)parent;
  		if (parentFile.getName().endsWith(".jar")) { //$NON-NLS-1$
        JarNode[] nodes = getJarNodes(parentFile);
  			if (nodes == null) return -1;
        for (int i = 0; i < nodes.length; i++) {
        	if (nodes[i].equals(child)) return i;
        }
  		}
      String[] children = ((File)parent).list();
      if (children == null) return -1;
      String childname = ((File)child).getName();
      for(int i = 0; i < children.length; i++) {
        if (childname.equals(children[i])) return i;
      }
  	}
  	else if (parent instanceof JarNode) {
  		JarNode treeNode = (JarNode)parent;
  		return treeNode.getIndex((JarNode)child);
  	}
    return -1;
  }

  // methods required by TreeModel
  // these methods are empty since this is not an editable model
  public void valueForPathChanged(TreePath path, Object newvalue) {/** empty method */}
  public void addTreeModelListener(TreeModelListener l) {/** empty method */}
  public void removeTreeModelListener(TreeModelListener l) {/** empty method */}

  public Object getChild(Object parent, String name) {
  	if (parent instanceof File)	{
  		File parentFile = (File)parent;
  		// if parent is the launch jar, return a JarNode
  		if (parentFile.getName().endsWith(".jar")) { //$NON-NLS-1$
        JarNode[] nodes = getJarNodes(parentFile);
  			if (nodes != null) {
  				for (int i = 0; i < nodes.length; i++) {
  					if (nodes[i].toString().equals(name)) {
  						return nodes[i];
  					}
  				}
  			}
  		}
      String[] children = parentFile.list();
      if (children != null) {
				for (int i = 0; i < children.length; i++) {
					if (children[i].toString().equals(name)) {
			      return new File(parentFile, children[i]) {
			      	public String toString() {
			      		return getName();
			      	}
			      };
					}
				}
      }
  	} 
  	else if (parent instanceof JarNode) {
  		JarNode treeNode = (JarNode)parent;
  		Enumeration e = treeNode.children();
  		while (e.hasMoreElements()) {
  			JarNode next = (JarNode)e.nextElement();
  			if (next.toString().equals(name)) return next;
  		}
  	}
  	return null;
  }

  // JarNode class represents a compressed file in a jar
  class JarNode extends DefaultMutableTreeNode {
  	String name;
  	public JarNode(String path) {
  		name = XML.getName(path);
  		if (name.equals("")) { //$NON-NLS-1$
  			name = XML.getName(path.substring(0, path.length()-1));
  		}
  	}
  	public String toString() {
  		return name;
  	}
  }
  
  // returns the JarNode associated with a path in a given jar file
  public JarNode getJarNode(File jarFile, String path) {
  	Map pathMap = (Map)pathMaps.get(jarFile);
  	if (pathMap == null) {
  		readJar(jarFile);
    	pathMap = (Map)pathMaps.get(jarFile);
  	}
  	return (JarNode)pathMap.get(path);
  }

  
  // returns the top-level JarNodes in the specified jar
  public JarNode[] getJarNodes(File jarFile) {
  	JarNode[] array = (JarNode[])topLevelNodeArrays.get(jarFile);
		if (array == null) {
			readJar(jarFile);
			array = (JarNode[])topLevelNodeArrays.get(jarFile);
		}
		return array;
  }
  
  // reads the specified jar
  private void readJar(File jarFile) {
			// get all jar entries
			Collection entries = getJarEntries(jarFile);
			ArrayList topLevelNodes = new ArrayList();
			Map nodes = new HashMap(); // maps String to JarNode
			for (Iterator it = entries.iterator(); it.hasNext();) {
				String path = XML.forwardSlash(it.next().toString());
				// don't include meta-inf files
				if (path.startsWith("META-INF")) continue; //$NON-NLS-1$
				// for each path, make and connect JarNodes in the path
				JarNode parent = null;
				String parentPath = ""; //$NON-NLS-1$
				while (path != null) {
					int n = path.indexOf("/"); //$NON-NLS-1$
					if (n > -1) { // found directory
						parentPath += path.substring(0, n+1);
						JarNode node = (JarNode)nodes.get(parentPath);
						if (node == null) {
							node = new JarNode(parentPath);
							nodes.put(parentPath, node);
							if (parent != null) {
								parent.add(node);
							}
							else {  // this is a top level node
								topLevelNodes.add(node);
							}
						}
						path = path.substring(n+1);
						parent = node;
					}
					else { // found file
						path = parentPath + path;
						JarNode node = (JarNode)nodes.get(path);
						if (node == null) {
							node = new JarNode(path);
							nodes.put(path, node);
							if (parent != null) {
								parent.add(node);
							}
							else {  // this is a top level node
								topLevelNodes.add(node);
							}
						}
						path = null;
					}
				}
			}
			JarNode[] array = (JarNode[])topLevelNodes.toArray(new JarNode[0]);
			topLevelNodeArrays.put(jarFile, array);
			pathMaps.put(jarFile, nodes);
  }
  
  // returns all JarEntries in the specified jar
  private Collection getJarEntries(File jarFile) {
		// create a JarFile
		JarFile jar = null;
		try {
			jar = new JarFile(jarFile);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		if (jar != null) {
			TreeSet entries = new TreeSet();
			for (Enumeration e = jar.entries(); e.hasMoreElements();) {
				JarEntry entry = (JarEntry)e.nextElement();
				entries.add(entry.getName());
			}
			return entries;
		}
		return null;
	}
}

/*
 * Open Source Physics software is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License (GPL) as
 * published by the Free Software Foundation; either version 2 of the License,
 * or(at your option) any later version.
 * 
 * Code that uses any portion of the code in the org.opensourcephysics package
 * or any subpackage (subdirectory) of this package must must also be be
 * released under the GNU GPL license.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this; if not, write to the Free Software Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston MA 02111-1307 USA or view the license online at
 * http://www.gnu.org/copyleft/gpl.html
 * 
 * Copyright (c) 2007 The Open Source Physics project
 * http://www.opensourcephysics.org
 */