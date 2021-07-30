/**
 * Course:      Data Structures and Algorithms for Computational Linguistics II SS2021
 * Assignment:  (Final KWIC Project)
 * Authors:      (Hoyeon Lee, Nkonye Gbadegoye, Dewi Surya, Jacqueline Becker, Felix Redfox)
 * Description: (Project to extract text from url and find/save needle typed results on it with their POS information)
 *
 * Honor Code:  We pledge that this program represents our own work.
 *  we did not receive help from anyone in designing and debugging this program.
 */

package gui;


import file.Saver;
import kwic.POSResult;
import kwic.SearchType;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static kwic.SearchType.*;
import static kwic.Searcher.search;
import static scraper.URLScraper.scrape;

public class UI {
    private JFrame frame; // Our top level window
    private String corpus;
    private JTextField wordField;
    private JTextField urlField;
    private JTextField surroundNfield;
    protected JTextComponent comp;
    protected Highlighter.HighlightPainter painter;
    JTextArea resultField;
    JTextArea urlText;
    JTextArea wordText;
    JTextArea surroundingNText;

    private JTextArea sentenceField = new JTextArea();
    String s1[] = {"Make your choice:", "Exact form", "Lemma", "Entering tag"};
    private JComboBox makeChoice;

    /**
     * Constructor for UI window
     */
    UI() {

        // Color
        Color inputC = new Color(223, 187, 187); //pale Chestnut
        Color outputC = new Color(249, 241, 214); //antique White
        Color buttonC = new Color(247, 202, 143); //peach Orange
        Color backgroundC = new Color(255, 223, 201); //unbleached Silk

        //Font
        Font fieldFont = new Font("Courier", Font.PLAIN, 15);
        Font contentFont = new Font("Courier", Font.PLAIN, 20);
        Font buttonFont = new Font("Courier", Font.BOLD, 20);

        //make the window and set size + background colour
        frame = new JFrame("Kwic UI");
        frame.setSize(1400, 800);
        frame.getContentPane().setBackground(backgroundC);

        //set size
        Dimension size = new Dimension(150, 30);

        //Create Buttons
        //Button: "Search"
        JButton searchButton = new JButton("Search");
        searchButton.setMaximumSize(size);
        searchButton.setBackground(buttonC);
        searchButton.addActionListener(new SearchButtonHandler());
        searchButton.setFont(buttonFont);

        //Button: "Save" with SaveButtonHandler
        JButton save = new JButton("Save");
        save.setSize(200, 50);
        save.addActionListener(new SaveButtonHandler());
        save.setBackground(buttonC);
        save.setFont(buttonFont);
        frame.getContentPane().add(save);

        //Button: "Clear"
        JButton clearButton = new JButton("Clear");
        clearButton.setSize(200, 50);
        clearButton.setBackground(buttonC);
        clearButton.addActionListener(new ClearButtonHandler());
        clearButton.setFont(buttonFont);

        //Panel1-----------------------------------------------------

        //initialize the first panel
        JPanel panel1 = new JPanel();
        panel1.setBackground(buttonC);

        //make Textboxes
        //Text infront of the "Enter the URL" box
        urlText = new JTextArea("URL : ");
        urlText.setMaximumSize(new Dimension(200, 30));
        urlText.setBackground(backgroundC);
        urlText.setEditable(false);
        urlText.setFont(contentFont);

        //Box: "Enter the URL"
        urlField = new JTextField(20);
        urlField.setMaximumSize(new Dimension(400, 30));
        urlField.setText("Enter the URL");
        urlField.setFont(fieldFont);
        urlField.setBackground(inputC);
        //making text in box disappear when clicked
        urlField.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                urlField.setText("");
            }
        });

        //Text in front of the "enter a word" box
        wordText = new JTextArea("Word : ");
        wordText.setMaximumSize(new Dimension(100, 30));
        wordText.setBackground(backgroundC);
        wordText.setEditable(false);
        wordText.setFont(contentFont);

        //Box: "Enter a word"
        wordField = new JTextField(10);
        wordField.setMaximumSize(new Dimension(400, 30));
        wordField.setText("Enter a word");
        wordField.setFont(fieldFont);
        wordField.setBackground(inputC);
        //making text in box disappear when clicked
        wordField.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                wordField.setText("");
            }
        });

        //Text in front of "surrounding words" box
        surroundingNText = new JTextArea("Surrounding words : ");
        surroundingNText.setMaximumSize(new Dimension(100, 30));
        surroundingNText.setBackground(backgroundC);
        surroundingNText.setEditable(false);
        surroundingNText.setFont(contentFont);

        //Box: "Surrounding words"
        surroundNfield = new JTextField(25);
        surroundNfield.setMaximumSize(new Dimension(200, 30));
        surroundNfield.setText("Enter the number of surrounding words");
        surroundNfield.setFont(fieldFont);
        surroundNfield.setBackground(inputC);
        //making text in box disappear when clicked
        surroundNfield.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                surroundNfield.setText("");
            }
        });

        //Box: "make Choice"
        makeChoice = new JComboBox(s1);
        makeChoice.setMaximumSize(new Dimension(200, 30));
        makeChoice.setFont(fieldFont);
        makeChoice.setBackground(inputC);

        //Add all the boxes and buttons to panel 1
        BoxLayout aBoxLayout1 = new BoxLayout(panel1, BoxLayout.X_AXIS);
        panel1.setLayout(aBoxLayout1);
        //Add URL Text + Box
        panel1.add(Box.createRigidArea(new Dimension(20, 0)));
        panel1.add(urlText);
        panel1.add(urlField);
        //Add Word Text + Box
        panel1.add(Box.createRigidArea(new Dimension(10, 0)));
        panel1.add(wordText);
        panel1.add(wordField);
        //Add Surrounding Text + Box
        panel1.add(Box.createRigidArea(new Dimension(10, 0)));
        panel1.add(surroundingNText);
        panel1.add(surroundNfield);
        //Add the search Button
        panel1.add(Box.createRigidArea(new Dimension(10, 0)));
        panel1.add(searchButton);
        //Add the make choice box
        panel1.add(Box.createRigidArea(new Dimension(10, 0)));
        panel1.add(makeChoice);
        //add background colour
        panel1.add(Box.createRigidArea(new Dimension(20, 0)));
        panel1.setBackground(backgroundC);
        //Center all the added boxes and buttons
        panel1.setAlignmentX(Component.CENTER_ALIGNMENT);


        //Panel2-----------------------------------------------------

        //output Textbox: "Sentence"
        sentenceField.setText("\t\t ----- Sentence ----- \t\t\n");
        sentenceField.setBackground(outputC);
        sentenceField.setEditable(false);
        sentenceField.setFont(contentFont);
        sentenceField.setMaximumSize(new Dimension(500, 300));

        //output Textbox: "Result"
        //make sure it can be scrolled through the results
        JScrollPane textAreasp = new JScrollPane(sentenceField);
        textAreasp.setSize(new Dimension(500, 300));
        resultField = new JTextArea("\t \t  ----- Result ----- \t \t\n");
        resultField.setBackground(outputC);
        resultField.setEditable(false);
        resultField.setFont(contentFont);

        //initialize the second panel
        JPanel panel2 = new JPanel();
        BoxLayout aBoxLayout = new BoxLayout(panel2, BoxLayout.X_AXIS);
        panel2.setLayout(aBoxLayout);
        panel2.setBackground(backgroundC);

        //Create rigid space between components
        panel2.add(Box.createRigidArea(new Dimension(20, 20)));
        panel2.add(textAreasp);
        panel2.add(Box.createRigidArea(new Dimension(20, 5)));
        panel2.add(resultField);
        panel2.add(Box.createRigidArea(new Dimension(20, 20)));


        //Panel3-----------------------------------------------------

        //Initialize the third panel
        //add the save button
        JPanel panel3 = new JPanel();
        panel3.add(save);
        panel3.add(Box.createRigidArea(new Dimension(50, 5)));
        panel3.add(clearButton);
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));
        //set to center alignment
        panel3.setAlignmentX(Component.CENTER_ALIGNMENT);
        //and set background colour
        panel3.setBackground(backgroundC);

        //add all the panels to the window
        //and set spaces between each panel
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.getContentPane().add(Box.createRigidArea(new Dimension(5, 20)));
        frame.getContentPane().add(panel1);
        frame.getContentPane().add(Box.createRigidArea(new Dimension(5, 20)));
        frame.getContentPane().add(panel2);
        frame.getContentPane().add(Box.createRigidArea(new Dimension(5, 20)));
        frame.getContentPane().add(panel3);
        frame.getContentPane().add(Box.createRigidArea(new Dimension(5, 20)));

        frame.addWindowListener(new MyWindowListener());
        frame.setVisible(true);

    }

    /**
     * @param text text to look up words from.
     * @param type type of word to find.
     * @param needle the word to find.
     * @param surroundingWords Number of words desired before & after the found result.
     * @return result of the search
     */
    private List<POSResult> searchText(String text, SearchType type, String needle, Integer surroundingWords) {
        return search(text, type, needle, surroundingWords, surroundingWords);
    }

    /**
     * @param url url address to scrape text from
     * @return
     */
    private String getTxtFromURL(String url) throws IOException {
        return scrape(url);
    }

    /**
     * @param url rl address to scrape text from
     * @param needle the word to find.
     * @param type type of word to find.
     * @param results information of the word(needle) in url
     */
    public void save(String url, String needle, SearchType type, List<POSResult> results) {
        Saver saver = new Saver(url, needle, type, results);
        saver.save();
    }


    /*
     * ActionListeners for the JButtons
     */
    private class SearchButtonHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String urlAddress = urlField.getText();
            String needle = wordField.getText();
            Integer surroundingWords = 0;
            try { //get surrounding words from the text box, if the given input is not an integer, show the message
                surroundingWords = Integer.valueOf(surroundNfield.getText());
            } catch (NumberFormatException a){
                a.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Please enter a positive integer for surrounding words");
            }
            sentenceField.setText("\t\t ----- Sentence ----- \t\t\n");
            resultField.setText("\t \t  ----- Result ----- \t \t\n");

            SearchType type = null;
            //set Type as selected item in the drop box
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

            List<POSResult> results =null;
            String text = null;


            try { //check if the given url address is valid and if not, show the message
                URL validatedUrl = new URL(urlAddress);
            } catch (MalformedURLException malformedURLException) {
                malformedURLException.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Please enter a valid URL address");
                return;
            }
            if (needle.isEmpty()|| needle.contains(" ")) {// if the given word is not valid, show the message
                JOptionPane.showMessageDialog(frame, "Please enter a word");
                return;
            }

            if (surroundingWords <1) { // if the given integer is smaller than 1, show the message
                JOptionPane.showMessageDialog(frame, "Please enter the number of surrounding words (that is larger than 0)");
                return;
            }

            if(type == null){ // if the type is not selected, show the message
                JOptionPane.showMessageDialog(frame, "Please select a type");
                return;
            }

            try { // try to load text from the url address
                text = getTxtFromURL(urlAddress);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            results = searchText(text, type, needle, surroundingWords);


            if (results.size() == 0) { //if there is no data in the result list, show message
                JOptionPane.showMessageDialog(frame, "word not found");
            }

            if (results.isEmpty()) { //if there is no data in the result list, print "(0 result)"
                sentenceField.append("  (0 result)\n");
                resultField.append("  (0 result)\n");
            } else { // if there is data, iterate the sentences and tokens in there and append it to the sentenceField
                for (POSResult a : results) {
                    String sentence = "";
                    for (String token : a.getSentence()) {
                        sentence += token + " ";

                    }
                    sentenceField.append("  " + sentence + "\n");

                    try { // look for the needle(word) from the sentenceField and save indexes to the List indexes
                        List<Integer> indexes = new ArrayList<>();
                        int index = 0;
                        while (index != -1) {
                            String textUppercase = sentenceField.getText();
                            index = textUppercase.indexOf(" " + a.getWord() + " ", index);
                            if (index != -1) {
                                indexes.add(index);
                                index++;
                            }
                        }

                        for (int i : indexes) { //highlight all the words using the saved indexes
                            sentenceField.getHighlighter().addHighlight(i + 1, i + a.getWord().length() + 1, new DefaultHighlighter.DefaultHighlightPainter(Color.yellow));
                        }

                    } catch (BadLocationException badLocationException) {
                        badLocationException.printStackTrace();
                    }
                    //print information to the resultField
                    resultField.append("  " + "Word: " + a.getWord() + ", Lemma: " + a.getLemma() + ", POSTag: " + a.getPos() + "\n");

                }


            }
        }
    }

    private class SaveButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            String urlAddress = urlField.getText();
            String needle = wordField.getText();
            Integer surroundingWords = Integer.valueOf(surroundNfield.getText());

            SearchType type = null;
            //set Type as selected item in the drop box
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

            String text = null;

            try {
                text = getTxtFromURL(urlAddress);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            List<POSResult> results = searchText(text, type, needle, surroundingWords);

            try {
                save(urlAddress, needle, type, results);
            } catch (NullPointerException en) {
                JOptionPane.showMessageDialog(frame, "Not saved");
            }

        }
    }


    private class ClearButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) { //set every field as the initial setting
            wordField.setText("Enter a word");
            surroundNfield.setText("Enter the number of surrounding words");
            urlField.setText("Enter the URL");
            sentenceField.setText("\t\t ----- Sentence ----- \t\t\n");
            resultField.setText("\t \t  ----- Result ----- \t \t\n");
            makeChoice.setSelectedIndex(0);
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
            //Disposes of the current window
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