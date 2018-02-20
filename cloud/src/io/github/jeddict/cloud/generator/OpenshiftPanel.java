/**
 * Copyright 2013-2018 the original author or authors from the Jeddict project (https://jeddict.github.io/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.github.jeddict.cloud.generator;

import io.github.jeddict.jcode.window.GenericDialog;
import org.apache.commons.lang3.StringUtils;
import org.openide.util.NbBundle;

/**
 *
 * @author jGauravGupta
 */
public class OpenshiftPanel extends GenericDialog {

    private final OpenshiftConfigData openshiftConfigData;
    
    /**
     * Creates new form KubernetesPanel
     * @param openshiftConfigData
     */
    public OpenshiftPanel(OpenshiftConfigData openshiftConfigData) {
        this.openshiftConfigData = openshiftConfigData;
        initComponents();
        getRootPane().setDefaultButton(saveButton);
        namespace_TextField.setText(openshiftConfigData.getNamespace());
        setSelectedDatabaseStorageType(openshiftConfigData.getDatabaseStorageType());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        serviceType_ButtonGroup = new javax.swing.ButtonGroup();
        root_LayeredPane = new javax.swing.JLayeredPane();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        namespace_LayeredPane = new javax.swing.JLayeredPane();
        namespace_Label = new javax.swing.JLabel();
        namespace_TextField = new javax.swing.JTextField();
        serviceType_LayeredPane = new javax.swing.JLayeredPane();
        serviceType_Label = new javax.swing.JLabel();
        serviceType_ButtonGroupPanel = new javax.swing.JLayeredPane();
        ephemeral_RadioButton = new javax.swing.JRadioButton();
        persistent_RadioButton = new javax.swing.JRadioButton();
        action_LayeredPane = new javax.swing.JLayeredPane();
        warningLabel = new javax.swing.JLabel();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setTitle(org.openide.util.NbBundle.getMessage(OpenshiftPanel.class, "OpenshiftPanel.title")); // NOI18N
        setSize(new java.awt.Dimension(500, 200));

        jLayeredPane1.setLayout(new java.awt.GridLayout(2, 1, 0, 12));

        namespace_LayeredPane.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(namespace_Label, org.openide.util.NbBundle.getMessage(OpenshiftPanel.class, "OpenshiftPanel.namespace_Label.text")); // NOI18N
        namespace_Label.setPreferredSize(new java.awt.Dimension(88, 14));
        namespace_LayeredPane.add(namespace_Label, java.awt.BorderLayout.WEST);

        namespace_TextField.setText(org.openide.util.NbBundle.getMessage(OpenshiftPanel.class, "OpenshiftPanel.namespace_TextField.text")); // NOI18N
        namespace_TextField.setToolTipText(org.openide.util.NbBundle.getMessage(OpenshiftPanel.class, "OpenshiftPanel.namespace_TextField.toolTipText")); // NOI18N
        namespace_LayeredPane.add(namespace_TextField, java.awt.BorderLayout.CENTER);

        jLayeredPane1.add(namespace_LayeredPane);

        serviceType_LayeredPane.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(serviceType_Label, org.openide.util.NbBundle.getMessage(OpenshiftPanel.class, "OpenshiftPanel.serviceType_Label.text")); // NOI18N
        serviceType_Label.setPreferredSize(new java.awt.Dimension(100, 14));
        serviceType_LayeredPane.add(serviceType_Label, java.awt.BorderLayout.WEST);

        serviceType_ButtonGroupPanel.setPreferredSize(new java.awt.Dimension(402, 25));
        serviceType_ButtonGroupPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 35, 0));

        serviceType_ButtonGroup.add(ephemeral_RadioButton);
        ephemeral_RadioButton.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(ephemeral_RadioButton, org.openide.util.NbBundle.getMessage(OpenshiftPanel.class, "OpenshiftPanel.ephemeral_RadioButton.text")); // NOI18N
        ephemeral_RadioButton.setToolTipText(org.openide.util.NbBundle.getMessage(OpenshiftPanel.class, "OpenshiftPanel.ephemeral_RadioButton.toolTipText")); // NOI18N
        serviceType_ButtonGroupPanel.add(ephemeral_RadioButton);

        serviceType_ButtonGroup.add(persistent_RadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(persistent_RadioButton, org.openide.util.NbBundle.getMessage(OpenshiftPanel.class, "OpenshiftPanel.persistent_RadioButton.text")); // NOI18N
        persistent_RadioButton.setToolTipText(org.openide.util.NbBundle.getMessage(OpenshiftPanel.class, "OpenshiftPanel.persistent_RadioButton.toolTipText")); // NOI18N
        serviceType_ButtonGroupPanel.add(persistent_RadioButton);

        serviceType_LayeredPane.add(serviceType_ButtonGroupPanel, java.awt.BorderLayout.CENTER);

        jLayeredPane1.add(serviceType_LayeredPane);

        action_LayeredPane.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        warningLabel.setForeground(new java.awt.Color(200, 0, 0));
        org.openide.awt.Mnemonics.setLocalizedText(warningLabel, org.openide.util.NbBundle.getMessage(OpenshiftPanel.class, "OpenshiftPanel.warningLabel.text")); // NOI18N
        warningLabel.setPreferredSize(new java.awt.Dimension(330, 20));
        action_LayeredPane.add(warningLabel);

        org.openide.awt.Mnemonics.setLocalizedText(saveButton, org.openide.util.NbBundle.getMessage(OpenshiftPanel.class, "OpenshiftPanel.saveButton.text")); // NOI18N
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        action_LayeredPane.add(saveButton);

        org.openide.awt.Mnemonics.setLocalizedText(cancelButton, org.openide.util.NbBundle.getMessage(OpenshiftPanel.class, "OpenshiftPanel.cancelButton.text")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        action_LayeredPane.add(cancelButton);

        root_LayeredPane.setLayer(jLayeredPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        root_LayeredPane.setLayer(action_LayeredPane, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout root_LayeredPaneLayout = new javax.swing.GroupLayout(root_LayeredPane);
        root_LayeredPane.setLayout(root_LayeredPaneLayout);
        root_LayeredPaneLayout.setHorizontalGroup(
            root_LayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(root_LayeredPaneLayout.createSequentialGroup()
                .addGroup(root_LayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(action_LayeredPane)
                    .addGroup(root_LayeredPaneLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLayeredPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)))
                .addContainerGap())
        );
        root_LayeredPaneLayout.setVerticalGroup(
            root_LayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, root_LayeredPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(action_LayeredPane, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(root_LayeredPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(root_LayeredPane)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        cancelActionPerformed(evt);
    }//GEN-LAST:event_cancelButtonActionPerformed

    public boolean hasError() {
        if (StringUtils.isBlank(namespace_TextField.getText())
                || !namespace_TextField.getText().matches("[a-z0-9]([-a-z0-9]*[a-z0-9])?")) {
            warningLabel.setText(NbBundle.getMessage(OpenshiftPanel.class, "KubernetesPanel.invalidNamespace.message"));
            return true;
        }
        return false;
    }
    
    
    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        if (hasError()) {
          return;  
        }
        setVisible(false);
        openshiftConfigData.setNamespace(namespace_TextField.getText());
        openshiftConfigData.setDatabaseStorageType(getSelectedDatabaseStorageType());
        this.setDialogResult(javax.swing.JOptionPane.OK_OPTION);
        dispose();
    }//GEN-LAST:event_saveButtonActionPerformed

    private String getSelectedDatabaseStorageType(){
        return persistent_RadioButton.isSelected()? PERSISTENT : EPHEMERAL;       
    }
    
    private void setSelectedDatabaseStorageType(String serviceType){
        if (PERSISTENT.equals(serviceType)) {
            persistent_RadioButton.setSelected(true);
        } else {
            ephemeral_RadioButton.setSelected(true);
        }
    }
    
    
    private final String PERSISTENT = "persistent";
    private final String EPHEMERAL = "ephemeral";

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLayeredPane action_LayeredPane;
    private javax.swing.JButton cancelButton;
    private javax.swing.JRadioButton ephemeral_RadioButton;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLabel namespace_Label;
    private javax.swing.JLayeredPane namespace_LayeredPane;
    private javax.swing.JTextField namespace_TextField;
    private javax.swing.JRadioButton persistent_RadioButton;
    private javax.swing.JLayeredPane root_LayeredPane;
    private javax.swing.JButton saveButton;
    private javax.swing.ButtonGroup serviceType_ButtonGroup;
    private javax.swing.JLayeredPane serviceType_ButtonGroupPanel;
    private javax.swing.JLabel serviceType_Label;
    private javax.swing.JLayeredPane serviceType_LayeredPane;
    private javax.swing.JLabel warningLabel;
    // End of variables declaration//GEN-END:variables
}
