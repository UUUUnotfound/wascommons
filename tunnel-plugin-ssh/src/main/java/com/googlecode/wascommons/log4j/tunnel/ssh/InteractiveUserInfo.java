/*
 * Copyright 2010 Andreas Veithen
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
package com.googlecode.wascommons.log4j.tunnel.ssh;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.jcraft.jsch.UserInfo;

public class InteractiveUserInfo implements UserInfo {
    private String password;
    private String passphrase;
    
    public String getPassword() {
        return password;
    }
    
    public String getPassphrase() {
        return passphrase;
    }
    
    private String doPromptPassword(String message) {
        JTextField password = new JPasswordField(20);
        if (JOptionPane.showConfirmDialog(null, new Object[] { password }, message, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            return password.getText();
        } else {
            return null;
        }
    }
    
    public boolean promptYesNo(String str) {
        Object[] options = { "yes", "no" };
        return JOptionPane.showOptionDialog(null,
             str,
             "Warning",
             JOptionPane.DEFAULT_OPTION,
             JOptionPane.WARNING_MESSAGE,
             null, options, options[0]) == 0;
    }
    
    public boolean promptPassphrase(String message) {
        passphrase = doPromptPassword(message);
        return passphrase != null;
    }
    
    public boolean promptPassword(String message) {
        password = doPromptPassword(message);
        return password != null;
    }
    
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
    
    public String[] promptKeyboardInteractive(String destination,
                                              String name,
                                              String instruction,
                                              String[] prompt,
                                              boolean[] echo){
        Container panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = 
            new GridBagConstraints(0,0,1,1,1,1,
                                   GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.NONE,
                                   new Insets(0,0,0,0),0,0);
          
        gbc.weightx = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridx = 0;
        panel.add(new JLabel(instruction), gbc);
        gbc.gridy++;
        
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        
        JTextField[] texts=new JTextField[prompt.length];
        for (int i=0; i<prompt.length; i++) {
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridx = 0;
            gbc.weightx = 1;
            panel.add(new JLabel(prompt[i]),gbc);
            
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weighty = 1;
            if (echo[i]) {
                texts[i] = new JTextField(20);
            } else {
                texts[i] = new JPasswordField(20);
            }
            panel.add(texts[i], gbc);
            gbc.gridy++;
        }

        if (JOptionPane.showConfirmDialog(null, panel, 
                                          destination+": "+name,
                                          JOptionPane.OK_CANCEL_OPTION,
                                          JOptionPane.QUESTION_MESSAGE)
                == JOptionPane.OK_OPTION) {
            String[] response=new String[prompt.length];
            for(int i=0; i<prompt.length; i++){
                response[i]=texts[i].getText();
            }
            return response;
        } else {
            return null;  // cancel
        }
    }
}
