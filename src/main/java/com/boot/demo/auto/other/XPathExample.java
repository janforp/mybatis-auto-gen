package com.boot.demo.auto.other;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

/**
 * 类说明：
 *
 * @author zhucj
 * @since 2020/3/14 - 下午11:08
 */
public class XPathExample {

    public static void main(String[] args) throws Exception {
        sbInsert();
        xml();
    }

    private static void sbInsert() {
        StringBuilder builder = new StringBuilder("123456");
        StringBuilder insert = builder.insert(1, false);
        System.out.println(insert);
        System.out.println(builder.toString());
    }

    private static void xml() throws Exception {
        String xmlFile = "/Users/janita/IdeaProjects/mybatis-auto-gen/src/main/resources/employees.xml";

        //Get DOM
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document xml = db.parse(xmlFile);

        //Get XPath
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();

        //Get first match
        String name = (String) xpath.evaluate("/employees/employee/firstName", xml, XPathConstants.STRING);

        System.out.println(name);   //Lokesh

        //Get all matches
        NodeList nodes = (NodeList) xpath.evaluate("/employees/employee/@id", xml, XPathConstants.NODESET);

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            String path = getPath(node);
            System.out.println("*******" + path);
            System.out.println(node.getNodeValue());   //1 2
        }

        Node evaluate = (Node) xpath.evaluate("/employees/employee/department/id", xml, XPathConstants.NODE);
        String path = getPath(evaluate);
        System.out.println(path);
    }

    //取得完全的path (a/b/c)
    public static String getPath(Node node) {
        //循环依次取得节点的父节点，然后倒序打印,也可以用一个堆栈实现
        StringBuilder builder = new StringBuilder();
        Node current = node;
        while (current != null && current instanceof Element) {
            if (current != node) {
                //在字符串的开头处添加一个 "/"
                builder.insert(0, "/");
            }
            builder.insert(0, current.getNodeName());
            current = current.getParentNode();
        }
        return builder.toString();
    }

}
