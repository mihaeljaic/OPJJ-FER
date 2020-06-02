package hr.fer.zemris.java.webserver.workers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Web worker that draws red circle and sends data to request context.
 * 
 * @author Mihael Jaić
 *
 */

public class CircleWorker implements IWebWorker {
	/**
	 * Radius.
	 */
	private static final int radius = 200;

	@Override
	public void processRequest(RequestContext context) throws Exception {
		BufferedImage bim = new BufferedImage(radius, radius, BufferedImage.TYPE_3BYTE_BGR);

		Graphics2D g2d = bim.createGraphics();
		g2d.setColor(Color.RED);
		g2d.fillOval(0, 0, radius, radius);
		g2d.dispose();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			context.setMimeType("image/png");
			ImageIO.write(bim, "png", bos);
			context.write(bos.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("Couldn't create image.");
		}
	}

}
