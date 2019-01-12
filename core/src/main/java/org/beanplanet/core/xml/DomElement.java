package org.beanplanet.core.xml;

import org.beanplanet.core.UncheckedException;
import org.beanplanet.core.dao.CardinalityException;
import org.beanplanet.core.dao.DataAccessException;
import org.beanplanet.core.dao.TooFewCardinalityException;
import org.beanplanet.core.dao.TooManyCardinalityException;
import org.beanplanet.core.io.IoUtil;
import org.beanplanet.core.io.resource.FileResource;
import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.io.resource.UriBasedResourceImpl;
import org.beanplanet.core.util.StringUtil;
import org.w3c.dom.*;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * XML DOM Element wrapper. Provides methods that the <a
 * href="http://www.w3.org/DOM/">W3C DOM</A> should!
 *
 * @author Gary Watson
 */
public class DomElement {
    private Element element;
    // private NamespaceResolver namespaceResolver;
    protected NamespaceContext namespaceContext;

    public DomElement(Element fromNode) {
        element = fromNode;
    }

    public DomElement(DomElement other) {
        element = other.element;
        namespaceContext = other.namespaceContext;
    }

    public DomElement(Element fromNode, NamespaceContext namespaceContext) {
        element = fromNode;
        setNamespaceContext(namespaceContext);
    }

    public DomElement(String documentElementTagName) {
        this(createNewDocumentElement(documentElementTagName));
    }

    public DomElement(File documentFile) throws IOException, DataAccessException {
        this(documentFile, false);
    }

    public DomElement(File documentFile, boolean namespaceAware) throws IOException, DataAccessException {
        this(createDocument(documentFile, namespaceAware));
    }

    public DomElement(File documentFile, boolean namespaceAware, NamespaceContext namespaceContext) throws IOException, DataAccessException {
        this(createDocument(documentFile, namespaceAware, namespaceContext));
    }

    public DomElement(URL documentURL) throws IOException, DataAccessException {
        this(documentURL, false);
    }

    public DomElement(URL documentURL, boolean namespaceAware) throws IOException, DataAccessException {
        this(createDocument(documentURL, namespaceAware));
    }

    public DomElement(URL documentURL, boolean namespaceAware, NamespaceContext namespaceContext) throws IOException, DataAccessException {
        this(createDocument(documentURL, namespaceAware, namespaceContext));
    }

    public DomElement(InputStream is) throws DataAccessException {
        this(createDocument(is));
    }

    public DomElement(InputStream is, boolean namespaceAware) throws DataAccessException {
        this(createDocument(is, namespaceAware));
    }

    public DomElement(InputStream is, boolean namespaceAware, NamespaceContext namespaceContext) throws DataAccessException {
        this(createDocument(is, namespaceAware, namespaceContext));
    }

    public DomElement(Resource resource) throws DataAccessException {
        this(resource, false);
    }

    public DomElement(Resource resource, boolean namespaceAware) throws DataAccessException {
        InputStream is = null;
        try {
            is = resource.getInputStream();
            element = createDocument(is, namespaceAware).getElement();
        } finally {
            IoUtil.closeIgnoringErrors(is);
        }
    }

    public DomElement(Resource resource, boolean namespaceAware, NamespaceContext namespaceContext) throws DataAccessException {
        InputStream is = null;
        try {
            is = resource.getInputStream();
            element = createDocument(is, namespaceAware, namespaceContext).getElement();
        } finally {
            IoUtil.closeIgnoringErrors(is);
        }
    }

    public Element getElement() {
        return element;
    }

    public DomElement addElement(String tagNamePath) throws DataAccessException {
        return addElement(tagNamePath, getNamespaceContext());
    }

