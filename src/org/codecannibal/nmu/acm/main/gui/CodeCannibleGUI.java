package org.codecannibal.nmu.acm.main.gui;

import org.codecannibal.nmu.acm.main.Cannibalize;
import org.codecannibal.nmu.acm.main.JarOpener;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;

public class CodeCannibleGUI extends JFrame {

	private JPanel contentPane;
	private JTree classTree;
	private JScrollPane classScrollPane;
	private DefaultMutableTreeNode rootNode;
    private DefaultTreeModel rootModel;
    private JScrollPane classDataScrollPane;
    private JTextArea classDataTextArea;
	private Cannibalize cannible;

	/**
	 * Create the frame.
	 */
	public CodeCannibleGUI(Cannibalize cannible) {
		this.cannible = cannible;
		init();
	}
	
	
	
	private void init(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 820, 837);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		classTree = new JTree();
		classTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent arg0) {
				new Thread(new Runnable(){

						public void run(){
				System.out.println("You've selected a class!");
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)arg0.getPath().getLastPathComponent();
				String name = (String)node.getUserObject();
				if(!name.endsWith(".class"))
					return;
				System.out.println("Getting pretty byte code list");
				List<String> byteCode = cannible.getPrettyByteCodeList(name.split("\\.")[0]);
				String finalCode = "";
				for(String s : byteCode){
					finalCode += s;
				}
				System.out.println("Setting bytecode stuff!");
				classDataTextArea.setText(finalCode);}}).run();
			}
		});
		rootNode = new DefaultMutableTreeNode("Root node");
		//fillClassTree();
        rootModel = new DefaultTreeModel(rootNode);
		classTree.setModel(rootModel);
//		tree.setBounds(0, 0, 72, 64);
		
		classScrollPane = new JScrollPane(classTree);
		classScrollPane.setBounds(10, 11, 190, 231);
		contentPane.add(classScrollPane);
		
		classDataTextArea = new JTextArea();
		
		classDataScrollPane = new JScrollPane(classDataTextArea);
		classDataScrollPane.setBounds(210,11,584,731);
		contentPane.add(classDataScrollPane)
;	}
	
	
	public void updateClassTree(List<String> classBlocks){
		rootNode.removeAllChildren();
		rootModel.reload();
		List<String[]> brokenClasses = new ArrayList<String[]>();
		for(String s : classBlocks){
			brokenClasses.add(s.split("/"));
		}
		addNodes(rootNode, brokenClasses);
		rootModel.reload();
		
	}
	
	private String[] getShortenedArray(String[] array){
		String[] newArray = new String[array.length-1];
		for(int i = 1; i < array.length; i++){
			newArray[i-1] = array[i];
		}
		return newArray;
	}
	
	private void addNodes(DefaultMutableTreeNode currNode, List<String[]> ourClasses){
		String lastClass = null;
		List<String[]> nextClasses = new ArrayList<>();
		for(String[] classPath : ourClasses){
			if(classPath == null || classPath.length == 0)
				continue;
			if(lastClass == null){
				lastClass = classPath[0];
				if(!lastClass.endsWith(".class")){
					nextClasses.add(getShortenedArray(classPath));
				}
			}
			else if(!classPath[0].equals(lastClass)){
				DefaultMutableTreeNode nextNode = new DefaultMutableTreeNode(lastClass);
				if(!lastClass.endsWith(".class")){
					addNodes(nextNode, nextClasses);
					nextClasses.clear();
				}
				currNode.add(nextNode);
				lastClass = classPath[0];
				if(!lastClass.endsWith(".class")){
					nextClasses.add(getShortenedArray(classPath));
				}
			}
			else{
				if(!lastClass.endsWith(".class")){
					nextClasses.add(getShortenedArray(classPath));
				}
			}
		}
		if(lastClass != null){
			DefaultMutableTreeNode nextNode = new DefaultMutableTreeNode(lastClass);
			if(!lastClass.endsWith(".class")){
				addNodes(nextNode, nextClasses);
			}
			currNode.add(nextNode);
		}
		
	}
}
