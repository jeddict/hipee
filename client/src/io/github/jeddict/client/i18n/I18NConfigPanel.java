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
package io.github.jeddict.client.i18n;

import io.github.jeddict.jcode.stack.config.panel.LayerConfigPanel;
import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static java.util.stream.Collectors.toList;
import javax.swing.JCheckBox;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.SourceGroup;
import static io.github.jeddict.client.i18n.LanguageUtil.getAllSupportedLanguageOptions;
import org.netbeans.modeler.properties.spec.ComboBoxValue;
import org.netbeans.modeler.search.AutocompleteJComboBox;

/**
 *
 * @author Gaurav Gupta
 */
public class I18NConfigPanel extends LayerConfigPanel<I18NConfigData> {

    private AutocompleteJComboBox<Language> nativeLangSearchBox;
    
    private final Map<Language, JCheckBox> otherLangsState = new HashMap<>();

    public I18NConfigPanel() {
        initComponents();
    }

    @Override
    public boolean hasError() {
        return false;
    }
    
    @Override
    public void read() {
        I18NConfigData data = this.getConfigData();
        List<Language> langs = getAllSupportedLanguageOptions();
        
        nativeLangSearchBox.setValue(langs.stream()
                .map(lang -> new ComboBoxValue<>(lang, lang.getName()))
                .collect(toList()));
        
        nativeLangSearchBox.setSelectedItem(new ComboBoxValue<>(data.getNativeLanguage(), data.getNativeLanguage().getName()));
        
        Set<Language> selectedLangsSet = new HashSet<>(data.getOtherLanguages() != null
                ? data.getOtherLanguages() : Collections.EMPTY_SET);
        for(Language lang : langs) {
            JCheckBox checkBox = new JCheckBox(lang.getName());
            checkBox.setSelected(selectedLangsSet.contains(lang));
            otherLangs_LayeredPane.add(checkBox);
            otherLangsState.put(lang, checkBox);
        }        
        
        enable_CheckBox.setVisible(false);
//        enable_CheckBox.setSelected(data.isEnabled());
//        enable_CheckBoxActionPerformed(null);
    }

    @Override
    public void store() {
        I18NConfigData data = this.getConfigData();
        data.setEnabled(enable_CheckBox.isSelected());
        if(nativeLangSearchBox.getSelectedItem() instanceof ComboBoxValue){
            data.setNativeLanguage(((ComboBoxValue<Language>)nativeLangSearchBox.getSelectedItem()).getValue());
        }
        data.setOtherLanguages(
                otherLangsState.entrySet().stream()
                        .filter(entry -> entry.getValue().isSelected())
                        .map(entry -> entry.getKey())
                        .collect(toList())
        );
    }

    @Override
    public void init(String _package, Project project, SourceGroup sourceGroup) {
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        root_LayeredPane = new javax.swing.JLayeredPane();
        nativeLang_LayeredPane = new javax.swing.JLayeredPane();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        enable_CheckBox = new javax.swing.JCheckBox();
        nativeLang_Label = new javax.swing.JLabel();
        nativeLangSearchBox= new AutocompleteJComboBox<>();
        nativeLang_ComboBox = nativeLangSearchBox;
        otherLangs_ScrollPane = new javax.swing.JScrollPane();
        otherLangs_LayeredPane = new javax.swing.JLayeredPane();

        root_LayeredPane.setPreferredSize(new java.awt.Dimension(648, 150));
        root_LayeredPane.setLayout(new java.awt.BorderLayout(0, 20));

        nativeLang_LayeredPane.setPreferredSize(new java.awt.Dimension(0, 22));
        nativeLang_LayeredPane.setLayout(new java.awt.BorderLayout());

        jLayeredPane1.setPreferredSize(new java.awt.Dimension(135, 22));
        jLayeredPane1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        enable_CheckBox.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(enable_CheckBox, org.openide.util.NbBundle.getMessage(I18NConfigPanel.class, "I18NConfigPanel.enable_CheckBox.text")); // NOI18N
        enable_CheckBox.setToolTipText(org.openide.util.NbBundle.getMessage(I18NConfigPanel.class, "I18NConfigPanel.enable_CheckBox.toolTipText")); // NOI18N
        enable_CheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enable_CheckBoxActionPerformed(evt);
            }
        });
        jLayeredPane1.add(enable_CheckBox);

        org.openide.awt.Mnemonics.setLocalizedText(nativeLang_Label, org.openide.util.NbBundle.getMessage(I18NConfigPanel.class, "I18NConfigPanel.nativeLang_Label.text")); // NOI18N
        nativeLang_Label.setToolTipText(org.openide.util.NbBundle.getMessage(I18NConfigPanel.class, "I18NConfigPanel.nativeLang_Label.toolTipText")); // NOI18N
        nativeLang_Label.setPreferredSize(new java.awt.Dimension(100, 14));
        jLayeredPane1.add(nativeLang_Label);

        nativeLang_LayeredPane.add(jLayeredPane1, java.awt.BorderLayout.WEST);

        nativeLang_ComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                nativeLang_ComboBoxItemStateChanged(evt);
            }
        });
        nativeLang_LayeredPane.add(nativeLang_ComboBox, java.awt.BorderLayout.CENTER);

        root_LayeredPane.add(nativeLang_LayeredPane, java.awt.BorderLayout.NORTH);

        otherLangs_ScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(I18NConfigPanel.class, "I18NConfigPanel.otherLangs_ScrollPane.border.title"))); // NOI18N
        otherLangs_ScrollPane.setHorizontalScrollBar(null);

        otherLangs_LayeredPane.setToolTipText(org.openide.util.NbBundle.getMessage(I18NConfigPanel.class, "I18NConfigPanel.otherLangs_LayeredPane.toolTipText")); // NOI18N
        otherLangs_LayeredPane.setLayout(new java.awt.GridLayout(9, 4));
        otherLangs_ScrollPane.setViewportView(otherLangs_LayeredPane);

        root_LayeredPane.add(otherLangs_ScrollPane, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(root_LayeredPane, javax.swing.GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(root_LayeredPane, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void enable_CheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enable_CheckBoxActionPerformed
       boolean state = enable_CheckBox.isSelected();
       nativeLang_ComboBox.setEnabled(state);
       nativeLang_Label.setEnabled(state);
       otherLangs_ScrollPane.setEnabled(state);
       otherLangs_LayeredPane.setEnabled(state);
       Arrays.stream(otherLangs_LayeredPane.getComponents())
               .forEach(comp -> comp.setEnabled(state));
    }//GEN-LAST:event_enable_CheckBoxActionPerformed

    private void nativeLang_ComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_nativeLang_ComboBoxItemStateChanged
         if (evt.getStateChange() == ItemEvent.SELECTED) {
          revalidate();//issue: https://bugs.openjdk.java.net/browse/JDK-8044493
       }
    }//GEN-LAST:event_nativeLang_ComboBoxItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox enable_CheckBox;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JComboBox nativeLang_ComboBox;
    private javax.swing.JLabel nativeLang_Label;
    private javax.swing.JLayeredPane nativeLang_LayeredPane;
    private javax.swing.JLayeredPane otherLangs_LayeredPane;
    private javax.swing.JScrollPane otherLangs_ScrollPane;
    private javax.swing.JLayeredPane root_LayeredPane;
    // End of variables declaration//GEN-END:variables

}
