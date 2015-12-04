package AIOHunter.Tasks;

import AIOHunter.AIOHunter;
import org.powerbot.script.Condition;
import org.powerbot.script.Locatable;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.ArrayList;

/**
 * Created by glitch on 12/3/15.
 */
public class LayTraps extends Task<ClientContext> {

    private int trapInvID = 0;
    private int trapObjID = 0;
    private boolean chins = false;
    private Tile anchor;

    public LayTraps(ClientContext ctx, Tile anchor, boolean chins, int trapInvID, int trapObjID)
    {
        super(ctx);
        this.anchor = anchor;
        this.chins = chins;
        this.trapInvID = trapInvID;
        this.trapObjID = trapObjID;

        populatePattern();
    }

    @Override
    public boolean activate()
    {
        // Not at max trap count
        // Has traps in inventry
       return !ctx.inventory.select().id(trapInvID).isEmpty()
              && AIOHunter.traps.size() < maxTraps() ;
    }


    @Override
    public void execute()
    {
        // Find empty space for trap in pattern
        Tile freeSpot = findSlot();
        if(freeSpot != null)
        {
            if(!freeSpot.equals(anchor))
            {
                ctx.movement.step(freeSpot);
                Condition.sleep(500);
            }
            // Lay trap
            ctx.inventory.select().id(trapInvID).action("Lay");
            // Check if trap was set successfully?
            AIOHunter.traps.add(freeSpot);
        }
        else
        {
            // Uhm... we're in trouble
        }
    }

    public Tile findSlot()
    {
        for(Tile patternTile : AIOHunter.pattern)
        {
            boolean free = true;
            for(GameObject trap : ctx.objects.select().id(trapObjID))
            {
                if(patternTile.equals(trap.tile()))
                {
                    free = false;
                    break;
                }
            }
            if(free)
                return patternTile;
        }
        return null;
    }

    public int maxTraps()
    {
        // Hunter id: 21
        int level = ctx.skills.level(21);
        if(level < 20)
            return 1;
        if(level < 40)
            return 2;
        if(level < 60)
            return 3;
        if(level < 80)
            return 4;
        return 5;
    }

    public void populatePattern()
    {
        // Populate the pattern with the location of the traps based on the anchor
        switch(maxTraps()) {
            case 1:
                AIOHunter.pattern.add(anchor);
                break;
            case 2:
                AIOHunter.pattern.add(new Tile(anchor.x() - 1, anchor.y()));
                AIOHunter.pattern.add(new Tile(anchor.x() + 1, anchor.y()));
                break;
            case 3:
                AIOHunter.pattern.add(new Tile(anchor.x() + 1, anchor.y()));
                AIOHunter.pattern.add(new Tile(anchor.x() - 1, anchor.y() - 1));
                AIOHunter.pattern.add(new Tile(anchor.x() - 1, anchor.y() + 1));
                break;
            case 4:
                AIOHunter.pattern.add(new Tile(anchor.x() + 1, anchor.y() - 1));
                AIOHunter.pattern.add(new Tile(anchor.x() + 1, anchor.y() + 1));
                AIOHunter.pattern.add(new Tile(anchor.x() - 1, anchor.y() - 1));
                AIOHunter.pattern.add(new Tile(anchor.x() - 1, anchor.y() + 1));
                break;
            case 5:
                AIOHunter.pattern.add(anchor);
                AIOHunter.pattern.add(new Tile(anchor.x() + 1, anchor.y() - 1));
                AIOHunter.pattern.add(new Tile(anchor.x() + 1, anchor.y() + 1));
                AIOHunter.pattern.add(new Tile(anchor.x() - 1, anchor.y() - 1));
                AIOHunter.pattern.add(new Tile(anchor.x() - 1, anchor.y() + 1));
                break;
        }
    }
}
