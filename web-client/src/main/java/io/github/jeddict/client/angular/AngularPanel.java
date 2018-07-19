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
package io.github.jeddict.client.angular;

import static io.github.jeddict.jcode.util.StringHelper.firstLower;
import static io.github.jeddict.jcode.util.StringHelper.kebabCase;
import static io.github.jeddict.jcode.util.StringHelper.startCase;
import org.apache.commons.lang3.StringUtils;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.SourceGroup;
import io.github.jeddict.client.web.main.WebData;
import static io.github.jeddict.client.web.main.WebData.DEFAULT_PREFIX;
import io.github.jeddict.client.web.main.domain.ClientPackager;
import io.github.jeddict.jcode.LayerConfigPanel;
import org.netbeans.modeler.properties.spec.ComboBoxValue;
import org.openide.util.NbBundle;

/**
 *
 * @author Gaurav Gupta
 */
public class AngularPanel extends LayerConfigPanel<WebData> {

    public AngularPanel() {
        initComponents();
    }

    @Override
    public boolean hasError() {
        warningLabel.setText("");
        setPrefix(getPrefix().replaceAll("[^a-zA-Z0-9]+", EMPTY));
        setModule(getModule().replaceAll("[^a-zA-Z0-9]+", EMPTY));
        if (StringUtils.isBlank(getPrefix())) {
            warningLabel.setText(NbBundle.getMessage(AngularPanel.class, "AngularPanel.invalidPrefix.message"));
            return true;
        }
        if (StringUtils.isBlank(getModule())) {
            warningLabel.setText(NbBundle.getMessage(AngularPanel.class, "AngularPanel.invalidModule.message"));
            return true;
        }
        if (StringUtils.isBlank(getApplicationTitle())) {
            warningLabel.setText(NbBundle.getMessage(AngularPanel.class, "AngularPanel.invalidTitle.message"));
            return true;
        }
//        if(!JavaUtil.isJava9()){
//            warningLabel.setText(NbBundle.getMessage(AngularPanel.class, "AngularPanel.minimumJava9Required.message"));
//            return true;
//        }
        return false;
    }


    @Override
    public void read() {
        WebData data = this.getConfigData();
        if (StringUtils.isNotBlank(data.getPrefix())) {
            setPrefix(data.getPrefix());
        }
        if (StringUtils.isNotBlank(data.getModule())) {
            setModule(data.getModule());
        }
        if (StringUtils.isNotBlank(data.getApplicationTitle())) {
            setApplicationTitle(data.getApplicationTitle());
        }
        
        if (data.getClientPackager()!=null) {
            setClientPackager(data.getClientPackager());
        }
        setProtractorTest(data.isProtractorTest());
        setSass(data.isSass());
    }

    @Override
    public void store() {
        this.getConfigData().setPrefix(getPrefix());
        this.getConfigData().setModule(getModule());
        this.getConfigData().setApplicationTitle(getApplicationTitle());
        this.getConfigData().setClientPackager(getClientPackager());
        this.getConfigData().setProtractorTest(isProtractorTest());
        this.getConfigData().setSass(isSass());
    }

    @Override
    public void init(String folder, Project project, SourceGroup sourceGroup) {
        setPrefix(DEFAULT_PREFIX);
        setModule(kebabCase(firstLower(project.getProjectDirectory().getName())));
        setApplicationTitle(startCase(project.getProjectDirectory().getName()));
        
        clientPackagerComboBox.removeAllItems();
        for (ClientPackager clientPackager : ClientPackager.values()) {
            clientPackagerComboBox.addItem(new ComboBoxValue(clientPackager, clientPackager.toString()));
        }
        clientPackagerPanel.setVisible(false);
    }
    
    private void setClientPackager(ClientPackager clientPackager) {
        if (clientPackager == null) {
            clientPackagerComboBox.setSelectedIndex(0);
        } else {
            for (int i = 0; i < clientPackagerComboBox.getItemCount(); i++) {
                if (((ComboBoxValue<ClientPackager>) clientPackagerComboBox.getItemAt(i)).getValue() == clientPackager) {
                    clientPackagerComboBox.setSelectedIndex(i);
                }
            }
        }
    }
    
