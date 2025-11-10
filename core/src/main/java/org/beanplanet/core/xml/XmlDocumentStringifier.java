/*
 * Copyright (c) 2001-present the original author or authors (see NOTICE herein).
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

package org.beanplanet.core.xml;

import org.w3c.dom.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Comparator;

public class XmlDocumentStringifier
{
    protected String encoding = "UTF-8";

    protected boolean prettyPrint = true;

    protected StringBuffer strBuf = new StringBuffer();

    protected boolean outputStartAndEndTagsForEmptyElements = true;

    protected boolean outputComments = true;

    private static class AttributeNodeComparator implements Comparator<Attr>
    {
        public int compare(Attr o1, Attr o2)
        {
            return ((Attr) o1).getNodeName().compareTo(((Attr) o2).getNodeName());
        }
    }

    private static final AttributeNodeComparator attrComparator = new AttributeNodeComparator();

    public XmlDocumentStringifier()
    {
    }

    public XmlDocumentStringifier(String encoding) {
        setEncoding(encoding);
    }

    public String getEncoding()
    {
        return encoding;
    }

    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }

    public boolean getPrettyPrint()
    {
        return prettyPrint;
    }

    public void setPrettyPrint(boolean prettyPrint)
    {
        this.prettyPrint = prettyPrint;
    }

    public boolean getOutputStartAndEndTagsForEmptyElements()
    {
        return outputStartAndEndTagsForEmptyElements;
    }

    public void setOutputStartAndEndTagsForEmptyElements(boolean outputStartAndEndTagsForEmptyElements)
    {
        this.outputStartAndEndTagsForEmptyElements = outputStartAndEndTagsForEmptyElements;
    }

    public boolean getOutputComments()
    {
        return outputComments;
    }

    public void setOutputComments(boolean outputComments)
    {
        this.outputComments = outputComments;
    }

    public String toString(Node node) throws UnsupportedEncodingException
    {
        return toString(node, getEncoding(), getPrettyPrint());
    }

    public String toString(Node node, String encoding) throws UnsupportedEncodingException
    {
        return toString(node, encoding, getPrettyPrint());
    }

    public String toString(Node node, boolean prettyPrint) throws UnsupportedEncodingException
    {
        return toString(node, getEncoding(), prettyPrint);
    }

    public String toString(Node node, String encoding, boolean prettyPrint) throws UnsupportedEncodingException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        print(node, new PrintWriter(baos), prettyPrint);
        return baos.toString(encoding);
    }

    public void print(Node node, PrintWriter pw, boolean prettyPrint)
    {
        print(node, pw, prettyPrint, 0);
    }

    /**
     * Prints the specified node, recursively.
     *
     * @param node
     *           Node to print
     * @param pw
     *           the writer used to output the document text
     * @param nesting
     *           nesting level (root element is at 0): facilitates pretty-print
     *           support
     */
    public void print(Node node, PrintWriter pw, boolean prettyPrint, int nesting)
    {
        if (node == null) return;

        int type = node.getNodeType();
        switch (type)
        {
            case Node.COMMENT_NODE:
                if (getOutputComments())
                {
                    pw.print("<!--");
                    pw.print(node.getNodeValue());
                    pw.print("-->");
                }
                break;

            // print document
            case Node.DOCUMENT_NODE:
                pw.println("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>");
                print(((Document) node).getDocumentElement(), pw, prettyPrint, 0);
                pw.flush();
                break;

            // print element with attributes
            case Node.ELEMENT_NODE:
                if (prettyPrint && nesting > 0)
                {
                    // pw.println();
                    for (int i = 0; i < nesting; i++)
                        pw.print("   ");
                }

                NodeList children = node.getChildNodes();
                pw.print('<');
                pw.print(node.getNodeName());
                Attr attrs[] = sortAttributes(node.getAttributes());
                for (int i = 0; i < attrs.length; i++)
                {
                    Attr attr = attrs[i];
                    pw.print(' ');
                    pw.print(attr.getNodeName());
                    pw.print("=\"");
                    pw.print(XmlUtil.escapeEntities(attr.getNodeValue()));
                    pw.print('"');
                }
                boolean outputEmptyElementTag = outputStartAndEndTagForElement((Element) node) == false;
                if (outputEmptyElementTag) pw.print('/'); // Empty element and no
                // end tag is required
                pw.print('>');

                if (children != null)
                {
                    int len = children.getLength();
                    for (int i = 0; i < len; i++)
                    {
                        print(children.item(i), pw, prettyPrint, nesting + 1);
                    }
                }

                if (!outputEmptyElementTag)
                {
                    pw.print("</");
                    pw.print(node.getNodeName());
                    pw.print('>');
                }
                if (prettyPrint) pw.println();
                break;

            // handle entity reference nodes
            case Node.ENTITY_REFERENCE_NODE:
            {
                pw.print('&');
                pw.print(node.getNodeName());
                pw.print(';');
                break;
            }

            // print cdata sections
            case Node.CDATA_SECTION_NODE:
                pw.print("<![CDATA[");
                pw.print(node.getNodeValue());
                pw.print("]]>");
                break;

            // print text
            case Node.TEXT_NODE:
                pw.print(XmlUtil.escapeEntities(node.getNodeValue()));
                break;

            // print processing instruction
            case Node.PROCESSING_INSTRUCTION_NODE:
                pw.print("<?");
                pw.print(node.getNodeName());
                String data = node.getNodeValue();
                if (data != null && data.length() > 0)
                {
                    pw.print(' ');
                    pw.print(data);
                }
                pw.print("?>");
                break;
        }
        pw.flush();
    }

    protected boolean outputStartAndEndTagForElement(Element element)
    {
        NodeList childList = element.getChildNodes();
        return (childList != null && childList.getLength() > 0) || getOutputStartAndEndTagsForEmptyElements();
    }

    /**
     * Returns a sorted list of attributes. Sorting is performed by names.
     *
     * @param attrs
     *           Attributes
     * @return a sorted array of attributes
     */
    protected Attr[] sortAttributes(NamedNodeMap attrs)
    {
        int len = (attrs != null) ? attrs.getLength() : 0;
        Attr atts[] = new Attr[len];
        for (int i = 0; i < len; i++)
        {
            atts[i] = (Attr) attrs.item(i);
        }
        Arrays.sort(atts, attrComparator);
        return atts;
    }
}
