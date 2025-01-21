package com.aapeli.tools;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Stack;

public class XmlUnit {
    private static final int BEFORE_STARTING_TAG = 0;
    private static final int IN_STARTING_TAG = 1;
    private static final int IN_TAG_BODY = 2;
    private static final int IN_CDATA = 3;
    private static final int IN_TAG_END = 4;
    private static final int TERMINATED = 5;
    private static final int AT_TAG_CLOSE = 6;
    private static final int ATTRIBUTE_START = 0;
    private static final int IN_ATTRIBUTE_NAME = 1;
    private static final int ATTRIBUTE_VALUE_START = 2;
    private static final int IN_ATTRIBUTE_VALUE = 3;
    private static final String validTagNameCharacters =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_";
    private static final String validAttributeCharacters =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-:";
    private String name;
    private String value;
    private List<XmlUnit> children;
    private Hashtable<String, String> attributes;

    private XmlUnit(String name) {
        this.name = name;
        this.value = null;
        this.children = new ArrayList<>();
        this.attributes = new Hashtable<>();
    }

    public static XmlUnit parseString(String data, boolean dontTrimData) throws Exception {
        data = data.trim();
        if (data.startsWith("<?xml")) {
            data = data.substring(data.indexOf('>') + 1).trim();
        }

        return parse(data, dontTrimData);
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public XmlUnit getChild(String name) {
        synchronized (this.children) {
            for (XmlUnit child : this.children) {
                if (child.getName().equals(name)) {
                    return child;
                }
            }

            return null;
        }
    }

    public String getChildValue(String name) {
        XmlUnit unit = this.getChild(name);
        return unit == null ? null : unit.getValue();
    }

    public XmlUnit[] getChildren() {
        synchronized (this.children) {
            int childrenCount = this.children.size();
            XmlUnit[] childrenArray = new XmlUnit[childrenCount];

            for (int index = 0; index < childrenCount; ++index) {
                childrenArray[index] = this.children.get(index);
            }

            return childrenArray;
        }
    }

    public XmlUnit[] getChildren(String name) {
        XmlUnit[] children = this.getChildren();
        int count = 0;

        for (XmlUnit xmlUnit : children) {
            if (xmlUnit.getName().equals(name)) {
                ++count;
            }
        }

        XmlUnit[] childrenArray = new XmlUnit[count];
        int i = 0;

        for (XmlUnit child : children) {
            if (child.getName().equals(name)) {
                childrenArray[i] = child;
                ++i;
            }
        }

        return childrenArray;
    }

    public String getAttribute(String name) {
        synchronized (this.attributes) {
            return this.attributes.get(name);
        }
    }

    private static XmlUnit parse(String data, boolean dontTrimData) throws Exception {
        Stack<XmlUnit> stack = new Stack<>();
        StringBuffer tagName = null;
        StringBuffer tagContents = null;
        XmlUnit unit = null;
        byte state = 0;
        int length = data.length();

        for (int i = 0; i < length; ++i) {
            char c = data.charAt(i);
            boolean tagWasClosed = false;
            boolean valid = false;
            if (state == BEFORE_STARTING_TAG) {
                if (c <= ' ') {
                    valid = true;
                } else if (c == '<') {
                    state = IN_STARTING_TAG;
                    tagName = new StringBuffer();
                    valid = true;
                }
            } else if (state == IN_STARTING_TAG) {
                if (validTagNameCharacters.indexOf(c) >= 0) {
                    tagName.append(c);
                    valid = true;
                } else if (c == '>' || c == '/' || c <= ' ') {
                    if (tagName.length() == 0) {
                        throw new Exception("Empty tag name");
                    }

                    unit = new XmlUnit(tagName.toString());
                    if (c <= ' ') {
                        i = parseAttribute(unit, data, i, length);
                        c = data.charAt(i);
                    }

                    if (c == '>') {
                        state = IN_TAG_BODY;
                        tagContents = new StringBuffer();
                    } else { // c == "/"
                        state = AT_TAG_CLOSE;
                    }

                    valid = true;
                }
            } else if (state == IN_TAG_BODY) {
                if (c == '<') {
                    if (data.startsWith("<![CDATA[", i)) {
                        state = IN_CDATA;
                        i += 8;
                    } else {
                        if (tagContents.length() > 0 || dontTrimData) {
                            unit.appendValue(tagContents.toString(), dontTrimData);
                        }

                        if (data.startsWith("</", i)) {
                            state = IN_TAG_END;
                            ++i;
                            tagName = new StringBuffer();
                        } else {
                            stack.push(unit);
                            state = IN_STARTING_TAG;
                            tagName = new StringBuffer();
                        }
                    }
                } else if (c == '&') {
                    i = unescape(tagContents, data, i);
                } else {
                    tagContents.append(c);
                }

                valid = true;
            } else if (state == IN_CDATA) {
                if (c == ']') {
                    if (data.startsWith("]]>", i)) {
                        state = IN_TAG_BODY;
                        i += 2;
                    } else {
                        tagContents.append(c);
                    }
                } else {
                    tagContents.append(c);
                }

                valid = true;
            } else if (state == IN_TAG_END) {
                if (validTagNameCharacters.indexOf(c) >= 0) {
                    tagName.append(c);
                    valid = true;
                } else if (c == '>') {
                    if (tagName.length() == 0) {
                        throw new Exception("Empty end tag name");
                    }

                    if (!unit.getName().contentEquals(tagName)) {
                        throw new Exception(
                                "End tag name (" + tagName + ") is different than start tag (" + unit.getName() + ")");
                    }

                    tagWasClosed = true;
                    valid = true;
                }
            } else if (state == TERMINATED) {
                if (c <= ' ') {
                    valid = true;
                }
            } else if (state == AT_TAG_CLOSE && c == '>') {
                tagWasClosed = true;
                valid = true;
            }

            if (!valid) {
                throw new Exception("Unexpected character '" + c + "'");
            }

            if (tagWasClosed) {
                if (stack.empty()) {
                    state = TERMINATED;
                } else {
                    XmlUnit parent = stack.pop();
                    parent.addChild(unit);
                    unit = parent;
                    state = IN_TAG_BODY;
                    tagContents = new StringBuffer();
                }
            }
        }

        if (state != TERMINATED) {
            throw new Exception("Premature end of xml data");
        } else {
            return unit;
        }
    }

    private static int parseAttribute(XmlUnit unit, String data, int i, int length) throws Exception {
        StringBuffer name = null;
        StringBuffer value = null;
        byte state = 0;
        char quotationMark = 0;

        char c;
        boolean valid;
        do {
            ++i;
            if (i == length) {
                throw new Exception("Premature end of attribute data");
            }

            c = data.charAt(i);
            valid = false;
            if (state == ATTRIBUTE_START) {
                if (validAttributeCharacters.indexOf(c) >= 0) {
                    state = IN_ATTRIBUTE_NAME;
                    name = new StringBuffer();
                    name.append(c);
                    valid = true;
                } else if (c == ' ') {
                    valid = true;
                } else if (c == '/' || c == '>') {
                    return i;
                }
            } else if (state == IN_ATTRIBUTE_NAME) {
                if (validAttributeCharacters.indexOf(c) >= 0) {
                    name.append(c);
                    valid = true;
                }

                if (c == '=') {
                    state = ATTRIBUTE_VALUE_START;
                    valid = true;
                }
            } else if (state == ATTRIBUTE_VALUE_START) {
                if (c == '"' || c == '\'') {
                    state = IN_ATTRIBUTE_VALUE;
                    quotationMark = c;
                    value = new StringBuffer();
                    valid = true;
                }
            } else if (state == IN_ATTRIBUTE_VALUE) {
                if (c != quotationMark) {
                    if (c == '&') {
                        i = unescape(value, data, i);
                    } else {
                        value.append(c);
                    }
                } else {
                    state = ATTRIBUTE_START;
                    unit.addAttribute(name.toString(), value.toString());
                }

                valid = true;
            }
        } while (valid);

        throw new Exception("Unexpected character '" + c + "' in attributes");
    }

    private static int unescape(StringBuffer buffer, String data, int offset) {
        if (data.startsWith("&amp;", offset)) {
            buffer.append('&');
            return offset + 4;
        } else if (data.startsWith("&lt;", offset)) {
            buffer.append('<');
            return offset + 3;
        } else if (data.startsWith("&gt;", offset)) {
            buffer.append('>');
            return offset + 3;
        } else if (data.startsWith("&quot;", offset)) {
            buffer.append('\"');
            return offset + 5;
        } else if (data.startsWith("&apos;", offset)) {
            buffer.append('\'');
            return offset + 5;
        } else if (data.startsWith("&#", offset)) {
            int startIndex;
            byte base;
            if (data.charAt(offset + 2) == 'x') {
                startIndex = offset + 3;
                base = 16;
            } else {
                startIndex = offset + 2;
                base = 10;
            }

            int endIndex = data.indexOf(';', startIndex);
            buffer.append((char) Integer.parseInt(data.substring(startIndex, endIndex), base));
            return endIndex;
        } else {
            buffer.append('&');
            return offset;
        }
    }

    private XmlUnit appendValue(String value, boolean dontTrim) {
        if (!dontTrim) {
            value = value.trim();
            if (value.length() == 0) {
                return this;
            }
        }

        if (this.value == null) {
            this.value = value;
        } else {
            this.value = this.value + " " + value;
        }

        return this;
    }

    private XmlUnit addChild(XmlUnit child) {
        synchronized (this.children) {
            this.children.add(child);
            return this;
        }
    }

    private XmlUnit addAttribute(String key, String value) {
        if (key.indexOf(' ') >= 0) {
            return this;
        } else {
            int containsSingleQuote = value.indexOf('\'');
            int containsDoubleQuote = value.indexOf('"');
            if (containsSingleQuote >= 0 && containsDoubleQuote >= 0) {
                return this;
            } else {
                synchronized (this.attributes) {
                    this.attributes.put(key, value);
                    return this;
                }
            }
        }
    }
}
