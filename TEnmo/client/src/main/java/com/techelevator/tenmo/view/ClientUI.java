package com.techelevator.tenmo.view;

import com.techelevator.tenmo.exceptions.BadImageException;
import com.techelevator.tenmo.services.ClientRestService;
import com.techelevator.tenmo.services.ClientService;

import javax.swing.*;

public class ClientUI {
    private JFrame frame;
    private JPanel mainPanel;
    ClientService service;

    public ClientUI(){
        initializeUI();
        this.service = new ClientRestService();
    }


    private void initializeUI(){
        mainPanel = new JPanel();

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Random Ducks \uD83E\uDD86");
        frame.add(mainPanel);
        frame.pack();
    }
}
