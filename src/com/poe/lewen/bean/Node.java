/**
 * 软件名称:android无限级树源码
 * 作者：shaolong
 * 网址:http://londroid.5d6d.com
 * 
 * londroid(龙卓论坛)申明：
 * 1)本站不保证所提供软件或程序的完整性和安全性
 * 2)转载或使用本站提供的资源或源码请勿删除本说明文件或文字
 * 3)本站源码为网上搜集或网友提供或本站技术人员编写,
 * 如果涉及或侵害到您的版权,请立即写信通知我们
 * 4)本站提供代码只可供研究学习使用,切勿用于商业用途,由此引起一切后果与本站无关
 * 5)源码后续升级或修补,我们会在该源码跟帖发布
 */

package com.poe.lewen.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 树节点
 * @author LongShao
 *
 */
public class Node {
    private Node parent;//父节点
    private List<Node> children = new ArrayList<Node>();//子节点
    private int id;//节点id
    private int parentId;//父亲节点的id
    private String text;//节点显示的文字
    private String value;//节点的值
    private boolean isExpanded = true;//是否处于展开状态
    
    /**
     * Node构造函数
     * @param text 节点显示的文字
     * @param value 节点的值
     */
    public Node(String text,String value){
    	this.text = text;
    	this.value = value;
    }
    
    public Node() {
		super();
		// TODO Auto-generated constructor stub
		this.id=-1;
		this.parentId = -1;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public int getParentId() {
		return parentId;
	}



	public void setParentId(int parentId) {
		this.parentId = parentId;
	}



	/**
     * 设置父节点
     * @param node
     */
    public void setParent(Node node){
    	this.parent = node;
    }
    /**
     * 获得父节点
     * @return
     */
    public Node getParent(){
    	return this.parent;
    }
    /**
     * 设置节点文本
     * @param text
     */
    public void setText(String text){
    	this.text = text;
    }
    /**
     * 获得节点文本
     * @return
     */
    public String getText(){
    	return this.text;
    }
    /**
     * 设置节点值
     * @param value
     */
    public void setValue(String value){
    	this.value = value;
    }
    /**
     * 获得节点值
     * @return
     */
    public String getValue(){
    	return this.value;
    }
    /**
     * 是否根节点
     * @return
     */
    public boolean isRoot(){
    	return parent==null?true:false;
    }
    /**
     * 获得子节点
     * @return
     */
    public List<Node> getChildren(){
    	return this.children;
    }
    /**
     * 添加子节点
     * @param node
     */
    public void add(Node node){
    	if(!children.contains(node)){
    		children.add(node);
    	}
    }
    /**
     * 清除所有子节点
     */
    public void clear(){
    	children.clear();
    }
    /**
     * 删除一个子节点
     * @param node
     */
    public void remove(Node node){
    	if(!children.contains(node)){
    		children.remove(node);
    	}
    }
    /**
     * 删除指定位置的子节点
     * @param location
     */
    public void remove(int location){
    	children.remove(location);
    }
    /**
     * 获得节点的级数,根节点为0
     * @return
     */
    public int getLevel(){
    	return parent==null?0:parent.getLevel()+1;
    }
    
    /**
     * 是否叶节点,即没有子节点的节点
     * @return
     */
    public boolean isLeaf(){
    	return children.size()<1?true:false;
    }
    /**
    * 当前节点是否处于展开状态 
    * @return
    */
    public boolean isExpanded(){
        return isExpanded;
    }
    /**
     * 设置节点展开状态
     * @return
     */
    public void setExpanded(boolean isExpanded){
    	 this.isExpanded =  isExpanded;
    }
    /**
     * 递归判断父节点是否处于折叠状态,有一个父节点折叠则认为是折叠状态
     * @return
     */
    public boolean isParentCollapsed(){
    	if(parent==null)return !isExpanded;
    	if(!parent.isExpanded())return true;
    	return parent.isParentCollapsed();
    }
    /**
     * 递归判断所给的节点是否当前节点的父节点
     * @param node 所给节点
     * @return
     */
    public boolean isParent(Node node){
    	if(parent==null)return false;
    	if(node.equals(parent))return true;
    	return parent.isParent(node);
    }
}