    private ClientPackager getClientPackager(){
        return ((ComboBoxValue<ClientPackager>) clientPackagerComboBox.getSelectedItem()).getValue();
    }

    public String getPrefix() {
        return prefixTextField.getText().trim();
    }
    
    private void setPrefix(String prefix) {
        prefixTextField.setText(prefix);
    }
    
    public String getModule() {
        return angularModuleTextField.getText().trim();
    }
    
    private void setModule(String module) {
        angularModuleTextField.setText(module);
    }
    
    public String getApplicationTitle() {
        return appTitleTextField.getText().trim();
    }
    
    private void setApplicationTitle(String module) {
        appTitleTextField.setText(module);
    }
    
    public boolean isProtractorTest() {
        return protractorTest_CheckBox.isSelected();
    }
    
    private void setProtractorTest(boolean test) {
        protractorTest_CheckBox.setSelected(test);
    }
    
    public boolean isSass() {
        return sass_CheckBox.isSelected();
    }
    
    private void setSass(boolean enable) {
        sass_CheckBox.setSelected(enable);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        warningPanel = new javax.swing.JPanel();
        warningLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        prefixLabel = new javax.swing.JLabel();
        prefixTextField = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        angularModuleLabel = new javax.swing.JLabel();
        angularModuleTextField = new javax.swing.JTextField();
        wrapperPanel1 = new javax.swing.JPanel();
        appTitleLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        titleStartLabel = new javax.swing.JLabel();
        appTitleTextField = new javax.swing.JTextField();
        titleEndLabel = new javax.swing.JLabel();
        wrapperPanel4 = new javax.swing.JPanel();
        protractorTest_CheckBox = new javax.swing.JCheckBox();
        sass_CheckBox = new javax.swing.JCheckBox();
        clientPackagerPanel = new javax.swing.JPanel();
        clientPackagerLabel = new javax.swing.JLabel();
        clientPackagerComboBox = new javax.swing.JComboBox();

        warningPanel.setLayout(new java.awt.BorderLayout(10, 0));

        warningLabel.setForeground(new java.awt.Color(200, 0, 0));
        warningLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.openide.awt.Mnemonics.setLocalizedText(warningLabel, org.openide.util.NbBundle.getMessage(AngularPanel.class, "AngularPanel.warningLabel.text")); // NOI18N
        warningPanel.add(warningLabel, java.awt.BorderLayout.CENTER);

        jPanel1.setPreferredSize(new java.awt.Dimension(217, 120));
        jPanel1.setLayout(new java.awt.GridLayout(5, 0, 0, 15));

        jPanel5.setPreferredSize(new java.awt.Dimension(187, 20));
        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.LINE_AXIS));

        org.openide.awt.Mnemonics.setLocalizedText(prefixLabel, org.openide.util.NbBundle.getMessage(AngularPanel.class, "AngularPanel.prefixLabel.text")); // NOI18N
        prefixLabel.setPreferredSize(new java.awt.Dimension(92, 14));
        jPanel5.add(prefixLabel);

        prefixTextField.setText(org.openide.util.NbBundle.getMessage(AngularPanel.class, "AngularPanel.prefixTextField.text")); // NOI18N
        jPanel5.add(prefixTextField);

        jPanel1.add(jPanel5);

        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));

        org.openide.awt.Mnemonics.setLocalizedText(angularModuleLabel, org.openide.util.NbBundle.getMessage(AngularPanel.class, "AngularPanel.angularModuleLabel.text")); // NOI18N
        angularModuleLabel.setPreferredSize(new java.awt.Dimension(92, 14));
        jPanel3.add(angularModuleLabel);

        angularModuleTextField.setText(org.openide.util.NbBundle.getMessage(AngularPanel.class, "AngularPanel.angularModuleTextField.text")); // NOI18N
        jPanel3.add(angularModuleTextField);

        jPanel1.add(jPanel3);

        wrapperPanel1.setLayout(new java.awt.BorderLayout(10, 0));

        org.openide.awt.Mnemonics.setLocalizedText(appTitleLabel, org.openide.util.NbBundle.getMessage(AngularPanel.class, "AngularPanel.appTitleLabel.text")); // NOI18N
        wrapperPanel1.add(appTitleLabel, java.awt.BorderLayout.LINE_START);

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        titleStartLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.openide.awt.Mnemonics.setLocalizedText(titleStartLabel, org.openide.util.NbBundle.getMessage(AngularPanel.class, "AngularPanel.titleStartLabel.text")); // NOI18N
        titleStartLabel.setPreferredSize(new java.awt.Dimension(45, 14));
        jPanel2.add(titleStartLabel);

        appTitleTextField.setText(org.openide.util.NbBundle.getMessage(AngularPanel.class, "AngularPanel.appTitleTextField.text")); // NOI18N
        jPanel2.add(appTitleTextField);

        titleEndLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.openide.awt.Mnemonics.setLocalizedText(titleEndLabel, org.openide.util.NbBundle.getMessage(AngularPanel.class, "AngularPanel.titleEndLabel.text")); // NOI18N
        titleEndLabel.setPreferredSize(new java.awt.Dimension(45, 14));
        jPanel2.add(titleEndLabel);

        wrapperPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel1.add(wrapperPanel1);

        org.openide.awt.Mnemonics.setLocalizedText(protractorTest_CheckBox, org.openide.util.NbBundle.getMessage(AngularPanel.class, "AngularPanel.protractorTest_CheckBox.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(sass_CheckBox, org.openide.util.NbBundle.getMessage(AngularPanel.class, "AngularPanel.sass_CheckBox.text")); // NOI18N
        sass_CheckBox.setToolTipText(org.openide.util.NbBundle.getMessage(AngularPanel.class, "AngularPanel.sass_CheckBox.toolTipText")); // NOI18N

        javax.swing.GroupLayout wrapperPanel4Layout = new javax.swing.GroupLayout(wrapperPanel4);
        wrapperPanel4.setLayout(wrapperPanel4Layout);
        wrapperPanel4Layout.setHorizontalGroup(
            wrapperPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, wrapperPanel4Layout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addComponent(protractorTest_CheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(sass_CheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(126, 126, 126))
        );
        wrapperPanel4Layout.setVerticalGroup(
            wrapperPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, wrapperPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(protractorTest_CheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(sass_CheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(wrapperPanel4);

        clientPackagerPanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(clientPackagerLabel, org.openide.util.NbBundle.getMessage(AngularPanel.class, "AngularPanel.clientPackagerLabel.text")); // NOI18N
        clientPackagerPanel.add(clientPackagerLabel, java.awt.BorderLayout.LINE_START);

        clientPackagerPanel.add(clientPackagerComboBox, java.awt.BorderLayout.CENTER);

        jPanel1.add(clientPackagerPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(warningPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(155, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(306, Short.MAX_VALUE)
                    .addComponent(warningPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel angularModuleLabel;
    private javax.swing.JTextField angularModuleTextField;
    private javax.swing.JLabel appTitleLabel;
    private javax.swing.JTextField appTitleTextField;
    private javax.swing.JComboBox clientPackagerComboBox;
    private javax.swing.JLabel clientPackagerLabel;
    private javax.swing.JPanel clientPackagerPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel prefixLabel;
    private javax.swing.JTextField prefixTextField;
    private javax.swing.JCheckBox protractorTest_CheckBox;
    private javax.swing.JCheckBox sass_CheckBox;
    private javax.swing.JLabel titleEndLabel;
    private javax.swing.JLabel titleStartLabel;
    private javax.swing.JLabel warningLabel;
    private javax.swing.JPanel warningPanel;
    private javax.swing.JPanel wrapperPanel1;
    private javax.swing.JPanel wrapperPanel4;
    // End of variables declaration//GEN-END:variables


}
