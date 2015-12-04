package AIOHunter.Tasks;

import AIOHunter.AIOHunter;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Item;

import java.util.ArrayList;

public class CheckTraps extends Task<ClientContext> {

    private int trapInvID = 0;
    private int trapObjID = 0;
    private boolean chins = false;
    private int trapCheckID = 0;
    private int[] dropables;
    private Tile anchor;
    private int bonesID = 562;

    public CheckTraps(ClientContext ctx, Tile anchor, boolean chins, int trapObjID, int trapCheckID, int[] dropables)
    {
        super(ctx);
        this.anchor = anchor;
        this.chins = chins;
        this.trapInvID = trapInvID;
        this.trapObjID = trapObjID;
        this.trapCheckID = trapCheckID;
        this.dropables = dropables;
    }

    @Override
    public boolean activate()
    {
        // Are there any traps to check?
        return checkTraps();
    }

    public boolean checkTraps()
    {
        for(GameObject obj : ctx.objects.select().id(trapCheckID))
        {
            if(AIOHunter.traps.contains(obj.tile()))
                return true;
        }
        return false;
    }


    @Override
    public void execute()
    {
        // Make sure we have enough inventory space
        if(ctx.inventory.size() >= 25)
            clearInv(); // otherwise start droppin

        for(GameObject obj : ctx.objects.select().id(trapCheckID))
        {
            if(AIOHunter.traps.contains(obj.tile()))
            {
                obj.interact("Check");
                // Check if successful
                AIOHunter.traps.remove(obj.tile());
            }
        }

        clearInv(); // drop some moar :3
    }

    public void clearInv()
    {
        for(int dropID: dropables) {
            for (Item item : ctx.inventory.select().id(dropID)) {
                item.interact("Drop");
            }
        }

        // Bury bones. NO EXP WASTE!
        for (Item item : ctx.inventory.select().id(bonesID)) {
            item.interact("Bury");
        }
    }
}
