package com.mrsalwater.kapteyn.ui.ui.panel.code;

import com.mrsalwater.kapteyn.ui.ui.theme.UITheme;
import com.mrsalwater.kapteyn.ui.ui.theme.UIThemeManager;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CodeStyledDocument extends DocumentFilter {

    private static final String[] KEYWORDS = new String[]{"abstract", "char", "class", "double", "enum", "extends", "final", "float", "implements", "int", "interface", "long", "native", "private", "protected", "public", "short", "static", "throws", "transient", "void", "volatile", "null"};

    private static Pattern createKeywordPattern() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0, length = KEYWORDS.length; i < length; i++) {
            builder.append("\\b").append(KEYWORDS[i]).append("\\b");
            if (i + 1 != length) builder.append("|");
        }

        return Pattern.compile(builder.toString());
    }

    private final JTextPane textPane;
    private final StyledDocument styledDocument;

    private final AttributeSet attributeSetWhite;
    private final AttributeSet attributeSetBlack;

    private final AttributeSet attributeSetOrange;
    private final AttributeSet attributeSetBlue;
    private final AttributeSet attributeSetGray;

    private final AttributeSet attributeSetBold;

    private final Pattern pattern;

    public CodeStyledDocument(JTextPane textPane) {
        this.textPane = textPane;
        this.styledDocument = textPane.getStyledDocument();

        StyleContext styleContext = StyleContext.getDefaultStyleContext();

        attributeSetWhite = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, new Color(169, 183, 198));
        attributeSetBlack = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.BLACK);

        attributeSetOrange = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, new Color(204, 120, 50));
        attributeSetBlue = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, new Color(0, 0, 128));
        attributeSetGray = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, new Color(128, 128, 128));

        attributeSetBold = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Bold, true);

        pattern = createKeywordPattern();

        ((AbstractDocument) textPane.getDocument()).setDocumentFilter(this);
    }

    public void updateTextStyles() {
        AttributeSet attributeSetText;
        AttributeSet attributeSetKeyword;

        if (UIThemeManager.getTheme() == UITheme.DARK) {
            attributeSetText = attributeSetWhite;
            attributeSetKeyword = attributeSetOrange;
        } else {
            attributeSetText = attributeSetBlack;
            attributeSetKeyword = attributeSetBlue;
        }

        styledDocument.setCharacterAttributes(0, textPane.getText().length(), attributeSetText, true);

        int position = 0;
        String[] lines = textPane.getText().split("\n");
        for (String line : lines) {
            boolean inlineComment = updateTextStyleInlineComment(position, line);

            if (!inlineComment) {
                updateTextStyleKeyword(position, line, attributeSetKeyword);
                updateTextStyleBlockComment(position, line);
            }

            position += line.length() + 1;
        }
    }

    private boolean updateTextStyleInlineComment(int position, String line) {
        char[] chars = line.toCharArray();
        for (int i = 0, length = chars.length; i < length; i++) {
            char c = chars[i];

            if (!Character.isSpaceChar(c)) {
                if (c == '/' && i++ != length && chars[i] == '/') {
                    styledDocument.setCharacterAttributes(position, line.length(), attributeSetGray, false);
                    return true;
                }

                return false;
            }
        }

        return false;
    }


    private void updateTextStyleKeyword(int position, String line, AttributeSet attributeSetKeyword) {
        Matcher matcher = pattern.matcher(line);

        while (matcher.find()) {
            int start = position + matcher.start();
            int end = matcher.end() - matcher.start();

            styledDocument.setCharacterAttributes(start, end, attributeSetKeyword, false);
            styledDocument.setCharacterAttributes(start, end, attributeSetBold, false);
        }
    }


    private void updateTextStyleBlockComment(int position, String line) {
        boolean inBlockComment = false;
        int inBlockStart = 0;

        char[] chars = line.toCharArray();
        for (int i = 0, length = chars.length; i < length; i++) {
            char c = chars[i];

            if (inBlockComment) {
                if (c == '*' && i++ != length && chars[i] == '/') {
                    inBlockComment = false;
                    styledDocument.setCharacterAttributes(position + inBlockStart, i - inBlockStart, attributeSetGray, true);
                }
            } else if (c == '/' && i + 1 != length && chars[i + 1] == '*') {
                inBlockComment = true;
                inBlockStart = i;
            }
        }
    }

}
