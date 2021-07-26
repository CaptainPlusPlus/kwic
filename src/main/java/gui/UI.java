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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

        frame = new JFrame("Kwic UI");
        frame.setSize(1400, 800);
        frame.getContentPane().setBackground(backgroundC);

        //Connect List and SelectionListener
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

        JPanel panel1 = new JPanel();
        panel1.setBackground(buttonC);

        //Box: "enter URL"
        urlText = new JTextArea("URL : ");
        urlText.setMaximumSize(new Dimension(200, 30));
        urlText.setBackground(backgroundC);
        urlText.setEditable(false);
        urlText.setFont(contentFont);

        urlField = new JTextField(20);
        urlField.setMaximumSize(new Dimension(400, 30));
        urlField.setText("Enter the URL");
        urlField.setFont(fieldFont);
        urlField.setBackground(inputC);
        urlField.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                urlField.setText("");
            }
        });

        //Box: "enter Word"
        wordText = new JTextArea("Word : ");
        wordText.setMaximumSize(new Dimension(100, 30));
        wordText.setBackground(backgroundC);
        wordText.setEditable(false);
        wordText.setFont(contentFont);

        wordField = new JTextField(10);
        wordField.setMaximumSize(new Dimension(400, 30));
        wordField.setText("Enter a word");
        wordField.setFont(fieldFont);
        wordField.setBackground(inputC);
        wordField.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                wordField.setText("");
            }
        });

        //Box: "Surrounding words"
        surroundingNText = new JTextArea("Surrounding words : ");
        surroundingNText.setMaximumSize(new Dimension(100, 30));
        surroundingNText.setBackground(backgroundC);
        surroundingNText.setEditable(false);
        surroundingNText.setFont(contentFont);

        surroundNfield = new JTextField(25);
        surroundNfield.setMaximumSize(new Dimension(200, 30));
        surroundNfield.setText("Enter the number of surrounding words");
        surroundNfield.setFont(fieldFont);
        surroundNfield.setBackground(inputC);
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

        //Override JFrames default layout manager.
        BoxLayout aBoxLayout1 = new BoxLayout(panel1, BoxLayout.X_AXIS);
        panel1.setLayout(aBoxLayout1);
        panel1.add(Box.createRigidArea(new Dimension(20, 0)));
        panel1.add(urlText);
        panel1.add(urlField);
        panel1.add(Box.createRigidArea(new Dimension(10, 0)));
        panel1.add(wordText);
        panel1.add(wordField);
        panel1.add(Box.createRigidArea(new Dimension(10, 0)));
        panel1.add(surroundingNText);
        panel1.add(surroundNfield);
        panel1.add(Box.createRigidArea(new Dimension(10, 0)));
        panel1.add(searchButton);
        panel1.add(Box.createRigidArea(new Dimension(10, 0)));
        panel1.add(makeChoice);
        panel1.add(Box.createRigidArea(new Dimension(20, 0)));
        panel1.setBackground(backgroundC);
        panel1.setAlignmentX(Component.CENTER_ALIGNMENT);


        //Panel2-----------------------------------------------------

        //output Textbox: "Sentence"
        sentenceField.setText("\t\t ----- Sentence ----- \t\t\n");
        sentenceField.setBackground(outputC);
        sentenceField.setEditable(false);
        sentenceField.setFont(contentFont);
        sentenceField.setMaximumSize(new Dimension(500, 300));

        //output Textbox: "Result"
        JScrollPane textAreasp = new JScrollPane(sentenceField);
        textAreasp.setSize(new Dimension(500, 300));
        resultField = new JTextArea("\t \t  ----- Result ----- \t \t\n");
        resultField.setBackground(outputC);
        resultField.setEditable(false);
        resultField.setFont(contentFont);

        JPanel panel2 = new JPanel();
        //Override JFrames default layout manager.
        BoxLayout aBoxLayout = new BoxLayout(panel2, BoxLayout.X_AXIS);
        panel2.setLayout(aBoxLayout);
        panel2.setBackground(backgroundC);

        //Add 5 Pixels rigid space between components
        panel2.add(Box.createRigidArea(new Dimension(20, 20)));
        panel2.add(textAreasp);
        panel2.add(Box.createRigidArea(new Dimension(20, 5)));
        panel2.add(resultField);
        panel2.add(Box.createRigidArea(new Dimension(20, 20)));


        //Panel3-----------------------------------------------------

        JPanel panel3 = new JPanel();
        panel3.add(save);
        panel3.add(Box.createRigidArea(new Dimension(50, 5)));
        panel3.add(clearButton);
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));
        panel3.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel3.setBackground(backgroundC);

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
            // shows result

            String urlAddress = urlField.getText();
            String needle = wordField.getText();
            Integer surroundingWords = 0;
            try {
                surroundingWords = Integer.valueOf(surroundNfield.getText());
            } catch (NumberFormatException a){
           a.printStackTrace();
               // JOptionPane.showMessageDialog(frame, "Please enter a integer");
            }
            sentenceField.setText("\t\t ----- Sentence ----- \t\t\n");
            resultField.setText("\t \t  ----- Result ----- \t \t\n");

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
            String text = "He crawls up, between my legs where he stops to rid me of my sodden panties. " +
                    "He slings it away carelessly, his ogling eyes never leaving the naked place they covered. " +
                    "He continues to stare, licking his lips – obviously beyond aroused by the sight but there’s nothing to hide my intimate folds and I feel exposed, squirming and certain that my blush reaches all the way down there.\n" +
                    "\n" +
                    "He takes his sweet, torturous time – luxuriating in his private viewing activity. " +
                    "He makes no move to touch me but the ravenous mould of his face is pushing me to run up the steps of desire, taking them three at a time. I shift in needy response.\n" +
                    "\n" +
                    "He growls, low in his chest while he grips my inner thighs, pushing them apart. " +
                    "“Keep still or I’ll make you.” I gasp at his provocative threat and on pure instinct and raw desire my hips tilt up by their own accord, crazy in its need for any contact. " +
                    "His hands slip around, cupping my backside as he pushes his nose into my sex, inhaling deeply.\n" +
                    "\n" +
                    "Oh my fucking my!";
            List<POSResult> results =null;
            try {
                results = searchText(text, type, needle, surroundingWords);
            } catch (NullPointerException n){
                n.printStackTrace();
                return;
            }


            if (text.isEmpty()|| urlAddress.contains(" ") || !urlAddress.contains(".")) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid URL address");
                return;
            }
            if (needle.isEmpty()|| needle.contains(" ")) {
                JOptionPane.showMessageDialog(frame, "Please enter a word");
                return;
            }

            if (surroundingWords <1) {
                JOptionPane.showMessageDialog(frame, "Please enter the number of surrounding words");
                return;
            }

            if(type == null){
                JOptionPane.showMessageDialog(frame, "Please select a type");
                return;
            }


            if (results.size() == 0) {
                JOptionPane.showMessageDialog(frame, "word not found");
            }

            if (results.isEmpty()) {
                sentenceField.append("  (0 result)\n");
                resultField.append("  (0 result)\n");
            } else {
                for (POSResult a : results) {
                    String sentence = "";
                    for (String token : a.getSentence()) {
                        sentence += token + " ";

                    }
                    sentenceField.append("  " + sentence + "\n");
                    try {
                        List<Integer> indexes = new ArrayList<>();
                        int index = 0;
                        while (index != -1) {
                            //String textUppercase = sentenceField.getText().toLowerCase(Locale.ROOT);
                           // index = textUppercase.indexOf(" " + needle.toLowerCase(Locale.ROOT) + " ", index);
                            String textUppercase = sentenceField.getText();
                            index = textUppercase.indexOf(" " + a.getWord() + " ", index);
                            if (index != -1) {
                                indexes.add(index);
                                index++;
                            }
                        }

                        for (int i : indexes) {
                            sentenceField.getHighlighter().addHighlight(i + 1, i + a.getWord().length() + 1, new DefaultHighlighter.DefaultHighlightPainter(Color.yellow));
                        }

                    } catch (BadLocationException badLocationException) {
                        badLocationException.printStackTrace();
                    }
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

            String text = getTxtFromURL(urlAddress);
            List<POSResult> results = searchText(text, type, needle, surroundingWords);

            try {
                save(urlAddress, needle, type, results);
            } catch (NullPointerException en) {// if the window is closed and point null, catch error
                JOptionPane.showMessageDialog(frame, "Saved");
            }

        }
    }


    private class ClearButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            wordField.setText("Enter a word");
            surroundNfield.setText("Enter the number of surrounding words");
            urlField.setText("Enter the URL");
            sentenceField.setText("\t\t ----- Sentence ----- \t\t\n");
            resultField.setText("\t \t  ----- Result ----- \t \t\n");
            makeChoice.setSelectedIndex(0);
        }
    }

    private class ValueReporter implements ListSelectionListener {  ///????이거뭐임

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