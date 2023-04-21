package research.exif;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.junit.Assert;
import org.junit.Test;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.jpeg.JpegDirectory;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.ResourceUtil;

public class ExifTest 
{
	@Test
	public void getExifWithMetaDataExtractor() throws Exception
	{
		Metadata metadata = ImageMetadataReader.readMetadata(ResourceUtil.getResourceAsStream("/research/exif/exif_rotate.jpg"));

		for (Directory directory : metadata.getDirectories()) {
		    for (Tag tag : directory.getTags()) {
		        System.out.format("[%s] - %s = %s (%s)\n",
		            directory.getName(), tag.getTagName(), tag.getDescription(), "" + tag.getTagType());
		    }
		    if (directory.hasErrors()) {
		        for (String error : directory.getErrors()) {
		            System.err.format("ERROR: %s", error);
		        }
		    }
		}		
		
	    Directory dirExif = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
	    JpegDirectory jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);

	    ImageInformation iinf = new ImageInformation(dirExif.getInt(ExifIFD0Directory.TAG_ORIENTATION), jpegDirectory.getImageWidth(), jpegDirectory.getImageHeight());
	    
		System.out.println("EXIF: " + iinf);
		
		AffineTransform atr = getExifTransformation(iinf);
		
		BufferedImage imgNew = transformImage(ImageIO.read(ResourceUtil.getResourceAsStream("/research/exif/exif_rotate.jpg")), atr);
		
		write(imgNew, new File("/temp/exif_javadone.jpg"));
		
		metadata = ImageMetadataReader.readMetadata(ResourceUtil.getResourceAsStream("/research/exif/noexif_rotate.jpg"));
		
		if (metadata != null)
		{
			dirExif = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
			
			if (dirExif != null)
			{
				Assert.fail("Invalid metadata!");
			}
		}
	}
	
	// Look at http://chunter.tistory.com/143 for information
	public static AffineTransform getExifTransformation(ImageInformation info)
	{

	    AffineTransform t = new AffineTransform();

	    switch (info.orientation) {
	    case 1:
	        break;
	    case 2: // Flip X
	        t.scale(-1.0, 1.0);
	        t.translate(-info.width, 0);
	        break;
	    case 3: // PI rotation 
	        t.translate(info.width, info.height);
	        t.rotate(Math.PI);
	        break;
	    case 4: // Flip Y
	        t.scale(1.0, -1.0);
	        t.translate(0, -info.height);
	        break;
	    case 5: // - PI/2 and Flip X
	        t.rotate(-Math.PI / 2);
	        t.scale(-1.0, 1.0);
	        break;
	    case 6: // -PI/2 and -width
	        t.translate(info.height, 0);
	        t.rotate(Math.PI / 2);
	        break;
	    case 7: // PI/2 and Flip
	        t.scale(-1.0, 1.0);
	        t.translate(-info.height, 0);
	        t.translate(0, info.width);
	        t.rotate(  3 * Math.PI / 2);
	        break;
	    case 8: // PI / 2
	        t.translate(0, info.width);
	        t.rotate(  3 * Math.PI / 2);
	        break;
	    }

	    return t;
	}
	
	public static BufferedImage transformImage(BufferedImage image, AffineTransform transform) throws Exception 
	{
	    AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);
	    
        Rectangle r = op.getBounds2D(image).getBounds();

        // If r.x (or r.y) is < 0, then we want to only create an image
        // that is in the positive range.
        // If r.x (or r.y) is > 0, then we need to create an image that
        // includes the translation.
        int w = r.x + r.width;
        int h = r.y + r.height;

	    BufferedImage destinationImage = new BufferedImage(w, h, image.getType());
	    Graphics2D g = destinationImage.createGraphics();
	    
	    try
	    {
		    g.setBackground(Color.WHITE);
		    g.clearRect(0, 0, destinationImage.getWidth(), destinationImage.getHeight());
		    destinationImage = op.filter(image, destinationImage);
	    }
	    finally
	    {
	    	g.dispose();
	    }
	    
	    return destinationImage;
	}
	
	public static void write(BufferedImage pImage, File pFile) throws Exception
	{
        Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName("jpg");
        
        if (!it.hasNext())
        {
            throw new RuntimeException("No image writer available for 'jpg'!");
        }
        else
        {
            ImageWriter writer = it.next();

            if (writer == null)
            {
                throw new IOException("Output format 'jpg' is not supported!");
            }
            
            ImageOutputStream os = null;
            
            try
            {
                ImageWriteParam param = writer.getDefaultWriteParam();
                
                if (param != null && param.canWriteCompressed())
                {
                    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    param.setCompressionQuality(1);
                }
                
                os = ImageIO.createImageOutputStream(pFile);
                
                writer.setOutput(os);
                writer.write(null, new IIOImage(pImage, null, null), param);
            }
            finally
            {
                CommonUtil.close(os);
                
                writer.dispose();
            }
        }
	}
	
	public static final class ImageInformation 
	{
	    public final int orientation;
	    public final int width;
	    public final int height;

	    public ImageInformation(int orientation, int width, int height) 
	    {
	        this.orientation = orientation;
	        this.width = width;
	        this.height = height;
	    }

	    public String toString() 
	    {
	        return width + "x" + height +", " + orientation;
	    }
	}
	
}
