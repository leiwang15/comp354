/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package edu.concordia.comp354.gui.editors;

/*
 * IntegerEditor is used by TableFTFEditDemo.java.
 */

import edu.concordia.comp354.gui.ActivityEntry;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;

/**
 * Implements a cell editor that uses a formatted text field
 * to edit Integer values.
 */
public class PredecessorEditor extends DefaultCellEditor {
    private final ActivityEntry activityEntry;
    JFormattedTextField ftf;
    NumberFormat integerFormat;
    private Integer minimum, maximum;
    private boolean DEBUG = false;

    public PredecessorEditor(ActivityEntry activityEntry) {
        super(new JFormattedTextField());
        ftf = (JFormattedTextField) getComponent();
//        minimum = new Integer(min);
//        maximum = new Integer(max);

        this.activityEntry = activityEntry;

//        //Set up the editor for the integer cells.
//        integerFormat = NumberFormat.getIntegerInstance();
//        NumberFormatter intFormatter = new NumberFormatter(integerFormat);
//        intFormatter.setFormat(integerFormat);
//        intFormatter.setMinimum(minimum);
//        intFormatter.setMaximum(maximum);
//
//        ftf.setFormatterFactory(
//                new DefaultFormatterFactory(intFormatter));
//        ftf.setValue(minimum);
//        ftf.setHorizontalAlignment(JTextField.TRAILING);
//        ftf.setFocusLostBehavior(JFormattedTextField.PERSIST);

        //React when the user presses Enter while the editor is
        //active.  (Tab is handled as specified by
        //JFormattedTextField's focusLostBehavior property.)
        ftf.getInputMap().put(KeyStroke.getKeyStroke(
                        KeyEvent.VK_ENTER, 0),
                "check");
        ftf.getActionMap().put("check", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!ftf.isEditValid()) { //The text is invalid.
                    if (userSaysRevert()) { //reverted
                        ftf.postActionEvent(); //inform the editor
                    }
                } else try {              //The text is valid,
                    ftf.commitEdit();     //so use it.
                    ftf.postActionEvent(); //stop editing
                } catch (java.text.ParseException exc) {
                }
            }
        });
    }

    //Override to invoke setValue on the formatted text field.
    public Component getTableCellEditorComponent(JTable table,
                                                 Object value, boolean isSelected,
                                                 int row, int column) {
        JFormattedTextField ftf =
                (JFormattedTextField) super.getTableCellEditorComponent(
                        table, value, isSelected, row, column);
        ftf.setValue(value);
        ftf.setHorizontalAlignment(SwingConstants.LEFT);
        ftf.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return ftf;
    }

//    //Override to ensure that the value remains an Integer.
//    public Object getCellEditorValue() {
//        JFormattedTextField ftf = (JFormattedTextField)getComponent();
//        Object o = ftf.getValue();
//        if (o instanceof Integer) {
//            return o;
//        } else if (o instanceof Number) {
//            return new Integer(((Number)o).intValue());
//        } else {
//            if (DEBUG) {
//                System.out.println("getCellEditorValue: o isn't a Number");
//            }
//            try {
//                return integerFormat.parseObject(o.toString());
//            } catch (ParseException exc) {
//                System.err.println("getCellEditorValue: can't parse o: " + o);
//                return null;
//            }
//        }
//    }

    //Override to check whether the edit is valid,
    //setting the value if it is and complaining if
    //it isn't.  If it's OK for the editor to go
    //away, we need to invoke the superclass's version
    //of this method so that everything gets cleaned up.
    public boolean stopCellEditing() {
        JFormattedTextField ftf = (JFormattedTextField) getComponent();

        boolean valid = true;
        JTextField field = (JTextField) getComponent();
        if (StringUtils.isNotEmpty(field.getText())) {
            String tmp = field.getText().replaceAll("[,;]", " ");
            if (StringUtils.isNumericSpace(tmp)) {
                for (String s : tmp.split(" ")) {
                    int pred = Integer.parseInt(s);
                    if (pred < 1 || pred > activityEntry.getProjectManager().getCurrentProject().getActivities().size()) {
                        valid = false;
                    }
                }
            }
        }

        if (valid) {
            ftf.setValue(ftf.getText());

            //  now check for cycles

        } else { //text is invalid
            if (!userSaysRevert()) { //user wants to edit
                return false; //don't let the editor go away
            }
        }
        boolean stop = super.stopCellEditing();

        if (valid && activityEntry.getProjectManager().getActivityList().hasCycles()) {
            stop = userSaysRevert();
//            System.out.println("Has cycles");
        }

        return stop;
    }

    /**
     * Lets the user know that the text they entered is
     * bad. Returns true if the user elects to revert to
     * the last good value.  Otherwise, returns false,
     * indicating that the user wants to continue editing.
     */
    protected boolean userSaysRevert() {
        Toolkit.getDefaultToolkit().beep();
        ftf.selectAll();
        Object[] options = {"Edit",
                "Revert"};
        int answer = JOptionPane.showOptionDialog(
                SwingUtilities.getWindowAncestor(ftf),
                "The value must be an integer between "
                        + 1 + " and "
                        + activityEntry.getProjectManager().getCurrentProject().getActivities().size() + " and not form cycles.\n"
                        + "You can either continue editing "
                        + "or revert to the last valid value.",
                "Invalid Text Entered",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                options,
                options[1]);

        if (answer == 1) { //Revert!
            ftf.setValue(ftf.getValue());
            return true;
        }
        return false;
    }
}