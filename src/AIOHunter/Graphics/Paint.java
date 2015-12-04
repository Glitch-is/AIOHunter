package AIOHunter.Graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;
import java.text.DecimalFormat;
import java.awt.Polygon;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.Tile;

import AIOHunter.AIOHunter;
import AIOHunter.Tasks.Task;
import org.powerbot.script.rt4.TileMatrix;

public class Paint extends Task<ClientContext>{
	private long startTime = 0;

	private int hunterXPStart = ctx.skills.experience(21);
	private int caught = 0;

	private int xpTable[] 			=
			{0,0,83,174,276,388,512,650,801,969,1154,1358,1584,1833,2107,2411,2746,
					3115,3523,3973,4470,5018,5624,6291,7028,7842,8740,9730,10824,12031,
					13363,14833,16456,18247,20224,22406,24815,27473,30408,33648,37224,41171,
					45529,50339,55649,61512,67983,75127,83014,91721,101333,111945,123660,
					136594,150872,166636,184040,203254,224466,247886,273742,302288,333804,
					368599,407015,449428,496254,547953,605032,668051,737627,814445,899257,
					992895,1096278,1210421,1336443,1475581,1629200,1798808,1986068,2192818,
					2421087,2673114,2951373,3258594,3597792,3972294,4385776,4842295,5346332,
					5902831,6517253,7195629,7944614,8771558,9684577,10692629,11805606,13034431
			};

	public Paint(ClientContext ctx, long time) {
		super(ctx);
		this.startTime = time;
	}

	@Override
	public boolean activate() {
		return true;
	}

	@Override
	public void execute() {
	}

	public String runTime() {
		DecimalFormat nf = new DecimalFormat("00");
		long millis = System.currentTimeMillis() - startTime;
		long hours = millis / (1000 * 60 * 60);
		millis -= hours * (1000 * 60 * 60);
		long minutes = millis / (1000 * 60);
		millis -= minutes * (1000 * 60);
		long seconds = millis / 1000;
		return nf.format(hours) + ":" + nf.format(minutes) + ":" + nf.format(seconds);
	}

	public void inputGraphic(Graphics g) {

		/*try {
			g.drawImage(ImageIO.read(getClass().getResource("/ChopnFletch/art/bg1.png")), 0, 0, null);
			g.drawImage(ImageIO.read(getClass().getResource("/ChopnFletch/art/fletchingIcon.png")),170,380,null);
			g.drawImage(ImageIO.read(getClass().getResource("/ChopnFletch/art/woodcuttingIcon.png")), 320, 380, null);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (java.lang.IllegalArgumentException IAe) {
			System.out.println(IAe.getMessage());
		}*/
		g.setFont(new Font("arial Regular", 1, 12));

		g.setColor(Color.WHITE);

		g.drawString("Runtime", 252, 356);
		g.drawString("Caught", 217, 374);
		g.drawString("Xp earned", 240, 392);
		g.drawString("Xp/hour", 254, 410);

		g.drawString("Status", 392, 356);

		g.setColor(new Color(0x32fcd8));

		g.drawString(runTime(), 305, 356);
		g.drawString(String.valueOf(caught), 305, 374);
		g.drawString(getHunterXPTotalPaint(), 305, 392);
		g.drawString(getHunterXPHourPaint(), 305, 410);

		g.drawString(AIOHunter.status, 435, 356);

		drawHunterBar(g);
		drawMousePaint(g);
	}

	private void drawMousePaint(Graphics g) {
		Point mouse = ctx.input.getLocation();
		g.setColor(Color.CYAN);
		g.drawLine(mouse.x - 5, mouse.y - 5, mouse.x + 5, mouse.y + 5);
		g.drawLine(mouse.x + 5, mouse.y - 5, mouse.x - 5, mouse.y + 5);
	}

	public void inputData(String data) {
		if(data.contains("caught"))
			caught++;
	}

	public String getHunterXPHourPaint() {
		double hunterXPGained = ctx.skills.experience(21) - hunterXPStart;

		if(hunterXPGained == 0) {
			return "0";
		}

		return 	String.valueOf((int)(hunterXPGained /
				((double)(System.currentTimeMillis()-startTime)/3600000)));
	}

	public String getHunterXPTotalPaint() {
		return String.valueOf(ctx.skills.experience(21) - hunterXPStart);
	}

	public void drawHunterBar(Graphics g) {

		g.setColor(Color.WHITE);
		g.drawRect(240, 428, 100, 10);

		g.setColor(Color.GREEN);
		g.fillRect(240, 428, getPercentIntoNextLevel(21)+1, 11);

		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", 1, 11));
		g.drawString(String.valueOf(getPercentIntoNextLevel(21)) + "%", 280, 438);

	}

	public int getPercentIntoNextLevel(int skillID) {

		double xpIntoLevel = ctx.skills.experience(skillID)-xpTable[ctx.skills.level(skillID)];
		double xpTotalLevel = (xpTable[ctx.skills.level(skillID)+1])-xpTable[ctx.skills.level(skillID)];

		double percentToNextLevel = ((xpIntoLevel/xpTotalLevel)*100);

		return (int) percentToNextLevel;
	}

	public void drawTiles(ClientContext ctx, Graphics g, ArrayList<Tile> traps, ArrayList<Tile> pattern)
	{
		g.setColor(Color.GREEN);
		for(Tile t : traps)
		{
			TileMatrix m = new TileMatrix(ctx, t);
			Polygon p = m.getBounds();
			if (isPolygonOnScreen(p)) {
                g.drawPolygon(p);
            }
		}

		g.setColor(Color.YELLOW);
		for(Tile t : pattern)
		{
			if(!traps.contains(t))
			{
				TileMatrix m = new TileMatrix(ctx, t);
				Polygon p = m.getBounds();
				if (isPolygonOnScreen(p)) {
					g.drawPolygon(p);
				}
			}
		}

	}

	private boolean isPolygonOnScreen(Polygon p) {
		for (int i = 0; i < p.npoints; i++) {
			if (!ctx.game.pointInViewport(p.xpoints[i], p.ypoints[i])) {
				return false;
			}
		}
		return true;
	}
}
