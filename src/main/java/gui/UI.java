package gui;


import file.Saver;
import kwic.POSResult;
import kwic.SearchType;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static kwic.SearchType.*;
import static kwic.Searcher.search;
import static scraper.URLScraper.scrape;

public class UI {
    private JFrame frame; // Our top level window
    private String corpus;
    private JTextField aTextField;
    private JTextField aTextField0;
    private JTextField aTextField1;
    JTextArea listPane2;
    private JTextArea textArea = new JTextArea();
    JList<String> aList; // list component
    String s1[] = {"Make your choice:", "Exact form", "Lemma", "Entering tag"};
    private JComboBox makeChoice = new JComboBox(s1);

    /**
     * Constructor for UI window
     */
    UI() {
        frame = new JFrame("Kwic UI");

        // Set window size
        frame.setSize(1000, 800);

        //Override JFrames default layout manager.
        //Text field to edit selection
        aTextField = new JTextField(60);
        aTextField.setMaximumSize(new Dimension(200, 30));
        aTextField.setText("Enter a word");

        aTextField1 = new JTextField(60);
        aTextField1.setMaximumSize(new Dimension(200, 30));
        aTextField1.setText("Enter the number of surrounding words");
        /* Create a fixed size for the text field. Otherwise the text field
         * will stretch when the window is resized
         */
        // aTextField.setMaximumSize(new Dimension(200, 40));

        // Data to display in the list

        //Connect List and SelectionListener
        Dimension size = new Dimension(150, 30);

        //Panel-----------------------------------------------------

        JPanel panel = new JPanel();

        aTextField0 = new JTextField(60);
        aTextField0.setMaximumSize(new Dimension(200, 30));
        aTextField0.setText("Enter the URL");


        //Override JFrames default layout manager.
        BoxLayout aBoxLayout0 = new BoxLayout(panel, BoxLayout.X_AXIS);
        panel.setLayout(aBoxLayout0);

        panel.setAlignmentX(Component.RIGHT_ALIGNMENT);


//Panel0-----------------------------------------------------
        JPanel panel0 = new JPanel();

        aTextField0 = new JTextField(200);
        aTextField0.setMaximumSize(new Dimension(200, 30));
        aTextField0.setText("Enter the URL");

        JButton searchButton = new JButton("Search");
        searchButton.setMaximumSize(size);
        searchButton.setBackground(new Color(255, 102, 102));
        searchButton.addActionListener(new SearchButtonHandler());

        makeChoice.setMaximumSize(new Dimension(200, 30));


        //Override JFrames default layout manager.
        BoxLayout aBoxLayout1 = new BoxLayout(panel0, BoxLayout.X_AXIS);
        panel0.setLayout(aBoxLayout1);
        panel0.setAlignmentX(Component.RIGHT_ALIGNMENT);
        panel0.add(Box.createRigidArea(new Dimension(10, 0)));
        panel0.add(aTextField0);
        panel0.add(Box.createRigidArea(new Dimension(10, 0)));
        panel0.add(aTextField);
        panel0.add(Box.createRigidArea(new Dimension(10, 0)));
        panel0.add(aTextField1);
        panel0.add(Box.createRigidArea(new Dimension(10, 0)));
        panel0.add(searchButton);
        panel0.add(Box.createRigidArea(new Dimension(10, 0)));
        panel0.add(makeChoice);


        //Panel1-----------------------------------------------------

        String readIn = "    ";

        textArea.setText(readIn);
        textArea.setSize(new Dimension(200, 300));
        JScrollPane textAreasp = new JScrollPane(textArea);
        textAreasp.setSize(new Dimension(200, 300));
        //String summary = "Categories: \n" + "Noun(%) \n" + "Verb(%)\n" + " \n" + "Position within a sentence:\n" + "Beginning(%)\n" + "Middle(%)\n" + "End(%)\n";
        listPane2 = new JTextArea("");
        listPane2.setMinimumSize(new Dimension(300, 300));

        JPanel panel4 = new JPanel();


        //Override JFrames default layout manager.
        BoxLayout aBoxLayout = new BoxLayout(panel4, BoxLayout.X_AXIS);
        panel4.setLayout(aBoxLayout);


        //Add 5 Pixels rigid space between components

        panel4.add(Box.createRigidArea(new Dimension(20, 20)));
        panel4.add(textAreasp);
        panel4.add(Box.createRigidArea(new Dimension(20, 5)));
        panel4.add(listPane2);
        panel4.add(Box.createRigidArea(new Dimension(20, 20)));


        //This is the common size of the buttons


        JButton rmButton = new JButton("Remove");
        rmButton.setMaximumSize(size);
        rmButton.addActionListener(new RMButtonHandler());


        //add save button and link it with SaveButtonHandler
        JButton save = new JButton("Save");
        save.setMaximumSize(size);
        save.addActionListener(new SaveButtonHandler());
        frame.getContentPane().add(save);


        JButton quitButton = new JButton("Quit");
        quitButton.setMaximumSize(size);
        quitButton.addActionListener(new QuitButtonHandler());


        //Panel2-----------------------------------------------------
        JPanel panel5 = new JPanel();
        panel5.add(Box.createRigidArea(new Dimension(200, 0)));
        panel5.add(save);
        panel5.add(Box.createRigidArea(new Dimension(0, 5)));
        panel5.add(quitButton);
        panel5.add(Box.createVerticalGlue());
        panel5.setLayout(new BoxLayout(panel5, BoxLayout.X_AXIS));
        panel5.setAlignmentX(Component.RIGHT_ALIGNMENT);


        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.getContentPane().add(Box.createRigidArea(new Dimension(5, 10)));
        frame.getContentPane().add(panel);
        frame.getContentPane().add(Box.createRigidArea(new Dimension(5, 0)));
        frame.getContentPane().add(panel0);
        frame.getContentPane().add(Box.createRigidArea(new Dimension(5, 0)));
        frame.getContentPane().add(panel4);
        frame.getContentPane().add(Box.createRigidArea(new Dimension(5, 0)));
        frame.getContentPane().add(panel5);

        frame.addWindowListener(new MyWindowListener());

        frame.setVisible(true);
    }

