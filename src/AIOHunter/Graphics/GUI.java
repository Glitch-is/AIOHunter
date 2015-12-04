package AIOHunter.Graphics;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import AIOHunter.AIOHunter;
import AIOHunter.Tasks.*;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.Tile;

/**
 * Created by glitch on 12/3/15.
 */
public class GUI {
    private JComboBox comboBox1;
    private JButton startButton;
    private String[] options = {"Chrimson Switfts", "Tropical Wagtails", "Grey Chins", "Red Chins", "Black Chins"};

    private ClientContext ctx;

    public void onStart()
    {
        int trapInvID = 0,
            trapObjID = 0;
        int trapCheckID = 0; // Both failed and caught traps
        int[] dropables = {9978, 10087, 10088}; //bones id: 526
        Tile anchor;

        boolean chins = false;

        switch(comboBox1.getSelectedIndex())
        {
            case 0:
            case 1:
                // Chrimson Swifts
                // Tropical Wagtails
                trapInvID = 10006;
                break;
            case 2:
            case 3:
            case 4:
                // Chins
                trapInvID = 10008;
                trapCheckID = 91232;
                chins = true;
                break;
        }

        anchor = ctx.players.local().tile();
        AIOHunter.taskList.add(new LayTraps(ctx, anchor, chins, trapInvID, trapObjID));
        AIOHunter.taskList.add(new CheckTraps(ctx, anchor, chins, trapObjID, trapCheckID, dropables));
    }

    public GUI(ClientContext ctx) {

        this.ctx = ctx;

        for(String item: options)
            comboBox1.addItem(item);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                onStart();
            }
        });
    }
}
