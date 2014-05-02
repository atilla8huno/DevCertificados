package br.com.devschool.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.Blob;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * <b>Title:</b> ImagemUtil
 * <br><b>Description:</b> Classe com metodos basicos para manipulacao de imagem
 * <br><b>Copyright:</b> Politec - Copyright (c) 2008
 * <br><b>Company:</b> Politec - SSP-GO
 * @author Bruno de Castro Oliveira
 * @version: $Revision: 1.1.1.1 $,$Date: 2009/06/25 17:57:02 $
 */
public class ImagemUtil {

	public ImagemUtil() {
		if(System.getProperty("java.awt.headless") == null)
			System.setProperty("java.awt.headless", "true");
	}
	
	@SuppressWarnings("all")
	public static byte[] fileToByte(File imagem) throws Exception {
		FileInputStream fis = new FileInputStream(imagem);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[8192];
		int bytesRead = 0;
		while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
			baos.write(buffer, 0, bytesRead);
		}
		return baos.toByteArray();
	}

	public static InputStream byteToInputStream(byte[] bytes) throws Exception {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		return bais;
	}

	public static Blob fileToBlob(File file) throws Exception {
//		byte[] fileInByte = fileToByte(file);
//		return Hibernate.createBlob(fileInByte);
	    return null;
	}
	
	public boolean verificarImagem(InputStream input) {
		try {
			ImageIO.read(input);
		} catch (IOException e) {
			e.printStackTrace();
			return true;
		}
		return false;
	}

	public BufferedImage toBufferedImage(Image image, ColorModel cm) {        
		int w = image.getWidth(null);        
		int h = image.getHeight(null);       
		boolean alphaPremultiplied = cm.isAlphaPremultiplied();       
		WritableRaster raster = cm.createCompatibleWritableRaster(w, h);       
		BufferedImage result = new BufferedImage(cm, raster, alphaPremultiplied, null);
		Graphics2D g = result.createGraphics();
		g.drawImage(image, 0, 0, null);       
		g.dispose();       

		return result; 
	}

	public Map<String, Integer> reduzirGravar(InputStream input, String caminho, String nomeArq, Integer novaLargura) throws IOException, URISyntaxException {        
		BufferedImage image1= ImageIO.read(input);      

		int newWidth = (int) (novaLargura.intValue());
		int newHeight = calcularAltura(novaLargura.intValue(), image1.getHeight(), image1.getWidth());

		Image imageScaledOrig = image1.getScaledInstance(newWidth, newHeight, Image.SCALE_AREA_AVERAGING);       
		BufferedImage biScaledOrig = toBufferedImage(imageScaledOrig, image1.getColorModel());
		String path = caminho + nomeArq;
		String extensao = nomeArq.substring(nomeArq.indexOf(".")+1, nomeArq.length());
		ImageIO.write(biScaledOrig, extensao, new File(path));
		HashMap<String, Integer> dimen = new HashMap<String, Integer>();
		dimen.put("largura", newWidth);
		dimen.put("altura", newHeight);

		return dimen;
	}

	public int calcularAltura(int novaLargura, int alturaOriginal, int larguraOriginal) {
		return (novaLargura * alturaOriginal)/larguraOriginal;		
	}

	/**
	 * Apaga uma estrutura de diretorios que contenham ou nao arquivos.
	 * @param dir
	 * @return boolean
	 */
    public static boolean deleteDir(File dir) {
        File candir;
        try {
            candir = dir.getCanonicalFile();
        } catch (IOException e) {
            return false;
        }
  
        if (!candir.equals(dir.getAbsoluteFile())) {
            return false;
        }
  
        File[] files = candir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
  
                boolean deleted = file.delete();
                if (!deleted) {
                    if (file.isDirectory()) deleteDir(file);
                }
            }
        }

        return dir.delete();
    }

    
    public static boolean deleteDirPorTempo(File dir, long milisegundos) {
        File candir;
        try {
            candir = dir.getCanonicalFile();
        } catch (IOException e) {
            return false;
        }
  
        if (!candir.equals(dir.getAbsoluteFile())) {
            return false;
        }
  
        File[] files = candir.listFiles();
        if (files != null) {

            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                Date tempo = new Date();
                long dif = tempo.getTime() - file.lastModified();

                if(dif >= milisegundos){
                    boolean deleted = file.delete();
                    if (!deleted) {
                        if (file.isDirectory()) deleteDir(file);
                    }
                } else {
                	return false;
                }
            }
        }

        return dir.delete();
    }
}
