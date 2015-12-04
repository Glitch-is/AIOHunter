package AIOHunter;

import java.util.ArrayList;
import java.awt.Graphics;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.Script;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Tile;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PaintListener;
import org.powerbot.script.MessageEvent;

import AIOHunter.Tasks.*;
import AIOHunter.Graphics.*;

@Script.Manifest(name = "AIOHunter", description = "All in one hunter")

public class AIOHunter extends PollingScript<ClientContext> implements MessageListener, PaintListener{
    public static ArrayList<Task> taskList 	= new ArrayList<Task>();
	public static ArrayList<Tile> traps;
	public static ArrayList<Tile> pattern;

	public static String status	= "Starting bot";
	public static boolean run = true;
	public long startTime = System.currentTimeMillis();
	public Paint paint	= new Paint(ctx, startTime);

    @Override
    public void start() {
        GUI settings = new GUI(ctx);

		status = "Bot starting ...";
    }

    @Override
    public void poll() {
        for(Task<?> task: taskList) {
			if(task.activate()) {
				task.execute();
			}
		}
    }

	@Override
    public void repaint(Graphics g) {
		paint.inputGraphic(g);
		paint.drawTiles(ctx, g, traps, pattern);
	}

	@Override
	public void messaged(MessageEvent arg0) {
		paint.inputData(arg0.text());

	}

}
