import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


public class ProductLoader {
	
	String products_data_path = "data/products.nes";
	public ProductLoader(){

	}
	
	protected ArrayList<Object[]> getProductList(){
		
		ArrayList<Object[]> products = new ArrayList<Object[]>();
		try {
			File file = new File(products_data_path);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				// skip comments
				System.out.println("Got here: ");
				if(!line.contains("#"))
				{
					String[] prod = line.split(";");
					products.add(new Object[]{prod[0],  new ImageIcon(Tools.getScaledImage(ImageIO.read(getClass().getResource(prod[1])), 200, 200)), prod[2],prod[3], prod[4]});
					System.out.println("Got here and added: " + prod[0]);
				}
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return products;
	}

}