    public DomElement addElement(String tagNamePath, NamespaceContext namespaceContext) throws DataAccessException {
        String path[] = tagNamePath.split("\\/");

        DomElement current = this;
        for (int n = 0; n < path.length; n++) {
            String elementName = path[n];
            int colonPos = elementName.indexOf(':');
            Element newElement;
            if (namespaceContext == null || colonPos < 0) {
                newElement = element.getOwnerDocument().createElement(elementName);
            } else {
                String nsPrefix = elementName.substring(0, colonPos);
                String localPart = elementName.substring(colonPos + 1);
                String nsURI = namespaceContext.getNamespaceURI(nsPrefix);

                if (nsURI == null) {
                    throw new DataAccessException("Namespace with prefix \"" + nsPrefix + "\" could not be resolved to any URI when adding elements \""
                            + tagNamePath + "\" to \"" + getAbsolutePath() + "\"");
                }
                newElement = element.getOwnerDocument().createElementNS(nsURI, localPart);
            }

            current.getElement().appendChild(newElement);
            current = new DomElement(newElement, namespaceContext);
        }
        return current;
    }

    public DomElement addElement(String tagNamePath, String textualContent) throws DataAccessException {
        return addElement(tagNamePath, textualContent, getNamespaceContext());
    }

    public DomElement addElementWithCData(String tagNamePath, String textualContent) throws DataAccessException {
        DomElement newElement = addElement(tagNamePath, getNamespaceContext());
        CDATASection cDataSection = newElement.getElement().getOwnerDocument().createCDATASection(textualContent);
        newElement.getElement().appendChild(cDataSection);

        return newElement;
    }

    public DomElement addElement(String tagNamePath, String textualContent, NamespaceContext namespaceContext) throws DataAccessException {
        DomElement childElement = addElement(tagNamePath, namespaceContext);
        childElement.setTextualValue(textualContent);
        return childElement;
    }

    public DomElement addElementNS(String namespace, String tagNamePath) throws DataAccessException {
        String path[] = tagNamePath.split("\\/");

        DomElement current = this;
        for (int n = 0; n < path.length; n++) {
            Element newElement = element.getOwnerDocument().createElementNS(namespace, path[n]);
            current.getElement().appendChild(newElement);
            current = new DomElement(newElement, namespaceContext);
        }
        return current;
    }

    public DomElement addElementNS(String namespace, String tagNamePath, String textualContent) throws DataAccessException {
        DomElement childElement = addElementNS(namespace, tagNamePath);
        childElement.setTextualValue(textualContent);
        return childElement;
    }

    public void setAttribute(String name, String value) {
        this.element.setAttribute(name, value);
    }

    public DomElement cloneElement(boolean deep) throws DataAccessException {
        return null;
    }

    public DomElement copyContentsTo(DomElement targetParent) throws DataAccessException {
        return null;
    }

    public DomElement copyToParent(DomElement targetParent) throws DataAccessException {
        return null;
    }

    public static DomElement createDocument() throws DataAccessException {
        return createDocument("ROOT");
    }

    public static DomElement createDocument(InputStream is) throws DataAccessException {
        return createDocument(is, false);
    }

    public static DomElement createDocument(InputStream is, boolean namespaceAware) throws DataAccessException {
        return createDocument(is, namespaceAware, null);
    }

