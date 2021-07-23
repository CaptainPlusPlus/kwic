package gui;


import file.Saver;
import kwic.POSResult;
import kwic.SearchType;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import static kwic.Searcher.search;
import static scraper.URLScraper.scrape;

public class UI {
    private JFrame frame; // Our top level window
    private String corpus;

    /**
     * Constructor for UI window
     */
    UI() {
        frame = new JFrame("Kwic UI");

        // Set window size
        frame.setSize(300, 200);

        //Override JFrames default layout manager.
        BoxLayout aBoxLayout = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS);
        frame.getContentPane().setLayout(aBoxLayout);

        frame.addWindowListener(new MyWindowListener());

        frame.setVisible(true);
    }

    /**
     *
     * @param text
     * @param type
     * @param needle
     * @param surroundingWords Number of words desired before & after the found result.
     * @return
     */
    private List<POSResult> searchText(String text, SearchType type, String needle, Integer surroundingWords) {
       return search(text, type, needle, surroundingWords);
    }

    private String getTxtFromURL(String url) {
        return scrape(url);
    }

    public void save(String url, String needle, SearchType type, List<POSResult> results) {
        Saver.save(url, needle, type, results);
    }




    /**
     * Our window listener terminates the program when the close window button
     * is clicked.
     */
    private class MyWindowListener extends WindowAdapter {
        public void windowClosing(WindowEvent e)
        {
            //Disposes of the current window, and closes the
            frame.dispose();
        }
    }

    /**
     * Opens a frame
     */
    public static void main ( String[] args )
    {
        new UI();
    }
}