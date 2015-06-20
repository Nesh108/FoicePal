import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.Hashtable;

import javazoom.jl.player.Player;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.aztec.AztecReader;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.datamatrix.DataMatrixReader;
import com.google.zxing.maxicode.MaxiCodeReader;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.oned.CodaBarReader;
import com.google.zxing.oned.Code128Reader;
import com.google.zxing.oned.Code39Reader;
import com.google.zxing.oned.Code93Reader;
import com.google.zxing.oned.EAN13Reader;
import com.google.zxing.oned.EAN8Reader;
import com.google.zxing.oned.ITFReader;
import com.google.zxing.oned.UPCAReader;
import com.google.zxing.oned.UPCEReader;
import com.google.zxing.oned.rss.RSS14Reader;
import com.google.zxing.oned.rss.expanded.RSSExpandedReader;
import com.google.zxing.pdf417.PDF417Reader;
import com.google.zxing.qrcode.QRCodeReader;

public class ProductScanner {

	BarcodeFormat bformat = BarcodeFormat.EAN_13;

	public ProductScanner() {

		ImageFetcher.openCamera();

		while(Config.runScanner){
			try {
				String barcode = readCode(ImageFetcher.fetchImage());
				
				if(barcode != null)
				{
					GUI.addProd(barcode);
					beepSound();
					Thread.sleep(800);
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ImageFetcher.closeCamera();
	}

	public String readCode(BufferedImage image) throws Exception {

		if (image == null)
			throw new IllegalArgumentException("Could not decode image.");
		Hashtable<DecodeHintType, Object> decodeHints = new Hashtable<DecodeHintType, Object>();
		decodeHints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

		LuminanceSource source = new BufferedImageLuminanceSource(image);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

		Result content = null;

		if (bformat == BarcodeFormat.QR_CODE)
			content = new QRCodeReader().decode(bitmap, decodeHints);
		else if (bformat == BarcodeFormat.AZTEC)
			content = new AztecReader().decode(bitmap, decodeHints);
		else if (bformat == BarcodeFormat.CODABAR)
			content = new CodaBarReader().decode(bitmap, decodeHints);
		else if (bformat == BarcodeFormat.CODE_128)
			content = new Code128Reader().decode(bitmap, decodeHints);
		else if (bformat == BarcodeFormat.CODE_39)
			content = new Code39Reader().decode(bitmap, decodeHints);
		else if (bformat == BarcodeFormat.CODE_93)
			content = new Code93Reader().decode(bitmap, decodeHints);
		else if (bformat == BarcodeFormat.DATA_MATRIX)
			content = new DataMatrixReader().decode(bitmap, decodeHints);
		else if (bformat == BarcodeFormat.EAN_13)
			content = new EAN13Reader().decode(bitmap, decodeHints);
		else if (bformat == BarcodeFormat.EAN_8)
			content = new EAN8Reader().decode(bitmap, decodeHints);
		else if (bformat == BarcodeFormat.MAXICODE)
			content = new MaxiCodeReader().decode(bitmap, decodeHints);
		else if (bformat == BarcodeFormat.PDF_417)
			content = new PDF417Reader().decode(bitmap, decodeHints);
		else if (bformat == BarcodeFormat.RSS_14)
			content = new RSS14Reader().decode(bitmap, decodeHints);
		else if (bformat == BarcodeFormat.RSS_EXPANDED)
			content = new RSSExpandedReader().decode(bitmap, decodeHints);
		else if (bformat == BarcodeFormat.UPC_A)
			content = new UPCAReader().decode(bitmap, decodeHints);
		else if (bformat == BarcodeFormat.UPC_E)
			content = new UPCEReader().decode(bitmap, decodeHints);
		else if (bformat == BarcodeFormat.ITF)
			content = new ITFReader().decode(bitmap, decodeHints);

		if(Config.DEBUG)
			System.out.println("Found: " + content.getText());
		
		return content.getText();

	}
	
	private void beepSound(){
		
		try{
	        // Play Beep sound
	        FileInputStream fileInputStream = new FileInputStream(new File("audio/beep_sound.mp3"));
	        Player player = new Player(fileInputStream);
	        player.play();
	        player.close();
		}
		catch(Exception e){}
	}
}