    public static DomElement createDocument(InputStream is, boolean namespaceAware, NamespaceContext namespaceContext) throws DataAccessException {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(namespaceAware);
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document newdoc = db.parse(is);
            DomElement DomElement = (namespaceContext != null ? new DomElement(newdoc.getDocumentElement(), namespaceContext) : new DomElement(newdoc
                    .getDocumentElement()));
            return DomElement;
        } catch (Exception ex) {
            throw new DataAccessException("Error creating new XML document.", ex);
        }
    }

    public static DomElement createDocument(File documentFile) throws DataAccessException {
        return createDocument(new FileResource(documentFile));
    }

    public static DomElement createDocument(File documentFile, boolean namespaceAware) throws DataAccessException {
        return createDocument(new FileResource(documentFile), namespaceAware);
    }

    public static DomElement createDocument(File documentFile, boolean namespaceAware, NamespaceContext namespaceContext) throws DataAccessException {
        return createDocument(new FileResource(documentFile), namespaceAware, namespaceContext);
    }

    public static DomElement createDocument(URL documentURL) throws DataAccessException {
        try {
            return createDocument(new UriBasedResourceImpl(documentURL.toURI()));
        } catch (URISyntaxException syntaxEx) {
            throw new DataAccessException(syntaxEx);
        }
    }

    public static DomElement createDocument(URL documentURL, boolean namespaceAware) throws DataAccessException {
        try {
            return createDocument(new UriBasedResourceImpl(documentURL.toURI()), namespaceAware);
        } catch (URISyntaxException syntaxEx) {
            throw new DataAccessException(syntaxEx);
        }
    }

    public static DomElement createDocument(URL documentURL, boolean namespaceAware, NamespaceContext namespaceContext) throws DataAccessException {
        try {
            return createDocument(new UriBasedResourceImpl(documentURL.toURI()), namespaceAware, namespaceContext);
        } catch (URISyntaxException syntaxEx) {
            throw new DataAccessException(syntaxEx);
        }
    }

    public static DomElement createDocument(Resource resource) throws DataAccessException {
        return createDocument(resource, false);
    }

    public static DomElement createDocument(Resource resource, boolean namespaceAware) throws DataAccessException {
        return createDocument(resource, namespaceAware, null);
    }

    public static DomElement createDocument(Resource resource, boolean namespaceAware, NamespaceContext namespaceContext) throws DataAccessException {
        InputStream is = null;
        try {
            is = resource.getInputStream();
            return createDocument(is, namespaceAware, namespaceContext);
        } finally {
            IoUtil.closeIgnoringErrors(is);
        }
    }

    public static DomElement createDocumentFromDocumentString(String xmlDocument) throws DataAccessException {
        return createDocumentFromDocumentString(xmlDocument, false);
    }

    public static DomElement createDocumentFromDocumentString(String xmlDocument, boolean namespaceAware) throws DataAccessException {
        return createDocument(new ByteArrayInputStream(xmlDocument.getBytes()), namespaceAware);
    }

    public static Element createNewDocumentElement(String documentElementTagName) throws DataAccessException {
        return createDocument(null, documentElementTagName).getElement();
    }

    public static Element createNewDocumentElement(String namespaceURI, String documentElementTagName) throws DataAccessException {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(namespaceURI != null);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document newdoc = db.newDocument();

            Element root;
            if (namespaceURI != null) {
                root = newdoc.createElementNS(namespaceURI, documentElementTagName);
            } else {
                root = newdoc.createElement(documentElementTagName);
            }
            newdoc.appendChild(root);

            return root;
        } catch (Exception ex) {
            throw new DataAccessException("Error creating new XML document.", ex);
        }
    }

    public static DomElement createDocument(String documentElementTagName) throws DataAccessException {
        return new DomElement(createNewDocumentElement(documentElementTagName));
    }

    public static DomElement createDocument(String namespaceURI, String documentElementTagName) throws DataAccessException {
        return new DomElement(createNewDocumentElement(namespaceURI, documentElementTagName));
    }

    public static DomElement createUnconnectedElement(Element relatedDocumentElement, String elementTagName) throws DataAccessException {
        return null;
    }

    public DomElement getElement(String xpathExpr) throws TooFewCardinalityException, TooManyCardinalityException {
        return getElement(xpathExpr, getNamespaceContext());
    }

    public DomElement getElement(String xpathExpr, NamespaceContext namespaceContext) throws TooFewCardinalityException, TooManyCardinalityException {
        NodeList nl = null;
        try {
            nl = (NodeList) getOrCreateXPathExpression(xpathExpr, namespaceContext).evaluate(element, XPathConstants.NODESET);
        } catch (Exception ex) {
            throw new DataAccessException("Error creating new XML document.", ex);
        }

        int numberOfElements = nl.getLength();

        if (numberOfElements == 1) {
            return new DomElement((Element) nl.item(0), (namespaceContext != null ? namespaceContext : this.namespaceContext));
        } else if (numberOfElements == 0) {
            throw new TooFewCardinalityException("Unexpected number of elements found (zero) when expecting exactly one for XPath expression, \"" + xpathExpr
                    + "\"");
        } else {
            throw new TooManyCardinalityException("Unexpected number of elements found (" + numberOfElements
                    + ") when expecting exactly one for XPath expression, \"" + xpathExpr + "\"");
        }
    }

    public DomElement getElementOrNull(String xpathExpr) throws TooManyCardinalityException {
        return getElementOrNull(xpathExpr, getNamespaceContext());
    }

    public DomElement getElementOrNull(String xpathExpr, NamespaceContext nsResolver) {
        try {
            return getElement(xpathExpr, nsResolver);
        } catch (TooManyCardinalityException tooFew) {
            return null;
        }
    }

    public List<DomElement> getElements(String xpathExpr) throws DataAccessException {
        return getElements(xpathExpr, getNamespaceContext());
    }

    public List<DomElement> getElements(String xpathExpr, NamespaceContext namespaceContext) throws DataAccessException {
        try {
            NodeList nl = (NodeList) getOrCreateXPathExpression(xpathExpr, namespaceContext).evaluate(element, XPathConstants.NODESET);
            int numberOfElements = nl.getLength();

            List<DomElement> elements = new ArrayList<DomElement>(numberOfElements);
            for (int n = 0; n < numberOfElements; n++) {
                elements.add(new DomElement((Element) nl.item(n), (namespaceContext != null ? namespaceContext : this.namespaceContext)));
            }

            return elements;
        } catch (Exception ex) {
            throw new DataAccessException("Error returning XML Elements for XPath expression, \"" + xpathExpr + "\".", ex);
        }
    }

    public Element[] getW3CDomElementsArray(String xpathExpr) {
        return getW3CDomElementsArray(xpathExpr, namespaceContext);
    }

    public Element[] getW3CDomElementsArray(String xpathExpr, NamespaceContext namespaceContext) {
        List<DomElement> domList = getElements(xpathExpr, (namespaceContext != null ? namespaceContext : this.namespaceContext));
        Element w3cElements[] = new Element[domList.size()];
        for (int n = 0; n < domList.size(); n++) {
            w3cElements[n] = (domList.get(n)).getElement();
        }
        return w3cElements;
    }

    public DomElement[] getDomElementsArray(String xpathExpr) {
        List<DomElement> domList = getElements(xpathExpr);
        return domList.toArray(new DomElement[domList.size()]);
    }

    @Override
    public String toString() throws DataAccessException {
        return getTextualValue();
    }

    public List<DomElement> getNestedElements() throws DataAccessException {
        return getElements(".//");
    }

    public Collection<String> getAttributeNames() throws DataAccessException {
        return getAttributesNvMap().keySet();
    }

    public String getAttributeOrEmptyString(String attributeName) throws DataAccessException {
        return getAttribute(attributeName, "");
    }

    public String getAttributeOrNull(String attributeName) {
        return getAttribute(attributeName, null);
    }

    public String getAttribute(String attributeName, String defaultValue) throws DataAccessException {
        return element.hasAttribute(attributeName) ? element.getAttribute(attributeName) : defaultValue;
    }

    public Map<String, String> getAttributesNvMap() throws DataAccessException {
        Map<String, String> attMvMap = new LinkedHashMap<>();
        NamedNodeMap attributesMap = element.getAttributes();
        int numberOfAttributes = attributesMap.getLength();
        for (int n = 0; n < numberOfAttributes; n++) {
            Node attr = attributesMap.item(n);
            String attName = attr.getNodeName();
            attMvMap.put(attName, element.getAttribute(attName));
        }
        return attMvMap;
    }

    public List<DomElement> getChildElements() {
        List<DomElement> elements = new ArrayList<DomElement>();
        Node currentNode = element.getFirstChild();
        while (currentNode != null) {
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                elements.add(new DomElement((Element) currentNode, namespaceContext));
            }
            currentNode = currentNode.getNextSibling();
        }

        return elements;
    }

    public List<DomElement> getChildElements(String tagName) {
        List<DomElement> elements = new ArrayList<DomElement>();
        Node currentNode = element.getFirstChild();
        while (currentNode != null) {
            if (currentNode.getNodeType() == Node.ELEMENT_NODE && ((Element) currentNode).getTagName().equals(tagName)) {
                elements.add(new DomElement((Element) currentNode, namespaceContext));
            }
            currentNode = currentNode.getNextSibling();
        }

        return elements;
    }

    public DomElement getFirstChildElement() throws DataAccessException {
        DomElement firstChildElem = getFirstChildElementOrNull();
        if (firstChildElem == null) {
            throw new TooFewCardinalityException("Unable to find first element node under \"" + getAbsolutePath());
        }

        return firstChildElem;
    }

    public DomElement getFirstChildElementOrNull() {
        Node currentNode = element.getFirstChild();
        while (currentNode != null) {
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                return new DomElement((Element) currentNode, namespaceContext);
            }
            currentNode = currentNode.getNextSibling();
        }

        return null;
    }

    public DomElement getFirstChildElement(String tagName) throws DataAccessException {
        return getElement("child::" + tagName);
    }

    public DomElement getFirstChildElementOrNull(String tagName) {
        try {
            return getFirstChildElement(tagName);
        } catch (DataAccessException ignoreEx) {
            return null;
        }
    }

    public List<DomElement> getChildNodes() {
        List<DomElement> nodes = new ArrayList<DomElement>();
        Node currentNode = element.getFirstChild();
        while (currentNode != null) {
            nodes.add(new DomElement((Element) currentNode, namespaceContext));
            currentNode = currentNode.getNextSibling();
        }

        return nodes;
    }

    public DomElement getPreceedingSiblingElement() {
        Node currentNode = element.getPreviousSibling();
        while (currentNode != null && currentNode.getNodeType() != Node.ELEMENT_NODE) {
            currentNode.getPreviousSibling();
        }
        return new DomElement((Element) currentNode);
    }

    public List<DomElement> getPreceedingSiblingElements() {
        List<DomElement> elements = new ArrayList<DomElement>();
        Node currentNode = element.getPreviousSibling();
        while (currentNode != null) {
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                elements.add(new DomElement((Element) currentNode, namespaceContext));
            }
            currentNode = currentNode.getPreviousSibling();
        }

        return elements;
    }

    public DomElement getFollowingSiblingElement() {
        Node currentNode = element.getNextSibling();
        while (currentNode != null && currentNode.getNodeType() != Node.ELEMENT_NODE) {
            currentNode.getNextSibling();
        }
        return new DomElement((Element) currentNode, namespaceContext);
    }

    public List<DomElement> getFollowingSiblingElements() {
        List<DomElement> elements = new ArrayList<DomElement>();
        Node currentNode = element.getNextSibling();
        while (currentNode != null) {
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                elements.add(new DomElement((Element) currentNode, namespaceContext));
            }
            currentNode = currentNode.getNextSibling();
        }

        return elements;
    }

    public DomElement getParentElementOrNull() {
        Node parent = element.getParentNode();
        return (parent instanceof Element ? new DomElement((Element) parent, namespaceContext) : null);
    }

    public String getAbsolutePath() {
        StringBuffer sBuf = new StringBuffer();
        getAbsolutePath(sBuf);
        return sBuf.toString();
    }

    public void getAbsolutePath(StringBuffer sBuf) {
        DomElement parent = getParentElementOrNull();
        if (parent != null) {
            parent.getAbsolutePath(sBuf);
        }
        sBuf.append("/").append(getElement().getTagName());
    }

    public DomElement getParentElementOrSelf(Element element) {
        DomElement parentElem = getParentElementOrNull();
        return parentElem != null ? parentElem : this;
    }

    public DomElement getRootElement() {
        return new DomElement(element.getOwnerDocument().getDocumentElement(), namespaceContext);
    }

    public boolean hasChildNodes() {
        return element.hasChildNodes();
    }

    public boolean hasChildElements() {
        Node currentNode = element.getFirstChild();
        while (currentNode != null) {
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                return true;
            }

            currentNode = currentNode.getNextSibling();
        }

        return false;
    }

    public boolean hasContent() {
        return hasChildNodes();
    }

    public boolean hasAttribute(String attributeName) {
        return element.hasAttribute(attributeName);
    }

    public DomElement removeAllChildNodes() {
        while (element.hasChildNodes()) {
            element.removeChild(element.getFirstChild());
        }
        return this;
    }

    public DomElement setTextualValue(String textualContent) {
        // Remove all existing child text nodes
        Node child = element.getFirstChild();
        while (child != null) {
            if (child instanceof Text) {
                element.removeChild(child);
            }
            child = child.getNextSibling();
        }

        if (textualContent != null) {
            element.appendChild(element.getOwnerDocument().createTextNode(textualContent));
        }

        return this;
    }

    public String getTextualValue() {
        return getTextualValue(true);
    }

    public String getTextualValue(boolean deep) {
        StringBuffer sBuf = new StringBuffer();
        getTextualValue(element, sBuf, deep);
        return sBuf.toString();
    }

    public String getTextualValue(String xPathExpr, String defaultValue) {
        return getTextualValue(xPathExpr, defaultValue, true);
    }

    public String getTextualValue(String xPathExpr, String defaultValue, boolean deep) {
        try {
            return getElement(xPathExpr).getTextualValue(deep);
        } catch (CardinalityException ex) {
            return defaultValue;
        }
    }

    public void getTextualValue(Node fromNode, StringBuffer sBuf, boolean deep) {
        if (fromNode instanceof Attr) {
            sBuf.append(fromNode.getNodeValue());
            return;
        }

        Node childNode = fromNode.getFirstChild();
        while (childNode != null) {
            switch (childNode.getNodeType()) {
                case Node.CDATA_SECTION_NODE:
                case Node.COMMENT_NODE:
                case Node.TEXT_NODE:
                    sBuf.append(childNode.getNodeValue());
                    break;
                case Node.ELEMENT_NODE:
                    if (deep) {
                        getTextualValue(childNode, sBuf, deep);
                    }
                    break;
            }
            childNode = childNode.getNextSibling();
        }
    }

    public void getTextualValue(String xPathExpr, Node fromNode, StringBuffer sBuf, boolean deep) throws TooFewCardinalityException, TooManyCardinalityException {
        getElement(xPathExpr).getTextualValue(fromNode, sBuf, deep);
    }

    public String getXMLString() {
        return getXMLString(element);
    }

    public String getXMLString(String encoding) throws UnsupportedEncodingException {
        return getXMLString(element, encoding);
    }

    public static String getXMLString(Node node) throws DataAccessException {
        try {
            return getXMLString(node, StringUtil.getDefaultCharacterEncoding());
        } catch (Throwable th) {
            throw new DataAccessException("Unable to stringify XML DOM: ", th);
        }
    }

    public static String getXMLString(Node node, String encoding) throws UnsupportedEncodingException {
        XmlDocumentStringifier stringifier = new XmlDocumentStringifier(encoding);
        return stringifier.toString(node);
    }

    public String getXMLDocumentString() {
        return getXMLDocumentString(element);
    }

    public static String getXMLDocumentString(Node node) throws DataAccessException {
        try {
            return getXMLDocumentString(node, StringUtil.getDefaultCharacterEncoding());
        } catch (Throwable th) {
            throw new DataAccessException("Error - unexpected error using ISO-8849-1 (Latin-1) encoding: ", th);
        }
    }

    public static String getXMLDocumentString(Node node, String encoding) throws UnsupportedEncodingException {
        XmlDocumentStringifier stringifier = new XmlDocumentStringifier(encoding);
        return stringifier.toString(node.getOwnerDocument());
    }

    public NamespaceContext getNamespaceContext() {
        return namespaceContext;
    }

    public void setNamespaceContext(NamespaceContext namespaceContext) {
        this.namespaceContext = namespaceContext;
    }

    public boolean evalXPATHBoolean(String xpath) {
        return evalXPATHBoolean(xpath, namespaceContext);
    }

    public boolean evalXPATHBoolean(String xpathExpr, NamespaceContext namespaceContext) {
        try {
            return (Boolean) getOrCreateXPathExpression(xpathExpr, namespaceContext).evaluate(element, XPathConstants.BOOLEAN);
        } catch (Exception ex) {
            throw new DataAccessException("Error evaulating boolean XPath expression [" + xpathExpr + "]: ", ex);
        }
    }

    public Number evalXPATHNumber(String xpathExpr, NamespaceContext namespaceContext) {
        try {
            return (Number) getOrCreateXPathExpression(xpathExpr, namespaceContext).evaluate(element, XPathConstants.NUMBER);
        } catch (Exception ex) {
            throw new DataAccessException("Error evaulating XPath expression [" + xpathExpr + "] as a number: ", ex);
        }
    }

    public int evalXPATHInteger(String xpathExpr) {
        return evalXPATHNumber(xpathExpr, namespaceContext).intValue();
    }

    public int evalXPATHInteger(String xpathExpr, NamespaceContext namespaceContext) {
        return evalXPATHNumber(xpathExpr, namespaceContext).intValue();
    }

    public long evalXPATHLong(String xpathExpr) {
        return evalXPATHLong(xpathExpr, namespaceContext);
    }

    public long evalXPATHLong(String xpathExpr, NamespaceContext namespaceContext) {
        return evalXPATHNumber(xpathExpr, namespaceContext).longValue();
    }

    public double evalXPATHDouble(String xpathExpr) {
        return evalXPATHNumber(xpathExpr, namespaceContext).doubleValue();
    }

    public double evalXPATHDouble(String xpathExpr, NamespaceContext namespaceContext) {
        return evalXPATHNumber(xpathExpr, namespaceContext).doubleValue();
    }

    public boolean removeFromParent() {
        Node parentNode = element.getParentNode();
        if (parentNode == null) {
            return false;
        }
        parentNode.removeChild(element);
        return true;
    }

    public XPath getOrCreateXPath() {
        return createXPath(this.namespaceContext);
    }

    public XPath getOrCreateXPath(NamespaceContext namespaceContext) {
        return createXPath(namespaceContext);
    }

    public XPathExpression getOrCreateXPathExpression(String expression) {
        return createXPathExpression(expression, this.namespaceContext);
    }

    public XPathExpression getOrCreateXPathExpression(String expression, NamespaceContext namespaceContext) {
        return createXPathExpression(expression, namespaceContext);
    }

    public static final XPath createXPath() {
        return createXPath(null);
    }

    public static final XPath createXPath(NamespaceContext namespaceContext) {
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        if (namespaceContext != null) {
            xpath.setNamespaceContext(namespaceContext);
        }
        return xpath;
    }

    public static final XPathExpression createXPathExpression(String expression) {
        return createXPathExpression(expression, null);
    }

    public static final XPathExpression createXPathExpression(String expression, NamespaceContext namespaceContext) {
        try {
            return createXPath(namespaceContext).compile(expression);
        } catch (XPathExpressionException xpathEx) {
            throw new UncheckedException(xpathEx);
        }
    }
}
