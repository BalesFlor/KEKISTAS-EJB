package com.grsc.exceptions;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

public class LetterOnlyVerifier extends InputVerifier {
    public boolean verify(JComponent input) {
        String text = ((JTextField) input).getText();
        return text.matches("[a-zA-Z]*");
    }
}