    /**
     * @param text
     * @param type
     * @param needle
     * @param surroundingWords Number of words desired before & after the found result.
     * @return
     */
    private List<POSResult> searchText(String text, SearchType type, String needle, Integer surroundingWords) {
        return search(text, type, needle, surroundingWords, surroundingWords);
    }

    private String getTxtFromURL(String url) {
        return scrape(url);
    }

    public void save(String url, String needle, SearchType type, List<POSResult> results) {
        Saver.save(url, needle, type, results);
    }


    /*
     * ActionListeners for the JButtons
     */


    private class SearchButtonHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
//Hightlight

            // shows result

            String urlAddress = aTextField0.getText();
            String needle = aTextField.getText();
            Integer surroundingWords = Integer.valueOf(aTextField1.getText());

            SearchType type = null;
            //"Exact form", "Lemma", "Entering tag"};
            switch (makeChoice.getSelectedItem().toString()) {
                case "Exact form":
                    type = ExactForm;
                    break;
                case "Lemma":
                    type = Lemma;
                    break;
                case "Entering tag":
                    type = EnteringTag;
                    break;
            }

            //String text = getTxtFromURL(urlAddress);
            String text = "the door is right there";
            List<POSResult> results = searchText(text, type, needle, surroundingWords);


            for (POSResult a : results) {
                String sentence = "";
                for (String token : a.getSentence()) {
                    sentence += token + " ";
                }
                textArea.append(sentence + "\n");

                listPane2.append("Word: " + a.getWord() + ", Lemma: " + a.getLemma() +", POSTag: " +a.getPos() + "\n") ;

            }

        }
    }

    private class RMButtonHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            int elementIndex = aList.getSelectedIndex();
            if (elementIndex != -1) {
                if (JOptionPane.showConfirmDialog(frame, "Really delete element??")
                        == JOptionPane.YES_OPTION) {
                    DefaultListModel<String> aListModel = (DefaultListModel<String>) aList.getModel();
                    aListModel.remove(elementIndex);
                }
            }
        }
    }


    private class SaveButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {


            String urlAddress = aTextField0.getText();
            String needle = aTextField.getText();
            Integer surroundingWords = Integer.valueOf(aTextField1.getText());

            SearchType type = null;
            //"Exact form", "Lemma", "Entering tag"};
            switch (makeChoice.getSelectedItem().toString()) {
                case "Extact form":
                    type = ExactForm;
                    ;
                    break;
                case "Lemma":
                    type = Lemma;
                    break;
                case "Entering tag":
                    type = EnteringTag;
                    break;
            }

/**
 if(makeChoice.getSelectedItem().equals("Extact form")) {
 type = ExactForm;
 } else if (makeChoice.getSelectedItem().equals("Lemma")) {
 type = Lemma;
 } else if (makeChoice.getSelectedItem().equals("Entering tag")) {
 type = EnteringTag;
 }
 */
            String text = getTxtFromURL(urlAddress);
            List<POSResult> results = searchText(text, type, needle, surroundingWords);


            try {
                save(urlAddress, needle, type, results);
            } catch (NullPointerException en) {// if the window is closed and point null, catch error
                JOptionPane.showMessageDialog(frame, "Saved");
            }


        }
    }


    private class QuitButtonHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    private class ValueReporter implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent event) {
        }
    }


    /**
     * Our window listener terminates the program when the close window button
     * is clicked.
     */
    private class MyWindowListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            //Disposes of the current window, and closes the
            frame.dispose();
        }
    }

    /**
     * Opens a frame
     */
    public static void main(String[] args) {
        new UI();
    }
}